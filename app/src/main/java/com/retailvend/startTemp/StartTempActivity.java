package com.retailvend.startTemp;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.gson.Gson;
import com.retailvend.R;
import com.retailvend.broadcast.ConnectivityReceiver;
import com.retailvend.model.startTempSales.StartTempData;
import com.retailvend.model.startTempSales.StartTempModel;
import com.retailvend.retrofit.RetrofitClient;
import com.retailvend.utills.CustomProgress;
import com.retailvend.utills.CustomToast;
import com.retailvend.utills.SessionManagerSP;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StartTempActivity extends AppCompatActivity {

    Toolbar toolbar;
    Menu menu;
    TextView mTitle, name, date, beat, total_outlet, new_outlet, start_time;
    ConstraintLayout main_constrain;
    List<StartTempData> startTempData;
    SessionManagerSP sessionManagerSP;
    ImageView back_arrow;

    String which = "";
    String bill_no = "";
    public static Bitmap bitScroll;
    File filePath;
    private static String storage;
    private static String DATA_PATH = Environment.getExternalStorageDirectory().toString();
    public static final String TESS_DATA = "/Hoisst";
    String empname = "";
    String dateRes = "";
    Activity activity;
    TextView view_pdf;
    Bitmap bmp, scaledbmp;

    String file_name_path = "";
    int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {

            WRITE_EXTERNAL_STORAGE,
            READ_EXTERNAL_STORAGE,

    };
    int pageHeight = 1120;
    int pagewidth = 792;
    private static final int PERMISSION_REQUEST_CODE = 200;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_temp);
        activity = this;

        if (android.os.Build.VERSION.SDK_INT >= 19) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimaryDark));
//            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
            window.setNavigationBarColor(this.getResources().getColor(R.color.white));

            toolbar = findViewById(R.id.toolbar);
            mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);

            name = findViewById(R.id.name);
            date = findViewById(R.id.date);
            beat = findViewById(R.id.beat);
            total_outlet = findViewById(R.id.total_outlet);
            new_outlet = findViewById(R.id.new_outlet);
            mTitle = findViewById(R.id.toolbar_title);
            main_constrain = findViewById(R.id.main_constrain);
            view_pdf = findViewById(R.id.view_pdf);
            start_time = findViewById(R.id.start_time);

            sessionManagerSP = new SessionManagerSP(StartTempActivity.this);
            startTempData = new ArrayList<>();

            mTitle.setText("Day Start Details");

            view_pdf.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewPdfFile();
                }
            });

            bmp = BitmapFactory.decodeResource(getResources(), R.drawable.logo_main);
            scaledbmp = Bitmap.createScaledBitmap(bmp, 150, 150, false);

            // below code is used for
            // checking our permissions.
            if (checkPermission()) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                requestPermission();
            }

            boolean isConnected = ConnectivityReceiver.isConnected();
            if (isConnected) {
                startTempApi();
            } else {
//            Toast.makeText(this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
                CustomToast.getInstance(StartTempActivity.this).showSmallCustomToast("Please check your internet connection");
            }
        }
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    public void viewPdfFile() {

        File file = new File(this.getExternalFilesDir(null).getAbsolutePath() + file_name_path);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "application/pdf");
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        this.menu = menu;
//        menu.findItem(R.id.action_profile).setVisible(false);
//        menu.findItem(R.id.action_search).setVisible(false);
        menu.findItem(R.id.action_download).setVisible(true);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
//        noinspection SimplifiableIfStatement
        if (id == R.id.action_download) {
//
            bitScroll = getBitmapFromView(main_constrain, main_constrain.getChildAt(0).getHeight(), main_constrain.getChildAt(0).getWidth());
////            createPdf(name.getText().toString(),date.getText().toString(),beat.getText().toString(),
////                    total_outlet.getText().toString(),new_outlet.getText().toString(),start_time.getText().toString());
//            System.out.println("shdsghgsghdghs");
////            generatePDF();
//            createPdf(name.getText().toString(), date.getText().toString(), beat.getText().toString(),
//                    total_outlet.getText().toString(), new_outlet.getText().toString(), start_time.getText().toString());

            takeScreenshot();
            return true;
        }
//        else if (id == R.id.action_clock_out) {
////            showSusscesDialog();
//
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
//        refreshActivity();
//        RegistorSharedPreManager.getInstance(DetailsActivity.this).clear();
        super.onBackPressed();
    }

    //create bitmap from the ScrollView
    private Bitmap getBitmapFromView(View view, int height, int width) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null)
            bgDrawable.draw(canvas);
        else
            canvas.drawColor(Color.WHITE);
        view.draw(canvas);
        return bitmap;
    }

    private boolean checkPermission() {
        // checking of permissions.
        int permission1 = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int permission2 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        return permission1 == PackageManager.PERMISSION_GRANTED && permission2 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        // requesting permissions if not provided.
        ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0) {

                // after requesting permissions we are showing
                // users a toast message of permission granted.
                boolean writeStorage = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean readStorage = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                if (writeStorage && readStorage) {
                    Toast.makeText(this, "Permission Granted..", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Permission Denied.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }
    }

    public void startTempApi() {
        CustomProgress.showProgress(activity);
        String emp_id = sessionManagerSP.getEmployeeId();
        System.out.println("_dayStartReport " + emp_id);

        Call<StartTempModel> call = RetrofitClient
                .getInstance().getApi().startTemp("_dayStartReport", emp_id);

        call.enqueue(new Callback<StartTempModel>() {
            @Override
            public void onResponse(@NonNull Call<StartTempModel> call, @NonNull Response<StartTempModel> response) {

                try {

                    Gson gson = new Gson();
                    String json = gson.toJson(response.body());
//                    System.out.println("responseOutletsss "+response.body());

                    StartTempModel detailsModel = gson.fromJson(json, StartTempModel.class);
                    String s = detailsModel.getMessage();

                    if (detailsModel.getStatus() == 1) {
//                        no_data_constrain.setVisibility(View.GONE);
//                        nodata_txt.setText("");

                        startTempData = detailsModel.getData();
                        empname = startTempData.get(0).getName();
                        dateRes = startTempData.get(0).getDate();
                        name.setText(empname);
                        date.setText(dateRes);
                        beat.setText(startTempData.get(0).getBeat());
                        total_outlet.setText(startTempData.get(0).getTotalOutlet());
                        new_outlet.setText(startTempData.get(0).getNewOutlet());
                        start_time.setText(startTempData.get(0).getStartTime());

                        CustomProgress.hideProgress(activity);

                    } else {
                        CustomToast.getInstance(StartTempActivity.this).showSmallCustomToast(s);
                        CustomProgress.hideProgress(activity);
                    }

                } catch (Exception e) {
                    Log.d("Exception", e.getMessage());
                    CustomProgress.hideProgress(activity);
                    CustomProgress.hideProgress(activity);
                }

            }

            @Override
            public void onFailure(@NonNull Call<StartTempModel> call, @NonNull Throwable t) {
                Log.d("Failure ", t.getMessage());
                CustomToast.getInstance(StartTempActivity.this).showSmallCustomToast("Something went wrong try again..");
                CustomProgress.hideProgress(activity);
            }
        });
    }

    private void takeScreenshot() {
        String mPath="";
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

        try {
            // image naming and path  to include sd card  a6rrgppending name you choose for file
//            String mPath = Environment.getExternalStorageDirectory().toString() + "/" + now + ".jpg";

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD_MR1) {
                mPath= getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DCIM) + "/" + now + "DayStart.jpeg";
            }
            else
            {
                mPath= Environment.getExternalStorageDirectory().toString() + "/" + now + ".jpeg";
            }

            // create bitmap screen capture
            View v1 = getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);

            File imageFile = new File(mPath);

            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();
            CustomToast.getInstance(StartTempActivity.this).showSmallCustomToast("File generated successfully. "+mPath);
            openScreenshot(imageFile);
        } catch (Throwable e) {
            // Several error may come out with file handling or DOM
            e.printStackTrace();
        }
    }

    private void openScreenshot(File imageFile) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(imageFile);
        intent.setDataAndType(uri, "image/*");
        startActivity(intent);
    }
}