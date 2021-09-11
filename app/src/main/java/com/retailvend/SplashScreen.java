package com.retailvend;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //////////////////////network receiver
        this.registerReceiver(this.mConnReceiver,
                new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        sessionManagerSP = new SessionManagerSP(SplashScreen.this);

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

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                if (Build.VERSION.SDK_INT >= 23) {
                    // Marshmallow+
                    String writePermission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
                    String readPermission = Manifest.permission.READ_EXTERNAL_STORAGE;

                    int hasPermission = 0;
                    int hasreadPermission = 0;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        hasPermission = ContextCompat.checkSelfPermission(SplashScreen.this, writePermission);
                        hasreadPermission = ContextCompat.checkSelfPermission(SplashScreen.this, readPermission);

                    }
                    String[] permissions = new String[]{writePermission, readPermission};
                    if (hasPermission != PackageManager.PERMISSION_GRANTED
                            || hasreadPermission != PackageManager.PERMISSION_GRANTED) {
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

                    // Pre-Marshmallow
                }

//                Intent i = new Intent(SplashScreen.this,LoginActivity.class);
//                startActivity(i);
//                finish();
            }
        }, 3500);
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

    private BroadcastReceiver mConnReceiver = new BroadcastReceiver() {
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