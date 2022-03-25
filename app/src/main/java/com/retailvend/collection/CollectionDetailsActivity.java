package com.retailvend.collection;

import static com.retailvend.utills.PaginationListener.PAGE_START;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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

import com.google.gson.Gson;
import com.retailvend.R;
import com.retailvend.broadcast.ConnectivityReceiver;
import com.retailvend.model.collectionmodel.CollectionDetailsListDatum;
import com.retailvend.model.collectionmodel.CollectionDetailsListModel;
import com.retailvend.model.outlets.AssignOutletsModel;
import com.retailvend.model.sales.SalesDetailsModel;
import com.retailvend.orderList.OrderListActivity;
import com.retailvend.orderList.OrderListAdapter;
import com.retailvend.retrofit.RetrofitClient;
import com.retailvend.sales.SalesDetailsActivity;
import com.retailvend.sales.SalesDetailsAdapter;
import com.retailvend.todayoutlet.TodayOutletActivity;
import com.retailvend.utills.CustomProgress;
import com.retailvend.utills.CustomToast;
import com.retailvend.utills.PaginationListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CollectionDetailsActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    TextView shop_name, distributor_name, amount, date, amount_type;
    ImageView leftArrow, nodata;
    Toolbar toolbar;
    Activity activity;
    TextView emptyView;
    ProgressBar progress;
    RecyclerView collection_recyclerView;
    CollectionDetailsAdapter collectionDetailsAdapter;

    //    SwipeRefreshLayout swipeRefresh;
    private int currentPage = PAGE_START;
    private boolean isLastPage = false;
    private int totalPage = 0;
    private boolean isLoading = false;
    int itemCount = 0;

    List<CollectionDetailsListDatum> collectionDetailsListData;

    int offset = 0;
    int limit = 10;
    int totalcount = 0;

    String outlet_id="";
    String shopName="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection_details);
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

        toolbar = findViewById(R.id.collection_toolbar);
        leftArrow = findViewById(R.id.left_arrow);
        progress = findViewById(R.id.progress);
        emptyView = findViewById(R.id.emptyView);
        nodata = findViewById(R.id.nodata);
        shop_name = findViewById(R.id.shop_name);
        collection_recyclerView = findViewById(R.id.collection_recyclerView);

        collectionDetailsListData = new ArrayList<>();
        leftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        outlet_id = getIntent().getExtras().getString("outlet_id");
        shopName = getIntent().getExtras().getString("shop_name");
        shop_name.setText(shopName);
//        System.out.println("outletIDDDD "+outlet_id);

        collection_recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        collection_recyclerView.setLayoutManager(layoutManager);

        collectionDetailsAdapter = new CollectionDetailsAdapter(CollectionDetailsActivity.this, collectionDetailsListData);
        collection_recyclerView.setAdapter(collectionDetailsAdapter);

        collection_recyclerView.addOnScrollListener(new PaginationListener(layoutManager, totalPage) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
//                currentPage++;

                boolean isConnected = ConnectivityReceiver.isConnected();
                if (isConnected) {
                    collectionDetailsListApi(offset, limit);

                } else {
                    CustomToast.getInstance(CollectionDetailsActivity.this).showSmallCustomToast("Please check your internet connection");
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
        collectionDetailsAdapter.clear();

        boolean isConnected = ConnectivityReceiver.isConnected();
        if (isConnected) {
            collectionDetailsListApi(offset, limit);
        } else {
            CustomToast.getInstance(CollectionDetailsActivity.this).showSmallCustomToast("Please check your internet connection");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        itemCount = 0;
        offset = 0;
        currentPage = PAGE_START;
        isLastPage = false;
        collectionDetailsAdapter.clear();
        boolean isConnected = ConnectivityReceiver.isConnected();
        if (isConnected) {
            collectionDetailsListApi(offset, limit);
        } else {
            CustomToast.getInstance(CollectionDetailsActivity.this).showSmallCustomToast("Please check your internet connection");
        }
    }

    public void collectionDetailsListApi(int offset1, int limit1) {
//        CustomProgress.showProgress(activity);

        if (isLoading) {
            progress.setVisibility(View.GONE);
            emptyView.setVisibility(View.GONE);
            nodata.setVisibility(View.GONE);
        } else {
            progress.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
            nodata.setVisibility(View.GONE);
        }


        Call<CollectionDetailsListModel> call = RetrofitClient
                .getInstance().getApi().getCollectionDetails("_outletCollectionReport",outlet_id,offset1, limit1);

        call.enqueue(new Callback<CollectionDetailsListModel>() {
            @Override
            public void onResponse(@NonNull Call<CollectionDetailsListModel> call, @NonNull Response<CollectionDetailsListModel> response) {

                try {

                    Gson gson = new Gson();
                    String json = gson.toJson(response.body());
                    CollectionDetailsListModel collectionDetailsListModel = gson.fromJson(json, CollectionDetailsListModel.class);

                    if (collectionDetailsListModel.getStatus() == 1) {
                        System.out.println("2131333333 ");

                        collection_recyclerView.setVisibility(View.VISIBLE);
                        progress.setVisibility(View.GONE);
                        emptyView.setVisibility(View.GONE);
                        nodata.setVisibility(View.GONE);

                        collectionDetailsListData = collectionDetailsListModel.getData();

                        offset = collectionDetailsListModel.getOffset();
                        limit = collectionDetailsListModel.getLimit();
                        totalcount = collectionDetailsListModel.getTotalRecord();

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
                            collectionDetailsAdapter.removeLoading();

                        collectionDetailsAdapter.addItems(collectionDetailsListData);

                        if (currentPage < totalPage) {
                            collectionDetailsAdapter.addLoading();
                        } else {
                            isLastPage = true;
                        }
                        isLoading = false;


//                        offset = siteListModel.getOffset();
                        progress.setVisibility(View.GONE);
                        emptyView.setVisibility(View.GONE);
                        nodata.setVisibility(View.GONE);

                    } else {
                        System.out.println("66666666666");
                        collection_recyclerView.setVisibility(View.GONE);
                        progress.setVisibility(View.GONE);
                        nodata.setVisibility(View.VISIBLE);
                        emptyView.setVisibility(View.VISIBLE);
                        emptyView.setText("No Record Found");
//                        siteListDataModelList.clear();
//                        Toast.makeText(LoginActivity.this, "Invalid User Name or Password", Toast.LENGTH_SHORT).show();
                        CustomToast.getInstance(CollectionDetailsActivity.this).showSmallCustomToast("No Record Found");
//                    Toast.makeText(LoginActivity.this, "Invalid User Name or Password", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    progress.setVisibility(View.GONE);
                    Log.d("Exceptionnnn", e.getMessage());
                }
            }

            @Override
            public void onFailure(@NonNull Call<CollectionDetailsListModel> call, @NonNull Throwable t) {
                collection_recyclerView.setVisibility(View.GONE);
                progress.setVisibility(View.GONE);
                emptyView.setVisibility(View.VISIBLE);
                nodata.setVisibility(View.VISIBLE);
                emptyView.setText("Something went wrong try again..");
            }
        });
    }
}