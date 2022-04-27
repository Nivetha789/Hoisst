package com.retailvend.deliveryman.collection;

import static com.retailvend.utills.PaginationListener.PAGE_START;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
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
import com.retailvend.model.delManModels.delCollection.DeliveryCollectionListData;
import com.retailvend.model.delManModels.delCollection.DeliveryCollectionListModel;
import com.retailvend.retrofit.RetrofitClient;
import com.retailvend.utills.CustomToast;
import com.retailvend.utills.PaginationListener;
import com.retailvend.utills.SessionManagerSP;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CollectionDeliveryActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    Activity activity;
    private int currentPage = PAGE_START;
    private boolean isLastPage = false;
    private int totalPage = 0;
    private boolean isLoading = false;
    int itemCount = 0;
    ProgressBar progress;
    TextView nodata_txt;

    SessionManagerSP sessionManagerSP;
    //distributor id
    String distributor_id="";

    int offset = 0;
    int limit = 10;
    int totalcount = 0;

    List<DeliveryCollectionListData> deliveryCollectionListData;

    CollectionDeliveryAdapter collectionDeliveryAdapter;
    RecyclerView collection_recyclerView;
    ImageView left_arrow,nodata;
    TextView emptyView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection_delivery);
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

        left_arrow=findViewById(R.id.left_arrow);
        nodata_txt=findViewById(R.id.nodata_txt);
        collection_recyclerView=findViewById(R.id.collection_recyclerView);
        progress = findViewById(R.id.progress);
        emptyView = findViewById(R.id.emptyView);
        nodata = findViewById(R.id.nodata);

        sessionManagerSP = new SessionManagerSP(CollectionDeliveryActivity.this);

        distributor_id=sessionManagerSP.getDistributorId();

        deliveryCollectionListData = new ArrayList<>();


//        swipeRefresh.setOnRefreshListener(this);
        collection_recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        collection_recyclerView.setLayoutManager(layoutManager);

        collectionDeliveryAdapter = new CollectionDeliveryAdapter(CollectionDeliveryActivity.this, deliveryCollectionListData);
        collection_recyclerView.setAdapter(collectionDeliveryAdapter);

        left_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        collection_recyclerView.addOnScrollListener(new PaginationListener(layoutManager, totalPage) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
//                currentPage++;

                boolean isConnected = ConnectivityReceiver.isConnected();
                if (isConnected) {
                    collectionListApi(offset, limit, "1");

                } else {
                    CustomToast.getInstance(CollectionDeliveryActivity.this).showSmallCustomToast("Please check your internet connection");
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
        collectionDeliveryAdapter.clear();

        boolean isConnected = ConnectivityReceiver.isConnected();
        if (isConnected) {
            collectionListApi(offset, limit, "1");
        } else {
            CustomToast.getInstance(CollectionDeliveryActivity.this).showSmallCustomToast("Please check your internet connection");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        itemCount = 0;
        offset = 0;
        currentPage = PAGE_START;
        isLastPage = false;
        collectionDeliveryAdapter.clear();
        boolean isConnected = ConnectivityReceiver.isConnected();
        if (isConnected) {
            collectionListApi(offset, limit, "1");
        } else {
            CustomToast.getInstance(CollectionDeliveryActivity.this).showSmallCustomToast("Please check your internet connection");
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void collectionListApi(int offset1, int limit1, String searchType) {
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

        Call<DeliveryCollectionListModel> call = RetrofitClient
                .getInstance().getApi().collectionList("_distributorOutletList",distributor_id,offset1, limit1);

        call.enqueue(new Callback<DeliveryCollectionListModel>() {
            @Override
            public void onResponse(@NonNull Call<DeliveryCollectionListModel> call, @NonNull Response<DeliveryCollectionListModel> response) {

                try {

                    Gson gson = new Gson();
                    String json = gson.toJson(response.body());
                    DeliveryCollectionListModel deliveryCollectionListModel = gson.fromJson(json, DeliveryCollectionListModel.class);

                    if (deliveryCollectionListModel.getStatus() == 1) {

                        collection_recyclerView.setVisibility(View.VISIBLE);
                        progress.setVisibility(View.GONE);

                        deliveryCollectionListData = deliveryCollectionListModel.getData();

                        if (searchType.equals("2")) {
//                            if (todayOutletsDatum.size() > 0) {
                            collectionDeliveryAdapter.clear();
//                            }
                        }

                        collection_recyclerView.setVisibility(View.VISIBLE);
                        emptyView.setVisibility(View.GONE);
                        nodata.setVisibility(View.GONE);
                        deliveryCollectionListData = deliveryCollectionListModel.getData();

                        if(deliveryCollectionListData.size()>0){
                            offset = deliveryCollectionListModel.getOffset();
                            limit = deliveryCollectionListModel.getLimit();
                            totalcount = deliveryCollectionListModel.getTotalRecord();

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
                                collectionDeliveryAdapter.removeLoading();

                            collectionDeliveryAdapter.addItems(deliveryCollectionListData);

                            if (currentPage < totalcount) {
                                collectionDeliveryAdapter.addLoading();
                            }else if(currentPage>totalPage){
                                collectionDeliveryAdapter.addLoading();
                                collectionDeliveryAdapter.removeLoading();
                            }
                            else {
                                isLastPage = true;
                                collectionDeliveryAdapter.removeLoading();
                            }
                        }else{
                            if(searchType.equals("2")){
                                progress.setVisibility(View.GONE);
                                emptyView.setVisibility(View.VISIBLE);
                                nodata.setVisibility(View.VISIBLE);
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
                            collection_recyclerView.setVisibility(View.GONE);
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
                    collection_recyclerView.setVisibility(View.GONE);
                    progress.setVisibility(View.GONE);
                    nodata.setVisibility(View.VISIBLE);
                    emptyView.setVisibility(View.VISIBLE);
                    emptyView.setText("No Data Found");
                    Log.d("Exceptionnnn", e.getMessage());
                }
            }

            @Override
            public void onFailure(@NonNull Call<DeliveryCollectionListModel> call, @NonNull Throwable t) {
                collection_recyclerView.setVisibility(View.GONE);
                progress.setVisibility(View.GONE);
                emptyView.setVisibility(View.VISIBLE);
                nodata.setVisibility(View.VISIBLE);
                emptyView.setText("Something went wrong try again..");
            }
        });
    }
}