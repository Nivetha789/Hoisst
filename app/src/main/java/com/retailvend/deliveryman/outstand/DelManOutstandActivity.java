package com.retailvend.deliveryman.outstand;

import static com.retailvend.utills.PaginationListener.PAGE_START;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.retailvend.R;
import com.retailvend.broadcast.ConnectivityReceiver;
import com.retailvend.model.delManModels.delCollection.invoiceHistory.InvoiceHistoryDatum;
import com.retailvend.model.delManModels.delCollection.invoiceHistory.InvoiceHistoryModel;
import com.retailvend.model.delManModels.delCollection.outstand.OutstandDatum;
import com.retailvend.model.delManModels.delCollection.outstand.OutstandModel;
import com.retailvend.retrofit.RetrofitClient;
import com.retailvend.utills.CustomToast;
import com.retailvend.utills.PaginationListener;
import com.retailvend.utills.SessionManagerSP;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DelManOutstandActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener  {

    RecyclerView invoice_history_recycler;
    Activity activity;
    LinearLayoutManager mLayoutManager;
    DelManOutstandAdapter delManOutstandAdapter;
    ImageView leftArrow;
    Toolbar toolbar;
    Menu menu;
    TextView nodata_txt;
    ConstraintLayout no_data_constrain;
    List<OutstandDatum> outstandListData;

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
    String assignId="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_del_man_outstand);
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
        invoice_history_recycler = findViewById(R.id.invoice_history_recyecler);
        progress = findViewById(R.id.progress);
        nodata_txt=findViewById(R.id.nodata_txt);
        no_data_constrain=findViewById(R.id.no_data_constrain);

        sessionManagerSP = new SessionManagerSP(DelManOutstandActivity.this);

        outstandListData = new ArrayList<>();
        leftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        invoice_history_recycler.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        invoice_history_recycler.setLayoutManager(layoutManager);

        delManOutstandAdapter = new DelManOutstandAdapter(DelManOutstandActivity.this, outstandListData);
        invoice_history_recycler.setAdapter(delManOutstandAdapter);

        invoice_history_recycler.addOnScrollListener(new PaginationListener(layoutManager, totalPage) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
//                currentPage++;

                boolean isConnected = ConnectivityReceiver.isConnected();
                if (isConnected) {
                    outstandListApi(offset, limit);

                } else {
                    CustomToast.getInstance(DelManOutstandActivity.this).showSmallCustomToast("Please check your internet connection");
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
        delManOutstandAdapter.clear();

        boolean isConnected = ConnectivityReceiver.isConnected();
        if (isConnected) {
            outstandListApi(offset, limit);
        } else {
            CustomToast.getInstance(DelManOutstandActivity.this).showSmallCustomToast("Please check your internet connection");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        itemCount = 0;
        offset = 0;
        currentPage = PAGE_START;
        isLastPage = false;
        delManOutstandAdapter.clear();
        boolean isConnected = ConnectivityReceiver.isConnected();
        if (isConnected) {
            outstandListApi(offset, limit);
        } else {
            CustomToast.getInstance(DelManOutstandActivity.this).showSmallCustomToast("Please check your internet connection");
        }
    }

    public void outstandListApi(int offset1, int limit1) {
//        CustomProgress.showProgress(activity);
        String distributorId = sessionManagerSP.getDistributorId();

        if (isLoading) {
            progress.setVisibility(View.GONE);
            no_data_constrain.setVisibility(View.GONE);
        } else {
            progress.setVisibility(View.VISIBLE);
            no_data_constrain.setVisibility(View.GONE);
        }

        Call<OutstandModel> call = RetrofitClient
                .getInstance().getApi().outstandList("_distributorOutletList", distributorId, offset1, limit1);

        call.enqueue(new Callback<OutstandModel>() {
            @Override
            public void onResponse(@NonNull Call<OutstandModel> call, @NonNull Response<OutstandModel> response) {

                try {

                    Gson gson = new Gson();
                    String json = gson.toJson(response.body());
                    OutstandModel productNameResModel = gson.fromJson(json, OutstandModel.class);

                    if (productNameResModel.getStatus() == 1) {

                        invoice_history_recycler.setVisibility(View.VISIBLE);
                        progress.setVisibility(View.GONE);
                        no_data_constrain.setVisibility(View.GONE);

                        outstandListData = productNameResModel.getData();

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
                            delManOutstandAdapter.removeLoading();

                        delManOutstandAdapter.addItems(outstandListData);

                        if (currentPage < totalPage) {
                            delManOutstandAdapter.addLoading();
                        } else {
                            isLastPage = true;
                        }
                        isLoading = false;


//                        offset = siteListModel.getOffset();
                        progress.setVisibility(View.GONE);
                        no_data_constrain.setVisibility(View.GONE);

                    } else {
                        invoice_history_recycler.setVisibility(View.GONE);
                        progress.setVisibility(View.GONE);
                        no_data_constrain.setVisibility(View.VISIBLE);
                        nodata_txt.setText("No Record Found");
//                        siteListDataModelList.clear();
//                        Toast.makeText(LoginActivity.this, "Invalid User Name or Password", Toast.LENGTH_SHORT).show();
                        CustomToast.getInstance(DelManOutstandActivity.this).showSmallCustomToast("No Record Found");
//                    Toast.makeText(LoginActivity.this, "Invalid User Name or Password", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    progress.setVisibility(View.GONE);
                    Log.d("Exceptionnnn", e.getMessage());
                }
            }

            @Override
            public void onFailure(@NonNull Call<OutstandModel> call, @NonNull Throwable t) {
                invoice_history_recycler.setVisibility(View.GONE);
                progress.setVisibility(View.GONE);
                no_data_constrain.setVisibility(View.VISIBLE);
                nodata_txt.setText("Something went wrong try again..");
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}