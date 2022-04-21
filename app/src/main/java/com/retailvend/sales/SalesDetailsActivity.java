package com.retailvend.sales;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
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
import com.retailvend.model.sales.SalesBillDetails;
import com.retailvend.model.sales.SalesDetailsModel;
import com.retailvend.model.sales.SalesProductDetail;
import com.retailvend.model.sales.SalesStoreDetails;
import com.retailvend.model.sales.SalesTaxDetail;
import com.retailvend.retrofit.RetrofitClient;
import com.retailvend.utills.CustomToast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SalesDetailsActivity extends AppCompatActivity {

    TextView txt_empty, txt_invoice_details_order, txt_inovoice_details_billed,
            txt_invoice_details_billed_address,
            txt_invoice_details_shipped_address,  txt_dis_invoice,
            txt_invoice_last_bill, txt_invoice_qty_total, txt_store_name,
            txt_store_ship,  invoice_num, comp_name, comp_address,
            gst_num, contact_no, state_code, pay_method, order_type, amount, bill_date,txt_invoice_current_total;
    RecyclerView recyclerView;
    ProgressBar progress;
    NestedScrollView lin_invoice_details_scrollview;

    SalesBillDetails salesBillDetails;
    SalesStoreDetails salesStoreDetails;
    SalesDetailsAdapter salesDetailsAdapter;
    List<SalesProductDetail> productDetails;
    List<SalesTaxDetail> taxDetails;
    String random_value = "";
    Activity activity;
    ImageView left_arrow;

    int qty;
    double totalPrice;
    double grandTotalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_details);
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
            random_value = getIntent().getExtras().getString("random_value");
            System.out.println("random_value " + random_value);
        }


        progress = findViewById(R.id.progress);
        txt_empty = findViewById(R.id.txt_empty);
        recyclerView = findViewById(R.id.recyclerView);
        txt_invoice_details_order = findViewById(R.id.txt_invoice_details_order);
        txt_inovoice_details_billed = findViewById(R.id.txt_inovoice_details_billed);
        txt_invoice_details_billed_address = findViewById(R.id.txt_invoice_details_billed_address);
        txt_invoice_details_shipped_address = findViewById(R.id.txt_invoice_details_shipped_address);
        txt_dis_invoice = findViewById(R.id.txt_dis_invoice);
        txt_invoice_last_bill = findViewById(R.id.txt_invoice_last_bill);
        lin_invoice_details_scrollview = findViewById(R.id.lin_invoice_details_scrollview);
        txt_store_name = findViewById(R.id.txt_store_name);
        txt_store_ship = findViewById(R.id.txt_store_ship);
        gst_num = findViewById(R.id.gst_num);
        invoice_num = findViewById(R.id.invoice_num);
        comp_name = findViewById(R.id.comp_name);
        comp_address = findViewById(R.id.comp_address);
        contact_no = findViewById(R.id.contact_no);
        state_code = findViewById(R.id.state_code);
        pay_method = findViewById(R.id.pay_method);
        order_type = findViewById(R.id.order_type);
        amount = findViewById(R.id.amount);
        bill_date = findViewById(R.id.bill_date);
        left_arrow = findViewById(R.id.left_arrow);
        txt_invoice_qty_total = findViewById(R.id.txt_invoice_qty_total);
        txt_invoice_current_total = findViewById(R.id.txt_invoice_current_total);

        left_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        productDetails = new ArrayList<>();
        taxDetails = new ArrayList<>();
        boolean isConnected = ConnectivityReceiver.isConnected();
        if (isConnected) {
            salesInvoiceDetails(random_value);
        } else {
            CustomToast.getInstance(SalesDetailsActivity.this).showSmallCustomToast("Please check your internet connection");
        }
    }

    public void salesInvoiceDetails(String randomValue) {
//        System.out.println("orderIDDDDD :"+orderId);

        progress.setVisibility(View.VISIBLE);
        lin_invoice_details_scrollview.setVisibility(View.GONE);
        txt_empty.setVisibility(View.GONE);


        Call<SalesDetailsModel> call = RetrofitClient
                .getInstance().getApi().salesDetails("_orderDetails", randomValue);


        call.enqueue(new Callback<SalesDetailsModel>() {
            @Override
            public void onResponse(@NonNull Call<SalesDetailsModel> call, @NonNull Response<SalesDetailsModel> response) {

                try {

                    Gson gson = new Gson();
                    String json = gson.toJson(response.body());
                    SalesDetailsModel salesDetailsModel = gson.fromJson(json, SalesDetailsModel.class);

                    if (salesDetailsModel.getStatus() == 1) {
                        if (salesDetailsModel.getData() != null) {
                            salesBillDetails = salesDetailsModel.getData().getBillDetails();
                            salesStoreDetails = salesDetailsModel.getData().getStoreDetails();
                            taxDetails = salesDetailsModel.getData().getTaxDetails();
                            productDetails=salesDetailsModel.getData().getProductDetails();
                            txt_invoice_details_order.setText(salesBillDetails.getOrderNo());
                            txt_store_name.setText(salesBillDetails.getStoreName());
                            txt_invoice_details_billed_address.setText(salesBillDetails.getContactName());
                            txt_store_ship.setText(salesStoreDetails.getCompanyName());
                            txt_invoice_details_shipped_address.setText(salesStoreDetails.getAddress());
                            gst_num.setText("GST: " + salesStoreDetails.getGstNo());
                            contact_no.setText("Mobile No: " + salesStoreDetails.getMobile());
                            state_code.setText("State Code: " + salesStoreDetails.getStateCode());
                            bill_date.setText("Bill Date: " + salesBillDetails.getCreatedate());

                            int b = 0;
                            double a;
                            for (int i = 0; i < productDetails.size(); i++) {
                                a = Double.parseDouble(productDetails.get(i).getPrice().trim());
                                b = Integer.parseInt(productDetails.get(i).getOrderQty().trim());
                                totalPrice = a * b;
                                grandTotalPrice += totalPrice;
                                String units = productDetails.get(i).getUnitVal();
                                System.out.println("unitsssss " + units);
//                                txt_inoive_details_total_units.setText(productDetails.ge);

                                System.out.println("totalPrice " + totalPrice);
                            }


                            for (int k = 0; k < productDetails.size(); k++) {
                                qty += Integer.valueOf(productDetails.get(k).getOrderQty());
                                System.out.println("qtyyyyyyyy " + qty);
                            }

                            txt_invoice_current_total.setText("\u20B9 " + grandTotalPrice);
                            txt_invoice_qty_total.setText(String.valueOf(qty));

                            productDetails = salesDetailsModel.getData().getProductDetails();

                            salesDetailsAdapter = new SalesDetailsAdapter(activity, productDetails);
                            // use a linear layout manager
                            LinearLayoutManager layoutManager = new LinearLayoutManager(getBaseContext());
                            recyclerView.setLayoutManager(layoutManager);
                            recyclerView.setAdapter(salesDetailsAdapter);
                            salesDetailsAdapter.notifyDataSetChanged();
                            progress.setVisibility(View.GONE);
                            lin_invoice_details_scrollview.setVisibility(View.VISIBLE);
                            txt_empty.setVisibility(View.GONE);
                        } else {
                            progress.setVisibility(View.GONE);
                            lin_invoice_details_scrollview.setVisibility(View.GONE);
                            txt_empty.setVisibility(View.VISIBLE);
                            CustomToast.getInstance(SalesDetailsActivity.this).showSmallCustomToast(salesDetailsModel.getMessage());
                        }
                    } else {
                        progress.setVisibility(View.GONE);
                        lin_invoice_details_scrollview.setVisibility(View.GONE);
                        txt_empty.setVisibility(View.VISIBLE);
                        CustomToast.getInstance(SalesDetailsActivity.this).showSmallCustomToast(salesDetailsModel.getMessage());
                    }
                } catch (Exception e) {
                    Log.d("Exception", e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<SalesDetailsModel> call, Throwable t) {
                Log.d("Failure ", t.getMessage());
                progress.setVisibility(View.GONE);
                lin_invoice_details_scrollview.setVisibility(View.GONE);
                txt_empty.setVisibility(View.VISIBLE);
                txt_empty.setText("Something went wrong try again..");
                CustomToast.getInstance(SalesDetailsActivity.this).showSmallCustomToast("Something went wrong try again..");

            }
        });

    }
}