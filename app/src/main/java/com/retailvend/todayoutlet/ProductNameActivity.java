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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.retailvend.R;
import com.retailvend.model.outlets.AssignOutletsModel;
import com.retailvend.model.outlets.ProductNameResData;
import com.retailvend.model.outlets.ProductNameResModel;
import com.retailvend.model.outlets.ProductTypeDatum;
import com.retailvend.model.outlets.ProductTypeModel;
import com.retailvend.retrofit.Api;
import com.retailvend.retrofit.RetrofitClient;
import com.retailvend.utills.CustomProgress;
import com.retailvend.utills.CustomToast;
import com.retailvend.utills.Loader;
import com.retailvend.utills.SharedPrefManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProductNameActivity extends AppCompatActivity {

    ProductNameAdapter productNameAdapter;
    RecyclerView product_name_recycler;
    List<ProductNameResData> productNameList = new ArrayList<>();
    TextView mTitle;
    private Toolbar toolbar;
    String WHICHPART="";
    EditText search;
    LinearLayout searchLayout;
    ImageView search_icon;
    Activity activity;
    String prod_name="";
    List<ProductTypeDatum> productTypeData;

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

        Intent iin = getIntent();
        Bundle b = iin.getExtras();

        if (b != null) {
            WHICHPART = (String) b.get("WHICHPART");
        }

        productNameApi();

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
//                productNameAdapter.getFilter().filter(arg0);
//                productNameAdapter.notifyDataSetChanged();
            }
        });
    }

    private void filter(String text) {
        ArrayList<ProductNameResData> filteredList = new ArrayList<>();
        for (ProductNameResData item : productNameList) {
            if (item.getProductName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        productNameAdapter.filterList(filteredList);
    }

    public void productNameApi() {
        final CustomProgress customProgress = new CustomProgress(this);
//        text_signIn.setVisibility(View.GONE);
        Loader.showLoad(customProgress, activity, true);

        Call<ProductNameResModel> call = RetrofitClient
                .getInstance().getApi().getProductName("_listTypeWiseProduct","1");

        call.enqueue(new Callback<ProductNameResModel>() {
            @Override
            public void onResponse(@NonNull Call<ProductNameResModel> call, @NonNull Response<ProductNameResModel> response) {

                try {

                    Gson gson = new Gson();
                    String json = gson.toJson(response.body());

                    ProductNameResModel productNameResModel = gson.fromJson(json, ProductNameResModel.class);
                    String s = productNameResModel.getMessage();

                    if (productNameResModel.getStatus()==1) {

                        productNameList = productNameResModel.getData();
//                        CustomToast.getInstance(ProductNameActivity.this).showSmallCustomToast(todayOutletList.getMessage());
                        productNameAdapter = new ProductNameAdapter(ProductNameActivity.this,productNameList);
                        LinearLayoutManager mLayoutManager = new LinearLayoutManager(ProductNameActivity.this);

                        product_name_recycler.setLayoutManager(mLayoutManager);
                        product_name_recycler.setItemAnimator(new DefaultItemAnimator());
                        product_name_recycler.setAdapter(productNameAdapter);
                        productNameAdapter.notifyDataSetChanged();

//                        text_signIn.setVisibility(View.VISIBLE);
                        Loader.showLoad(customProgress, activity, false);

                    } else {
//                        text_signIn.setVisibility(View.VISIBLE);
                        Loader.showLoad(customProgress, activity, false);
                        //                        Toast.makeText(LoginActivity.this, "Invalid User Name or Password", Toast.LENGTH_SHORT).show();
                        CustomToast.getInstance(ProductNameActivity.this).showSmallCustomToast(productNameResModel.getMessage());
//                    Toast.makeText(LoginActivity.this, "Invalid User Name or Password", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    Log.d("Exception", e.getMessage());
                    Loader.showLoad(customProgress, activity, false);
                }

            }

            @Override
            public void onFailure(@NonNull Call<ProductNameResModel> call, @NonNull Throwable t) {
                Log.d("Failure ", t.getMessage());
//                Toast.makeText(LoginActivity.this, "Invalid User Name or Password", Toast.LENGTH_SHORT).show();
                CustomToast.getInstance(ProductNameActivity.this).showSmallCustomToast("Something went wrong try again..");
//                text_signIn.setVisibility(View.VISIBLE);
                Loader.showLoad(customProgress, activity, false);
            }
        });
    }

    public void updateProdName(String prodName, String prodId, String gst, String hsn){
        Intent productIntent=new Intent(ProductNameActivity.this, CreateOutletOrderActivity.class);
        productIntent.putExtra("prod_name",prodName);
        productIntent.putExtra("prod_id",prodId);
        productIntent.putExtra("gst",gst);
        productIntent.putExtra("hsn",hsn);
        startActivity(productIntent);
        finish();
    }
}