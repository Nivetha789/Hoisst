package com.retailvend.todayoutlet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.retailvend.R;
import com.retailvend.broadcast.ConnectivityReceiver;
import com.retailvend.model.outlets.AddAttendanceModel;
import com.retailvend.model.outlets.AttendanceTypeDatum;
import com.retailvend.model.outlets.outletHistory.OutletHisAttendanceData;
import com.retailvend.model.outlets.outletHistory.OutletHisOrderData;
import com.retailvend.model.outlets.outletHistory.OutletHisPaymentData;
import com.retailvend.model.outlets.outletHistory.OutletHistoryData;
import com.retailvend.model.outlets.outletHistory.OutletHistoryModel;
import com.retailvend.retrofit.RetrofitClient;
import com.retailvend.utills.CustomProgress;
import com.retailvend.utills.CustomToast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OutletHistoryActivity extends AppCompatActivity {

    TextView order_no,order_date,dist_name,bill_no,amount,amount_type,collected_type,emp_name;
    Activity activity;
    String outlet_id="";
    List<OutletHisAttendanceData> outletHisAttendanceData;
    List<OutletHisOrderData> outletHisOrderData;
    List<OutletHisPaymentData> outletHisPaymentData;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outlet_history);
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
            outlet_id = getIntent().getExtras().getString("outlet_id");
            System.out.println("outlet_idddd "+outlet_id);
        }
        order_no=findViewById(R.id.order_no);
        order_date=findViewById(R.id.order_date);
        dist_name=findViewById(R.id.dist_name);
        bill_no=findViewById(R.id.bill_no);
        amount=findViewById(R.id.amount);
        amount_type=findViewById(R.id.amount_type);
        collected_type=findViewById(R.id.collected_type);
        emp_name=findViewById(R.id.emp_name);
        back=findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        boolean isConnected = ConnectivityReceiver.isConnected();
        if (isConnected) {
            outletHistoryApi();
        } else {
//            Toast.makeText(this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
            CustomToast.getInstance(OutletHistoryActivity.this).showSmallCustomToast("Please check your internet connection");
        }
    }

    public void outletHistoryApi() {
        CustomProgress.showProgress(activity);

        Call<OutletHistoryModel> call = RetrofitClient
                .getInstance().getApi().getOutletHistory("_outletHistory",outlet_id);

        call.enqueue(new Callback<OutletHistoryModel>() {
            @Override
            public void onResponse(@NonNull Call<OutletHistoryModel> call, @NonNull Response<OutletHistoryModel> response) {

                try {

                    Gson gson = new Gson();
                    String json = gson.toJson(response.body());

                    OutletHistoryModel outletHistoryModel = gson.fromJson(json, OutletHistoryModel.class);

                    if (outletHistoryModel.getStatus() == 1) {

//                        CustomToast.getInstance(OutletHistoryActivity.this).showSmallCustomToast(outletHistoryModel.getMessage());
                        outletHisAttendanceData=outletHistoryModel.getData().getAttendanceData();
                        outletHisOrderData=outletHistoryModel.getData().getOrderData();
                        outletHisPaymentData=outletHistoryModel.getData().getPaymentData();
                        emp_name.setText(outletHisAttendanceData.get(0).getEmployeeName());
                        order_no.setText(outletHisOrderData.get(0).getOrderNo());
                        order_date.setText(outletHisAttendanceData.get(0).getAttendanceDate());
                        dist_name.setText(outletHisPaymentData.get(0).getDistributorName());
                        bill_no.setText(outletHisPaymentData.get(0).getBillNo());
                        amount.setText(outletHisPaymentData.get(0).getAmount());
                        amount_type.setText(outletHisPaymentData.get(0).getAmtType());
                        collected_type.setText(outletHisPaymentData.get(0).getCollectionType());
                        CustomProgress.hideProgress(activity);
                    } else {
                        CustomProgress.hideProgress(activity);
                        CustomToast.getInstance(OutletHistoryActivity.this).showSmallCustomToast(outletHistoryModel.getMessage());
                    }

                } catch (Exception e) {
                    Log.d("Exception", e.getMessage());
                    CustomProgress.hideProgress(activity);
                }

            }

            @Override
            public void onFailure(@NonNull Call<OutletHistoryModel> call, @NonNull Throwable t) {
                Log.d("Failure ", t.getMessage());
                CustomToast.getInstance(OutletHistoryActivity.this).showSmallCustomToast("Something went wrong try again..");
                CustomProgress.hideProgress(activity);
            }
        });
    }
}