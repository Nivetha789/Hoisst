package com.retailvend;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.retailvend.broadcast.ConnectivityReceiver;
import com.retailvend.changePass.ChangePasswordActivity;
import com.retailvend.collateral.CollateralsActivity;
import com.retailvend.collection.CollectionActivity;
import com.retailvend.createOutlet.CreateOutletsActivity;
import com.retailvend.deliveryman.InvoiceList.InvoiceListActivity;
import com.retailvend.deliveryman.collection.CollectionDeliveryActivity;
import com.retailvend.deliveryman.outlet.DelManTodayOutletsActivity;
import com.retailvend.deliveryman.outstand.DelManOutstandActivity;
import com.retailvend.endTemp.EndTempActivity;
import com.retailvend.model.dashboard.SalesDashboardCountDatum;
import com.retailvend.model.dashboard.SalesDashboardCountModel;
import com.retailvend.orderList.OrderListActivity;
import com.retailvend.outstand.OutstandingActivity;
import com.retailvend.retrofit.RetrofitClient;
import com.retailvend.startTemp.StartTempActivity;
import com.retailvend.targetDetails.TargetDetailsActivity;
import com.retailvend.todayoutlet.TodayOutletActivity;
import com.retailvend.utills.CustomProgress;
import com.retailvend.utills.CustomToast;
import com.retailvend.utills.SessionManagerSP;
import com.retailvend.utills.SharedPrefManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardActivity extends AppCompatActivity {

    CardView collection_main_cardview, outlet_main_cardview, sales_main_cardview, outstand_main_cardview;
    ImageView profile;
    SessionManagerSP sessionManagerSP;
    ImageView navigaion;
    DrawerLayout drawerLayout;
    NavigationView navView;
    ImageView menu, close, create_outlet_img;
    String login_type = "";
    TextView sales_main_txt, sales_list;
    String distributor_id = "";
    LinearLayout sales_man_count_details, del_man_cnt_details;
    Activity activity;
    TextView nodata_txt;
    TextView tot_outlet_del_count, visit_outlet_del_count, pending_del_count, tot_outlet_count, visit_outlet_count,
            pending_count, target_count, achievements_count, order_count, order_tot_count, order_list, create_outlet_list;
    ConstraintLayout no_data_constrain, today_outlet_menu, target_menu, start_temp_menu, end_temp_menu, collection_menu,
            create_outlet_menu, collaterals_menu;
    List<SalesDashboardCountDatum> salesDashboardCountData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_drawer);
        ButterKnife.bind(this);
        activity = this;

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
        sales_man_count_details = findViewById(R.id.sales_man_count_details);
        del_man_cnt_details = findViewById(R.id.del_man_cnt_details);
        nodata_txt = findViewById(R.id.nodata_txt);
        no_data_constrain = findViewById(R.id.no_data_constrain);
        tot_outlet_del_count = findViewById(R.id.tot_outlet_del_count);
        visit_outlet_del_count = findViewById(R.id.visit_outlet_del_count);
        pending_del_count = findViewById(R.id.pending_del_count);
        tot_outlet_count = findViewById(R.id.tot_outlet_count);
        visit_outlet_count = findViewById(R.id.visit_outlet_count);
        pending_count = findViewById(R.id.pending_count);
        target_count = findViewById(R.id.target_count);
        achievements_count = findViewById(R.id.achievements_count);
        order_count = findViewById(R.id.order_count);
        order_tot_count = findViewById(R.id.order_tot_count);
        order_list = findViewById(R.id.order_list);
        today_outlet_menu = findViewById(R.id.today_outlet_menu);
        target_menu = findViewById(R.id.target_menu);
        start_temp_menu = findViewById(R.id.start_temp_menu);
        end_temp_menu = findViewById(R.id.end_temp_menu);
        collection_menu = findViewById(R.id.collection_menu);
        create_outlet_menu = findViewById(R.id.create_outlet_menu);
        create_outlet_img = findViewById(R.id.create_outlet_img);
        create_outlet_list = findViewById(R.id.create_outlet_list);
        collaterals_menu = findViewById(R.id.collaterals_menu);

        sessionManagerSP = new SessionManagerSP(DashboardActivity.this);

        salesDashboardCountData = new ArrayList<>();

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
            sales_man_count_details.setVisibility(View.GONE);
            del_man_cnt_details.setVisibility(View.VISIBLE);
            sales_main_txt.setText("DELIVERY DETAILS");
            sales_main_txt.setTextSize(15);
            Typeface font = Typeface.createFromAsset(
                    this.getAssets(),
                    "font/quicksand_medium.ttf");
            sales_main_txt.setTypeface(font);
            sales_main_txt.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

            order_list.setText("INVOICE LIST");

            today_outlet_menu.setVisibility(View.GONE);
            target_menu.setVisibility(View.GONE);
            start_temp_menu.setVisibility(View.GONE);
            end_temp_menu.setVisibility(View.GONE);
            create_outlet_menu.setVisibility(View.GONE);
            collaterals_menu.setVisibility(View.GONE);

//            Typeface font1 = Typeface.createFromAsset(
//                    this.getAssets(),
//                    "font/quicksand_medium.ttf");
//            sales_main_txt.setTypeface(font);
//            sales_main_txt.setText("DELIVERY LIST");
        } else {
            collection_menu.setVisibility(View.GONE);
        }
//        } else {
//            sales_man_count_details.setVisibility(View.VISIBLE);
//            del_man_cnt_details.setVisibility(View.GONE);
//            sales_main_txt.setText("SALES DETAILS");
//            sales_main_txt.setTextSize(15);
//            Typeface font = Typeface.createFromAsset(
//                    this.getAssets(),
//                    "font/quicksand_medium.ttf");
//            sales_main_txt.setTypeface(font);
//            sales_main_txt.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
//            Typeface font1 = Typeface.createFromAsset(
//                    this.getAssets(),
//                    "font/quicksand_bold.ttf");
//            sales_list.setTypeface(font1);
//            sales_list.setText("SALES LIST");
//        }

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
                    Intent collectionIntent1 = new Intent(DashboardActivity.this, CollectionDeliveryActivity.class);
                    startActivity(collectionIntent1);
                }
            }
        });

        outstand_main_cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("distributor_id " + distributor_id);
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
                    Intent salesIntent = new Intent(DashboardActivity.this, OrderListActivity.class);
                    startActivity(salesIntent);
                } else {
                    Intent delIntent = new Intent(DashboardActivity.this, InvoiceListActivity.class);
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

        boolean isConnected = ConnectivityReceiver.isConnected();
        if (isConnected) {
            countDetailsApi();
        } else {
            CustomToast.getInstance(DashboardActivity.this).showSmallCustomToast("Please check your internet connection");
        }
    }


    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @OnClick({R.id.order_list_constrain, R.id.menu, R.id.logout_constrain, R.id.logout_txt, R.id.collection_menu, R.id.today_outlet_menu,
            R.id.outstand_menu, R.id.target_menu, R.id.start_temp_menu, R.id.end_temp_menu, R.id.change_pass_menu,
            R.id.create_outlet_menu, R.id.collaterals_menu})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.order_list_constrain:
                if (distributor_id.equals("0")) {
                    Intent orderIntent = new Intent(this, OrderListActivity.class);
                    startActivity(orderIntent);
                } else {
                    Intent orderIntent = new Intent(this, InvoiceListActivity.class);
                    startActivity(orderIntent);
                }
                break;
            case R.id.collection_menu:
                if (distributor_id.equals("0")) {
                    Intent collectionIntent = new Intent(DashboardActivity.this, CollectionActivity.class);
                    startActivity(collectionIntent);
                } else {
                    Intent collectionIntent1 = new Intent(DashboardActivity.this, CollectionDeliveryActivity.class);
                    startActivity(collectionIntent1);
                }
                break;
            case R.id.today_outlet_menu:
                System.out.println("iddddd idjdj " + distributor_id);
                if (distributor_id.equals("0")) {
                    Intent i = new Intent(DashboardActivity.this, TodayOutletActivity.class);
                    startActivity(i);
                } else {
                    Intent todayOutletIntent1 = new Intent(DashboardActivity.this, DelManTodayOutletsActivity.class);
                    startActivity(todayOutletIntent1);
                }
                break;
            case R.id.outstand_menu:
                if (distributor_id.equals("0")) {
                    Intent outstandIntent = new Intent(DashboardActivity.this, OutstandingActivity.class);
                    startActivity(outstandIntent);
                } else {
                    Intent outstandIntent1 = new Intent(DashboardActivity.this, DelManOutstandActivity.class);
                    startActivity(outstandIntent1);
                }
                break;
            case R.id.menu:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
//            case R.id.close:
//                drawerLayout.closeDrawer(GravityCompat.START);
//                break;
            case R.id.target_menu:
                if (distributor_id.equals("0")) {
                    Intent salesIntent = new Intent(DashboardActivity.this, TargetDetailsActivity.class);
                    startActivity(salesIntent);
                } else {
                    Intent delIntent = new Intent(DashboardActivity.this, TargetDetailsActivity.class);
                    startActivity(delIntent);
                }
                break;

            case R.id.start_temp_menu:
                Intent startIntent = new Intent(DashboardActivity.this, StartTempActivity.class);
                startActivity(startIntent);
                break;

            case R.id.end_temp_menu:
                Intent endIntent = new Intent(DashboardActivity.this, EndTempActivity.class);
                startActivity(endIntent);
                break;

            case R.id.change_pass_menu:
                Intent delIntent = new Intent(DashboardActivity.this, ChangePasswordActivity.class);
                startActivity(delIntent);
                break;
            case R.id.create_outlet_menu:
                Intent createOutletIntent = new Intent(DashboardActivity.this, CreateOutletsActivity.class);
                startActivity(createOutletIntent);
                break;
            case R.id.collaterals_menu:
                Intent collateralsIntent = new Intent(DashboardActivity.this, CollateralsActivity.class);
                startActivity(collateralsIntent);
                break;
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

    @Override
    protected void onRestart() {
        super.onRestart();

        boolean isConnected = ConnectivityReceiver.isConnected();
        if (isConnected) {
            countDetailsApi();
        } else {
            CustomToast.getInstance(DashboardActivity.this).showSmallCustomToast("Please check your internet connection");
        }
    }

    public void countDetailsApi() {
        CustomProgress.showProgress(activity);
        String emp_id = sessionManagerSP.getEmployeeId();
        System.out.println("emmmpidd " + emp_id);

        Call<SalesDashboardCountModel> call = RetrofitClient
                .getInstance().getApi().dashboardCount("_employeeDashboard", emp_id);

        call.enqueue(new Callback<SalesDashboardCountModel>() {
            @Override
            public void onResponse(@NonNull Call<SalesDashboardCountModel> call, @NonNull Response<SalesDashboardCountModel> response) {

                try {

                    Gson gson = new Gson();
                    String json = gson.toJson(response.body());
//                    System.out.println("responseOutletsss "+response.body());

                    SalesDashboardCountModel salesDashboardCountModel = gson.fromJson(json, SalesDashboardCountModel.class);
                    String s = salesDashboardCountModel.getMessage();

                    if (salesDashboardCountModel.getStatus() == 1) {
//                        no_data_constrain.setVisibility(View.GONE);
//                        nodata_txt.setText("");
                        salesDashboardCountData = salesDashboardCountModel.getData();

                        //del man
                        tot_outlet_del_count.setText(salesDashboardCountData.get(0).getTotalInvoice());
                        visit_outlet_del_count.setText(salesDashboardCountData.get(0).getVisitInvoice());
                        pending_del_count.setText(salesDashboardCountData.get(0).getPendingInvoice());

                        //sales man
                        tot_outlet_count.setText(salesDashboardCountData.get(0).getTotalOutlet());
                        visit_outlet_count.setText(salesDashboardCountData.get(0).getVisitOutlet());
                        pending_count.setText(salesDashboardCountData.get(0).getPendingOutlet());
                        target_count.setText(salesDashboardCountData.get(0).getTargetValue());
                        achievements_count.setText(salesDashboardCountData.get(0).getAchievement());
                        order_count.setText(salesDashboardCountData.get(0).getOrderCount());
                        order_tot_count.setText(salesDashboardCountData.get(0).getOrderTotal());

                        CustomProgress.hideProgress(activity);

                    } else {
                        CustomProgress.hideProgress(activity);
//                        CustomToast.getInstance(TodayOutletActivity.this).showSmallCustomToast(todayOutletList.getMessage());
//                        no_data_constrain.setVisibility(View.VISIBLE);
//                        nodata_txt.setText(salesDashboardCountModel.getMessage());
//                        System.out.println("nodata "+salesDashboardCountModel.getMessage());
                    }

                } catch (Exception e) {
                    Log.d("Exception", e.getMessage());
                    CustomProgress.hideProgress(activity);
//                    no_data_constrain.setVisibility(View.VISIBLE);
//                    nodata_txt.setText("");
                }

            }

            @Override
            public void onFailure(@NonNull Call<SalesDashboardCountModel> call, @NonNull Throwable t) {
                Log.d("Failure ", t.getMessage());
//                CustomToast.getInstance(TodayOutletActivity.this).showSmallCustomToast("Something went wrong try again..");
//                text_signIn.setVisibility(View.VISIBLE);
                CustomProgress.hideProgress(activity);
//                no_data_constrain.setVisibility(View.VISIBLE);
//                nodata_txt.setText("Something went wrong try again..");
            }
        });
    }
}