package com.retailvend.todayoutlet;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.retailvend.R;
import com.retailvend.model.order.CreateOrderModel;
import com.retailvend.model.outlets.ProductNameResModel;
import com.retailvend.productModel.AddProductModel;
import com.retailvend.retrofit.RetrofitClient;
import com.retailvend.utills.CustomProgress;
import com.retailvend.utills.CustomToast;
import com.retailvend.utills.Loader;
import com.retailvend.utills.SharedPrefManager;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.OnItemSelected;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateOutletOrderActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    Toolbar toolbar;
    Menu menu;
    TextView mTitle;

    RecyclerView tableList;
//    ArrayList<CreatePurchaseModel> modelMains;
    private CreateOutletAdapter adapterMain;

    List<AddProductModel> addProductModel=new ArrayList<>();
    String addProductJson = "";

    public static TextView txtTotalPrice, txtTotalNos, txtTotalkg;

    Spinner spin_name;
    TextView txt_date, txt_invoice_mob, txt_invoice_address,product_name;
    EditText txtPrice,qty;
    LinearLayout linBtn;
    Calendar c;
    int mYear, mMonth, mDay, mHour, mMinute;
    String[] unitVal = { "KG", "Gram"};
    Activity activity;
    String prod_name="";
    String type_id="";
    String gst_val="";
    String hsn_code="";
    String store_id="";
    String unitItem="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_sales_order);
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

        txtTotalPrice =  findViewById(R.id.txtTotalPrice);
        txtTotalNos =  findViewById(R.id.txtTotalNos);
        txtTotalkg =  findViewById(R.id.txtTotalkg);
        tableList =  findViewById(R.id.tableList);
        product_name =  findViewById(R.id.product_name);
        txtPrice =  findViewById(R.id.txtPrice);
        qty =  findViewById(R.id.edt_qty);
        tableList =  findViewById(R.id.tableList);
        spin_name =  findViewById(R.id.spin_unit);
        linBtn =  findViewById(R.id.linBtn);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, unitVal);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_name.setAdapter(dataAdapter);

        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            prod_name = bundle.getString("prod_name");
            type_id = bundle.getString("type_id");
            gst_val = bundle.getString("gst");
            hsn_code = bundle.getString("hsn");
            store_id = bundle.getString("store_id");
            System.out.println("store_id :"+store_id);
            product_name.setText(prod_name);
        }else{
            product_name.setText("");
        }

        product_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent productIntent=new Intent(CreateOutletOrderActivity.this, ProductNameActivity.class);
                startActivity(productIntent);
            }
        });

        linBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!product_name.getText().toString().isEmpty()){
                    if(spin_name!=null){
                        if(!txtPrice.getText().toString().isEmpty()){
                           if(!qty.getText().toString().isEmpty()){
                               addProductList();
                           }else{
                               CustomToast.getInstance(CreateOutletOrderActivity.this).showSmallCustomToast("Qty should not be empty");
                           }
                        }else{
                            CustomToast.getInstance(CreateOutletOrderActivity.this).showSmallCustomToast("Price Amount should not be empty");
                        }
                    }else{
                        CustomToast.getInstance(CreateOutletOrderActivity.this).showSmallCustomToast("Unit Value should not be empty");
                    }
                }else{
                    CustomToast.getInstance(CreateOutletOrderActivity.this).showSmallCustomToast("Product Name should not be empty");
                }
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        unitItem = parent.getItemAtPosition(position).toString();
        System.out.println("SelectedSelected "+unitItem);
    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
//        refreshActivity();
//        RegistorSharedPreManager.getInstance(DetailsActivity.this).clear();
        super.onBackPressed();
    }

    public void datePicker() {

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        String formattedDayOfMonth = "";
                        String formattedMonth = "";

                        if (dayOfMonth < 10) {

                            formattedDayOfMonth = "0" + dayOfMonth;
                        } else {
                            formattedDayOfMonth = String.valueOf(dayOfMonth);
                        }
                        if ((monthOfYear + 1) < 10) {

                            formattedMonth = "0" + (monthOfYear + 1);
                        } else {
                            formattedMonth = String.valueOf((monthOfYear + 1));
                        }


//                        txt_date.setText(strDate);
                        txt_date.setText(formattedDayOfMonth + "-" + formattedMonth + "-" + year);

//                        select_spin_date = formattedDayOfMonth + "-" + formattedMonth + "-" + year;

//                        System.out.println("dayOfMonth  " + (dayOfMonth));
//                        System.out.println("monthOfYear " + (monthOfYear + 1));
//                        System.out.println("currreent " + select_spin_date);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

/*
    public void createOrderApi() {
        final CustomProgress customProgress = new CustomProgress(this);
//        text_signIn.setVisibility(View.GONE);
        Loader.showLoad(customProgress, activity, true);

        String emp_id= SharedPrefManager.getInstance(CreateOutletOrderActivity.this).getUser().getId();

        Call<CreateOrderModel> call = RetrofitClient
                .getInstance().getApi().createOrder("_addSalesOrder",emp_id,store_id,);

        call.enqueue(new Callback<CreateOrderModel>() {
            @Override
            public void onResponse(@NonNull Call<CreateOrderModel> call, @NonNull Response<CreateOrderModel> response) {

                try {

                    Gson gson = new Gson();
                    String json = gson.toJson(response.body());

                    CreateOrderModel productNameResModel = gson.fromJson(json, CreateOrderModel.class);
                    String s = productNameResModel.getMessage();

                    if (productNameResModel.getStatus()==1) {

//                        text_signIn.setVisibility(View.VISIBLE);
                        Loader.showLoad(customProgress, activity, false);

                    } else {
//                        text_signIn.setVisibility(View.VISIBLE);
                        Loader.showLoad(customProgress, activity, false);
                        //                        Toast.makeText(LoginActivity.this, "Invalid User Name or Password", Toast.LENGTH_SHORT).show();
                        CustomToast.getInstance(CreateOutletOrderActivity.this).showSmallCustomToast("Invalid User Name or Password");
//                    Toast.makeText(LoginActivity.this, "Invalid User Name or Password", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    Log.d("Exception", e.getMessage());
                    Loader.showLoad(customProgress, activity, false);
                }

            }

            @Override
            public void onFailure(@NonNull Call<CreateOrderModel> call, @NonNull Throwable t) {
                Log.d("Failure ", t.getMessage());
//                Toast.makeText(LoginActivity.this, "Invalid User Name or Password", Toast.LENGTH_SHORT).show();
                CustomToast.getInstance(CreateOutletOrderActivity.this).showSmallCustomToast("Something went wrong try again..");
//                text_signIn.setVisibility(View.VISIBLE);
                Loader.showLoad(customProgress, activity, false);
            }
        });
    }
*/

    public void addProductList() {
        addProductModel.add(new AddProductModel(product_name.getText().toString(), spin_name.toString(), txtPrice.getText().toString(), qty.getText().toString()));

        updateAddProductAdapter("add", 0);
    }


    public void updateAddProductAdapter(String which, int pos) {

            if (addProductModel.size() > 0) {
                if (which.equals("remove")) {
                    addProductModel.remove(pos);
//                    lin_edit.setVisibility(View.VISIBLE);
                }
//                txt_measure_title.setVisibility(View.VISIBLE);
                LinearLayoutManager layoutManager = new LinearLayoutManager(this);
                adapterMain = new CreateOutletAdapter(CreateOutletOrderActivity.this,addProductModel);
                tableList.setLayoutManager(layoutManager);
                tableList.setAdapter(adapterMain);
                adapterMain.notifyDataSetChanged();

                Type baseType = new TypeToken<List<AddProductModel>>() {
                }.getType();
                Gson gson = new Gson();
                addProductJson = gson.toJson(addProductModel, baseType);
                System.out.println("x     " + addProductJson);
            } else {
//                txt_measure_title.setVisibility(View.GONE);
            }
        }
}