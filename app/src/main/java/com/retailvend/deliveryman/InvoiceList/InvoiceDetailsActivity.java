package com.retailvend.deliveryman.InvoiceList;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.retailvend.BuildConfig;
import com.retailvend.R;
import com.retailvend.broadcast.ConnectivityReceiver;
import com.retailvend.model.delManModels.delCollection.invoiceDetails.InvoiceBillDetails;
import com.retailvend.model.delManModels.delCollection.invoiceDetails.InvoiceDetailsModel;
import com.retailvend.model.delManModels.delCollection.invoiceDetails.InvoiceProductDetail;
import com.retailvend.model.delManModels.delCollection.invoiceDetails.InvoiceStoreDetails;
import com.retailvend.model.delManModels.delCollection.invoiceDetails.InvoiceTaxDetail;
import com.retailvend.retrofit.RetrofitClient;
import com.retailvend.utills.CustomToast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InvoiceDetailsActivity extends AppCompatActivity {

    TextView txt_empty, txt_invoice_details_order, txt_inovoice_details_billed,
            txt_invoice_details_billed_address,
            txt_invoice_details_shipped_address, txt_dis_invoice, txt_invoice_qty_total, txt_store_name,
            txt_store_ship, invoice_num, comp_name, comp_address,
            gst_num, contact_no, state_code, pay_method, order_type,
            amount, bill_date, txt_invoice_current_total, toolbar_title;
    RecyclerView recyclerView;
    ProgressBar progress;
    NestedScrollView lin_invoice_details_scrollview;

    InvoiceBillDetails invoiceBillDetails;
    InvoiceStoreDetails invoiceStoreDetails;
    InvoiceDetailsAdapter invoiceDetailsAdapter;
    List<InvoiceProductDetail> productDetails;
    List<InvoiceTaxDetail> taxDetails;
    String random_value = "";
    Activity activity;
    ImageView left_arrow;
    LinearLayout lin_print;

    int qty;
    double totalPrice;
    double grandTotalPrice;

    String print_invoice="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_details);
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
        toolbar_title = findViewById(R.id.toolbar_title);
        txt_empty = findViewById(R.id.txt_empty);
        recyclerView = findViewById(R.id.recyclerView);
        txt_invoice_details_order = findViewById(R.id.txt_invoice_details_order);
        txt_inovoice_details_billed = findViewById(R.id.txt_inovoice_details_billed);
        txt_invoice_details_billed_address = findViewById(R.id.txt_invoice_details_billed_address);
        txt_invoice_details_shipped_address = findViewById(R.id.txt_invoice_details_shipped_address);
        txt_dis_invoice = findViewById(R.id.txt_dis_invoice);
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
        lin_print = findViewById(R.id.lin_print);

        left_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        lin_print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("print_invoiceprint_invoice " + print_invoice);

                if(!print_invoice.isEmpty()){
                    WebView mWebView=new WebView(InvoiceDetailsActivity.this);
                    mWebView.getSettings().setJavaScriptEnabled(true);
                    mWebView.getSettings().setMediaPlaybackRequiresUserGesture(true);
                    mWebView.loadUrl("https://docs.google.com/gview?embedded=true&url="+print_invoice);
                    setContentView(mWebView);
                }
            }
        });

        productDetails = new ArrayList<>();
        taxDetails = new ArrayList<>();

        boolean isConnected = ConnectivityReceiver.isConnected();
        if (isConnected) {
            invoiceDetails(random_value);
        } else {
            CustomToast.getInstance(InvoiceDetailsActivity.this).showSmallCustomToast("Please check your internet connection");
        }
    }


    //invoice details
    public void invoiceDetails(String randomValue) {
//        System.out.println("orderIDDDDD :"+orderId);

        progress.setVisibility(View.VISIBLE);
        lin_invoice_details_scrollview.setVisibility(View.GONE);
        txt_empty.setVisibility(View.GONE);


        Call<InvoiceDetailsModel> call = RetrofitClient
                .getInstance().getApi().invoiceDetails("_detailInvoice", randomValue);


        call.enqueue(new Callback<InvoiceDetailsModel>() {
            @Override
            public void onResponse(@NonNull Call<InvoiceDetailsModel> call, @NonNull Response<InvoiceDetailsModel> response) {

                try {

                    Gson gson = new Gson();
                    String json = gson.toJson(response.body());
                    InvoiceDetailsModel invoiceDetailsModel = gson.fromJson(json, InvoiceDetailsModel.class);

                    if (invoiceDetailsModel.status == 1) {
                        if (invoiceDetailsModel.data != null) {
                            invoiceBillDetails = invoiceDetailsModel.data.billDetails;
                            invoiceStoreDetails = invoiceDetailsModel.data.storeDetails;
                            taxDetails = invoiceDetailsModel.data.taxDetails;
                            productDetails = invoiceDetailsModel.data.productDetails;
                            txt_invoice_details_order.setText(invoiceBillDetails.invoiceNo);
//                            txt_store_name.setText(invoiceBillDetails.());
//                            txt_invoice_details_billed_address.setText(invoiceBillDetails.getContactName());
                            txt_store_ship.setText(invoiceStoreDetails.companyName);
                            txt_invoice_details_shipped_address.setText(invoiceStoreDetails.address);
                            gst_num.setText("GST: " + invoiceStoreDetails.gstNo);
                            contact_no.setText("Mobile No: " + invoiceStoreDetails.mobile);
                            state_code.setText("State Name: " + invoiceStoreDetails.stateName);
                            bill_date.setText("Bill Date: " + invoiceBillDetails.createdate);

                            int b = 0;
                            double a;
                            for (int i = 0; i < productDetails.size(); i++) {
                                a = Double.parseDouble(productDetails.get(i).price.trim());
                                b = Integer.parseInt(productDetails.get(i).orderQty.trim());
                                totalPrice = a * b;
                                grandTotalPrice += totalPrice;
                                String units = productDetails.get(i).unitVal;
                                System.out.println("unitsssss " + units);
//                                txt_inoive_details_total_units.setText(productDetails.ge);

                                System.out.println("totalPrice " + totalPrice);
                            }


                            for (int k = 0; k < productDetails.size(); k++) {
                                qty += Integer.valueOf(productDetails.get(k).orderQty);
                            }

                            txt_invoice_current_total.setText("\u20B9 " + grandTotalPrice);
                            txt_invoice_qty_total.setText(String.valueOf(qty));

                            print_invoice=invoiceDetailsModel.data.getPrintInvoice();
                            productDetails = invoiceDetailsModel.data.productDetails;

                            invoiceDetailsAdapter = new InvoiceDetailsAdapter(activity, productDetails);
                            // use a linear layout manager
                            LinearLayoutManager layoutManager = new LinearLayoutManager(getBaseContext());
                            recyclerView.setLayoutManager(layoutManager);
                            recyclerView.setAdapter(invoiceDetailsAdapter);
                            invoiceDetailsAdapter.notifyDataSetChanged();
                            progress.setVisibility(View.GONE);
                            lin_invoice_details_scrollview.setVisibility(View.VISIBLE);
                            txt_empty.setVisibility(View.GONE);
                        } else {
                            progress.setVisibility(View.GONE);
                            lin_invoice_details_scrollview.setVisibility(View.GONE);
                            txt_empty.setVisibility(View.VISIBLE);
                            CustomToast.getInstance(InvoiceDetailsActivity.this).showSmallCustomToast(invoiceDetailsModel.message);
                        }
                    } else {
                        progress.setVisibility(View.GONE);
                        lin_invoice_details_scrollview.setVisibility(View.GONE);
                        txt_empty.setVisibility(View.VISIBLE);
                        CustomToast.getInstance(InvoiceDetailsActivity.this).showSmallCustomToast(invoiceDetailsModel.message);
                    }
                } catch (Exception e) {
                    Log.d("Exception", e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<InvoiceDetailsModel> call, Throwable t) {
                Log.d("Failure ", t.getMessage());
                progress.setVisibility(View.GONE);
                lin_invoice_details_scrollview.setVisibility(View.GONE);
                txt_empty.setVisibility(View.VISIBLE);
                txt_empty.setText("Something went wrong try again..");
                CustomToast.getInstance(InvoiceDetailsActivity.this).showSmallCustomToast("Something went wrong try again..");

            }
        });
    }
}