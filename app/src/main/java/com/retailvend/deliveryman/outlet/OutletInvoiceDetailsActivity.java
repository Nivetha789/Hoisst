package com.retailvend.deliveryman.outlet;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
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
import com.retailvend.model.delManModels.delCollection.todayOutletsDetails.TodayOutletDetailsBillDetails;
import com.retailvend.model.delManModels.delCollection.todayOutletsDetails.TodayOutletDetailsBuyerDetails;
import com.retailvend.model.delManModels.delCollection.todayOutletsDetails.TodayOutletDetailsDistributorDetails;
import com.retailvend.model.delManModels.delCollection.todayOutletsDetails.TodayOutletDetailsModel;
import com.retailvend.model.delManModels.delCollection.todayOutletsDetails.TodayOutletDetailsProductDetail;
import com.retailvend.model.delManModels.delCollection.todayOutletsDetails.TodayOutletDetailsStoreDetails;
import com.retailvend.model.delManModels.delCollection.todayOutletsDetails.TodayOutletReturnDetails;
import com.retailvend.model.delManModels.delCollection.todayOutletsDetails.TodayOutletTotalDetails;
import com.retailvend.model.delManModels.delCollection.todayOutletsDetails.UpdateBillModel;
import com.retailvend.retrofit.RetrofitClient;
import com.retailvend.utills.CustomProgress;
import com.retailvend.utills.CustomToast;
import com.retailvend.utills.SessionManagerSP;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OutletInvoiceDetailsActivity extends AppCompatActivity {

    TextView txt_empty, txt_invoice_details_order, txt_inovoice_details_billed,
            txt_invoice_details_address, txt_inovoice_details_store,
            txt_invoice_details_shipped_address, txt_dis_invoice,
            txt_invoice_last_bill, txt_invoice_details_dist, txt_store_name,
            txt_store_ship,invoice_num, comp_name, comp_address,
            gst_num, contact_no, state_code, pay_method, order_type, amount, bill_date, txt_invoice_details_dist_address,
            txt_invoice_qty, invoice, invoiceClick, delivery, deliveryClick, txt_invoice_sub_total, txt_invoice_current_total;
    RecyclerView recyclerView;
    ProgressBar progress;
    NestedScrollView lin_invoice_details_scrollview;
    LinearLayout order_status_layout, lin_submit,pdf_viewer;
    ImageView left_arrow;

    TodayOutletDetailsBillDetails todayOutletDetailsBillDetails;
    TodayOutletDetailsStoreDetails todayOutletDetailsStoreDetails;
    TodayOutletDetailsDistributorDetails todayOutletDetailsDistributorDetails;
    TodayOutletDetailsBuyerDetails todayOutletDetailsBuyerDetails;
    OutletInvoiceDetailsAdapter outletInvoiceDetailsAdapter;
    List<TodayOutletDetailsProductDetail> productDetails;
    TodayOutletTotalDetails todayOutletTotalDetails;
    TodayOutletReturnDetails todayOutletReturnDetails;
    String random_value = "";
    Activity activity;
    SessionManagerSP sessionManagerSP;

    int qty;
    double totalPrice;
    double grandTotalPrice;

    String pdfUrl="";

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
//        txt_inoive_details_total_nos = findViewById(R.id.txt_inoive_details_total_nos);
//        txt_inoive_details_total_kgs = findViewById(R.id.txt_inoive_details_total_kgs);
        txt_dis_invoice = findViewById(R.id.txt_dis_invoice);
        txt_invoice_last_bill = findViewById(R.id.txt_invoice_last_bill);
        lin_invoice_details_scrollview = findViewById(R.id.lin_invoice_details_scrollview);
        txt_store_name = findViewById(R.id.txt_store_name);
        txt_store_ship = findViewById(R.id.txt_store_ship);
        gst_num = findViewById(R.id.gst_num);
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
        txt_invoice_sub_total = findViewById(R.id.txt_invoice_sub_total);
        txt_invoice_current_total = findViewById(R.id.txt_invoice_current_total);
        left_arrow = findViewById(R.id.left_arrow);
        pdf_viewer = findViewById(R.id.pdf_viewer);

        left_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        pdf_viewer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(pdfUrl));
                startActivity(browserIntent);
            }
        });

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
        deliveryClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                invoice.setVisibility(View.GONE);
                invoiceClick.setVisibility(View.VISIBLE);
                delivery.setVisibility(View.VISIBLE);
                deliveryClick.setVisibility(View.GONE);
            }
        });

        lin_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(activity);
                builder1.setMessage("Are you sure you want to change order status?");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                boolean isConnected = ConnectivityReceiver.isConnected();
                                if (isConnected) {
                                    updateBillApi(random_value);
                                } else {
                                    CustomToast.getInstance(OutletInvoiceDetailsActivity.this).showSmallCustomToast("Please check your internet connection");
                                }
                            }
                        });

                builder1.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();
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
                .getInstance().getApi().delManOutletInvoice("_detailInvoice", emp_id, randomValue);


        call.enqueue(new Callback<TodayOutletDetailsModel>() {
            @Override
            public void onResponse(@NonNull Call<TodayOutletDetailsModel> call, @NonNull Response<TodayOutletDetailsModel> response) {

                try {

                    Gson gson = new Gson();
                    String json = gson.toJson(response.body());
                    TodayOutletDetailsModel todayOutletDetailsModel = gson.fromJson(json, TodayOutletDetailsModel.class);

                    if (todayOutletDetailsModel.getStatus() == 1) {
                        if (todayOutletDetailsModel.getData() != null) {
                            todayOutletDetailsBillDetails = todayOutletDetailsModel.getData().getBillDetails();
                            todayOutletDetailsStoreDetails = todayOutletDetailsModel.getData().getStoreDetails();
                            productDetails = todayOutletDetailsModel.getData().getProductDetails();
                            todayOutletDetailsDistributorDetails = todayOutletDetailsModel.getData().getDistributorDetails();
                            todayOutletDetailsBuyerDetails = todayOutletDetailsModel.getData().getBuyerDetails();
                            todayOutletTotalDetails = todayOutletDetailsModel.getData().getTotalDetails();
                            todayOutletReturnDetails = todayOutletDetailsModel.getData().getReturnDetails();

                            txt_invoice_details_order.setText(todayOutletDetailsBillDetails.getInvoiceNo());
                            txt_inovoice_details_store.setText(todayOutletDetailsStoreDetails.getCompanyName());
                            txt_invoice_details_address.setText(todayOutletDetailsStoreDetails.getAddress());
                            txt_invoice_details_dist.setText(todayOutletDetailsDistributorDetails.getCompanyName());
                            txt_invoice_details_dist_address.setText(todayOutletDetailsDistributorDetails.getAddress());
//                            txt_invoice_qty.setText(todayOutletTotalDetails.getTotalQty());
                            txt_invoice_sub_total.setText(todayOutletTotalDetails.getSubTotal());

                            pdfUrl= todayOutletDetailsModel.getData().getPrintInvoice();

                            int b = 0;
                            double a;
                            for (int i = 0; i < productDetails.size(); i++) {
                                a = Double.parseDouble(productDetails.get(i).getPrice().trim());
                                b = Integer.parseInt(productDetails.get(i).getOrderQty().trim());
                                totalPrice = a * b;
                                grandTotalPrice+=totalPrice;
//                                System.out.println("totalPrice " + totalPrice);
                            }


                            for (int k = 0; k < productDetails.size(); k++) {
                                qty += Integer.valueOf(productDetails.get(k).getOrderQty());
//                                System.out.println("qtyyyyyyyy " + qty);
                            }

                            txt_invoice_current_total.setText("\u20B9 " + grandTotalPrice);
                            txt_invoice_qty.setText(String.valueOf(qty));

//                            gst_num.setText("GST: " + todayOutletDetailsDistributorDetails.getGstNo());
//                            contact_no.setText("Mobile No: " + todayOutletDetailsDistributorDetails.getMobile());
//                            state_code.setText("State Code: " + todayOutletDetailsDistributorDetails.getStateCode());
//                            bill_date.setText("Bill Date: " + todayOutletDetailsStoreDetails.getCreatedate());
//                            amount.setText("Amount: " + todayOutletDetailsDistributorDetails.ge());
//                            pay_method.setText("Payment Method :"+todayOutletDetailsDistributorDetails.getpa);
//                            order_type.setText("Order Type " + todayOutletDetailsDistributorDetails.get());
//                        txt_invoice_last_bill.setText(salesDetailsModel.getLastbill());
//                        txt_invoice_bal_amt.setText("\u20B9 " + salesDetailsModel.getBalance());
//
//                            for(int i=0; i<taxDetails.size(); i++){
//                                String hsnCode1 = taxDetails.get(i).getHsnCode();
//                                hsnCode.setText(hsnCode1);
//                            }


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

    public void updateBillApi(String randomValue) {
        String emp_id = sessionManagerSP.getEmployeeId();
        CustomProgress.showProgress(OutletInvoiceDetailsActivity.this);

        Call<UpdateBillModel> call = RetrofitClient
                .getInstance().getApi().delManOutletInvoiceUpdateStatus("_employeeUpdateBill", emp_id, randomValue, "11");


        call.enqueue(new Callback<UpdateBillModel>() {
            @Override
            public void onResponse(@NonNull Call<UpdateBillModel> call, @NonNull Response<UpdateBillModel> response) {

                try {

                    Gson gson = new Gson();
                    String json = gson.toJson(response.body());
                    UpdateBillModel updateBillModel = gson.fromJson(json, UpdateBillModel.class);

                    if (updateBillModel.getStatus() == 1) {
                        CustomToast.getInstance(OutletInvoiceDetailsActivity.this).showSmallCustomToast(updateBillModel.getMessage());
                        CustomProgress.hideProgress(OutletInvoiceDetailsActivity.this);
//                        onBackPressed();
                        order_status_layout.setVisibility(View.GONE);
                    } else {
                        progress.setVisibility(View.GONE);
                        lin_invoice_details_scrollview.setVisibility(View.GONE);
                        txt_empty.setVisibility(View.VISIBLE);
                        CustomToast.getInstance(OutletInvoiceDetailsActivity.this).showSmallCustomToast(updateBillModel.getMessage());

                        CustomProgress.hideProgress(OutletInvoiceDetailsActivity.this);
                    }
                } catch (Exception e) {
                    Log.d("Exception", e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<UpdateBillModel> call, Throwable t) {
                Log.d("Failure ", t.getMessage());
                progress.setVisibility(View.GONE);
                CustomProgress.hideProgress(OutletInvoiceDetailsActivity.this);
                lin_invoice_details_scrollview.setVisibility(View.GONE);
                txt_empty.setVisibility(View.VISIBLE);
                txt_empty.setText("Something went wrong try again..");
                CustomToast.getInstance(OutletInvoiceDetailsActivity.this).showSmallCustomToast("Something went wrong try again..");

            }
        });

    }
}