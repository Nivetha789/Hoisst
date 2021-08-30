package com.retailvend;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.retailvend.collection.CollectionActivity;
import com.retailvend.outstand.OutstandingActivity;
import com.retailvend.sales.SalesActivity;
import com.retailvend.todayoutlet.TodayOutletActivity;

public class DashboardActivity extends AppCompatActivity {

    CardView todayOutletCardview;
    CardView collectionCard;
    CardView outstanding_cardview;
    ImageView profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        todayOutletCardview=findViewById(R.id.today_outlet_cardview);
        collectionCard=findViewById(R.id.collection_cardview);
        outstanding_cardview=findViewById(R.id.outstanding_cardview);
        profile=findViewById(R.id.profile);

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
        todayOutletCardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DashboardActivity.this, TodayOutletActivity.class);
                startActivity(i);
            }
        });
        collectionCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent collectionIntent=new Intent(DashboardActivity.this, CollectionActivity.class);
                startActivity(collectionIntent);
            }
        });

        outstanding_cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent outstandIntent=new Intent(DashboardActivity.this, OutstandingActivity.class);
                startActivity(outstandIntent);
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent profileIntent=new Intent(DashboardActivity.this, ProfileActivity.class);
                startActivity(profileIntent);
            }
        });
    }
}