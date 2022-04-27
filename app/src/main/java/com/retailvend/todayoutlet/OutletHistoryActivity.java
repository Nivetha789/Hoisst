package com.retailvend.todayoutlet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.retailvend.model.outlets.outletHistory.OutletHisInvoiceData;
import com.retailvend.model.outlets.outletHistory.OutletHisOrderData;
import com.retailvend.model.outlets.outletHistory.OutletHisPaymentData;
import com.retailvend.model.outlets.outletHistory.OutletHistoryData;
import com.retailvend.model.outlets.outletHistory.OutletHistoryModel;
import com.retailvend.retrofit.RetrofitClient;
import com.retailvend.targetDetails.BeatTargetAdapter;
import com.retailvend.targetDetails.ProductTargetAdapter;
import com.retailvend.targetDetails.TargetDetailsActivity;
import com.retailvend.utills.CustomProgress;
import com.retailvend.utills.CustomToast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OutletHistoryActivity extends AppCompatActivity {

    TextView outlet_name,
            emptyView,emptyView1,emptyView2,emptyView3;
    Activity activity;
    String outlet_id="";
    List<OutletHisAttendanceData> outletHisAttendanceData;
    List<OutletHisOrderData> outletHisOrderData;
    List<OutletHisPaymentData> outletHisPaymentData;
    List<OutletHisInvoiceData> outletHisInvoiceData;
    OutletHistoryData outletHisMainData;
    ImageView back, nodata,nodata1,nodata2,nodata3;
    RecyclerView order_data_recycler,attendance_details_recycler,payment_details_recycler,invoice_details_recycler;

    OutletHistoryAdapter outletHistoryAdapter;
    OutletHisAttenAdapter outletHisAttenAdapter;
    OutletHisPaymentAdapter outletHisPaymentAdapter;
    OutletHisInvoiceAdapter outletHisInvoiceAdapter;

    String shop_name="";

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

        outletHisAttendanceData=new ArrayList<>();
        outletHisOrderData=new ArrayList<>();
        outletHisPaymentData=new ArrayList<>();
        outletHisInvoiceData=new ArrayList<>();

        outlet_name=findViewById(R.id.outlet_name);
        back=findViewById(R.id.back);
        nodata=findViewById(R.id.nodata);
        nodata1=findViewById(R.id.nodata1);
        nodata2=findViewById(R.id.nodata2);
        nodata3=findViewById(R.id.nodata3);
        emptyView=findViewById(R.id.emptyView);
        emptyView1=findViewById(R.id.emptyView1);
        emptyView2=findViewById(R.id.emptyView2);
        emptyView3=findViewById(R.id.emptyView3);
        order_data_recycler=findViewById(R.id.order_data_recycler);
        attendance_details_recycler=findViewById(R.id.attendance_details_recycler);
        payment_details_recycler=findViewById(R.id.payment_details_recycler);
        invoice_details_recycler=findViewById(R.id.invoice_details_recycler);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            outlet_id = getIntent().getExtras().getString("outlet_id");
            shop_name=getIntent().getExtras().getString("outlet_name");
            System.out.println("outlet_idddd "+shop_name);
            outlet_name.setText(shop_name);

        }

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

                        emptyView.setVisibility(View.GONE);
                        emptyView1.setVisibility(View.GONE);
                        emptyView2.setVisibility(View.GONE);
                        nodata.setVisibility(View.GONE);
                        nodata1.setVisibility(View.GONE);
                        nodata2.setVisibility(View.GONE);
                        order_data_recycler.setVisibility(View.VISIBLE);
                        attendance_details_recycler.setVisibility(View.VISIBLE);
                        payment_details_recycler.setVisibility(View.VISIBLE);

                        outletHisMainData=outletHistoryModel.getData();
                        outletHisAttendanceData=outletHisMainData.getAttendanceData();
                        outletHisOrderData=outletHisMainData.getOrderData();
                        outletHisPaymentData=outletHisMainData.getPaymentData();
                        outletHisInvoiceData=outletHisMainData.getInvoiceData();

                        if(outletHisOrderData.size()>0){
                            emptyView.setVisibility(View.GONE);
                            nodata.setVisibility(View.GONE);
                            order_data_recycler.setVisibility(View.VISIBLE);
                            LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
                            order_data_recycler.setLayoutManager(layoutManager);

                            outletHistoryAdapter = new OutletHistoryAdapter(OutletHistoryActivity.this, outletHisOrderData);
                            order_data_recycler.setAdapter(outletHistoryAdapter);
                            outletHistoryAdapter.notifyDataSetChanged();

                        }else{
                            nodata.setVisibility(View.VISIBLE);
                            emptyView.setText("No Data Found!!");
                            emptyView.setVisibility(View.VISIBLE);
                            order_data_recycler.setVisibility(View.GONE);
                        }

                        if(outletHisAttendanceData.size()>0){
                            emptyView1.setVisibility(View.GONE);
                            nodata1.setVisibility(View.GONE);
                            attendance_details_recycler.setVisibility(View.VISIBLE);
                            LinearLayoutManager layoutManager1 = new LinearLayoutManager(activity);
                            attendance_details_recycler.setLayoutManager(layoutManager1);

                            outletHisAttenAdapter = new OutletHisAttenAdapter(OutletHistoryActivity.this, outletHisAttendanceData);
                            attendance_details_recycler.setAdapter(outletHisAttenAdapter);
                            outletHisAttenAdapter.notifyDataSetChanged();
                        }else{
                            nodata1.setVisibility(View.VISIBLE);
                            emptyView1.setText("No Data Found!!");
                            emptyView1.setVisibility(View.VISIBLE);
                            attendance_details_recycler.setVisibility(View.GONE);
                        }

                        if(outletHisPaymentData.size()>0){
                            emptyView2.setVisibility(View.GONE);
                            nodata2.setVisibility(View.GONE);
                            payment_details_recycler.setVisibility(View.VISIBLE);
                            LinearLayoutManager layoutManager2 = new LinearLayoutManager(activity);
                            payment_details_recycler.setLayoutManager(layoutManager2);

                            outletHisPaymentAdapter = new OutletHisPaymentAdapter(OutletHistoryActivity.this, outletHisPaymentData);
                            payment_details_recycler.setAdapter(outletHisPaymentAdapter);
                            outletHisPaymentAdapter.notifyDataSetChanged();
                        }else{
                            nodata2.setVisibility(View.VISIBLE);
                            emptyView2.setText("No Data Found!!");
                            emptyView2.setVisibility(View.VISIBLE);
                            payment_details_recycler.setVisibility(View.GONE);
                        }

                        if(outletHisInvoiceData.size()>0){
                            emptyView3.setVisibility(View.GONE);
                            nodata3.setVisibility(View.GONE);
                            invoice_details_recycler.setVisibility(View.VISIBLE);
                            LinearLayoutManager layoutManager3 = new LinearLayoutManager(activity);
                            invoice_details_recycler.setLayoutManager(layoutManager3);

                            outletHisInvoiceAdapter = new OutletHisInvoiceAdapter(OutletHistoryActivity.this, outletHisInvoiceData);
                            invoice_details_recycler.setAdapter(outletHisInvoiceAdapter);
                            outletHisInvoiceAdapter.notifyDataSetChanged();
                        }else{
                            nodata3.setVisibility(View.VISIBLE);
                            emptyView3.setText("No Data Found!!");LinearLayoutManager layoutManager3 = new LinearLayoutManager(activity);
                            invoice_details_recycler.setLayoutManager(layoutManager3);

                            outletHisInvoiceAdapter = new OutletHisInvoiceAdapter(OutletHistoryActivity.this, outletHisInvoiceData);
                            invoice_details_recycler.setAdapter(outletHisInvoiceAdapter);
                            outletHisInvoiceAdapter.notifyDataSetChanged();
                            emptyView3.setVisibility(View.VISIBLE);
                            invoice_details_recycler.setVisibility(View.GONE);
                        }

                        CustomProgress.hideProgress(activity);
                    } else {
                        CustomProgress.hideProgress(activity);
                        nodata.setVisibility(View.VISIBLE);
                        nodata1.setVisibility(View.VISIBLE);
                        nodata2.setVisibility(View.VISIBLE);
                        nodata3.setVisibility(View.VISIBLE);
                        emptyView.setText("No Data Found!!");
                        emptyView1.setText("No Data Found!!");
                        emptyView2.setText("No Data Found!!");
                        emptyView3.setText("No Data Found!!");
                        emptyView.setText(outletHistoryModel.getMessage());
                        emptyView.setVisibility(View.VISIBLE);
                        emptyView1.setText(outletHistoryModel.getMessage());
                        emptyView2.setVisibility(View.VISIBLE);
                        emptyView3.setVisibility(View.VISIBLE);
                        order_data_recycler.setVisibility(View.GONE);
                        attendance_details_recycler.setVisibility(View.GONE);
                        payment_details_recycler.setVisibility(View.GONE);
                        invoice_details_recycler.setVisibility(View.GONE);
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
                nodata.setVisibility(View.VISIBLE);
                nodata1.setVisibility(View.VISIBLE);
                nodata2.setVisibility(View.VISIBLE);
                nodata3.setVisibility(View.VISIBLE);
                emptyView.setVisibility(View.VISIBLE);
                emptyView1.setText("Something went wrong try again..");
                emptyView2.setText("Something went wrong try again..");
                emptyView1.setVisibility(View.VISIBLE);
                emptyView2.setVisibility(View.VISIBLE);
                emptyView3.setVisibility(View.VISIBLE);
                order_data_recycler.setVisibility(View.GONE);
                attendance_details_recycler.setVisibility(View.GONE);
                payment_details_recycler.setVisibility(View.GONE);
                invoice_details_recycler.setVisibility(View.GONE);
                emptyView.setText("Something went wrong try again..");
            }
        });
    }
}