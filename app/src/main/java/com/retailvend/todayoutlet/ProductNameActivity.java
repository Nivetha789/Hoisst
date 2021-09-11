package com.retailvend.todayoutlet;

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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.retailvend.R;
import com.retailvend.broadcast.ConnectivityReceiver;
import com.retailvend.model.outlets.ProductNameResData;
import com.retailvend.model.outlets.ProductNameResModel;
import com.retailvend.retrofit.RetrofitClient;
import com.retailvend.utills.CustomProgress;
import com.retailvend.utills.CustomToast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductNameActivity extends AppCompatActivity {

    ProductNameAdapter productNameAdapter;
    RecyclerView product_name_recycler;
    List<ProductNameResData> productNameList = new ArrayList<>();
    TextView mTitle,emptyView;
    private Toolbar toolbar;
    String order_type="";
    EditText search;
    LinearLayout searchLayout;
    ImageView search_icon,left_arrow;
    Activity activity;
    String prod_name="";
    String prod_id="";
    String gst="";
    String hsn="";

    String order_id="";

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
        product_name_recycler = findViewById(R.id.product_name_recycler);


        productNameList = new ArrayList<>();

        left_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        search_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productNameApi();
            }
        });

        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                        || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER){

                    //execute our method for searching
                }

                return false;
            }
        });

        search.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                productNameAdapter.getFilter().filter(cs);
                productNameAdapter.notifyDataSetChanged();

                if (cs.length() >= 1) {
                    productNameAdapter.getFilter().filter(cs);
                }
                else {
                    productNameAdapter = new ProductNameAdapter(ProductNameActivity.this,productNameList);
                    LinearLayoutManager mLayoutManager = new LinearLayoutManager(ProductNameActivity.this);

                    product_name_recycler.setLayoutManager(mLayoutManager);
                    product_name_recycler.setItemAnimator(new DefaultItemAnimator());
                    product_name_recycler.setAdapter(productNameAdapter);
                    productNameAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
//                                    Toast.makeText(getApplicationContext(),"before text change",Toast.LENGTH_LONG).show();
            }

            @Override
            public void afterTextChanged(Editable arg0) {
//                partCodeAdapter.getFilter().filter(arg0);
//                partCodeAdapter.notifyDataSetChanged();
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        boolean isConnected = ConnectivityReceiver.isConnected();
        if (isConnected) {
            productNameApi();
        } else {
            CustomToast.getInstance(ProductNameActivity.this).showSmallCustomToast("Please check your internet connection");
        }
    }

    public void productNameApi() {
        CustomProgress.showProgress(activity);

        Call<ProductNameResModel> call = RetrofitClient
                .getInstance().getApi().getProductName("_listTypeWiseProduct",order_id,search.getText().toString());

        call.enqueue(new Callback<ProductNameResModel>() {
            @Override
            public void onResponse(@NonNull Call<ProductNameResModel> call, @NonNull Response<ProductNameResModel> response) {

                try {

                    Gson gson = new Gson();
                    String json = gson.toJson(response.body());
                    ProductNameResModel productNameResModel = gson.fromJson(json, ProductNameResModel.class);

                    if (productNameResModel.getStatus() == 1) {

                        productNameList = productNameResModel.getData();

                        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
                        product_name_recycler.setLayoutManager(layoutManager);

                        productNameAdapter = new ProductNameAdapter(ProductNameActivity.this, productNameList);
                        product_name_recycler.setAdapter(productNameAdapter);
                        CustomProgress.hideProgress(activity);

                    } else {
//                        sales_agent_name_recycler.setVisibility(View.GONE);
//                        siteListDataModelList.clear();
//                        Toast.makeText(LoginActivity.this, "Invalid User Name or Password", Toast.LENGTH_SHORT).show();
                        CustomToast.getInstance(ProductNameActivity.this).showSmallCustomToast("No Record Found");
                        CustomProgress.hideProgress(activity);
//                    Toast.makeText(LoginActivity.this, "Invalid User Name or Password", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    Log.d("Exceptionnnn", e.getMessage());
                    CustomProgress.hideProgress(activity);
                }

            }

            @Override
            public void onFailure(@NonNull Call<ProductNameResModel> call, @NonNull Throwable t) {
                Log.d("Failure ", t.getMessage());
                CustomProgress.hideProgress(activity);
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