package com.retailvend.todayoutlet;

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
import com.retailvend.model.outlets.AssignOutletsDatum;
import com.retailvend.model.outlets.AssignOutletsModel;
import com.retailvend.retrofit.RetrofitClient;
import com.retailvend.utills.CustomToast;
import com.retailvend.utills.PaginationListener;
import com.retailvend.utills.SessionManagerSP;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TodayOutletActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    RecyclerView todayOutletRecycler;
    Activity activity;
    LinearLayoutManager mLayoutManager;
    TodayOutletAdapter todayOutletAdapter;
    ImageView leftArrow;
    List<AssignOutletsDatum> todayOutletsDatum;
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
        setContentView(R.layout.activity_today_outlet);
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

        todayOutletRecycler = findViewById(R.id.today_outlet_recycler);
        leftArrow = findViewById(R.id.left_arrow);
        progress = findViewById(R.id.progress);
        search = findViewById(R.id.search);
        search_icon = findViewById(R.id.search_icon);
        searchLayout = findViewById(R.id.searchLayout);
        emptyView = findViewById(R.id.emptyView);
        nodata = findViewById(R.id.nodata);

        sessionManagerSP = new SessionManagerSP(TodayOutletActivity.this);

        todayOutletsDatum = new ArrayList<>();

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

                        if (todayOutletsDatum.size() > 0) {
                            todayOutletAdapter.removeLoading();
                        }
                        todayOutletAdapter.clear();

                        boolean isConnected = ConnectivityReceiver.isConnected();
                        if (isConnected) {
                            todayOutletListApi(offset, limit, "2",s.toString());
                        } else {
                            CustomToast.getInstance(TodayOutletActivity.this).showSmallCustomToast("Please check your internet connection");
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

        todayOutletRecycler.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        todayOutletRecycler.setLayoutManager(layoutManager);

        todayOutletAdapter = new TodayOutletAdapter(TodayOutletActivity.this, todayOutletsDatum);
        todayOutletRecycler.setAdapter(todayOutletAdapter);

        todayOutletRecycler.addOnScrollListener(new PaginationListener(layoutManager, totalPage) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
//                currentPage++;

                boolean isConnected = ConnectivityReceiver.isConnected();
                if (isConnected) {
                    todayOutletListApi(offset, limit, "1","");

                } else {
                    CustomToast.getInstance(TodayOutletActivity.this).showSmallCustomToast("Please check your internet connection");
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
        todayOutletAdapter.clear();

        boolean isConnected = ConnectivityReceiver.isConnected();
        if (isConnected) {
            todayOutletListApi(offset, limit, "1","");
        } else {
            CustomToast.getInstance(TodayOutletActivity.this).showSmallCustomToast("Please check your internet connection");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        itemCount = 0;
        offset = 0;
        currentPage = PAGE_START;
        isLastPage = false;
        todayOutletAdapter.clear();
        boolean isConnected = ConnectivityReceiver.isConnected();
        if (isConnected) {
            todayOutletListApi(offset, limit, "1","");
        } else {
            CustomToast.getInstance(TodayOutletActivity.this).showSmallCustomToast("Please check your internet connection");
        }
    }


    public void todayOutletListApi(int offset1, int limit1, String searchType,String searchTxt) {
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

        Call<AssignOutletsModel> call = RetrofitClient
                .getInstance().getApi().todayOutletList("_employeeWiseList", offset1, limit1, emp_id, searchTxt);

        call.enqueue(new Callback<AssignOutletsModel>() {
            @Override
            public void onResponse(@NonNull Call<AssignOutletsModel> call, @NonNull Response<AssignOutletsModel> response) {

//                try {
//
//                    Gson gson = new Gson();
//                    String json = gson.toJson(response.body());
//                    AssignOutletsModel assignOutletsModel = gson.fromJson(json, AssignOutletsModel.class);
//
//                    if (assignOutletsModel.getStatus() == 1) {
//
//                        if (searchType.equals("2")) {
////                            if (todayOutletsDatum.size() > 0) {
//                                todayOutletAdapter.clear();
////                            }
//                        }
//
//                        todayOutletRecycler.setVisibility(View.VISIBLE);
//                        emptyView.setVisibility(View.GONE);
//                        nodata.setVisibility(View.GONE);
//                        searchLayout.setVisibility(View.VISIBLE);
//                        todayOutletsDatum = assignOutletsModel.getData();
//
//                        if(todayOutletsDatum.size()>0){
//                            offset = assignOutletsModel.getOffset();
//                            limit = assignOutletsModel.getLimit();
//                            totalcount = assignOutletsModel.getTotalRecord();
//
////                        int offest1 = offset;
////                        int totalcount1;
////                        if (totalcount > offset) {
////                            totalcount1 = offset + limit;
////                        } else {
////                            totalcount1 = offset;
////                        }
//
//
//                            currentPage = offset;
////                        totalPage = totalcount;
//
//
//                            if (currentPage != PAGE_START)
//                                todayOutletAdapter.removeLoading();
//
//                            todayOutletAdapter.addItems(todayOutletsDatum);
//
//                            if (currentPage < totalcount) {
//                                todayOutletAdapter.addLoading();
//                            }else if(currentPage>totalPage){
//                                todayOutletAdapter.addLoading();
//                                todayOutletAdapter.removeLoading();
//                            }
//                            else {
//                                isLastPage = true;
//                                todayOutletAdapter.removeLoading();
//                            }
//                        }else{
//                            if(searchType.equals("2")){
//                                progress.setVisibility(View.GONE);
//                                emptyView.setVisibility(View.VISIBLE);
//                                nodata.setVisibility(View.VISIBLE);
//                                searchLayout.setVisibility(View.GONE);
//                            }
//                        }
//
//                        isLoading = false;
//
//
////                        offset = siteListModel.getOffset();
//                        progress.setVisibility(View.GONE);
////                        emptyView.setVisibility(View.GONE);
////                        nodata.setVisibility(View.GONE);
////                        searchLayout.setVisibility(View.VISIBLE);
//
//                    } else {
//                        if(searchType.equals("2")){
//                            progress.setVisibility(View.GONE);
//                            emptyView.setVisibility(View.VISIBLE);
//                            nodata.setVisibility(View.VISIBLE);
//                            searchLayout.setVisibility(View.GONE);
//                            todayOutletRecycler.setVisibility(View.GONE);
//                        }
//                        todayOutletsDatum.clear();
//                        progress.setVisibility(View.GONE);
//                        todayOutletRecycler.setVisibility(View.GONE);
//                        nodata.setVisibility(View.VISIBLE);
//                        emptyView.setVisibility(View.VISIBLE);
//                        emptyView.setText(assignOutletsModel.getMessage());
////                        searchLayout.setVisibility(View.GONE);
////                        siteListDataModelList.clear();
////                        Toast.makeText(LoginActivity.this, "Invalid User Name or Password", Toast.LENGTH_SHORT).show();
////                        CustomToast.getInstance(TodayOutletActivity.this).showSmallCustomToast("No Record Found");
////                    Toast.makeText(TodayOutletActivity.this, "Invalid User Name or Password", Toast.LENGTH_SHORT).show();
//                    }
//
//                } catch (Exception e) {
//                    progress.setVisibility(View.GONE);
//                    todayOutletRecycler.setVisibility(View.GONE);
//                    progress.setVisibility(View.GONE);
//                    nodata.setVisibility(View.VISIBLE);
//                    emptyView.setVisibility(View.VISIBLE);
//                    emptyView.setText("No Data Found");
//                    searchLayout.setVisibility(View.GONE);
//                    Log.d("Exceptionnnn", e.getMessage());
//                }


                try {

                    Gson gson = new Gson();
                    String json = gson.toJson(response.body());
                    AssignOutletsModel productModel = gson.fromJson(json, AssignOutletsModel.class);
//                LoginModule loginModule = response.body();

//                    if (propertiesList != null) {
//                        productListAdapter.clear();
//                    }

                    if (productModel.getStatus() == 1) {
                        todayOutletRecycler.setVisibility(View.VISIBLE);
                        progress.setVisibility(View.GONE);
                        emptyView.setVisibility(View.GONE);
                        todayOutletsDatum = productModel.getData();
                        nodata.setVisibility(View.GONE);
                        searchLayout.setVisibility(View.VISIBLE);

                        offset = productModel.getOffset();

                        currentPage = productModel.getOffset();
                        totalPage = productModel.getTotalRecord();


                        if (currentPage != PAGE_START)
                            todayOutletAdapter.removeLoading();
                        todayOutletAdapter.addItems(todayOutletsDatum);
//                        swipeRefresh.setRefreshing(false);
                        // check weather is last page or not
                        if (currentPage < totalPage) {
                            todayOutletAdapter.addLoading();
                        } else {
                            isLastPage = true;
                        }
                        isLoading = false;

                        currentPage = productModel.getOffset();
                        progress.setVisibility(View.GONE);
                        emptyView.setVisibility(View.GONE);
                        nodata.setVisibility(View.GONE);
                        searchLayout.setVisibility(View.VISIBLE);

                    } else {
                        todayOutletRecycler.setVisibility(View.GONE);
                        progress.setVisibility(View.GONE);
                        emptyView.setVisibility(View.VISIBLE);
                        nodata.setVisibility(View.VISIBLE);
                        searchLayout.setVisibility(View.VISIBLE);
                        emptyView.setText(productModel.getMessage());
                        todayOutletsDatum.clear();
//                        Toast.makeText(LoginActivity.this, "Invalid User Name or Password", Toast.LENGTH_SHORT).show();
                        CustomToast.getInstance(TodayOutletActivity.this).showSmallCustomToast("No Record Found");
//                    Toast.makeText(LoginActivity.this, "Invalid User Name or Password", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    Log.d("Exception", e.getMessage());
                }
            }

            @Override
            public void onFailure(@NonNull Call<AssignOutletsModel> call, @NonNull Throwable t) {
                todayOutletRecycler.setVisibility(View.GONE);
                progress.setVisibility(View.GONE);
                emptyView.setVisibility(View.VISIBLE);
                nodata.setVisibility(View.VISIBLE);
                searchLayout.setVisibility(View.GONE);
                emptyView.setText("Something went wrong try again..");
            }
        });
    }

}