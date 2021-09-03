package com.retailvend.todayoutlet;

import static android.content.ContentValues.TAG;

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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.retailvend.R;
import com.retailvend.model.order.CreateOrderModel;
import com.retailvend.model.outlets.AddAttendanceModel;
import com.retailvend.model.outlets.ProductTypeDatum;
import com.retailvend.model.outlets.ProductTypeModel;
import com.retailvend.model.outlets.SalesAgentData;
import com.retailvend.model.outlets.SalesAgentsListModel;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateOutletOrderActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Toolbar toolbar;
    Menu menu;
    TextView mTitle;

    RecyclerView tableList;
    //    ArrayList<CreatePurchaseModel> modelMains;
    private CreateOutletAdapter adapterMain;

    List<AddProductModel> addProductModel = new ArrayList<>();
    String addProductJson = "";
    int totalPriceList = 0;
    String totalQtyList = "";

    public static TextView txtTotalPrice, txtTotalQty;

    Spinner spin_name,spin_sales_agent;
    TextView txt_date, txt_invoice_mob, txt_invoice_address, product_name;
    EditText txtPrice, qty;
    LinearLayout linBtn, lin_submit,linSalesAgent;
    Calendar c;
    int mYear, mMonth, mDay, mHour, mMinute;
    Activity activity;
    String prod_name = "";
    String prod_id = "";
    String gst_val = "";
    String hsn_code = "";
    String store_id = "";
    String order_type = "";
    String btn_Type_id = "";
    String btn_Type_val = "";
    String type_id = "";
    String unitItem = "";
    String unitId = "";
    String salesAgentName="";
    String sales_agentId="";
    String lat_val="";
    String long_val="";
    List<ProductTypeDatum> productTypeData;
    List<SalesAgentData> salesAgentDataList;
    private final static int MY_REQUEST_CODE = 1;
    String[] unitValue;
    String[] salesAgentValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_sales_order);
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

        txtTotalPrice = findViewById(R.id.txtTotalPrice);
        txtTotalQty = findViewById(R.id.txtTotalQty);
        tableList = findViewById(R.id.tableList);
        product_name = findViewById(R.id.product_name);
        txtPrice = findViewById(R.id.txtPrice);
        qty = findViewById(R.id.edt_qty);
        tableList = findViewById(R.id.tableList);
        spin_name = findViewById(R.id.spin_unit);
        spin_sales_agent = findViewById(R.id.spin_sales_agent);
        linBtn = findViewById(R.id.linBtn);
        linSalesAgent = findViewById(R.id.linSalesAgent);
        lin_submit = findViewById(R.id.lin_submit);

        Bundle b = new Bundle();
        b = getIntent().getExtras();
        if (b != null) {
            btn_Type_id = getIntent().getExtras().getString("type_id");
            store_id = getIntent().getExtras().getString("store_id");
            btn_Type_val = getIntent().getExtras().getString("type");
            lat_val = getIntent().getExtras().getString("lat");
            long_val = getIntent().getExtras().getString("long");

        } else {
            Log.e(TAG, "product", null);
        }
        salesAgentType();

//            store_id = bundle.getString("store_id");

        lin_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qty.setEnabled(false);
                createOrderApi();
            }
        });
        product_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent productIntent = new Intent(CreateOutletOrderActivity.this, ProductNameActivity.class);
                startActivityForResult(productIntent, MY_REQUEST_CODE);
            }
        });

        linBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qty.setEnabled(false);
                if (!product_name.getText().toString().isEmpty()) {
                    if (spin_name != null) {
                        if (!txtPrice.getText().toString().isEmpty()) {
                            if (!qty.getText().toString().isEmpty()) {
                                addProductList();
                            } else {
                                CustomToast.getInstance(CreateOutletOrderActivity.this).showSmallCustomToast("Qty should not be empty");
                                qty.setEnabled(true);
                            }
                        } else {
                            CustomToast.getInstance(CreateOutletOrderActivity.this).showSmallCustomToast("Price Amount should not be empty");
                        }
                    } else {
                        CustomToast.getInstance(CreateOutletOrderActivity.this).showSmallCustomToast("Unit Value should not be empty");
                    }
                } else {
                    CustomToast.getInstance(CreateOutletOrderActivity.this).showSmallCustomToast("Product Name should not be empty");
                }
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        unitItem = parent.getItemAtPosition(position).toString();
        System.out.println("SelectedSelected " + unitItem);
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

    public void createOrderApi() {
        final CustomProgress customProgress = new CustomProgress(this);
//        text_signIn.setVisibility(View.GONE);
        Loader.showLoad(customProgress, activity, true);

        String emp_id = SharedPrefManager.getInstance(CreateOutletOrderActivity.this).getUser().getId();

        Call<CreateOrderModel> call = RetrofitClient
                .getInstance().getApi().createOrder("_addSalesOrder", emp_id, store_id, "1", sales_agentId, addProductJson);

        call.enqueue(new Callback<CreateOrderModel>() {
            @Override
            public void onResponse(@NonNull Call<CreateOrderModel> call, @NonNull Response<CreateOrderModel> response) {

                try {

                    Gson gson = new Gson();
                    String json = gson.toJson(response.body());

                    CreateOrderModel createOrderModel = gson.fromJson(json, CreateOrderModel.class);
                    String s = createOrderModel.getMessage();

                    if (createOrderModel.getStatus() == 1) {

//                        text_signIn.setVisibility(View.VISIBLE);
                        CustomToast.getInstance(CreateOutletOrderActivity.this).showSmallCustomToast(createOrderModel.getMessage());

                        Loader.showLoad(customProgress, activity, false);


                    } else {
//                        text_signIn.setVisibility(View.VISIBLE);
                        Loader.showLoad(customProgress, activity, false);
                        //                        Toast.makeText(LoginActivity.this, "Invalid User Name or Password", Toast.LENGTH_SHORT).show();
                        CustomToast.getInstance(CreateOutletOrderActivity.this).showSmallCustomToast(createOrderModel.getMessage());
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

    public void addProductList() {
        String[] parts = txtPrice.getText().toString().split("\\.");
        String part1 = parts[0];
        String part2 = parts[1];
        int a = Integer.parseInt(part2);
        int b = Integer.parseInt(qty.getText().toString().trim());
        totalPriceList = a * b;
        addProductModel.add(new AddProductModel("1", prod_id, type_id, unitId, hsn_code, gst_val, qty.getText().toString(), totalPriceList));
        updateAddProductAdapter("add", 0);
        product_name.setText("");
        productTypeData.clear();
        spin_name.setAdapter(null);
        txtPrice.setText("");
        qty.setText("");
    }

    public void updateAddProductAdapter(String which, int pos) {
        if (addProductModel.size() > 0) {
            if (which.equals("remove")) {
                addProductModel.remove(pos);
//                    lin_edit.setVisibility(View.VISIBLE);
            }
//                txt_measure_title.setVisibility(View.VISIBLE);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            adapterMain = new CreateOutletAdapter(CreateOutletOrderActivity.this, addProductModel, prod_name, unitItem);
            tableList.setLayoutManager(layoutManager);
            tableList.setAdapter(adapterMain);
            adapterMain.notifyDataSetChanged();

            Type baseType = new TypeToken<List<AddProductModel>>() {
            }.getType();
            Gson gson = new Gson();
            addProductJson = gson.toJson(addProductModel, baseType);
            System.out.println("addProductJson " + addProductJson);
            totalQtyList = addProductModel.get(pos).getQty();
            totalPriceList = addProductModel.get(pos).getPrice();

        } else {
//                txt_measure_title.setVisibility(View.GONE);
        }
    }

    public void productTypeApi(String prodId) {
        final CustomProgress customProgress = new CustomProgress(activity);
//        text_signIn.setVisibility(View.GONE);
        Loader.showLoad(customProgress, activity, true);

//        String emp_id= SharedPrefManager.getInstance(CreateOutletOrderActivity.this).getUser().getId();

        Call<ProductTypeModel> call = RetrofitClient
                .getInstance().getApi().productType("_listProductType", prodId);

        call.enqueue(new Callback<ProductTypeModel>() {
            @Override
            public void onResponse(@NonNull Call<ProductTypeModel> call, @NonNull Response<ProductTypeModel> response) {

                try {

                    Gson gson = new Gson();
                    String json = gson.toJson(response.body());

                    ProductTypeModel productTypeModel = gson.fromJson(json, ProductTypeModel.class);

                    if (productTypeModel.getStatus() == 1) {
                        Loader.showLoad(customProgress, activity, false);
                        productTypeData = productTypeModel.getData();

                        if (productTypeData != null) {
                            ArrayList<String> list = new ArrayList<String>();
                            for (int i = 0; i < productTypeData.size(); i++) {
                                list.add(productTypeData.get(i).getProductType() + " " + productTypeData.get(i).getProductUnit());
                                unitItem = productTypeData.get(i).getProductType();
                                unitId = productTypeData.get(i).getProductUnit();
                                txtPrice.setText("₹." + productTypeData.get(i).getProductPrice());
                                type_id = productTypeData.get(i).getTypeId();
                            }

                            unitValue = list.toArray(new String[list.size()]);

                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, unitValue);
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spin_name.setAdapter(dataAdapter);
                            qty.setEnabled(true);
                        }
                    } else {
                        Loader.showLoad(customProgress, activity, false);
//                        CustomToast.getInstance(activity).showSmallCustomToast(productTypeModel.getMessage());
                    }

                } catch (Exception e) {
                    Log.d("Exception", e.getMessage());
                    Loader.showLoad(customProgress, activity, false);
                }

            }

            @Override
            public void onFailure(@NonNull Call<ProductTypeModel> call, @NonNull Throwable t) {
                Log.d("Failure ", t.getMessage());
//                Toast.makeText(LoginActivity.this, "Invalid User Name or Password", Toast.LENGTH_SHORT).show();
                CustomToast.getInstance(activity).showSmallCustomToast("Something went wrong try again..");
//                text_signIn.setVisibility(View.VISIBLE);
                Loader.showLoad(customProgress, activity, false);
            }
        });
    }

    public void salesAgentType() {
        final CustomProgress customProgress = new CustomProgress(activity);
//        text_signIn.setVisibility(View.GONE);
        Loader.showLoad(customProgress, activity, true);

//        String emp_id= SharedPrefManager.getInstance(CreateOutletOrderActivity.this).getUser().getId();

        Call<SalesAgentsListModel> call = RetrofitClient
                .getInstance().getApi().salesAgentsType("_listSalesagents");

        call.enqueue(new Callback<SalesAgentsListModel>() {
            @Override
            public void onResponse(@NonNull Call<SalesAgentsListModel> call, @NonNull Response<SalesAgentsListModel> response) {

                try {

                    Gson gson = new Gson();
                    String json = gson.toJson(response.body());

                    SalesAgentsListModel salesAgentsListModel = gson.fromJson(json, SalesAgentsListModel.class);

                    if (salesAgentsListModel.getStatus() == 1) {
                        Loader.showLoad(customProgress, activity, false);
                        salesAgentDataList = salesAgentsListModel.getData();

                        if (salesAgentDataList != null) {
                            ArrayList<String> list = new ArrayList<String>();
                            for (int i = 0; i < salesAgentDataList.size(); i++) {
                                list.add(salesAgentDataList.get(i).getCompanyName());
                                salesAgentName = salesAgentDataList.get(i).getCompanyName();
                                sales_agentId = salesAgentDataList.get(i).getAgentsId();
                                System.out.println("sales_agentId "+sales_agentId);
                            }

                            salesAgentValue = list.toArray(new String[list.size()]);

                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, salesAgentValue);
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spin_sales_agent.setAdapter(dataAdapter);
                            qty.setEnabled(true);
                        }
                    } else {
                        Loader.showLoad(customProgress, activity, false);
//                        CustomToast.getInstance(activity).showSmallCustomToast(productTypeModel.getMessage());
                    }

                } catch (Exception e) {
                    Log.d("Exception", e.getMessage());
                    Loader.showLoad(customProgress, activity, false);
                }

            }

            @Override
            public void onFailure(@NonNull Call<SalesAgentsListModel> call, @NonNull Throwable t) {
                Log.d("Failure ", t.getMessage());
//                Toast.makeText(LoginActivity.this, "Invalid User Name or Password", Toast.LENGTH_SHORT).show();
                CustomToast.getInstance(activity).showSmallCustomToast("Something went wrong try again..");
//                text_signIn.setVisibility(View.VISIBLE);
                Loader.showLoad(customProgress, activity, false);
            }
        });
    }

    public void updateAttendanceApi() {
        final CustomProgress customProgress = new CustomProgress(this);
//        text_signIn.setVisibility(View.GONE);
        Loader.showLoad(customProgress, this, true);
        String emp_id= SharedPrefManager.getInstance(this).getUser().getId();

        Call<AddAttendanceModel> call = RetrofitClient
                .getInstance().getApi().updateAttendance("_updateAttendance",emp_id,store_id,lat_val,long_val,btn_Type_val,"",btn_Type_id);

        call.enqueue(new Callback<AddAttendanceModel>() {
            @Override
            public void onResponse(@NonNull Call<AddAttendanceModel> call, @NonNull Response<AddAttendanceModel> response) {

                try {

                    Gson gson = new Gson();
                    String json = gson.toJson(response.body());

                    AddAttendanceModel attendanceTypeModel = gson.fromJson(json, AddAttendanceModel.class);

                    if (attendanceTypeModel.getStatus()==1) {

                        CustomToast.getInstance(CreateOutletOrderActivity.this).showSmallCustomToast(attendanceTypeModel.getMessage());

                        Loader.showLoad(customProgress, activity, false);

                    } else {
                        Loader.showLoad(customProgress, activity, false);
                        CustomToast.getInstance(CreateOutletOrderActivity.this).showSmallCustomToast(attendanceTypeModel.getMessage());
                    }

                } catch (Exception e) {
                    Log.d("Exception", e.getMessage());
                    Loader.showLoad(customProgress, activity, false);
                }

            }

            @Override
            public void onFailure(@NonNull Call<AddAttendanceModel> call, @NonNull Throwable t) {
                Log.d("Failure ", t.getMessage());
                CustomToast.getInstance(CreateOutletOrderActivity.this).showSmallCustomToast("Something went wrong try again..");
                Loader.showLoad(customProgress, activity, false);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == MY_REQUEST_CODE) {
                if (data != null) {
                    prod_name = data.getStringExtra("prod_name");
                    prod_id = data.getStringExtra("prod_id");
                    gst_val = data.getStringExtra("gst");
                    hsn_code = data.getStringExtra("hsn");
                    product_name.setText(prod_name);
                    productTypeApi(prod_id);
                }
            }
        }
    }
}