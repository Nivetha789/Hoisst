 package com.retailvend.sales;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.retailvend.R;

public class SalesActivity extends AppCompatActivity {

    RecyclerView salesRecycler;
    Activity activity;
    LinearLayoutManager mLayoutManager;
    SalesAdapter salesAdapter;
    ImageView leftArrow;
    private boolean isLastPage = false;
    private int totalPage = 0;
    private boolean isLoading = false;
    int itemCount = 0;
    Toolbar toolbar;
    Menu menu;
    TextView mTitle, emptyView;
    TextView txt_dis_add;
    ProgressBar progress;
    LinearLayout lin_add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales);
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

        toolbar = findViewById(R.id.toolbar);
        salesRecycler = findViewById(R.id.sales_recyeclerview);
        mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mTitle.setText("Today Outlets");


//        swipeRefresh.setOnRefreshListener(this);
//        salesRecycler.setHasFixedSize(true);
//        saleslists = new ArrayList<>();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        salesRecycler.setLayoutManager(layoutManager);

        salesAdapter = new SalesAdapter(activity);
        salesRecycler.setAdapter(salesAdapter);


//        salesRecycler.addOnScrollListener(new PaginationListener(layoutManager, totalPage) {
//            @Override
//            protected void loadMoreItems() {
//                isLoading = true;
////                currentPage++;
//                boolean isConnected = ConnectivityReceiver.isConnected();
//                if (isConnected) {
//                    salesInoice("1", currentPage, "");
//
//                } else {
//                    CustomToast.getInstance(SalesInvoiceActivity.this).showSmallCustomToast("Please check your internet connection");
//                }
//
//            }
//
//            @Override
//            public boolean isLastPage() {
//                return isLastPage;
//            }
//
//            @Override
//            public boolean isLoading() {
//                return isLoading;
//            }
//        });

    }

    @Override
    public void onBackPressed() {
//        refreshActivity();
//        RegistorSharedPreManager.getInstance(DetailsActivity.this).clear();
        super.onBackPressed();
    }

/*
    public void search(SearchView searchView) {

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                itemCount = 0;
                currentPage = PAGE_START;
                isLastPage = false;
                if (saleslists.size() > 0) {
                    SalesAdapter.removeLoading();
                }
                SalesAdapter.clear();
                boolean isConnected = ConnectivityReceiver.isConnected();
                if (isConnected) {

                    salesInoice("2", 1, query);

                } else {


//            Toast.makeText(this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
                    CustomToast.getInstance(SalesInvoiceActivity.this).showSmallCustomToast("Please check your internet connection");
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!(newText.length() > 0)) {
                    onRefresh();
                }
                return true;
            }
        });
    }
*/

    /*public void salesInoice(String type, int cPage, String search) {

        if (isLoading) {
            progress.setVisibility(View.GONE);
            emptyView.setVisibility(View.GONE);
        } else {
            progress.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }

        Call<SalesInvoiceModel> call = RetrofitClient
                .getInstance().getApi().salesInvoice(type, String.valueOf(cPage), search);


        call.enqueue(new Callback<SalesInvoiceModel>() {
            @Override
            public void onResponse(Call<SalesInvoiceModel> call, Response<SalesInvoiceModel> response) {

                try {

                    Gson gson = new Gson();
                    String json = gson.toJson(response.body());
                    SalesInvoiceModel salesInvoiceModel = gson.fromJson(json, SalesInvoiceModel.class);
//                LoginModule loginModule = response.body();

//                    if (saleslists != null) {
//                        salesInvoiceAdapter.clear();
//                    }

                    if (salesInvoiceModel.getCode() == 1) {

                        salesRecycler.setVisibility(View.VISIBLE);
                        progress.setVisibility(View.GONE);
                        emptyView.setVisibility(View.GONE);

                        saleslists = salesInvoiceModel.getSaleslist();

                        currentPage = Integer.parseInt(salesInvoiceModel.getCurrentPage());
                        totalPage = Integer.parseInt(salesInvoiceModel.getTotalPage());


                        if (currentPage != PAGE_START)
                            SalesAdapter.removeLoading();
                        SalesAdapter.addItems(saleslists);
                        swipeRefresh.setRefreshing(false);
                        // check weather is last page or not
                        if (currentPage < totalPage) {
                            SalesAdapter.addLoading();
                        } else {
                            isLastPage = true;
                        }
                        isLoading = false;

                        currentPage = Integer.parseInt(salesInvoiceModel.getNextPage());
                        progress.setVisibility(View.GONE);
                        emptyView.setVisibility(View.GONE);

                    } else {
                        salesRecycler.setVisibility(View.GONE);
                        progress.setVisibility(View.GONE);
                        emptyView.setVisibility(View.VISIBLE);
                        emptyView.setText("No Record Found");
                        saleslists.clear();
//                        Toast.makeText(LoginActivity.this, "Invalid User Name or Password", Toast.LENGTH_SHORT).show();
                        CustomToast.getInstance(SalesInvoiceActivity.this).showSmallCustomToast("No Record Found");
//                    Toast.makeText(LoginActivity.this, "Invalid User Name or Password", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    Log.d("Exception", e.getMessage());
                }

            }

            @Override
            public void onFailure(Call<SalesInvoiceModel> call, Throwable t) {
                Log.d("Failure ", t.getMessage());
                salesRecycler.setVisibility(View.GONE);
                progress.setVisibility(View.GONE);
                emptyView.setVisibility(View.VISIBLE);
                emptyView.setText("Something went wrong try again..");
                saleslists.clear();
//                Toast.makeText(LoginActivity.this, "Invalid User Name or Password", Toast.LENGTH_SHORT).show();
                CustomToast.getInstance(SalesInvoiceActivity.this).showSmallCustomToast("Something went wrong try again..");

            }
        });

    }

    public void deleteSalesInvoice(String type, String billid, String sup_id) {


        progress.setVisibility(View.VISIBLE);


        Call<AddPaymentModel> call = RetrofitClient
                .getInstance().getApi().delectSalesInvoice(type, billid, sup_id);


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
                        SalesAdapter.clear();
                        boolean isConnected = ConnectivityReceiver.isConnected();
                        if (isConnected) {

                            salesInoice("1", currentPage, "");

                        } else {
                            CustomToast.getInstance(SalesInvoiceActivity.this).showSmallCustomToast("Please check your internet connection");
                        }

                        CustomToast.getInstance(SalesInvoiceActivity.this).showSmallCustomToast(addPaymentModel.getMessage());

                        progress.setVisibility(View.GONE);


                    } else {

                        CustomToast.getInstance(SalesInvoiceActivity.this).showSmallCustomToast(addPaymentModel.getMessage());
                        progress.setVisibility(View.GONE);
                    }

                } catch (Exception e) {
                    Log.d("Exception", e.getMessage());
                }

            }

            @Override
            public void onFailure(Call<AddPaymentModel> call, Throwable t) {
                Log.d("Failure ", t.getMessage());
                CustomToast.getInstance(SalesInvoiceActivity.this).showSmallCustomToast("Something went wrong try again..");
                progress.setVisibility(View.GONE);

            }
        });

    }*/
}