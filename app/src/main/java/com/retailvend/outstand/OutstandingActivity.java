package com.retailvend.outstand;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.retailvend.R;

public class OutstandingActivity extends AppCompatActivity {

    RecyclerView outstandingRecycler;
    Activity activity;
    LinearLayoutManager mLayoutManager;
    OutstandingAdapter outstandingAdapter;
    ImageView leftArrow;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outstanding);

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

        toolbar = findViewById(R.id.toolbar);
        outstandingRecycler = findViewById(R.id.outstanding_recyclerView);
        leftArrow = findViewById(R.id.left_arrow);


//        swipeRefresh.setOnRefreshListener(this);
//        salesRecycler.setHasFixedSize(true);
//        saleslists = new ArrayList<>();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        outstandingRecycler.setLayoutManager(layoutManager);

        outstandingAdapter = new OutstandingAdapter(this);
        outstandingRecycler.setAdapter(outstandingAdapter);

        leftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
//        refreshActivity();
//        RegistorSharedPreManager.getInstance(DetailsActivity.this).clear();
        super.onBackPressed();
    }
}