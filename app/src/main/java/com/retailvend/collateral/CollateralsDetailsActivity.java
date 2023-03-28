package com.retailvend.collateral;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.halilibo.bvpkotlin.BetterVideoPlayer;
import com.retailvend.R;
import com.retailvend.broadcast.ConnectivityReceiver;
import com.retailvend.model.collateralsDetails.CollateralsDetailsData;
import com.retailvend.model.collateralsDetails.CollateralsDetailsModel;
import com.retailvend.retrofit.RetrofitClient;
import com.retailvend.utills.CustomToast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CollateralsDetailsActivity extends AppCompatActivity{

    TextView txt_empty, name,view_pdf_desc;
    ProgressBar progress;
    NestedScrollView lin_invoice_details_scrollview;
    String random_value = "";
    Activity activity;
    ImageView collaterals_image,left_arrow;

    BetterVideoPlayer betterVideoPlayer;

    CollateralsDetailsData collateralsDetailsData;
    LinearLayout image_vis, video_control, pdf_viewer;

    String pdfUrl="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collaterals_details);
        activity = this;

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
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            random_value = getIntent().getExtras().getString("random_value");
            System.out.println("random_value " + random_value);
        }
        progress = findViewById(R.id.progress);
        txt_empty = findViewById(R.id.txt_empty);
        name = findViewById(R.id.name);
        lin_invoice_details_scrollview = findViewById(R.id.lin_invoice_details_scrollview);
        image_vis = findViewById(R.id.image_vis);
        video_control = findViewById(R.id.video_control);
        collaterals_image = findViewById(R.id.collaterals_image);
        betterVideoPlayer = findViewById(R.id.video_player);
        pdf_viewer = findViewById(R.id.pdf_viewer);
        view_pdf_desc = findViewById(R.id.view_pdf_desc);
        left_arrow = findViewById(R.id.left_arrow);

//        betterVideoPlayer.setCallback(this);

        pdf_viewer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(pdfUrl));
                startActivity(browserIntent);
            }
        });

        left_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        boolean isConnected = ConnectivityReceiver.isConnected();
        if (isConnected) {
            collateralsDetailsApi(random_value);
        } else {
            CustomToast.getInstance(CollateralsDetailsActivity.this).showSmallCustomToast("Please check your internet connection");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        // Make sure the player stops playing if the user presses the home button.
        betterVideoPlayer.pause();
    }

    // Methods for the implemented EasyVideoCallback


    //collaterals details
    public void collateralsDetailsApi(String randomValue) {
//        System.out.println("orderIDDDDD :"+orderId);

        progress.setVisibility(View.VISIBLE);
        lin_invoice_details_scrollview.setVisibility(View.GONE);
        txt_empty.setVisibility(View.GONE);


        Call<CollateralsDetailsModel> call = RetrofitClient
                .getInstance().getApi().collateralsListDetails("_collateralsDetails", randomValue);


        call.enqueue(new Callback<CollateralsDetailsModel>() {
            @Override
            public void onResponse(@NonNull Call<CollateralsDetailsModel> call, @NonNull Response<CollateralsDetailsModel> response) {

                try {

                    Gson gson = new Gson();
                    String json = gson.toJson(response.body());
                    CollateralsDetailsModel collateralsDetailsModel = gson.fromJson(json, CollateralsDetailsModel.class);

                    if (collateralsDetailsModel.getStatus() == 1) {
                        if (collateralsDetailsModel.getData() != null) {
                            collateralsDetailsData = collateralsDetailsModel.getData();

                            name.setText(collateralsDetailsData.getName());

                            if (collateralsDetailsData.getFileType().equals("Image")) {
                                image_vis.setVisibility(View.VISIBLE);
                                video_control.setVisibility(View.GONE);
                                pdf_viewer.setVisibility(View.GONE);

                                String url = collateralsDetailsData.getFile();
                                view_pdf_desc.setText(collateralsDetailsData.getDescription());
                                Glide.with(CollateralsDetailsActivity.this).load(url).into(collaterals_image);

                            } else if (collateralsDetailsData.getFileType().equals("Video")) {
                                image_vis.setVisibility(View.GONE);
                                video_control.setVisibility(View.VISIBLE);
                                pdf_viewer.setVisibility(View.GONE);

                                String url = collateralsDetailsData.getFile();
                                betterVideoPlayer.setSource(Uri.parse(url));
                                betterVideoPlayer.setAutoPlay(true);
                                view_pdf_desc.setText(collateralsDetailsData.getDescription());

                            } else {
                                video_control.setVisibility(View.GONE);
                                image_vis.setVisibility(View.GONE);
                                pdf_viewer.setVisibility(View.VISIBLE);
                                pdfUrl= collateralsDetailsData.getFile();
                                System.out.println("urllllll123 "+pdfUrl);
                                view_pdf_desc.setText(collateralsDetailsData.getDescription());

                            }
                            progress.setVisibility(View.GONE);
                            lin_invoice_details_scrollview.setVisibility(View.VISIBLE);
                            txt_empty.setVisibility(View.GONE);
                        } else {
                            progress.setVisibility(View.GONE);
                            lin_invoice_details_scrollview.setVisibility(View.GONE);
                            txt_empty.setVisibility(View.VISIBLE);
                            CustomToast.getInstance(CollateralsDetailsActivity.this).showSmallCustomToast(collateralsDetailsModel.getMessage());
                        }
                    } else {
                        progress.setVisibility(View.GONE);
                        lin_invoice_details_scrollview.setVisibility(View.GONE);
                        txt_empty.setVisibility(View.VISIBLE);
                        CustomToast.getInstance(CollateralsDetailsActivity.this).showSmallCustomToast(collateralsDetailsModel.getMessage());
                    }
                } catch (Exception e) {
                    Log.d("Exception", e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<CollateralsDetailsModel> call, Throwable t) {
                Log.d("Failure ", t.getMessage());
                progress.setVisibility(View.GONE);
                lin_invoice_details_scrollview.setVisibility(View.GONE);
                txt_empty.setVisibility(View.VISIBLE);
                txt_empty.setText("Something went wrong try again..");
                CustomToast.getInstance(CollateralsDetailsActivity.this).showSmallCustomToast("Something went wrong try again..");

            }
        });
    }
}