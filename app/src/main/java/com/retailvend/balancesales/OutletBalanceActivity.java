package com.retailvend.balancesales;

import static com.retailvend.utills.PaginationListener.PAGE_START;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.retailvend.R;
import com.retailvend.broadcast.ConnectivityReceiver;
import com.retailvend.model.outletBalanceModule.OutletBalanceDatum;
import com.retailvend.model.outletBalanceModule.OutletBalanceModel;
import com.retailvend.retrofit.RetrofitClient;
import com.retailvend.utills.CustomToast;
import com.retailvend.utills.PaginationListener;
import com.retailvend.utills.SessionManagerSP;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OutletBalanceActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    RecyclerView loyaltyRecycler;
    Activity activity;
    LinearLayoutManager mLayoutManager;
    OutletBalanceAdapter outletBalanceAdapter;
    ImageView leftArrow;
    List<OutletBalanceDatum> outletBalanceDatum;
    SessionManagerSP sessionManagerSP;
    LinearLayout searchLayout;
    ImageView search_icon, nodata;
    EditText search;
    TextView emptyView;
    ProgressBar progress;

    //    SwipeRefreshLayout swipeRefresh;
    private int currentPage = PAGE_START;
    private boolean isLastPage = false;
    private int totalPage = 0;
    private boolean isLoading = false;
    int itemCount = 0;

    int offset = 0;
    int limit = 10;
    int totalcount = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outlet_balance);

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

        loyaltyRecycler = findViewById(R.id.out_bal_recycler);
        leftArrow = findViewById(R.id.left_arrow);
        progress = findViewById(R.id.progress);
        search = findViewById(R.id.search);
        search_icon = findViewById(R.id.search_icon);
        searchLayout = findViewById(R.id.searchLayout);
        emptyView = findViewById(R.id.emptyView);
        nodata = findViewById(R.id.nodata);

        sessionManagerSP = new SessionManagerSP(com.retailvend.balancesales.OutletBalanceActivity.this);

        outletBalanceDatum = new ArrayList<>();
        leftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        search.addTextChangedListener(new TextWatcher() {
            CountDownTimer timer = null;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (timer != null) {
                    timer.cancel();
                }

                timer = new CountDownTimer(1500, 1000) {

                    public void onTick(long millisUntilFinished) {
                    }

                    public void onFinish() {

                        //do what you wish
                        itemCount = 0;
                        offset = 0;
                        currentPage = PAGE_START;
                        isLastPage = false;

                        if (outletBalanceDatum.size() > 0) {
                            outletBalanceAdapter.removeLoading();
                        }
                        outletBalanceAdapter.clear();

                        boolean isConnected = ConnectivityReceiver.isConnected();
                        if (isConnected) {
                            OutletBalanceApi(offset, limit, "2", s.toString());
                        } else {
                            CustomToast.getInstance(com.retailvend.balancesales.OutletBalanceActivity.this).showSmallCustomToast("Please check your internet connection");
                        }

                    }

                }.start();

                return;
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!(s.length() > 0)) {
                    if (timer != null) {
                        timer.cancel();
                    }

                    onRefresh();
                }
                return;
            }
        });

        loyaltyRecycler.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        loyaltyRecycler.setLayoutManager(layoutManager);

        outletBalanceAdapter = new OutletBalanceAdapter(com.retailvend.balancesales.OutletBalanceActivity.this, outletBalanceDatum);
        loyaltyRecycler.setAdapter(outletBalanceAdapter);

        loyaltyRecycler.addOnScrollListener(new PaginationListener(layoutManager, totalPage) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
//                currentPage++;

                boolean isConnected = ConnectivityReceiver.isConnected();
                if (isConnected) {
                    OutletBalanceApi(offset, limit, "1", "");

                } else {
                    CustomToast.getInstance(com.retailvend.balancesales.OutletBalanceActivity.this).showSmallCustomToast("Please check your internet connection");
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
        outletBalanceAdapter.clear();

        boolean isConnected = ConnectivityReceiver.isConnected();
        if (isConnected) {
            OutletBalanceApi(offset, limit, "1", "");
        } else {
            CustomToast.getInstance(com.retailvend.balancesales.OutletBalanceActivity.this).showSmallCustomToast("Please check your internet connection");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        itemCount = 0;
        offset = 0;
        currentPage = PAGE_START;
        isLastPage = false;
        outletBalanceAdapter.clear();
        boolean isConnected = ConnectivityReceiver.isConnected();
        if (isConnected) {
            OutletBalanceApi(offset, limit, "1", "");
        } else {
            CustomToast.getInstance(com.retailvend.balancesales.OutletBalanceActivity.this).showSmallCustomToast("Please check your internet connection");
        }
    }


    public void OutletBalanceApi(int offset1, int limit1, String searchType, String searchTxt) {
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

        Call<OutletBalanceModel> call = RetrofitClient
                .getInstance().getApi().outletBalance("_outletPaymentList", emp_id, offset1, limit1, searchTxt);

        call.enqueue(new Callback<OutletBalanceModel>() {
            @Override
            public void onResponse(@NonNull Call<OutletBalanceModel> call, @NonNull Response<OutletBalanceModel> response) {

                try {

                    Gson gson = new Gson();
                    String json = gson.toJson(response.body());
                    OutletBalanceModel loyaltyModel = gson.fromJson(json, OutletBalanceModel.class);
//                LoginModule loginModule = response.body();

//                    if (propertiesList != null) {
//                        productListAdapter.clear();
//                    }

                    if (loyaltyModel.getStatus() == 1) {
                        loyaltyRecycler.setVisibility(View.VISIBLE);
                        progress.setVisibility(View.GONE);
                        emptyView.setVisibility(View.GONE);
                        outletBalanceDatum = loyaltyModel.getData();
                        nodata.setVisibility(View.GONE);
                        searchLayout.setVisibility(View.GONE);

//                        offset = loyaltyModel.get();
//
//                        currentPage = loyaltyModel.getOffset();
//                        totalPage = loyaltyModel.getTotalRecord();


                        if (currentPage != PAGE_START)
                            outletBalanceAdapter.removeLoading();
                        outletBalanceAdapter.addItems(outletBalanceDatum);
//                        swipeRefresh.setRefreshing(false);
                        // check weather is last page or not
                        if (currentPage < totalPage) {
                            outletBalanceAdapter.addLoading();
                        } else {
                            isLastPage = true;
                        }
                        isLoading = false;

//                        currentPage = loyaltyModel.getOffset();
                        progress.setVisibility(View.GONE);
                        emptyView.setVisibility(View.GONE);
                        nodata.setVisibility(View.GONE);
                        searchLayout.setVisibility(View.GONE);

                    } else {
                        loyaltyRecycler.setVisibility(View.GONE);
                        progress.setVisibility(View.GONE);
                        emptyView.setVisibility(View.VISIBLE);
                        nodata.setVisibility(View.VISIBLE);
                        searchLayout.setVisibility(View.GONE);
                        emptyView.setText(loyaltyModel.getMessage());
                        outletBalanceDatum.clear();
//                        Toast.makeText(LoginActivity.this, "Invalid User Name or Password", Toast.LENGTH_SHORT).show();
                        CustomToast.getInstance(com.retailvend.balancesales.OutletBalanceActivity.this).showSmallCustomToast("No Record Found");
//                    Toast.makeText(LoginActivity.this, "Invalid User Name or Password", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    Log.d("Exception", e.getMessage());
                }
            }

            @Override
            public void onFailure(@NonNull Call<OutletBalanceModel> call, @NonNull Throwable t) {
                loyaltyRecycler.setVisibility(View.GONE);
                progress.setVisibility(View.GONE);
                emptyView.setVisibility(View.VISIBLE);
                nodata.setVisibility(View.VISIBLE);
                searchLayout.setVisibility(View.GONE);
                emptyView.setText("Something went wrong try again..");
            }
        });
    }
}