package com.retailvend.deliveryman.outlet;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.retailvend.R;
import com.retailvend.broadcast.ConnectivityReceiver;
import com.retailvend.deliveryman.outstand.DelManOutstandActivity;
import com.retailvend.model.delManModels.delCollection.todayOutletsDetails.TodayOutletDetailsBuyerDetails;
import com.retailvend.model.delManModels.delCollection.todayOutletsDetails.TodayOutletDetailsDistributorDetails;
import com.retailvend.model.delManModels.delCollection.todayOutletsDetails.TodayOutletDetailsModel;
import com.retailvend.model.delManModels.delCollection.todayOutletsDetails.TodayOutletDetailsProductDetail;
import com.retailvend.model.delManModels.delCollection.todayOutletsDetails.TodayOutletDetailsStoreDetails;
import com.retailvend.retrofit.RetrofitClient;
import com.retailvend.utills.CustomToast;
import com.retailvend.utills.SessionManagerSP;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OutletInvoiceDetailsActivity extends AppCompatActivity {

    TextView txt_empty, txt_invoice_details_order, txt_inovoice_details_billed,
            txt_invoice_details_address, txt_inovoice_details_store,
            txt_invoice_details_shipped_address, txt_inoive_details_total_nos,
            txt_inoive_details_total_kgs, txt_inoive_details_total_price, txt_dis_invoice,
            txt_invoice_last_bill, txt_invoice_bal_amt, txt_invoice_details_dist, txt_store_name,
            txt_store_ship, hsnCode, tax_val, central_tax_rate, central_tax_amount, state_tax_rate, state_tax_amount,
            tax_val_total, central_total, state_total, final_hsn_total, amount_in_words, invoice_num, comp_name, comp_address,
            gst_num, contact_no, state_code, pay_method, order_type, amount, bill_date, txt_invoice_details_dist_address,
            txt_invoice_qty,invoice,invoiceClick,delivery,deliveryClick;
    RecyclerView recyclerView;
    ProgressBar progress;
    NestedScrollView lin_invoice_details_scrollview;
    LinearLayout order_status_layout,lin_submit;

    TodayOutletDetailsStoreDetails todayOutletDetailsStoreDetails;
    TodayOutletDetailsDistributorDetails todayOutletDetailsDistributorDetails;
    TodayOutletDetailsBuyerDetails todayOutletDetailsBuyerDetails;
    OutletInvoiceDetailsAdapter outletInvoiceDetailsAdapter;
    List<TodayOutletDetailsProductDetail> productDetails;
    String random_value = "";
    Activity activity;
    SessionManagerSP sessionManagerSP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outlet_invoice_details);
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
        sessionManagerSP = new SessionManagerSP(OutletInvoiceDetailsActivity.this);
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            random_value = getIntent().getExtras().getString("random_value");
            System.out.println("random_value " + random_value);
        }


        progress = findViewById(R.id.progress);
        txt_empty = findViewById(R.id.txt_empty);
        recyclerView = findViewById(R.id.recyclerView);
        txt_invoice_details_order = findViewById(R.id.txt_invoice_details_order);
        txt_inovoice_details_billed = findViewById(R.id.txt_inovoice_details_billed);
        txt_invoice_details_address = findViewById(R.id.txt_invoice_details_address);
        txt_invoice_details_shipped_address = findViewById(R.id.txt_invoice_details_shipped_address);
        txt_inoive_details_total_nos = findViewById(R.id.txt_inoive_details_total_nos);
        txt_inoive_details_total_kgs = findViewById(R.id.txt_inoive_details_total_kgs);
        txt_inoive_details_total_price = findViewById(R.id.txt_inoive_details_total_price);
        txt_dis_invoice = findViewById(R.id.txt_dis_invoice);
        txt_invoice_last_bill = findViewById(R.id.txt_invoice_last_bill);
        txt_invoice_bal_amt = findViewById(R.id.txt_invoice_bal_amt);
        lin_invoice_details_scrollview = findViewById(R.id.lin_invoice_details_scrollview);
        txt_store_name = findViewById(R.id.txt_store_name);
        txt_store_ship = findViewById(R.id.txt_store_ship);
        gst_num = findViewById(R.id.gst_num);
        hsnCode = findViewById(R.id.hsnCode);
        tax_val = findViewById(R.id.tax_val);
        central_tax_rate = findViewById(R.id.central_tax_rate);
        central_tax_amount = findViewById(R.id.central_tax_amount);
        state_tax_rate = findViewById(R.id.state_tax_rate);
        state_tax_amount = findViewById(R.id.state_tax_amount);
        tax_val_total = findViewById(R.id.tax_val_total);
        central_total = findViewById(R.id.central_total);
        state_total = findViewById(R.id.state_total);
        final_hsn_total = findViewById(R.id.final_hsn_total);
//        amount_in_words = findViewById(R.id.amount_in_words);
        invoice_num = findViewById(R.id.invoice_num);
        comp_name = findViewById(R.id.comp_name);
        comp_address = findViewById(R.id.comp_address);
        contact_no = findViewById(R.id.contact_no);
        state_code = findViewById(R.id.state_code);
        pay_method = findViewById(R.id.pay_method);
        order_type = findViewById(R.id.order_type);
        amount = findViewById(R.id.amount);
        bill_date = findViewById(R.id.bill_date);
        txt_inovoice_details_store = findViewById(R.id.txt_inovoice_details_store);
        txt_invoice_details_dist = findViewById(R.id.txt_invoice_details_dist);
        txt_invoice_details_dist_address = findViewById(R.id.txt_invoice_details_dist_address);
        txt_invoice_qty = findViewById(R.id.txt_invoice_qty);
        order_status_layout = findViewById(R.id.order_status_layout);
        invoice = findViewById(R.id.invoice);
        invoiceClick = findViewById(R.id.invoiceClick);
        delivery = findViewById(R.id.delivery);
        deliveryClick = findViewById(R.id.deliveryClick);
        lin_submit = findViewById(R.id.lin_submit);

        invoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                invoice.setVisibility(View.GONE);
                invoiceClick.setVisibility(View.VISIBLE);
                delivery.setVisibility(View.VISIBLE);
                deliveryClick.setVisibility(View.GONE);
            }
        });

        invoiceClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                invoice.setVisibility(View.VISIBLE);
                invoiceClick.setVisibility(View.GONE);
                delivery.setVisibility(View.GONE);
                deliveryClick.setVisibility(View.VISIBLE);
            }
        });

        delivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                invoice.setVisibility(View.VISIBLE);
                invoiceClick.setVisibility(View.GONE);
                delivery.setVisibility(View.GONE);
                deliveryClick.setVisibility(View.VISIBLE);
            }
        });

        boolean isConnected = ConnectivityReceiver.isConnected();
        if (isConnected) {
            delManOutletInvoiceApi(random_value);
        } else {
            CustomToast.getInstance(OutletInvoiceDetailsActivity.this).showSmallCustomToast("Please check your internet connection");
        }
    }

    public void delManOutletInvoiceApi(String randomValue) {
        String emp_id = sessionManagerSP.getEmployeeId();
        progress.setVisibility(View.VISIBLE);
        lin_invoice_details_scrollview.setVisibility(View.GONE);
        txt_empty.setVisibility(View.GONE);


        Call<TodayOutletDetailsModel> call = RetrofitClient
                .getInstance().getApi().delManOutletInvoice("_employeeBillDetail", emp_id, randomValue);


        call.enqueue(new Callback<TodayOutletDetailsModel>() {
            @Override
            public void onResponse(@NonNull Call<TodayOutletDetailsModel> call, @NonNull Response<TodayOutletDetailsModel> response) {

                try {

                    Gson gson = new Gson();
                    String json = gson.toJson(response.body());
                    TodayOutletDetailsModel todayOutletDetailsModel = gson.fromJson(json, TodayOutletDetailsModel.class);

                    if (todayOutletDetailsModel.getStatus() == 1) {
                        if (todayOutletDetailsModel.getData() != null) {
                            todayOutletDetailsStoreDetails = todayOutletDetailsModel.getData().getStoreDetails();
                            todayOutletDetailsDistributorDetails = todayOutletDetailsModel.getData().getDistributorDetails();
                            todayOutletDetailsBuyerDetails = todayOutletDetailsModel.getData().getBuyerDetails();
                            txt_invoice_details_order.setText(todayOutletDetailsBuyerDetails.getOrderNo());
                            txt_inovoice_details_store.setText(todayOutletDetailsStoreDetails.getCompanyName());
                            txt_invoice_details_address.setText(todayOutletDetailsStoreDetails.getAddress());
                            txt_invoice_details_dist.setText(todayOutletDetailsDistributorDetails.getCompanyName());
                            txt_invoice_details_dist_address.setText(todayOutletDetailsDistributorDetails.getAddress());
//                            txt_invoice_qty.setText(todayOutletDetailsDistributorDetails.getq());
//                            gst_num.setText("GST: " + todayOutletDetailsDistributorDetails.getGstNo());
//                            contact_no.setText("Mobile No: " + todayOutletDetailsDistributorDetails.getMobile());
//                            state_code.setText("State Code: " + todayOutletDetailsDistributorDetails.getStateCode());
//                            bill_date.setText("Bill Date: " + todayOutletDetailsStoreDetails.getCreatedate());
//                            amount.setText("Amount: " + todayOutletDetailsDistributorDetails.ge());
//                            pay_method.setText("Payment Method :"+todayOutletDetailsDistributorDetails.getpa);
//                            order_type.setText("Order Type " + todayOutletDetailsDistributorDetails.get());
//                        txt_invoice_last_bill.setText(salesDetailsModel.getLastbill());
//                        txt_invoice_bal_amt.setText("\u20B9 " + salesDetailsModel.getBalance());
//                            txt_invoice_current_total.setText("\u20B9 " + productDetails.get(0).getPrice());
//                            for(int i=0; i<taxDetails.size(); i++){
//                                String hsnCode1 = taxDetails.get(i).getHsnCode();
//                                hsnCode.setText(hsnCode1);
//                            }

                            productDetails = todayOutletDetailsModel.getData().getProductDetails();

                            outletInvoiceDetailsAdapter = new OutletInvoiceDetailsAdapter(activity, productDetails);
                            // use a linear layout manager
                            LinearLayoutManager layoutManager = new LinearLayoutManager(getBaseContext());
                            recyclerView.setLayoutManager(layoutManager);
                            recyclerView.setAdapter(outletInvoiceDetailsAdapter);
                            outletInvoiceDetailsAdapter.notifyDataSetChanged();
                            progress.setVisibility(View.GONE);
                            lin_invoice_details_scrollview.setVisibility(View.VISIBLE);
                            txt_empty.setVisibility(View.GONE);
                        } else {
                            progress.setVisibility(View.GONE);
                            lin_invoice_details_scrollview.setVisibility(View.GONE);
                            txt_empty.setVisibility(View.VISIBLE);
                            CustomToast.getInstance(OutletInvoiceDetailsActivity.this).showSmallCustomToast(todayOutletDetailsModel.getMessage());
                        }
                    } else {
                        progress.setVisibility(View.GONE);
                        lin_invoice_details_scrollview.setVisibility(View.GONE);
                        txt_empty.setVisibility(View.VISIBLE);
                        CustomToast.getInstance(OutletInvoiceDetailsActivity.this).showSmallCustomToast(todayOutletDetailsModel.getMessage());
                    }
                } catch (Exception e) {
                    Log.d("Exception", e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<TodayOutletDetailsModel> call, Throwable t) {
                Log.d("Failure ", t.getMessage());
                progress.setVisibility(View.GONE);
                lin_invoice_details_scrollview.setVisibility(View.GONE);
                txt_empty.setVisibility(View.VISIBLE);
                txt_empty.setText("Something went wrong try again..");
                CustomToast.getInstance(OutletInvoiceDetailsActivity.this).showSmallCustomToast("Something went wrong try again..");

            }
        });

    }
}