package com.retailvend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.content.Intent;
import android.os.*;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.retailvend.broadcast.ConnectivityReceiver;
import com.retailvend.broadcast.MyApplication;
import com.retailvend.utills.CustomToast;
import com.retailvend.utills.SessionManagerSP;

public class SplashScreen extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener{

    SessionManagerSP sessionManagerSP;
    private static final int SPLASH_TIME_OUT = 3500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (Build.VERSION.SDK_INT >= 19) {

            Window window = getWindow();

            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                            | View.SYSTEM_UI_FLAG_LOW_PROFILE
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_IMMERSIVE);
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            window.setStatusBarColor(this.getResources().getColor(R.color.white));
//            window.setNavigationBarColor(this.getResources().getColor(R.color.black_overlay));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }

        //////////////////////network receiver
        this.registerReceiver(this.mConnReceiver,
                new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        sessionManagerSP = new SessionManagerSP(SplashScreen.this);



        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {


                if (Build.VERSION.SDK_INT >= 23) {
                    // Marshmallow+
                    String writePermission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
                    String readPermission = Manifest.permission.READ_EXTERNAL_STORAGE;
                    String cameraPermission = Manifest.permission.CAMERA;
                    String accessFineLocation = Manifest.permission.ACCESS_FINE_LOCATION;
                    String accessCoarseLocation = Manifest.permission.ACCESS_COARSE_LOCATION;
//
                    int hasPermission = 0;
                    int hascamPermission = 0;
                    int hasreadPermission = 0;
                    int hasaccessFineLocationPermission = 0;
                    int hasaccessCoarseLocationPermission = 0;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        hasPermission = ContextCompat.checkSelfPermission(SplashScreen.this, writePermission);
                        hascamPermission = ContextCompat.checkSelfPermission(SplashScreen.this, cameraPermission);
                        hasreadPermission = ContextCompat.checkSelfPermission(SplashScreen.this, readPermission);
                        hasaccessFineLocationPermission = ContextCompat.checkSelfPermission(SplashScreen.this, accessFineLocation);
                        hasaccessCoarseLocationPermission = ContextCompat.checkSelfPermission(SplashScreen.this, accessCoarseLocation);

                    }
                    String[] permissions = new String[]{writePermission, cameraPermission, readPermission, accessFineLocation, accessCoarseLocation};
                    if (hasPermission != PackageManager.PERMISSION_GRANTED
                            || hascamPermission != PackageManager.PERMISSION_GRANTED
                            || hasreadPermission != PackageManager.PERMISSION_GRANTED
                            || hasaccessFineLocationPermission != PackageManager.PERMISSION_GRANTED
                            || hasaccessCoarseLocationPermission != PackageManager.PERMISSION_GRANTED) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            requestPermissions(permissions, 101);

                        }

                    } else {

                        if (sessionManagerSP.getPhonelogin().equals("1")) {
                            Intent i = new Intent(SplashScreen.this, DashboardActivity.class);
                            startActivity(i);

                            // close this activity
                            finish();
                        } else {
                            sessionManagerSP.setPhonelogin("0");
                            Intent i = new Intent(SplashScreen.this, LoginActivity.class);
                            startActivity(i);

                            // close this activity
                            finish();
                        }

                    }

                    // Pre-Marshmallow
                }


//                Intent i = new Intent(SplashScreen.this,LoginActivity.class);
//                startActivity(i);
//                finish();
            }
        }, SPLASH_TIME_OUT);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        if (requestCode == 101) {
            // BEGIN_INCLUDE(permission_result)
            // Received permission result for camera permission.
//            Log.i(TAG, "Received response for Camera permission request.");

            // Check if the only required permission has been granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Camera permission has been granted, preview can be displayed
//                Log.i(TAG, "CAMERA permission has now been granted. Showing preview.");
//                Toast.makeText(MainActivity.this, "Granded", Toast.LENGTH_SHORT).show();

                if (sessionManagerSP.getPhonelogin().equals("1")) {
                    Intent i = new Intent(SplashScreen.this, DashboardActivity.class);
                    startActivity(i);

                    // close this activity
                    finish();
                } else {
                    sessionManagerSP.setPhonelogin("0");
                    Intent i = new Intent(SplashScreen.this, LoginActivity.class);
                    startActivity(i);

                    // close this activity
                    finish();
                }

            } else {
//                Log.i(TAG, "CAMERA permission was NOT granted.");
                CustomToast.getInstance(SplashScreen.this).showSmallCustomToast("Permission Denied");
            }
            // END_INCLUDE(permission_result)

        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    }

    private void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
//        showSnack(isConnected);
        String message;
        if (isConnected) {

        } else {
            message = "Sorry! Not connected to internet";

//            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            CustomToast.getInstance(SplashScreen.this).showSmallCustomToast(message);
        }
    }

    @Override
    protected void onStop() {
//        LocalBroadcastManager.getInstance(this).unregisterReceiver(this.mConnReceiver);
//      //  unregisterReceiver(this.mConnReceiver);
//        super.onStop();

        try {
            if (this.mConnReceiver != null)
                unregisterReceiver(this.mConnReceiver);
        } catch (Exception e) {

        }
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // register connection status listener
        MyApplication.getInstance().setConnectivityListener(SplashScreen.this);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
//        showSnack(isConnected);

        String message;
        if (isConnected) {

        } else {
            message = "Sorry! Not connected to internet";

//            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            CustomToast.getInstance(SplashScreen.this).showSmallCustomToast(message);
        }
    }

    private final BroadcastReceiver mConnReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            boolean noConnectivity = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
            String reason = intent.getStringExtra(ConnectivityManager.EXTRA_REASON);
            boolean isFailover = intent.getBooleanExtra(ConnectivityManager.EXTRA_IS_FAILOVER, false);

            NetworkInfo currentNetworkInfo = (NetworkInfo) intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
            NetworkInfo otherNetworkInfo = (NetworkInfo) intent.getParcelableExtra(ConnectivityManager.EXTRA_OTHER_NETWORK_INFO);

            if (currentNetworkInfo.isConnected()) {

                checkConnection();

//                Toast.makeText(getApplicationContext(), "Connected", Toast.LENGTH_LONG).show();
            } else {
//                Toast.makeText(getApplicationContext(), "Please check your internet connection", Toast.LENGTH_LONG).show();
                CustomToast.getInstance(SplashScreen.this).showSmallCustomToast("Please check your internet connection");
            }
        }
    };
}