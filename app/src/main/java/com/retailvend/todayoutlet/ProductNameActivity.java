package com.retailvend.todayoutlet;

import static com.retailvend.utills.PaginationListener.PAGE_START;

import android.app.Activity;
import android.content.Intent;
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
import com.retailvend.model.outlets.ProductNameResData;
import com.retailvend.model.outlets.ProductNameResModel;
import com.retailvend.retrofit.RetrofitClient;
import com.retailvend.utills.CustomToast;
import com.retailvend.utills.PaginationListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductNameActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    ProductNameAdapter productNameAdapter;
    RecyclerView product_name_recycler;
    List<ProductNameResData> productNameList;
    TextView mTitle,emptyView;
    private Toolbar toolbar;
    String order_type="";
    EditText search;
    LinearLayout searchLayout;
    ImageView search_icon,left_arrow,nodata;
    Activity activity;
    String prod_name="";
    String prod_id="";
    String gst="";
    String hsn="";

    String order_id="";

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
        setContentView(R.layout.activity_product_name);
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

        product_name_recycler=findViewById(R.id.product_name_recycler);
        toolbar = findViewById(R.id.toolbar);
        search = findViewById(R.id.search);
        search_icon = findViewById(R.id.search_icon);
        searchLayout = findViewById(R.id.searchLayout);
        mTitle = toolbar.findViewById(R.id.toolbar_title);
        left_arrow = findViewById(R.id.left_arrow);
        progress = findViewById(R.id.progress);
        emptyView = findViewById(R.id.emptyView);
        nodata = findViewById(R.id.nodata);

        Intent iin = getIntent();
        Bundle b = iin.getExtras();

        if (b != null) {
            order_type = (String) b.get("order_type");
            if(order_type.equals("Sales Agent")){
                order_id="2";
            }else{
                order_id="1";
            }
        }

        productNameList = new ArrayList<>();

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

                productNameApi(offset, limit, "2");

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


//        swipeRefresh.setOnRefreshListener(this);
        product_name_recycler.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        product_name_recycler.setLayoutManager(layoutManager);

        productNameAdapter = new ProductNameAdapter(ProductNameActivity.this, productNameList);
        product_name_recycler.setAdapter(productNameAdapter);

        product_name_recycler.addOnScrollListener(new PaginationListener(layoutManager, totalPage) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
//                currentPage++;

                boolean isConnected = ConnectivityReceiver.isConnected();
                if (isConnected) {
                    productNameApi(offset, limit, "1");

                } else {
                    CustomToast.getInstance(ProductNameActivity.this).showSmallCustomToast("Please check your internet connection");
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
        productNameAdapter.clear();

        boolean isConnected = ConnectivityReceiver.isConnected();
        if (isConnected) {
            productNameApi(offset, limit, "1");
        } else {
            CustomToast.getInstance(ProductNameActivity.this).showSmallCustomToast("Please check your internet connection");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        itemCount = 0;
        offset = 0;
        currentPage = PAGE_START;
        isLastPage = false;
        productNameAdapter.clear();
        boolean isConnected = ConnectivityReceiver.isConnected();
        if (isConnected) {
            productNameApi(offset, limit, "1");
        } else {
            CustomToast.getInstance(ProductNameActivity.this).showSmallCustomToast("Please check your internet connection");
        }
    }

    public void productNameApi(int offset1, int limit1, String searchType) {
//        CustomProgress.showProgress(activity);

        if (isLoading) {
            progress.setVisibility(View.GONE);
            emptyView.setVisibility(View.GONE);
        } else {
            progress.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }

        Call<ProductNameResModel> call = RetrofitClient
                .getInstance().getApi().getProductName("_listTypeWiseProduct",order_id,offset1, limit1, searchTxt);

        call.enqueue(new Callback<ProductNameResModel>() {
            @Override
            public void onResponse(@NonNull Call<ProductNameResModel> call, @NonNull Response<ProductNameResModel> response) {

                try {

                    Gson gson = new Gson();
                    String json = gson.toJson(response.body());
                    ProductNameResModel productNameResModel = gson.fromJson(json, ProductNameResModel.class);

                    if (productNameResModel.getStatus() == 1) {

                        if (searchType.equals("2")) {
//                            if (todayOutletsDatum.size() > 0) {
                            productNameAdapter.clear();
//                            }
                        }

                        product_name_recycler.setVisibility(View.VISIBLE);
                        emptyView.setVisibility(View.GONE);
                        nodata.setVisibility(View.GONE);
                        searchLayout.setVisibility(View.VISIBLE);
                        productNameList = productNameResModel.getData();

                        if(productNameList.size()>0){
                            offset = productNameResModel.getOffset();
                            limit = productNameResModel.getLimit();
                            totalcount = productNameResModel.getTotalRecord();

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
                                productNameAdapter.removeLoading();

                            productNameAdapter.addItems(productNameList);

                            if (currentPage < totalcount) {
                                productNameAdapter.addLoading();
                            }else if(currentPage>totalPage){
                                productNameAdapter.addLoading();
                                productNameAdapter.removeLoading();
                            }
                            else {
                                isLastPage = true;
                                productNameAdapter.removeLoading();
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
                    product_name_recycler.setVisibility(View.GONE);
                    progress.setVisibility(View.GONE);
                    nodata.setVisibility(View.VISIBLE);
                    emptyView.setVisibility(View.VISIBLE);
                    emptyView.setText("No Data Found");
                    searchLayout.setVisibility(View.GONE);
                    Log.d("Exceptionnnn", e.getMessage());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ProductNameResModel> call, @NonNull Throwable t) {
                product_name_recycler.setVisibility(View.GONE);
                progress.setVisibility(View.GONE);
                emptyView.setVisibility(View.VISIBLE);
                emptyView.setText("Something went wrong try again..");
            }
        });
    }

    public void updateProdName(String prodName, String prodId, String gstVal, String hsnCode){
        prod_name=prodName;
        prod_id=prodId;
        gst=gstVal;
        hsn=hsnCode;
        Intent mIntent = new Intent();
        mIntent.putExtra("prod_name",prod_name);
        mIntent.putExtra("prod_id",prod_id);
        mIntent.putExtra("gst", gst);
        mIntent.putExtra("hsn", hsn);
        activity.setResult(RESULT_OK, mIntent);
        finish();
    }

//    @Override
//    public void onBackPressed() {
//        Intent intent = new Intent();
//        intent.putExtra("prod_name", prod_name);
//        intent.putExtra("prod_id", prod_id);
//        intent.putExtra("gst", gst);
//        intent.putExtra("hsn", hsn);
//        setResult(RESULT_OK, intent);
//        finish();
//        super.onBackPressed();
//    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}