package com.retailvend.startTemp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.retailvend.R;
import com.retailvend.utills.CustomToast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;


public class StartTempActivity extends AppCompatActivity {

    SwipeRefreshLayout swipeRefresh;
    ProgressBar progress;
    public static Bitmap bitScroll;
    Menu menu;
    String which = "";

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_temp);

        if (android.os.Build.VERSION.SDK_INT >= 19) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimaryDark));
//            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
            window.setNavigationBarColor(this.getResources().getColor(R.color.colorAccent));
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        this.menu = menu;
//        menu.findItem(R.id.action_profile).setVisible(false);
//        menu.findItem(R.id.action_search).setVisible(false);
        menu.findItem(R.id.action_download).setVisible(false);

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
        if (id == R.id.action_download) {

//            bitScroll = getBitmapFromView(lin_invoice_details_scrollview, lin_invoice_details_scrollview.getChildAt(0).getHeight(), lin_invoice_details_scrollview.getChildAt(0).getWidth());


            createPdf();


            return true;
        }
//        else if (id == R.id.action_clock_out) {
////            showSusscesDialog();
//
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    private void createPdf() {

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
        if (which.equals("purchaseReport")) {
//            mPath = "/" + cmpyname + " Over All Purchase Report " + txt_report_filtter_date.getText().toString() + " " + now + ".pdf";
        } else {
//            mPath = "/" + cmpyname + " Over All Sales Report " + txt_report_filtter_date.getText().toString() + " " + now + ".pdf";
        }
        String root = (Environment.getExternalStorageDirectory().toString() + "/Noor Traders/");
//        File myDir = new File(root + TESS_DATA);
        File myDir = new File(root);
        myDir.mkdirs();

        String fname = mPath;
//        filePath = new File(myDir, fname);
//        if (filePath.exists()) filePath.delete();

//        filePath = new File(mPath);
        try {
//            FileOutputStream out = new FileOutputStream(filePath);
//            document.writeTo(new FileOutputStream(filePath));
//            out.flush();
//            out.close();
            CustomToast.getInstance(StartTempActivity.this).showSmallCustomToast("Pdf Files saved on Hoisst Folder");
        }
        catch (Exception e) {
//            e.printStackTrace();
//            Toast.makeText(this, "Something wrong: " + e.toString(), Toast.LENGTH_LONG).show();
//            CustomToast.getInstance(StartTempActivity.this).showSmallCustomToast("Something wrong: " + e.toString());
        }

        // close the document
        document.close();
    }
}