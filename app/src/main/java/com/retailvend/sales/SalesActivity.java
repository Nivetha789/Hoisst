package com.retailvend.sales;

import static com.retailvend.utills.PaginationListener.PAGE_START;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
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
import com.retailvend.model.manageorder.OrderListDatum;
import com.retailvend.model.manageorder.OrderListModel;
import com.retailvend.retrofit.RetrofitClient;
import com.retailvend.utills.CustomToast;
import com.retailvend.utills.PaginationListener;
import com.retailvend.utills.SessionManagerSP;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SalesActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    RecyclerView salesRecycler;
    Activity activity;
    LinearLayoutManager mLayoutManager;
    SalesAdapter salesAdapter;
    ImageView leftArrow;
    Toolbar toolbar;
    Menu menu;
    TextView nodata_txt, emptyView;
    List<OrderListDatum> orderListData;

    private int currentPage = PAGE_START;
    private boolean isLastPage = false;
    private int totalPage = 0;
    private boolean isLoading = false;
    int itemCount = 0;
    ProgressBar progress;

    int offset = 0;
    int limit = 10;
    int totalcount = 0;
    SessionManagerSP sessionManagerSP;

    LinearLayout searchLayout;
    ImageView search_icon, nodata;
    EditText search;

    String searchTxt = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales);
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

        toolbar = findViewById(R.id.toolbar);
        leftArrow = findViewById(R.id.left_arrow);
        salesRecycler = findViewById(R.id.sales_recyeclerview);
        progress = findViewById(R.id.progress);
        search = findViewById(R.id.search);
        search_icon = findViewById(R.id.search_icon);
        searchLayout = findViewById(R.id.searchLayout);
        emptyView = findViewById(R.id.emptyView);
        nodata = findViewById(R.id.nodata);

        sessionManagerSP = new SessionManagerSP(SalesActivity.this);

        orderListData = new ArrayList<>();
        leftArrow.setOnClickListener(new View.OnClickListener() {
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
                    orderListApi(offset, limit, "2");
                } else {
                    CustomToast.getInstance(SalesActivity.this).showSmallCustomToast("Please check your internet connection");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        salesRecycler.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        salesRecycler.setLayoutManager(layoutManager);

        salesAdapter = new SalesAdapter(SalesActivity.this, orderListData);
        salesRecycler.setAdapter(salesAdapter);

        salesRecycler.addOnScrollListener(new PaginationListener(layoutManager, totalPage) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
//                currentPage++;

                boolean isConnected = ConnectivityReceiver.isConnected();
                if (isConnected) {
                    orderListApi(offset, limit, "1");

                } else {
                    CustomToast.getInstance(SalesActivity.this).showSmallCustomToast("Please check your internet connection");
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
        salesAdapter.clear();

        boolean isConnected = ConnectivityReceiver.isConnected();
        if (isConnected) {
            orderListApi(offset, limit, "1");
        } else {
            CustomToast.getInstance(SalesActivity.this).showSmallCustomToast("Please check your internet connection");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        itemCount = 0;
        offset = 0;
        currentPage = PAGE_START;
        isLastPage = false;
        salesAdapter.clear();
        boolean isConnected = ConnectivityReceiver.isConnected();
        if (isConnected) {
            orderListApi(offset, limit, "1");
        } else {
            CustomToast.getInstance(SalesActivity.this).showSmallCustomToast("Please check your internet connection");
        }
    }

    public void orderListApi(int offset1, int limit1, String searchType) {
//        CustomProgress.showProgress(activity);
        String emp_id = sessionManagerSP.getEmployeeId();

        if (isLoading) {
            progress.setVisibility(View.GONE);
            emptyView.setVisibility(View.GONE);
            nodata.setVisibility(View.GONE);
        } else {
            progress.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
            nodata.setVisibility(View.GONE);
        }

        Call<OrderListModel> call = RetrofitClient
                .getInstance().getApi().orderList("_listEmployeeOrderPaginate", emp_id, offset1, limit1, searchTxt);

        call.enqueue(new Callback<OrderListModel>() {
            @Override
            public void onResponse(@NonNull Call<OrderListModel> call, @NonNull Response<OrderListModel> response) {

                try {

                    Gson gson = new Gson();
                    String json = gson.toJson(response.body());
                    OrderListModel productNameResModel = gson.fromJson(json, OrderListModel.class);

                    if (productNameResModel.getStatus() == 1) {

                        if (searchType.equals("2")) {
                            if (orderListData.size() > 0) {
                                salesAdapter.clear();
                            }
                        }

                        salesRecycler.setVisibility(View.VISIBLE);
                        progress.setVisibility(View.GONE);
                        emptyView.setVisibility(View.GONE);
                        nodata.setVisibility(View.GONE);
                        searchLayout.setVisibility(View.VISIBLE);

                        orderListData = productNameResModel.getData();

                        offset = productNameResModel.getOffset();
                        limit = productNameResModel.getLimit();
                        totalcount = productNameResModel.getTotalRecord();

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
                            salesAdapter.removeLoading();

                        salesAdapter.addItems(orderListData);

                        if (currentPage < totalPage) {
                            salesAdapter.addLoading();
                        } else {
                            isLastPage = true;
                        }
                        isLoading = false;


//                        offset = siteListModel.getOffset();
                        progress.setVisibility(View.GONE);
                        emptyView.setVisibility(View.GONE);
                        nodata.setVisibility(View.GONE);
                        searchLayout.setVisibility(View.VISIBLE);

                    } else {
                        salesRecycler.setVisibility(View.GONE);
                        progress.setVisibility(View.GONE);
                        nodata.setVisibility(View.VISIBLE);
                        emptyView.setVisibility(View.VISIBLE);
                        emptyView.setText(productNameResModel.getMessage());
                        searchLayout.setVisibility(View.GONE);
//                        siteListDataModelList.clear();
//                        Toast.makeText(LoginActivity.this, "Invalid User Name or Password", Toast.LENGTH_SHORT).show();
                        CustomToast.getInstance(SalesActivity.this).showSmallCustomToast("No Record Found");
//                    Toast.makeText(LoginActivity.this, "Invalid User Name or Password", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    progress.setVisibility(View.GONE);
                    Log.d("Exceptionnnn", e.getMessage());
                }
            }

            @Override
            public void onFailure(@NonNull Call<OrderListModel> call, @NonNull Throwable t) {
                salesRecycler.setVisibility(View.GONE);
                progress.setVisibility(View.GONE);
                emptyView.setVisibility(View.VISIBLE);
                nodata.setVisibility(View.VISIBLE);
                searchLayout.setVisibility(View.GONE);
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