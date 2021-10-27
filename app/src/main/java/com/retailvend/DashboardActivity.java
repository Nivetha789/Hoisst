package com.retailvend;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.retailvend.collection.CollectionActivity;
import com.retailvend.orderList.OrderListActivity;
import com.retailvend.outstand.OutstandingActivity;
import com.retailvend.sales.SalesActivity;
import com.retailvend.todayoutlet.TodayOutletActivity;
import com.retailvend.utills.SessionManagerSP;
import com.retailvend.utills.SharedPrefManager;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class DashboardActivity extends AppCompatActivity {

    CardView collection_main_cardview, outlet_main_cardview, sales_main_cardview, outstand_main_cardview;
    ImageView profile;
    SessionManagerSP sessionManagerSP;
    ImageView navigaion;
    DrawerLayout drawerLayout;
    NavigationView navView;
    ImageView menu, close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_drawer);
        ButterKnife.bind(this);

        outlet_main_cardview = findViewById(R.id.outlet_main_cardview);
        collection_main_cardview = findViewById(R.id.collection_main_cardview);
        sales_main_cardview = findViewById(R.id.sales_main_cardview);
        outstand_main_cardview = findViewById(R.id.outstand_main_cardview);
        profile = findViewById(R.id.profile);
        navigaion = findViewById(R.id.menu);
        drawerLayout = findViewById(R.id.drawer_layout);
        navView = findViewById(R.id.nav_view);
        menu = findViewById(R.id.menu);
        close = findViewById(R.id.close);

        sessionManagerSP = new SessionManagerSP(DashboardActivity.this);

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


        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(DashboardActivity.this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(DashboardActivity.this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        outlet_main_cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DashboardActivity.this, TodayOutletActivity.class);
                startActivity(i);
            }
        });
        collection_main_cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent collectionIntent = new Intent(DashboardActivity.this, CollectionActivity.class);
                startActivity(collectionIntent);
            }
        });

        outstand_main_cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent outstandIntent = new Intent(DashboardActivity.this, OutstandingActivity.class);
                startActivity(outstandIntent);
            }
        });

        sales_main_cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent salesIntent = new Intent(DashboardActivity.this, SalesActivity.class);
                startActivity(salesIntent);
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent profileIntent = new Intent(DashboardActivity.this, ProfileActivity.class);
                startActivity(profileIntent);
            }
        });
    }


    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @OnClick({R.id.order_list_constrain, R.id.menu, R.id.logout_constrain, R.id.logout_txt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.order_list_constrain:
                Intent orderIntent = new Intent(this, OrderListActivity.class);
                startActivity(orderIntent);
                break;
            case R.id.menu:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
//            case R.id.close:
//                drawerLayout.closeDrawer(GravityCompat.START);
//                break;
            case R.id.logout_constrain:
            case R.id.logout_txt:
                AlertDialog.Builder builder = new AlertDialog.Builder(DashboardActivity.this);
                builder.setMessage("Are you sure you want to logout?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", (dialog, which) -> {
                            sessionManagerSP.setPhonelogin("0");
                            sessionManagerSP.setMobile("");
                            sessionManagerSP.setPass("");
                            sessionManagerSP.setSalesNameId("");
                            sessionManagerSP.setSalesName("");
                            SharedPrefManager.getInstance(getApplicationContext()).clear();
                            Intent loginIntent = new Intent(DashboardActivity.this, LoginActivity.class);
                            TaskStackBuilder.create(this).addNextIntentWithParentStack(loginIntent).startActivities();
                        })
                        .setNegativeButton("No", (dialog, which) -> dialog.dismiss());
                AlertDialog alert = builder.create();
                alert.setTitle("Confirm Logout..!!!");
                alert.show();
                break;
        }
    }

    public static void setWindowFlag(Activity activity, final int bits, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }
}