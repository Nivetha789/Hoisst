package com.retailvend.targetDetails;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
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
import com.retailvend.model.targetDetailssales.TargetDetailsData;
import com.retailvend.model.targetDetailssales.TargetDetailsModel;
import com.retailvend.model.targetDetailssales.TargetDetailsTarget;
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

    ImageView left_arrow;
    Activity activity;
    SessionManagerSP sessionManagerSP;
    BarChart barChart;
    List<TargetDetailsTarget> targetDetailsDataList;
    TextView target,achieve;
    String year="";

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

        left_arrow=findViewById(R.id.left_arrow);

        left_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        barChart=findViewById(R.id.chart);
        target=findViewById(R.id.target);
        achieve=findViewById(R.id.achieve);

        boolean isConnected = ConnectivityReceiver.isConnected();
        if (isConnected) {
            targetDetailsApi();
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
                        target.setText(detailsModel.getData().getTargetVal());
                        achieve.setText(detailsModel.getData().getAchieveVal());
                        targetDetailsDataList = detailsModel.getData().getTargetList();

                        ArrayList<BarEntry> targets= new ArrayList<>();

                        for(int i=0; i<=targets.size();i++){
                            String[] separated = targetDetailsDataList.get(i).getDate().split("-");
                            int first= Integer.valueOf(separated[0]);
                            year= separated[2];
                            System.out.println("secccc "+year);
                            targets.add(new BarEntry(Float.parseFloat(year), Float.parseFloat(targetDetailsDataList.get(i).getValue().toString())));
                        }
//                        targets.add(new BarEntry(Float.parseFloat(year), 508));

//                        targets.add(new BarEntry(2015, 475));
//                        targets.add(new BarEntry(2016, 508));
//                        targets.add(new BarEntry(2017, 660));
//                        targets.add(new BarEntry(2018, 550));
                        BarDataSet barDataSet=new BarDataSet(targets,"Targets/Achievements");
                        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
                        barDataSet.setValueTextColor(Color.BLACK);
                        barDataSet.setValueTextSize(16f);

                        BarData barData= new BarData(barDataSet);

                        barChart.setFitBars(true);
                        barChart.setData(barData);
                        barChart.getDescription().setText("Targets/Achievements");
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
}