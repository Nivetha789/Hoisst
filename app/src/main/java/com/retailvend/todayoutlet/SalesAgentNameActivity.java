package com.retailvend.todayoutlet;

import static com.retailvend.todayoutlet.PaginationListener.PAGE_START;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.retailvend.R;
import com.retailvend.broadcast.ConnectivityReceiver;
import com.retailvend.model.outlets.SalesAgentData;
import com.retailvend.model.outlets.SalesAgentsListModel;
import com.retailvend.retrofit.RetrofitClient;
import com.retailvend.utills.CustomProgress;
import com.retailvend.utills.CustomToast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SalesAgentNameActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    SalesAgentAdapter salesAgentAdapter;
    RecyclerView sales_agent_name_recycler;
    TextView mTitle, emptyView;
    private Toolbar toolbar;
    String WHICHPART = "";
    EditText search;
    LinearLayout searchLayout;
    ImageView search_icon, left_arrow;
    Activity activity;
    List<SalesAgentData> salesAgentDataList;
    String sales_agent_name = "";
    String sales_agentId = "";

    //    SwipeRefreshLayout swipeRefresh;
    private int currentPage = PAGE_START;
    private boolean isLastPage = false;
    private int totalPage = 0;
    private boolean isLoading = false;
    int itemCount = 0;
    ProgressBar progress;

    int offset = 0;
    int limit = 10;
    int totalcount = 0;

    String searchTxt = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_agent_name);
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

        Intent iin = getIntent();
        Bundle b = iin.getExtras();

        if (b != null) {
            WHICHPART = (String) b.get("WHICHPART");
        }


        toolbar = findViewById(R.id.toolbar);
        search = findViewById(R.id.search);
        search_icon = findViewById(R.id.search_icon);
        searchLayout = findViewById(R.id.searchLayout);
        mTitle = toolbar.findViewById(R.id.toolbar_title);
        left_arrow = findViewById(R.id.left_arrow);

//        swipeRefresh = findViewById(R.id.swipeRefresh);
        progress = findViewById(R.id.progress);
        sales_agent_name_recycler = findViewById(R.id.sales_agent_name_recycler);
        emptyView = findViewById(R.id.emptyView);


        salesAgentDataList = new ArrayList<>();


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

                salesAgentType(offset, limit, "2");

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


//        swipeRefresh.setOnRefreshListener(this);
        sales_agent_name_recycler.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        sales_agent_name_recycler.setLayoutManager(layoutManager);

        salesAgentAdapter = new SalesAgentAdapter(SalesAgentNameActivity.this, salesAgentDataList);
        sales_agent_name_recycler.setAdapter(salesAgentAdapter);

        sales_agent_name_recycler.addOnScrollListener(new PaginationListener(layoutManager, totalPage) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
//                currentPage++;

                boolean isConnected = ConnectivityReceiver.isConnected();
                if (isConnected) {
                    salesAgentType(offset, limit, "1");

                } else {
                    CustomToast.getInstance(SalesAgentNameActivity.this).showSmallCustomToast("Please check your internet connection");
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
        salesAgentAdapter.clear();

        boolean isConnected = ConnectivityReceiver.isConnected();
        if (isConnected) {
            salesAgentType(offset, limit, "1");
        } else {
            CustomToast.getInstance(SalesAgentNameActivity.this).showSmallCustomToast("Please check your internet connection");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        itemCount = 0;
        offset = 0;
        currentPage = PAGE_START;
        isLastPage = false;
        salesAgentAdapter.clear();
        boolean isConnected = ConnectivityReceiver.isConnected();
        if (isConnected) {
            salesAgentType(offset, limit, "1");
        } else {
            CustomToast.getInstance(SalesAgentNameActivity.this).showSmallCustomToast("Please check your internet connection");
        }
    }

    public void salesAgentType(int offset1, int limit1, String searchType) {

        if (isLoading) {
            progress.setVisibility(View.GONE);
            emptyView.setVisibility(View.GONE);
        } else {
            progress.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }

//        String emp_id= SharedPrefManager.getInstance(CreateOutletOrderActivity.this).getUser().getId();

        Call<SalesAgentsListModel> call = RetrofitClient
                .getInstance().getApi().salesAgentsType("_listOverallSalesagents", offset1, limit1, searchTxt);

        call.enqueue(new Callback<SalesAgentsListModel>() {
            @Override
            public void onResponse(Call<SalesAgentsListModel> call, Response<SalesAgentsListModel> response) {


                try {

                    Gson gson = new Gson();
                    String json = gson.toJson(response.body());
                    SalesAgentsListModel salesAgentsListModel = gson.fromJson(json, SalesAgentsListModel.class);

                    if (salesAgentsListModel.getStatus() == 1) {

                        if (searchType.equals("2")) {
                            if (salesAgentDataList.size() > 0) {
                                salesAgentAdapter.clear();
                            }
                        }

                        sales_agent_name_recycler.setVisibility(View.VISIBLE);
                        progress.setVisibility(View.GONE);
                        emptyView.setVisibility(View.GONE);

                        salesAgentDataList = salesAgentsListModel.getData();

                        offset = salesAgentsListModel.getOffset();
                        limit = salesAgentsListModel.getLimit();
                        totalcount = salesAgentsListModel.getTotalRecord();

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
                            salesAgentAdapter.removeLoading();

                        salesAgentAdapter.addItems(salesAgentDataList);

                        if (currentPage < totalPage) {
                            salesAgentAdapter.addLoading();
                        } else {
                            isLastPage = true;
                        }
                        isLoading = false;


//                        offset = siteListModel.getOffset();
                        progress.setVisibility(View.GONE);
                        emptyView.setVisibility(View.GONE);

                    } else {
                        sales_agent_name_recycler.setVisibility(View.GONE);
                        progress.setVisibility(View.GONE);
                        emptyView.setVisibility(View.VISIBLE);
                        emptyView.setText("No Record Found");
//                        siteListDataModelList.clear();
//                        Toast.makeText(LoginActivity.this, "Invalid User Name or Password", Toast.LENGTH_SHORT).show();
                        CustomToast.getInstance(SalesAgentNameActivity.this).showSmallCustomToast("No Record Found");
//                    Toast.makeText(LoginActivity.this, "Invalid User Name or Password", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    progress.setVisibility(View.GONE);
                    Log.d("Exceptionnnn", e.getMessage());
                }

            }

            @Override
            public void onFailure(Call<SalesAgentsListModel> call, Throwable t) {
                Log.d("Failure ", t.getMessage());
                sales_agent_name_recycler.setVisibility(View.GONE);
                progress.setVisibility(View.GONE);
                emptyView.setVisibility(View.VISIBLE);
                emptyView.setText("Something went wrong try again..");
            }
        });
    }

    public void updateSalesNameAdapter(String salesAgentName, String id) {
        sales_agent_name = salesAgentName;
        sales_agentId = id;
        Intent mIntent = new Intent(SalesAgentNameActivity.this, CreateOutletOrderActivity.class);
        mIntent.putExtra("sales_name", sales_agent_name);
        mIntent.putExtra("sales_id", sales_agentId);
        startActivity(mIntent);
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}