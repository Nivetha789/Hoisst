package com.retailvend.endTemp;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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

import com.google.gson.Gson;
import com.retailvend.R;
import com.retailvend.broadcast.ConnectivityReceiver;
import com.retailvend.model.endTempSales.EndTempData;
import com.retailvend.model.endTempSales.EndTempModel;
import com.retailvend.retrofit.RetrofitClient;
import com.retailvend.utills.CustomProgress;
import com.retailvend.utills.CustomToast;
import com.retailvend.utills.SessionManagerSP;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EndTempActivity extends AppCompatActivity {

    Toolbar toolbar;
    Menu menu;
    TextView mTitle, name, date, beat, total_outlet, new_outlet, start_time;
    ConstraintLayout main_constrain;
    List<EndTempData> endTempData;
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
        setContentView(R.layout.activity_end_temp);
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

            sessionManagerSP = new SessionManagerSP(EndTempActivity.this);
            endTempData=new ArrayList<>();

            mTitle.setText("Day End Details");

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
                endTempApi();
            } else {
//            Toast.makeText(this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
                CustomToast.getInstance(EndTempActivity.this).showSmallCustomToast("Please check your internet connection");
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
//        menu.findItem(R.id.action_download).setVisible(true);

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
        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_download) {
//
//            bitScroll = getBitmapFromView(main_constrain, main_constrain.getChildAt(0).getHeight(), main_constrain.getChildAt(0).getWidth());
////            createPdf(name.getText().toString(),date.getText().toString(),beat.getText().toString(),
////                    total_outlet.getText().toString(),new_outlet.getText().toString(),start_time.getText().toString());
//            System.out.println("shdsghgsghdghs");
////            generatePDF();
//            createPdf(name.getText().toString(), date.getText().toString(), beat.getText().toString(),
//                    total_outlet.getText().toString(), new_outlet.getText().toString(), start_time.getText().toString());
//
//
//            return true;
//        }
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

    /*private void createPdf() {
        System.out.println("starttttt");
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics displaymetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        float hight = displaymetrics.heightPixels;
        float width = displaymetrics.widthPixels;

        int convertHighet = (int) hight, convertWidth = (int) width;

//        Resources mResources = getResources();
//        Bitmap bitmap = BitmapFactory.decodeResource(mResources, R.drawable.screenshot);

        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(bitScroll.getWidth(), bitScroll.getHeight(), 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);

        Canvas canvas = page.getCanvas();


        Paint paint = new Paint();
        paint.setColor(Color.parseColor("#ffffff"));
        canvas.drawPaint(paint);


        bitScroll = Bitmap.createScaledBitmap(bitScroll, bitScroll.getWidth(), bitScroll.getHeight(), true);

        paint.setColor(Color.BLUE);
        canvas.drawBitmap(bitScroll, 0, 0, null);
        document.finishPage(page);


        // write the document content
//        String targetPdf = "/sdcard/test.pdf";
        Date now = new Date();
        android.text.format.DateFormat.format("dd-MM-yyyy", now);
        String mPath = "";
//        filePath = new File(Environment.getExternalStorageDirectory().toString() + "/" + "NoorTrader");

            mPath = "/" + empname+ " " +dateRes + " Start Temp "  + " " + now + ".pdf";
        String root = (Environment.getExternalStorageDirectory().toString() + "/Hoisst/");
//        File myDir = new File(root + TESS_DATA);
        File myDir = new File(root);
        myDir.mkdirs();

        String fname = mPath;
        filePath = new File(myDir, fname);
        if (filePath.exists()) filePath.delete();
        System.out.println("fileeeeeee "+filePath);

//        filePath = new File(mPath);
        try {
            FileOutputStream out = new FileOutputStream(filePath);
            document.writeTo(new FileOutputStream(filePath));
            out.flush();
            out.close();
            CustomToast.getInstance(StartTempActivity.this).showSmallCustomToast("Pdf Files saved on Hoisst Folder");
        } catch (IOException e) {
            e.printStackTrace();
//            Toast.makeText(this, "Something wrong: " + e.toString(), Toast.LENGTH_LONG).show();
            CustomToast.getInstance(StartTempActivity.this).showSmallCustomToast("Something wrong: " + e.toString());
            System.out.println("errorfilenot "+e.toString());
        }

        // close the document
        document.close();
    }*/

    /*private void createPdf(String name,String date, String beat,String tot_outlet,String new_outlet,String start_time){
        // create a new document
        PdfDocument document = new PdfDocument();
        // crate a page description
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(300, 600, 1).create();
        // start a page
        PdfDocument.Page page = document.startPage(pageInfo);
        Canvas canvas = page.getCanvas();
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        canvas.drawCircle(50, 50, 30, paint);
        paint.setColor(Color.BLACK);
        canvas.drawText(name, 80, 50, paint);
        canvas.drawText(date, 80, 50, paint);
        canvas.drawText(beat, 80, 50, paint);
        canvas.drawText(tot_outlet, 80, 50, paint);
        canvas.drawText(new_outlet, 80, 50, paint);
        canvas.drawText(start_time, 80, 50, paint);
        //canvas.drawt
        // finish the page
        document.finishPage(page);
// draw text on the graphics object of the page
        // Create Page 2
        pageInfo = new PdfDocument.PageInfo.Builder(300, 600, 2).create();
        page = document.startPage(pageInfo);
        canvas = page.getCanvas();
        paint = new Paint();
        paint.setColor(Color.BLUE);
        canvas.drawCircle(100, 100, 100, paint);
        document.finishPage(page);
        // write the document content
        String directory_path = Environment.getExternalStorageDirectory().getPath() + "/mypdf/";
        File file = new File(directory_path);
        if (!file.exists()) {
            file.mkdirs();
        }
        String targetPdf = directory_path+"test-2.pdf";
        File filePath = new File(targetPdf);
        try {
            document.writeTo(new FileOutputStream(filePath));
            Toast.makeText(this, "Done", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Log.e("main", "error "+e.toString());
            Toast.makeText(this, "Something wrong: " + e.toString(),  Toast.LENGTH_LONG).show();
        }
        // close the document
        document.close();
    }

    // Method for opening a pdf file
    private void viewPdf(String file, String directory) {

        File pdfFile = new File(Environment.getExternalStorageDirectory() + "/" + directory + "/" + file);
        Uri path = Uri.fromFile(pdfFile);

        // Setting the intent for pdf reader
        Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
        pdfIntent.setDataAndType(path, "application/pdf");
        pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        try {
            startActivity(pdfIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(StartTempActivity.this, "Can't read pdf file", Toast.LENGTH_SHORT).show();
        }
    }
*/

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
                    Toast.makeText(this, "Permission Denined.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }
    }

    public void endTempApi() {
        CustomProgress.showProgress(activity);
        String emp_id = sessionManagerSP.getEmployeeId();
        System.out.println("emmmpidd " + emp_id);

        Call<EndTempModel> call = RetrofitClient
                .getInstance().getApi().endTemp("_dayEndReport", emp_id);

        call.enqueue(new Callback<EndTempModel>() {
            @Override
            public void onResponse(@NonNull Call<EndTempModel> call, @NonNull Response<EndTempModel> response) {

                try {

                    Gson gson = new Gson();
                    String json = gson.toJson(response.body());
//                    System.out.println("responseOutletsss "+response.body());

                    EndTempModel detailsModel = gson.fromJson(json, EndTempModel.class);
                    String s = detailsModel.getMessage();

                    if (detailsModel.getStatus() == 1) {
//                        no_data_constrain.setVisibility(View.GONE);
//                        nodata_txt.setText("");

                        endTempData = detailsModel.getData();
                        empname = endTempData.get(0).getName();
                        dateRes = endTempData.get(0).getDate();
                        name.setText(empname);
                        date.setText(dateRes);
                        beat.setText(endTempData.get(0).getBeat());
                        total_outlet.setText(endTempData.get(0).getTotalOutlet());
                        new_outlet.setText(endTempData.get(0).getNewOutlet());
                        start_time.setText(endTempData.get(0).getCloseTime());

                        CustomProgress.hideProgress(activity);

                    } else {
                        CustomToast.getInstance(EndTempActivity.this).showSmallCustomToast(s);
                        CustomProgress.hideProgress(activity);
                    }

                } catch (Exception e) {
                    Log.d("Exception", e.getMessage());
                    CustomProgress.hideProgress(activity);
                    CustomProgress.hideProgress(activity);
                }

            }

            @Override
            public void onFailure(@NonNull Call<EndTempModel> call, @NonNull Throwable t) {
                Log.d("Failure ", t.getMessage());
                CustomToast.getInstance(EndTempActivity.this).showSmallCustomToast("Something went wrong try again..");
                CustomProgress.hideProgress(activity);
            }
        });
    }

    private void generatePDF() {
        // creating an object variable
        // for our PDF document.
        PdfDocument pdfDocument = new PdfDocument();

        // two variables for paint "paint" is used
        // for drawing shapes and we will use "title"
        // for adding text in our PDF file.
        Paint paint = new Paint();
        Paint title = new Paint();

        // we are adding page info to our PDF file
        // in which we will be passing our pageWidth,
        // pageHeight and number of pages and after that
        // we are calling it to create our PDF.
        PdfDocument.PageInfo mypageInfo = new PdfDocument.PageInfo.Builder(pagewidth, pageHeight, 1).create();

        // below line is used for setting
        // start page for our PDF file.
        PdfDocument.Page myPage = pdfDocument.startPage(mypageInfo);

        // creating a variable for canvas
        // from our page of PDF.
        Canvas canvas = myPage.getCanvas();

        // below line is used to draw our image on our PDF file.
        // the first parameter of our drawbitmap method is
        // our bitmap
        // second parameter is position from left
        // third parameter is position from top and last
        // one is our variable for paint.
        canvas.drawBitmap(scaledbmp, 100, 100, paint);

        // below line is used for adding typeface for
        // our text which we will be adding in our PDF file.
        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));

        // below line is used for setting text size
        // which we will be displaying in our PDF file.
        title.setTextSize(15);

        // below line is sued for setting color
        // of our text inside our PDF file.
        title.setColor(ContextCompat.getColor(this, R.color.black));

        // below line is used to draw text in our PDF file.
        // the first parameter is our text, second parameter
        // is position from start, third parameter is position from top
        // and then we are passing our variable of paint which is title.
        canvas.drawText("Day Start Report", 209, 100, title);
        canvas.drawText("", 209, 80, title);

        // similarly we are creating another text and in this
        // we are aligning this text to center of our PDF file.
        title.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        title.setColor(ContextCompat.getColor(this, R.color.black));
        title.setTextSize(20);

        // below line is used for setting
        // our text to center of PDF.
        title.setTextAlign(Paint.Align.LEFT);
        canvas.drawText("Name : " + name.getText().toString(), 396, 560, title);
        canvas.drawText("Date : " + date.getText().toString(), 396, 560, title);
        canvas.drawText("Beat : " + beat.getText().toString(), 396, 560, title);
        canvas.drawText("Total Outlet : " + total_outlet.getText().toString(), 396, 560, title);
        canvas.drawText("New Outlet : " + new_outlet.getText().toString(), 396, 560, title);
        canvas.drawText("Start Time : " + start_time.getText().toString(), 396, 560, title);

        // after adding all attributes to our
        // PDF file we will be finishing our page.
        pdfDocument.finishPage(myPage);

        // below line is used to set the name of
        // our PDF file and its path.
        File file = new File(Environment.getExternalStorageDirectory(), "DayStart.pdf");

        try {
            // after creating a file name we will
            // write our PDF file to that location.
            pdfDocument.writeTo(new FileOutputStream(file));

            // below line is to print toast message
            // on completion of PDF generation.
            Toast.makeText(EndTempActivity.this, "PDF file generated successfully.", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            // below line is used
            // to handle error
            e.printStackTrace();
        }
        // after storing our pdf to that
        // location we are closing our PDF file.
        pdfDocument.close();
    }

    private void createPdf(String name, String date, String beat, String tot_outlet, String new_outlet, String start_time) {
        // create a new document
        PdfDocument document = new PdfDocument();
        // crate a page description
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(300, 600, 1).create();
        // start a page
        PdfDocument.Page page = document.startPage(pageInfo);
        Canvas name1 = page.getCanvas();
        Canvas date1 = page.getCanvas();
        Canvas beat1 = page.getCanvas();
        Paint paint = new Paint();
        paint.setColor(R.drawable.logo_main);
        name1.drawCircle(50, 50, 30, paint);
        date1.drawCircle(50, 50, 30, paint);
        paint.setColor(Color.BLACK);
        name1.drawText(name, 80, 50, paint);
        date1.drawText(date, 80, 50, paint);
        beat1.drawText(beat, 80, 50, paint);
        //canvas.drawt
        // finish the page
        document.finishPage(page);
// draw text on the graphics object of the page
        // Create Page 2
        pageInfo = new PdfDocument.PageInfo.Builder(300, 600, 2).create();
        page = document.startPage(pageInfo);
        name1 = page.getCanvas();
        date1 = page.getCanvas();
        beat1 = page.getCanvas();
        paint = new Paint();
        paint.setColor(Color.BLUE);
        name1.drawCircle(100, 100, 100, paint);
        date1.drawCircle(100, 100, 100, paint);
        beat1.drawCircle(100, 100, 100, paint);
        document.finishPage(page);
        // write the document content
        String directory_path = Environment.getExternalStorageDirectory().getPath() + "/mypdf/";
        File file = new File(directory_path);
        if (!file.exists()) {
            file.mkdirs();
        }
        String targetPdf = directory_path + "day_start_report.pdf";
        File filePath = new File(targetPdf);
        try {
            document.writeTo(new FileOutputStream(filePath));
            Toast.makeText(this, "Done", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Log.e("main", "error " + e.toString());
            Toast.makeText(this, "Something wrong: " + e.toString(), Toast.LENGTH_LONG).show();
        }
        // close the document
        document.close();
    }
}