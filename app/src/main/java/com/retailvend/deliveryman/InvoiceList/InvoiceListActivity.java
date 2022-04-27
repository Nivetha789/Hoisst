package com.retailvend.deliveryman.InvoiceList;

import static com.retailvend.utills.PaginationListener.PAGE_START;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.retailvend.R;
import com.retailvend.broadcast.ConnectivityReceiver;
import com.retailvend.model.invoiceListModel.InvoiceListDatum;
import com.retailvend.model.invoiceListModel.InvoiceListModel;
import com.retailvend.retrofit.RetrofitClient;
import com.retailvend.utills.CustomToast;
import com.retailvend.utills.PaginationListener;
import com.retailvend.utills.SessionManagerSP;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InvoiceListActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    InvoiceListAdapter invoiceListAdapter;
    RecyclerView order_list_recycler;
    List<InvoiceListDatum> invoiceListData;
    TextView mTitle, emptyView;
    private Toolbar toolbar;
    String order_type = "";
    ImageView left_arrow, nodata;
    Activity activity;

    private int currentPage = PAGE_START;
    private boolean isLastPage = false;
    private int totalPage = 0;
    private boolean isLoading = false;
    int itemCount = 0;
    ProgressBar progress;

    int offset = 0;
    int limit = 10;
    int totalcount = 0;

    LinearLayout searchLayout;
    ImageView search_icon;
    EditText search;

    String searchTxt = "";

    SessionManagerSP sessionManagerSP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_list);
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

        order_list_recycler = findViewById(R.id.order_recycler);
        toolbar = findViewById(R.id.toolbar);
        mTitle = toolbar.findViewById(R.id.toolbar_title);
        left_arrow = findViewById(R.id.left_arrow);
        progress = findViewById(R.id.progress);
        emptyView = findViewById(R.id.emptyView);
        search = findViewById(R.id.search);
        search_icon = findViewById(R.id.search_icon);
        searchLayout = findViewById(R.id.searchLayout);
        nodata = findViewById(R.id.nodata);

        sessionManagerSP = new SessionManagerSP(InvoiceListActivity.this);

        invoiceListData = new ArrayList<>();

        left_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                offset = 0;
                searchTxt = s.toString();

                boolean isConnected = ConnectivityReceiver.isConnected();
                if (isConnected) {
                    orderListDelManApi(offset, limit, "2");
                } else {
                    CustomToast.getInstance(InvoiceListActivity.this).showSmallCustomToast("Please check your internet connection");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


//        swipeRefresh.setOnRefreshListener(this);
        order_list_recycler.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        order_list_recycler.setLayoutManager(layoutManager);

        invoiceListAdapter = new InvoiceListAdapter(InvoiceListActivity.this, invoiceListData);
        order_list_recycler.setAdapter(invoiceListAdapter);

        order_list_recycler.addOnScrollListener(new PaginationListener(layoutManager, totalPage) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
//                currentPage++;

                boolean isConnected = ConnectivityReceiver.isConnected();
                if (isConnected) {
                    orderListDelManApi(offset, limit, "1");
                } else {
                    CustomToast.getInstance(InvoiceListActivity.this).showSmallCustomToast("Please check your internet connection");
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
    }

    @Override
    public void onRefresh() {
        itemCount = 0;
        offset = 0;
        currentPage = PAGE_START;
        isLastPage = false;
        invoiceListAdapter.clear();

        boolean isConnected = ConnectivityReceiver.isConnected();
        if (isConnected) {
            orderListDelManApi(offset, limit, "1");
        } else {
            CustomToast.getInstance(InvoiceListActivity.this).showSmallCustomToast("Please check your internet connection");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        itemCount = 0;
        offset = 0;
        currentPage = PAGE_START;
        isLastPage = false;
        invoiceListAdapter.clear();

        boolean isConnected = ConnectivityReceiver.isConnected();
        if (isConnected) {
            orderListDelManApi(offset, limit, "1");
        } else {
            CustomToast.getInstance(InvoiceListActivity.this).showSmallCustomToast("Please check your internet connection");
        }
    }


    public void orderListDelManApi(int offset1, int limit1, String searchType) {
//        CustomProgress.showProgress(activity);
        String emp_id = sessionManagerSP.getEmployeeId();

        if (isLoading) {
            progress.setVisibility(View.GONE);
            emptyView.setVisibility(View.GONE);
        } else {
            progress.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }

        Call<InvoiceListModel> call = RetrofitClient
                .getInstance().getApi().delManOrderList("_employeeInvoice", emp_id, offset1, limit1, searchTxt);

        call.enqueue(new Callback<InvoiceListModel>() {
            @Override
            public void onResponse(@NonNull Call<InvoiceListModel> call, @NonNull Response<InvoiceListModel> response) {

                try {

                    Gson gson = new Gson();
                    String json = gson.toJson(response.body());
                    InvoiceListModel invoiceListModel = gson.fromJson(json, InvoiceListModel.class);

                    if (invoiceListModel.getStatus() == 1) {

                        if (searchType.equals("2")) {
//                            if (todayOutletsDatum.size() > 0) {
                            invoiceListAdapter.clear();
//                            }
                        }

                        order_list_recycler.setVisibility(View.VISIBLE);
                        emptyView.setVisibility(View.GONE);
                        nodata.setVisibility(View.GONE);
                        searchLayout.setVisibility(View.VISIBLE);
                        invoiceListData = invoiceListModel.getData();

                        if(invoiceListData.size()>0){
                            offset = invoiceListModel.getOffset();
                            limit = invoiceListModel.getLimit();
                            totalcount = invoiceListModel.getTotalRecord();

//                        int offest1 = offset;
//                        int totalcount1;
//                        if (totalcount > offset) {
//                            totalcount1 = offset + limit;
//                        } else {
//                            totalcount1 = offset;
//                        }


                            currentPage = offset;
//                        totalPage = totalcount;


                            if (currentPage != PAGE_START)
                                invoiceListAdapter.removeLoading();

                            invoiceListAdapter.addItems(invoiceListData);

                            if (currentPage < totalcount) {
                                invoiceListAdapter.addLoading();
                            }else if(currentPage>totalPage){
                                invoiceListAdapter.addLoading();
                                invoiceListAdapter.removeLoading();
                            }
                            else {
                                isLastPage = true;
                                invoiceListAdapter.removeLoading();
                            }
                        }else{
                            if(searchType.equals("2")){
                                progress.setVisibility(View.GONE);
                                emptyView.setVisibility(View.VISIBLE);
                                nodata.setVisibility(View.VISIBLE);
                                searchLayout.setVisibility(View.GONE);
                            }
                        }

                        isLoading = false;


//                        offset = siteListModel.getOffset();
                        progress.setVisibility(View.GONE);
//                        emptyView.setVisibility(View.GONE);
//                        nodata.setVisibility(View.GONE);
//                        searchLayout.setVisibility(View.VISIBLE);

                    } else {
                        if(searchType.equals("2")){
                            progress.setVisibility(View.GONE);
                            emptyView.setVisibility(View.VISIBLE);
                            nodata.setVisibility(View.VISIBLE);
                            searchLayout.setVisibility(View.GONE);
                            order_list_recycler.setVisibility(View.GONE);
                        }
//                        todayOutletRecycler.setVisibility(View.GONE);
//                        progress.setVisibility(View.GONE);
//                        nodata.setVisibility(View.VISIBLE);
//                        emptyView.setVisibility(View.VISIBLE);
//                        emptyView.setText(assignOutletsModel.getMessage());
//                        searchLayout.setVisibility(View.GONE);
//                        siteListDataModelList.clear();
//                        Toast.makeText(LoginActivity.this, "Invalid User Name or Password", Toast.LENGTH_SHORT).show();
//                        CustomToast.getInstance(TodayOutletActivity.this).showSmallCustomToast("No Record Found");
//                    Toast.makeText(TodayOutletActivity.this, "Invalid User Name or Password", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    progress.setVisibility(View.GONE);
                    order_list_recycler.setVisibility(View.GONE);
                    progress.setVisibility(View.GONE);
                    nodata.setVisibility(View.VISIBLE);
                    emptyView.setVisibility(View.VISIBLE);
                    emptyView.setText("No Data Found");
                    searchLayout.setVisibility(View.GONE);
                    Log.d("Exceptionnnn", e.getMessage());
                }
            }

            @Override
            public void onFailure(@NonNull Call<InvoiceListModel> call, @NonNull Throwable t) {
                order_list_recycler.setVisibility(View.GONE);
                progress.setVisibility(View.GONE);
                emptyView.setVisibility(View.VISIBLE);
                nodata.setVisibility(View.VISIBLE);
                emptyView.setText("Something went wrong try again..");
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}