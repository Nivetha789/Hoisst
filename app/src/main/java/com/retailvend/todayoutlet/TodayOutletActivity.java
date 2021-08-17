package com.retailvend.todayoutlet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
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

public class TodayOutletActivity extends AppCompatActivity {

    RecyclerView todayOutletRecycler;
    Activity activity;
    LinearLayoutManager mLayoutManager;
    TodayOutletAdapter todayOutletAdapter;
    ImageView leftArrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today_outlet);
        activity=this;

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

        todayOutletRecycler=findViewById(R.id.today_outlet_recycler);
        leftArrow=findViewById(R.id.left_arrow);

        todayOutletAdapter = new TodayOutletAdapter(activity);
        mLayoutManager = new LinearLayoutManager(activity);
        //RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        todayOutletRecycler.setLayoutManager(mLayoutManager);
        todayOutletRecycler.setItemAnimator(new DefaultItemAnimator());
        todayOutletRecycler.setAdapter(todayOutletAdapter);

        leftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}