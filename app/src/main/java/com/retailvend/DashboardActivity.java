package com.retailvend;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.retailvend.collection.CollectionActivity;
import com.retailvend.deliveryman.deliveryDetails.DeliveryDetailsActivity;
import com.retailvend.deliveryman.outlet.DelManTodayOutletsActivity;
import com.retailvend.deliveryman.outstand.DelManOutstandActivity;
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
    String login_type = "";
    TextView sales_main_txt;
    String distributor_id = "";

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
        drawerLayout = findViewById(R.id.drawer_layout);
        navView = findViewById(R.id.nav_view);
        menu = findViewById(R.id.menu);
        close = findViewById(R.id.close);
        sales_main_txt = findViewById(R.id.sales_main_txt);

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
        login_type = sessionManagerSP.getLoginType();
        distributor_id = sessionManagerSP.getDistributorId();

        if (login_type.equals("1")) {
            sales_main_txt.setText("DELIVERY DETAILS");
            sales_main_txt.setTextSize(15);
            Typeface font = Typeface.createFromAsset(
                    this.getAssets(),
                    "font/quicksand_medium.ttf");
            sales_main_txt.setTypeface(font);
            sales_main_txt.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        } else {
            sales_main_txt.setText("SALES DETAILS");
            sales_main_txt.setTextSize(15);
            Typeface font = Typeface.createFromAsset(
                    this.getAssets(),
                    "font/quicksand_medium.ttf");
            sales_main_txt.setTypeface(font);
            sales_main_txt.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        }

        outlet_main_cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (distributor_id.equals("0")) {
                    Intent i = new Intent(DashboardActivity.this, TodayOutletActivity.class);
                    startActivity(i);
                } else {
                    Intent todayOutletIntent1 = new Intent(DashboardActivity.this, DelManTodayOutletsActivity.class);
                    startActivity(todayOutletIntent1);
                }
            }
        });
        collection_main_cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                System.out.println("distributoridddd " + distributor_id);
                if (distributor_id.equals("0")) {
                    Intent collectionIntent = new Intent(DashboardActivity.this, CollectionActivity.class);
                    startActivity(collectionIntent);
                } else {
                    Intent collectionIntent1 = new Intent(DashboardActivity.this, CollectionActivity.class);
                    startActivity(collectionIntent1);
                }
            }
        });

        outstand_main_cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("distributor_id "+distributor_id);
                if (distributor_id.equals("0")) {
                    Intent outstandIntent = new Intent(DashboardActivity.this, OutstandingActivity.class);
                    startActivity(outstandIntent);
                } else {
                    Intent outstandIntent1 = new Intent(DashboardActivity.this, DelManOutstandActivity.class);
                    startActivity(outstandIntent1);
                }
            }
        });

        sales_main_cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (distributor_id.equals("0")) {
                    Intent salesIntent = new Intent(DashboardActivity.this, SalesActivity.class);
                    startActivity(salesIntent);
                } else {
                    Intent delIntent = new Intent(DashboardActivity.this, DeliveryDetailsActivity.class);
                    startActivity(delIntent);
                }
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
                            SharedPrefManager.getInstance(getApplicationContext()).clear();
                            sessionManagerSP.setPhonelogin("0");
                            sessionManagerSP.setMobile("");
                            sessionManagerSP.setPass("");
                            sessionManagerSP.setSalesNameId("");
                            sessionManagerSP.setSalesName("");
                            sessionManagerSP.setDistributorId("");
                            sessionManagerSP.setEmployeeId("");
                            sessionManagerSP.setLoginType("");
                            sessionManagerSP.setLat("");
                            sessionManagerSP.setLong("");
                            sessionManagerSP.setAttendanceId("");
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