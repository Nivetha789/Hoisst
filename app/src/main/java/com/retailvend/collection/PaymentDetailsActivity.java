package com.retailvend.collection;

import static com.retailvend.utills.PaginationListener.PAGE_START;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.retailvend.R;
import com.retailvend.broadcast.ConnectivityReceiver;
import com.retailvend.model.delCollection.paymentCollection.PaymentCollectionDatum;
import com.retailvend.model.delCollection.paymentCollection.PaymentCollectionModel;
import com.retailvend.payment.AddPaymentActivity;
import com.retailvend.retrofit.RetrofitClient;
import com.retailvend.todayoutlet.ProductNameActivity;
import com.retailvend.utills.CustomToast;
import com.retailvend.utills.PaginationListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentDetailsActivity extends AppCompatActivity  implements SwipeRefreshLayout.OnRefreshListener{

    SwipeRefreshLayout swipeRefresh;
    private PaymentDetailsAdapter paymentDetailsAdapter;
    RecyclerView recyclerView;
    List<PaymentCollectionDatum> paymentDetailsDataModelList;
    Toolbar toolbar;
    Menu menu;
    TextView mTitle, emptyView, txt_dis_pay_details_name, txt_pay_details_name;
    ProgressBar progress;
    LinearLayout lin_payment_add;
    TextView txt_payment_details_bal_amt;
    String assignId="";

    private int currentPage = PAGE_START;
    private boolean isLastPage = false;
    private int totalPage = 0;
    private boolean isLoading = false;
    int itemCount = 0;

    int offset = 0;
    int limit = 10;
    int totalcount = 0;
    Activity activity;
    String name="";
    String amt="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_details);
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

//        swipeRefresh = findViewById(R.id.swipeRefresh);
        recyclerView = findViewById(R.id.recyclerView);
        emptyView = findViewById(R.id.emptyView);
        txt_dis_pay_details_name = findViewById(R.id.txt_dis_pay_details_name);
        txt_pay_details_name = findViewById(R.id.txt_pay_details_name);
        progress = findViewById(R.id.progress);
        lin_payment_add = findViewById(R.id.lin_payment_add);
        txt_payment_details_bal_amt = findViewById(R.id.txt_payment_details_bal_amt);


        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            name = getIntent().getExtras().getString("name");
            assignId = getIntent().getExtras().getString("assign_id");
            System.out.println("assign_idassign_id " + assignId);
        }

//        swipeRefresh.setOnRefreshListener(this);
        recyclerView.setHasFixedSize(true);
        paymentDetailsDataModelList = new ArrayList<>();

        txt_pay_details_name.setText(name);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        paymentDetailsAdapter = new PaymentDetailsAdapter(PaymentDetailsActivity.this, paymentDetailsDataModelList,assignId);
        LinearLayoutManager llm = new LinearLayoutManager(PaymentDetailsActivity.this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(paymentDetailsAdapter);


        recyclerView.addOnScrollListener(new PaginationListener(layoutManager, totalPage) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
//                currentPage++;
                    boolean isConnected = ConnectivityReceiver.isConnected();
                    if (isConnected) {

                        payDetailsApi(offset,limit, assignId);

                    } else {
                        CustomToast.getInstance(PaymentDetailsActivity.this).showSmallCustomToast("Please check your internet connection");
                    }
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });

        lin_payment_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(PaymentDetailsActivity.this, AddPaymentActivity.class);
                intent.putExtra("assignId", assignId);
                intent.putExtra("balamt", amt);
                intent.putExtra("name", name);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onRefresh() {
        itemCount = 0;
        offset = 0;
        currentPage = PAGE_START;
        isLastPage = false;
        paymentDetailsAdapter.clear();

        boolean isConnected = ConnectivityReceiver.isConnected();
        if (isConnected) {
            payDetailsApi(offset, limit, assignId);
        } else {
            CustomToast.getInstance(PaymentDetailsActivity.this).showSmallCustomToast("Please check your internet connection");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        itemCount = 0;
        offset = 0;
        currentPage = PAGE_START;
        isLastPage = false;
        paymentDetailsAdapter.clear();
        boolean isConnected = ConnectivityReceiver.isConnected();
        if (isConnected) {
            payDetailsApi(offset, limit, assignId);
        } else {
            CustomToast.getInstance(PaymentDetailsActivity.this).showSmallCustomToast("Please check your internet connection");
        }
    }


    public void payDetailsApi(int offset1, int limit1, String assignId) {

        if (isLoading) {
            progress.setVisibility(View.GONE);
            emptyView.setVisibility(View.GONE);
        } else {
            progress.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }

        Call<PaymentCollectionModel> call = RetrofitClient
                .getInstance().getApi().collectionPaymentList("_listDistributorOutletPaymentPaginate", assignId,offset1,limit1);


        call.enqueue(new Callback<PaymentCollectionModel>() {
            @Override
            public void onResponse(Call<PaymentCollectionModel> call, Response<PaymentCollectionModel> response) {

                try {

                    Gson gson = new Gson();
                    String json = gson.toJson(response.body());
                    PaymentCollectionModel paymentDatailsModel = gson.fromJson(json, PaymentCollectionModel.class);
//                LoginModule loginModule = response.body();


                    if (paymentDatailsModel.getStatus() == 1) {

                        recyclerView.setVisibility(View.VISIBLE);
                        progress.setVisibility(View.GONE);
                        emptyView.setVisibility(View.GONE);

                        paymentDetailsDataModelList = paymentDatailsModel.getData();

                        offset = paymentDatailsModel.getOffset();
                        limit = paymentDatailsModel.getLimit();
                        totalcount = paymentDatailsModel.getTotalRecord();

                        int offest1 = offset;
                        int totalcount1;
                        if (totalcount > offset) {
                            totalcount1 = offset + limit;
                        } else {
                            totalcount1 = offset;
                        }


                        currentPage = offest1;
                        totalPage = totalcount1;


                        if (currentPage != PAGE_START)
                            paymentDetailsAdapter.removeLoading();

                        paymentDetailsAdapter.addItems(paymentDetailsDataModelList);

                        if (currentPage < totalPage) {
                            paymentDetailsAdapter.addLoading();
                        } else {
                            isLastPage = true;
                        }
                        isLoading = false;


//                        offset = siteListModel.getOffset();
                        progress.setVisibility(View.GONE);
                        emptyView.setVisibility(View.GONE);

                    } else {
                        recyclerView.setVisibility(View.GONE);
                        progress.setVisibility(View.GONE);
                        emptyView.setVisibility(View.VISIBLE);
                        emptyView.setText("No Record Found");
//                        siteListDataModelList.clear();
//                        Toast.makeText(LoginActivity.this, "Invalid User Name or Password", Toast.LENGTH_SHORT).show();
                        CustomToast.getInstance(PaymentDetailsActivity.this).showSmallCustomToast("No Record Found");
//                    Toast.makeText(LoginActivity.this, "Invalid User Name or Password", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    Log.d("Exception", e.getMessage());
                }

            }

            @Override
            public void onFailure(Call<PaymentCollectionModel> call, Throwable t) {
                Log.d("Failure ", t.getMessage());
                recyclerView.setVisibility(View.GONE);
                progress.setVisibility(View.GONE);
                emptyView.setVisibility(View.VISIBLE);
                emptyView.setText("Something went wrong try again..");
                amt = "0";
                txt_payment_details_bal_amt.setText("\u20B9 " + amt);
//                Toast.makeText(LoginActivity.this, "Invalid User Name or Password", Toast.LENGTH_SHORT).show();
                CustomToast.getInstance(PaymentDetailsActivity.this).showSmallCustomToast("Something went wrong try again..");

            }
        });

    }

   /* public void deletePaymentList(String type, String pay_id, String user_id) {


        progress.setVisibility(View.VISIBLE);


        Call<AddPaymentModel> call = RetrofitClient
                .getInstance().getApi().deleteCustomerPayment(type, pay_id, user_id);


        call.enqueue(new Callback<AddPaymentModel>() {
            @Override
            public void onResponse(Call<AddPaymentModel> call, Response<AddPaymentModel> response) {

                try {

                    Gson gson = new Gson();
                    String json = gson.toJson(response.body());
                    AddPaymentModel addPaymentModel = gson.fromJson(json, AddPaymentModel.class);
//                LoginModule loginModule = response.body();


                    if (addPaymentModel.getCode() == 1) {

                        itemCount = 0;
                        currentPage = PAGE_START;
                        isLastPage = false;
                        paymentDetailsAdapter.clear();
                        boolean isConnected = ConnectivityReceiver.isConnected();
                        if (isConnected) {

                            custPayDetails("2", currentPage, userid);

                        } else {
                            CustomToast.getInstance(PaymentDetailsActivity.this).showSmallCustomToast("Please check your internet connection");
                        }

                        CustomToast.getInstance(PaymentDetailsActivity.this).showSmallCustomToast(addPaymentModel.getMessage());

                        progress.setVisibility(View.GONE);


                    } else {

                        CustomToast.getInstance(PaymentDetailsActivity.this).showSmallCustomToast(addPaymentModel.getMessage());
                        progress.setVisibility(View.GONE);
                    }

                } catch (Exception e) {
                    Log.d("Exception", e.getMessage());
                }

            }

            @Override
            public void onFailure(Call<AddPaymentModel> call, Throwable t) {
                Log.d("Failure ", t.getMessage());
                CustomToast.getInstance(PaymentDetailsActivity.this).showSmallCustomToast("Something went wrong try again..");
                progress.setVisibility(View.GONE);

            }
        });

    }*/
}