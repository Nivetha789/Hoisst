package com.retailvend.outstand;

import static com.retailvend.utills.PaginationListener.PAGE_START;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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

import com.google.gson.Gson;
import com.retailvend.LoginActivity;
import com.retailvend.R;
import com.retailvend.broadcast.ConnectivityReceiver;
import com.retailvend.model.outlets.AssignOutletsDatum;
import com.retailvend.model.outlets.AssignOutletsModel;
import com.retailvend.retrofit.RetrofitClient;
import com.retailvend.todayoutlet.TodayOutletActivity;
import com.retailvend.todayoutlet.TodayOutletAdapter;
import com.retailvend.utills.CustomProgress;
import com.retailvend.utills.CustomToast;
import com.retailvend.utills.PaginationListener;
import com.retailvend.utills.SessionManagerSP;
import com.retailvend.utills.SharedPrefManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OutstandingActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    RecyclerView outstandingRecycler;
    Activity activity;
    LinearLayoutManager mLayoutManager;
    OutstandingAdapter outstandingAdapter;
    ImageView leftArrow;
    Toolbar toolbar;
    List<AssignOutletsDatum> todayOutletsDatum;
    TextView total_amount;
    SessionManagerSP sessionManagerSP;
    TextView nodata_txt;
    ConstraintLayout no_data_constrain;
    LinearLayout searchLayout;
    ImageView search_icon,nodata;
    EditText search;
    ProgressBar progress;
    TextView emptyView;

    private int currentPage = PAGE_START;
    private boolean isLastPage = false;
    private int totalPage = 0;
    private boolean isLoading = false;
    int itemCount = 0;

    int offset = 0;
    int limit = 10;
    int totalcount = 0;

    String searchTxt = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outstanding);
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
        outstandingRecycler = findViewById(R.id.outstanding_recyclerView);
        leftArrow = findViewById(R.id.left_arrow);
        total_amount=findViewById(R.id.total_amount);
        nodata_txt=findViewById(R.id.nodata_txt);
        progress = findViewById(R.id.progress);
        search = findViewById(R.id.search);
        search_icon = findViewById(R.id.search_icon);
        searchLayout = findViewById(R.id.searchLayout);
        emptyView = findViewById(R.id.emptyView);
        nodata = findViewById(R.id.nodata);

        sessionManagerSP=new SessionManagerSP(OutstandingActivity.this);
        todayOutletsDatum=new ArrayList<>();

//        swipeRefresh.setOnRefreshListener(this);
//        salesRecycler.setHasFixedSize(true);
//        saleslists = new ArrayList<>();

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
                    outstandListApi(offset, limit, "2");
                } else {
                    CustomToast.getInstance(OutstandingActivity.this).showSmallCustomToast("Please check your internet connection");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        outstandingRecycler.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        outstandingRecycler.setLayoutManager(layoutManager);

        outstandingAdapter = new OutstandingAdapter(OutstandingActivity.this, todayOutletsDatum);
        outstandingRecycler.setAdapter(outstandingAdapter);

        outstandingRecycler.addOnScrollListener(new PaginationListener(layoutManager, totalPage) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
//                currentPage++;

                boolean isConnected = ConnectivityReceiver.isConnected();
                if (isConnected) {
                    outstandListApi(offset, limit, "1");

                } else {
                    CustomToast.getInstance(OutstandingActivity.this).showSmallCustomToast("Please check your internet connection");
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
        outstandingAdapter.clear();

        boolean isConnected = ConnectivityReceiver.isConnected();
        if (isConnected) {
            outstandListApi(offset, limit, "1");
        } else {
            CustomToast.getInstance(OutstandingActivity.this).showSmallCustomToast("Please check your internet connection");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        itemCount = 0;
        offset = 0;
        currentPage = PAGE_START;
        isLastPage = false;
        outstandingAdapter.clear();
        boolean isConnected = ConnectivityReceiver.isConnected();
        if (isConnected) {
            outstandListApi(offset, limit, "1");
        } else {
            CustomToast.getInstance(OutstandingActivity.this).showSmallCustomToast("Please check your internet connection");
        }
    }

    @Override
    public void onBackPressed() {
//        refreshActivity();
//        RegistorSharedPreManager.getInstance(DetailsActivity.this).clear();
        super.onBackPressed();
    }

    public void outstandListApi(int offset1, int limit1, String searchType) {
//        CustomProgress.showProgress(activity);
        String emp_id= sessionManagerSP.getEmployeeId();
//        System.out.println("emmmpidd "+emp_id);

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
                .getInstance().getApi().todayOutletList("_employeeWiseList",offset1, limit1,emp_id, searchTxt);

        call.enqueue(new Callback<AssignOutletsModel>() {
            @Override
            public void onResponse(Call<AssignOutletsModel> call, Response<AssignOutletsModel> response) {

                try {

                    Gson gson = new Gson();
                    String json = gson.toJson(response.body());
                    AssignOutletsModel assignOutletsModel = gson.fromJson(json, AssignOutletsModel.class);

                    if (assignOutletsModel.getStatus() == 1) {

                        if (searchType.equals("2")) {
                            if (todayOutletsDatum.size() > 0) {
                                outstandingAdapter.clear();
                            }
                        }

                        outstandingRecycler.setVisibility(View.VISIBLE);
                        progress.setVisibility(View.GONE);
                        emptyView.setVisibility(View.GONE);
                        nodata.setVisibility(View.GONE);
                        searchLayout.setVisibility(View.VISIBLE);
                        todayOutletsDatum = assignOutletsModel.getData();

                        offset = assignOutletsModel.getOffset();
                        limit = assignOutletsModel.getLimit();
                        totalcount = assignOutletsModel.getTotalRecord();

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
                            outstandingAdapter.removeLoading();

                        outstandingAdapter.addItems(todayOutletsDatum);

                        if (currentPage < totalPage) {
                            outstandingAdapter.addLoading();
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
                        outstandingRecycler.setVisibility(View.GONE);
                        progress.setVisibility(View.GONE);
                        nodata.setVisibility(View.VISIBLE);
                        emptyView.setVisibility(View.VISIBLE);
                        emptyView.setText(assignOutletsModel.getMessage());
                        searchLayout.setVisibility(View.GONE);
//                        siteListDataModelList.clear();
//                        Toast.makeText(LoginActivity.this, "Invalid User Name or Password", Toast.LENGTH_SHORT).show();
                        CustomToast.getInstance(OutstandingActivity.this).showSmallCustomToast("No Record Found");
//                    Toast.makeText(TodayOutletActivity.this, "Invalid User Name or Password", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    progress.setVisibility(View.GONE);
                    Log.d("Exceptionnnn", e.getMessage());
                }

            }

            @Override
            public void onFailure(Call<AssignOutletsModel> call, Throwable t) {
                outstandingRecycler.setVisibility(View.GONE);
                progress.setVisibility(View.GONE);
                emptyView.setVisibility(View.VISIBLE);
                nodata.setVisibility(View.VISIBLE);
                searchLayout.setVisibility(View.GONE);
                emptyView.setText("Something went wrong try again..");
            }
        });
    }
}