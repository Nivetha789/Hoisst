package com.retailvend.targetDetails;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.gson.Gson;
import com.retailvend.DashboardActivity;
import com.retailvend.R;
import com.retailvend.broadcast.ConnectivityReceiver;
import com.retailvend.model.dashboard.SalesDashboardCountModel;
import com.retailvend.model.targetDetailssales.EmployeeTargetDetailsData.EmployeeTargetBeatTarget;
import com.retailvend.model.targetDetailssales.EmployeeTargetDetailsData.EmployeeTargetDetailsDatum;
import com.retailvend.model.targetDetailssales.EmployeeTargetDetailsData.EmployeeTargetDetailsModel;
import com.retailvend.model.targetDetailssales.EmployeeTargetDetailsData.EmployeeTargetProductTarget;
import com.retailvend.model.targetDetailssales.TargetDetailsData;
import com.retailvend.model.targetDetailssales.TargetDetailsModel;
import com.retailvend.model.targetDetailssales.TargetDetailsTarget;
import com.retailvend.orderList.OrderListActivity;
import com.retailvend.orderList.OrderListAdapter;
import com.retailvend.retrofit.RetrofitClient;
import com.retailvend.utills.CustomProgress;
import com.retailvend.utills.CustomToast;
import com.retailvend.utills.SessionManagerSP;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TargetDetailsActivity extends AppCompatActivity {

    ImageView left_arrow,nodata1,nodata;
    Activity activity;
    SessionManagerSP sessionManagerSP;
    BarChart barChart;
    List<TargetDetailsTarget> targetDetailsDataList;
    TextView target,achieve,emptyView,emptyView1;
    String year="";
    ArrayList<TargetDetailsTarget> data = new ArrayList<>();
    ArrayList<BarEntry> values = new ArrayList<>();
    ProductTargetAdapter productTargetAdapter;
    BeatTargetAdapter beatTargetAdapter;
    RecyclerView prod_target_recycler,beat_target_recycler;
    List<EmployeeTargetDetailsDatum> employeeTargetDetailsData;
    List<EmployeeTargetProductTarget> employeeTargetProductTargetList;
    List<EmployeeTargetBeatTarget> employeeTargetBeatTargets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_target_details);
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

        sessionManagerSP = new SessionManagerSP(TargetDetailsActivity.this);
        targetDetailsDataList=new ArrayList<>();
        employeeTargetDetailsData=new ArrayList<>();
        employeeTargetProductTargetList=new ArrayList<>();
        employeeTargetBeatTargets=new ArrayList<>();

        left_arrow=findViewById(R.id.left_arrow);
        prod_target_recycler=findViewById(R.id.prod_target_recycler);
        beat_target_recycler=findViewById(R.id.beat_target_recycler);
        barChart=findViewById(R.id.chart);
        target=findViewById(R.id.target);
        achieve=findViewById(R.id.achieve);
        emptyView=findViewById(R.id.emptyView);
        emptyView1=findViewById(R.id.emptyView1);
        nodata1=findViewById(R.id.nodata1);
        nodata=findViewById(R.id.nodata);

        left_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        boolean isConnected = ConnectivityReceiver.isConnected();
        if (isConnected) {
            targetDetailsApi();
            targetDetailsDataApi();
        } else {
            CustomToast.getInstance(TargetDetailsActivity.this).showSmallCustomToast("Please check your internet connection");
        }
    }

    public void targetDetailsApi() {
        CustomProgress.showProgress(activity);
        String emp_id= sessionManagerSP.getEmployeeId();
        System.out.println("emmmpidd "+emp_id);

        Call<TargetDetailsModel> call = RetrofitClient
                .getInstance().getApi().targetDetails("_targetDetails",emp_id);

        call.enqueue(new Callback<TargetDetailsModel>() {
            @Override
            public void onResponse(@NonNull Call<TargetDetailsModel> call, @NonNull Response<TargetDetailsModel> response) {

                try {

                    Gson gson = new Gson();
                    String json = gson.toJson(response.body());
//                    System.out.println("responseOutletsss "+response.body());

                    TargetDetailsModel detailsModel = gson.fromJson(json, TargetDetailsModel.class);
                    String s = detailsModel.getMessage();

                    if (detailsModel.getStatus()==1) {
//                        no_data_constrain.setVisibility(View.GONE);
//                        nodata_txt.setText("");
                        target.setText(detailsModel.getData().get(0).getTargetVal());
                        achieve.setText(detailsModel.getData().get(0).getAchieveVal());
                        targetDetailsDataList = detailsModel.getData().get(0).getTargetList();

                        for (int i = 0; i < targetDetailsDataList.size(); i++) {
                            TargetDetailsTarget dataObject = targetDetailsDataList.get(i);
                            if(dataObject.getValue()!=0){
                                String[] separated = dataObject.getDate().split("-");
                                String separate=separated[0];
                                String separate1=separated[1];
                                String separate2=separated[2];
//                                System.out.println("separate2 "+separate2);
                                values.add(new BarEntry(Float.parseFloat(separate2), Float.parseFloat(dataObject.getValue().toString())));
                            }
                        }
//                        targets.add(new BarEntry(Float.parseFloat(year), 508));

//                        targets.add(new BarEntry(2015, 475));
//                        targets.add(new BarEntry(2016, 508));
//                        targets.add(new BarEntry(2017, 660));
//                        targets.add(new BarEntry(2018, 550));
                        BarDataSet barDataSet=new BarDataSet(values,"Achievements");
                        barDataSet.setColors(Color.GREEN);
                        barDataSet.setValueTextColor(Color.BLACK);
                        barDataSet.setValueTextSize(16f);

                        BarData barData= new BarData(barDataSet);

                        barChart.setFitBars(false);
                        barChart.setData(barData);
                        barChart.getDescription().setText("");
                        barChart.animateY(2000);

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
            public void onFailure(@NonNull Call<TargetDetailsModel> call, @NonNull Throwable t) {
                Log.d("Failure ", t.getMessage());
//                CustomToast.getInstance(TodayOutletActivity.this).showSmallCustomToast("Something went wrong try again..");
//                text_signIn.setVisibility(View.VISIBLE);
                CustomProgress.hideProgress(activity);
//                no_data_constrain.setVisibility(View.VISIBLE);
//                nodata_txt.setText("Something went wrong try again..");
            }
        });
    }

    public void targetDetailsDataApi() {
//        CustomProgress.showProgress(activity);
        String emp_id= sessionManagerSP.getEmployeeId();
        System.out.println("_employeeTargetData "+emp_id);

        Call<EmployeeTargetDetailsModel> call = RetrofitClient
                .getInstance().getApi().employeeTargetDetails("_employeeTargetData",emp_id);

        call.enqueue(new Callback<EmployeeTargetDetailsModel>() {
            @Override
            public void onResponse(@NonNull Call<EmployeeTargetDetailsModel> call, @NonNull Response<EmployeeTargetDetailsModel> response) {

                try {

                    Gson gson = new Gson();
                    String json = gson.toJson(response.body());

                    EmployeeTargetDetailsModel detailsModel = gson.fromJson(json, EmployeeTargetDetailsModel.class);
                    String s = detailsModel.getMessage();

                    if (detailsModel.getStatus()==1) {
//                        System.out.println("_employeeTargetData_employeeTargetData "+response.body());

//                        CustomProgress.hideProgress(activity);

                        employeeTargetDetailsData=detailsModel.getData();
                        if(employeeTargetDetailsData!=null){
//                            System.out.println("xxxxxxxffffffff "+employeeTargetDetailsData.toString());

//                            emptyView.setText(detailsModel.getMessage());
                            emptyView.setVisibility(View.GONE);
                            nodata.setVisibility(View.GONE);
                            nodata1.setVisibility(View.GONE);
                            prod_target_recycler.setVisibility(View.VISIBLE);
                            beat_target_recycler.setVisibility(View.VISIBLE);
                            employeeTargetProductTargetList=employeeTargetDetailsData.get(0).getProductTarget();
                            employeeTargetBeatTargets=employeeTargetDetailsData.get(0).getBeatTarget();
                                emptyView.setVisibility(View.GONE);
                                prod_target_recycler.setVisibility(View.VISIBLE);
                                LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
                                prod_target_recycler.setLayoutManager(layoutManager);

                                productTargetAdapter = new ProductTargetAdapter(TargetDetailsActivity.this, employeeTargetProductTargetList);
                                prod_target_recycler.setAdapter(productTargetAdapter);

                                LinearLayoutManager layoutManager1 = new LinearLayoutManager(activity);
                                beat_target_recycler.setLayoutManager(layoutManager1);

                                beatTargetAdapter = new BeatTargetAdapter(TargetDetailsActivity.this, employeeTargetBeatTargets);
                                beat_target_recycler.setAdapter(beatTargetAdapter);
                                beatTargetAdapter.notifyDataSetChanged();

                        }else {
//                            CustomProgress.hideProgress(activity);
//                        CustomToast.getInstance(TodayOutletActivity.this).showSmallCustomToast(todayOutletList.getMessage());
//                        no_data_constrain.setVisibility(View.VISIBLE);
                            nodata.setVisibility(View.VISIBLE);
                            nodata1.setVisibility(View.VISIBLE);
                            emptyView.setText(detailsModel.getMessage());
                            emptyView.setVisibility(View.VISIBLE);
                            emptyView1.setText(detailsModel.getMessage());
                            emptyView1.setVisibility(View.VISIBLE);
                            prod_target_recycler.setVisibility(View.GONE);
                            beat_target_recycler.setVisibility(View.GONE);
//                        System.out.println("nodata "+salesDashboardCountModel.getMessage());
                        }

                    } else {
//                        CustomToast.getInstance(TodayOutletActivity.this).showSmallCustomToast(todayOutletList.getMessage());
//                        no_data_constrain.setVisibility(View.VISIBLE);
                        emptyView.setText(detailsModel.getMessage());
                        nodata.setVisibility(View.VISIBLE);
                        nodata1.setVisibility(View.VISIBLE);
                        emptyView.setVisibility(View.VISIBLE);
                        emptyView1.setText(detailsModel.getMessage());
                        emptyView1.setVisibility(View.VISIBLE);
                        prod_target_recycler.setVisibility(View.GONE);
                        beat_target_recycler.setVisibility(View.GONE);
                        System.out.println("nodata "+detailsModel.getMessage());
//                        CustomProgress.hideProgress(activity);

                    }

                } catch (Exception e) {
                    Log.d("Exceptioneeeeee", e.getMessage());
//                    CustomProgress.hideProgress(activity);
//                    no_data_constrain.setVisibility(View.VISIBLE);
//                    nodata_txt.setText("");
                }

            }

            @Override
            public void onFailure(@NonNull Call<EmployeeTargetDetailsModel> call, @NonNull Throwable t) {
                Log.d("Failure ", t.getMessage());
//                CustomToast.getInstance(TodayOutletActivity.this).showSmallCustomToast("Something went wrong try again..");
//                CustomProgress.hideProgress(activity);
                nodata.setVisibility(View.VISIBLE);
                nodata1.setVisibility(View.VISIBLE);
                emptyView.setVisibility(View.VISIBLE);
                emptyView1.setText("Something went wrong try again..");
                emptyView1.setVisibility(View.VISIBLE);
                prod_target_recycler.setVisibility(View.GONE);
                beat_target_recycler.setVisibility(View.GONE);
                emptyView.setText("Something went wrong try again..");
            }
        });
    }
}