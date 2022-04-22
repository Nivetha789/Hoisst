package com.retailvend.todayoutlet;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.retailvend.broadcast.ConnectivityReceiver;
import com.retailvend.model.order.CreateOrderModel;
import com.retailvend.model.outlets.AddAttendanceModel;
import com.retailvend.model.outlets.ProductTypeDatum;
import com.retailvend.model.outlets.ProductTypeModel;
import com.retailvend.productModel.AddProductModel;
import com.retailvend.retrofit.RetrofitClient;
import com.retailvend.utills.CustomProgress;
import com.retailvend.utills.CustomToast;
import com.retailvend.utills.ProductAdapter;
import com.retailvend.utills.SessionManagerSP;
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

    Spinner spin_name;
    TextView txt_date, txt_invoice_mob, txt_invoice_address, rv, sales_agent, product_name, order_type_txt, cod, credit, txt_sales_agent, discount, dueDays;
    EditText txtPrice, qty;
    LinearLayout linBtn, lin_submit, linSalesAgent;
    ImageView left_arrow;
    Calendar c;
    int mYear, mMonth, mDay, mHour, mMinute;
    Activity activity;
    String prod_name = "";
    int auto_id = 0;
    String prod_id = "";
    String gst_val = "";
    String hsn_code = "";
    String store_id = "";
    String bill_type = "";
    String order_type = "";
    String price = "";
    String btn_Type_val = "";
    String type_id = "";
    String unitItem = "";
    String unitId = "";
    String lat_val = "";
    String long_val = "";
    List<ProductTypeDatum> productTypeData;
    private final static int MY_REQUEST_CODE = 1;
    private final static int MY_REQUEST_CODE1 = 2;
    //    String[] unitValue;
    SessionManagerSP sessionManagerSP;
    String sales_agent_name = "";
    String sales_agent_id = "";
    LinearLayout sales_agent_show, rv_show, discountLayout;

    ProductAdapter productAdapter;

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
        txt_sales_agent = findViewById(R.id.txt_sales_agent);
        linBtn = findViewById(R.id.linBtn);
        linSalesAgent = findViewById(R.id.linSalesAgent);
        lin_submit = findViewById(R.id.lin_submit);
        order_type_txt = findViewById(R.id.order_type_txt);
        cod = findViewById(R.id.cod);
        credit = findViewById(R.id.credit);
        left_arrow = findViewById(R.id.left_arrow);
        rv = findViewById(R.id.rv);
        sales_agent = findViewById(R.id.sales_agent);
        sales_agent_show = findViewById(R.id.sales_agent_show);
        rv_show = findViewById(R.id.rv_show);
        discount = findViewById(R.id.discount);
        dueDays = findViewById(R.id.dueDays);
        discountLayout = findViewById(R.id.discountLayout);

        left_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        sessionManagerSP = new SessionManagerSP(CreateOutletOrderActivity.this);

        Intent data = getIntent();
        if (data != null) {
            store_id = getIntent().getStringExtra("store_id");
            btn_Type_val = getIntent().getStringExtra("type");
            System.out.println("typejhjhrhj "+btn_Type_val);

            if(btn_Type_val.equals("Sales Order")){
                btn_Type_val="1";
            }
        }

        lat_val = sessionManagerSP.getLat();
        long_val = sessionManagerSP.getLong();

        rv.setBackgroundResource(R.drawable.background);
        sales_agent.setBackgroundResource(R.drawable.lin_storke);
        rv.setTextColor(Color.parseColor("#ffffff"));
        sales_agent.setTextColor(Color.parseColor("#000000"));
        rv_show.setVisibility(View.VISIBLE);
        sales_agent_show.setVisibility(View.GONE);
        order_type = "1";

        lin_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qty.setEnabled(false);
                boolean isConnected = ConnectivityReceiver.isConnected();
                if (isConnected) {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(activity);
                    builder1.setMessage("Confirm your Order!!!");
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    createOrderApi();
                                }
                            });

                    builder1.setNegativeButton(
                            "No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                } else {
//            Toast.makeText(this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
                    CustomToast.getInstance(CreateOutletOrderActivity.this).showSmallCustomToast("Please check your internet connection");
                }
            }
        });
        product_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                System.out.println("prodtype::: " + order_type);
//                System.out.println("bill_typebill_typebill_type::: " + bill_type);
                if (!TextUtils.isEmpty(bill_type)) {
//                    if (!TextUtils.isEmpty(bill_type)) {
                    Intent productIntent = new Intent(CreateOutletOrderActivity.this, ProductNameActivity.class);
                    productIntent.putExtra("order_type", order_type);
                    startActivityForResult(productIntent, MY_REQUEST_CODE);
//                    else {
//                        CustomToast.getInstance(CreateOutletOrderActivity.this).showSmallCustomToast("Select Bill Type");
//                    }
                } else {
                    CustomToast.getInstance(CreateOutletOrderActivity.this).showSmallCustomToast("Select Bill Type");
                }
            }
        });

        spin_name.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                ProductTypeDatum productTypeDatum = productTypeData.get(i);

                unitItem = productTypeDatum.getDescription();
                unitId = productTypeDatum.getProductUnit();
                price = productTypeDatum.getProductPrice();
                txtPrice.setText("₹." + price);
                type_id = productTypeDatum.getTypeId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

//        rv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                rv.setBackgroundResource(R.drawable.background);
//                sales_agent.setBackgroundResource(R.drawable.lin_storke);
//                rv.setTextColor(Color.parseColor("#ffffff"));
//                sales_agent.setTextColor(Color.parseColor("#000000"));
//                rv_show.setVisibility(View.VISIBLE);
//                sales_agent_show.setVisibility(View.GONE);
//                order_type = "1";
//            }
//        });

//        sales_agent.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                sales_agent.setBackgroundResource(R.drawable.background);
//                rv.setBackgroundResource(R.drawable.lin_storke);
//                rv.setTextColor(Color.parseColor("#000000"));
//                sales_agent.setTextColor(Color.parseColor("#ffffff"));
//                rv_show.setVisibility(View.GONE);
//                sales_agent_show.setVisibility(View.VISIBLE);
//                order_type = "2";
//            }
//        });

//        linSalesAgent.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent salesIntent = new Intent(CreateOutletOrderActivity.this, SalesAgentNameActivity.class);
//                startActivityForResult(salesIntent, MY_REQUEST_CODE1);
//            }
//        });

        cod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bill_type = "1";
                cod.setBackgroundResource(R.drawable.background);
                credit.setBackgroundResource(R.drawable.lin_storke);
                cod.setTextColor(Color.parseColor("#ffffff"));
                credit.setTextColor(Color.parseColor("#000000"));
                discountLayout.setVisibility(View.VISIBLE);
            }
        });

        credit.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                bill_type = "2";
                cod.setBackgroundResource(R.drawable.lin_storke);
                credit.setBackgroundResource(R.drawable.background);
                cod.setTextColor(Color.parseColor("#000000"));
                credit.setTextColor(Color.parseColor("#ffffff"));
                discountLayout.setVisibility(View.GONE);
            }
        });

        linBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qty.setEnabled(false);
//                System.out.println("billltypeee "+bill_type);
                if (!TextUtils.isEmpty(bill_type)) {
                    if (bill_type.equals("1")) {
                        if (!TextUtils.isEmpty(discount.getText().toString())) {
                            if (!TextUtils.isEmpty(dueDays.getText().toString())) {
                                if (!TextUtils.isEmpty(product_name.getText().toString())) {
                                    if (spin_name != null) {
                                        if (!TextUtils.isEmpty(txtPrice.getText().toString())) {
                                            if (!TextUtils.isEmpty(qty.getText().toString())) {
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
                            } else {
                                CustomToast.getInstance(CreateOutletOrderActivity.this).showSmallCustomToast("Due Days should not be empty");
                            }
                        } else {
                            CustomToast.getInstance(CreateOutletOrderActivity.this).showSmallCustomToast("Discount should not be empty");
                        }
                    } else {
                        if (!TextUtils.isEmpty(product_name.getText().toString())) {
                            if (spin_name != null) {
                                if (!TextUtils.isEmpty(txtPrice.getText().toString())) {
                                    if (!TextUtils.isEmpty(qty.getText().toString())) {
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
//                        CustomToast.getInstance(CreateOutletOrderActivity.this).showSmallCustomToast("Discount and Due Days should not be empty");
                    }
                } else {
                    CustomToast.getInstance(CreateOutletOrderActivity.this).showSmallCustomToast("Select Bill Type");
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!TextUtils.isEmpty(sessionManagerSP.getSalesName())) {
            rv_show.setVisibility(View.GONE);
            sales_agent_show.setVisibility(View.VISIBLE);
            order_type = "2";
            sales_agent_name = sessionManagerSP.getSalesName();
            sales_agent_id = sessionManagerSP.getSalesNameId();
//            System.out.println("fggffgffggf " + sales_agent_name);
//            System.out.println("iddddddddd " + sales_agent_id);
            txt_sales_agent.setText(sales_agent_name);
        } else {
            rv_show.setVisibility(View.VISIBLE);
            sales_agent_show.setVisibility(View.GONE);
            order_type = "1";
            sessionManagerSP.setSalesName("");
            sessionManagerSP.setSalesNameId("");
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        unitItem = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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
        finish();
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
        CustomProgress.showProgress(activity);

        String emp_id = sessionManagerSP.getEmployeeId();
        String attandance_id = sessionManagerSP.getAssignId();

        Call<CreateOrderModel> call = RetrofitClient
                .getInstance().getApi().createOrder("_addSalesOrder", emp_id, store_id, bill_type, discount.getText().toString(), dueDays.getText().toString(), "1", attandance_id,addProductJson);

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

                        CustomProgress.hideProgress(activity);
                        boolean isConnected = ConnectivityReceiver.isConnected();
                        if (isConnected) {
                            updateAttendanceApi();
                        } else {
//            Toast.makeText(this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
                            CustomToast.getInstance(CreateOutletOrderActivity.this).showSmallCustomToast("Please check your internet connection");
                        }

                    } else {
//                        text_signIn.setVisibility(View.VISIBLE);
                        CustomProgress.hideProgress(activity);
                        //                        Toast.makeText(LoginActivity.this, "Invalid User Name or Password", Toast.LENGTH_SHORT).show();
                        CustomToast.getInstance(CreateOutletOrderActivity.this).showSmallCustomToast(createOrderModel.getMessage());
//                    Toast.makeText(LoginActivity.this, "Invalid User Name or Password", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    Log.d("Exception", e.getMessage());
                    CustomProgress.hideProgress(activity);
                }

            }

            @Override
            public void onFailure(@NonNull Call<CreateOrderModel> call, @NonNull Throwable t) {
                Log.d("Failure ", t.getMessage());
//                Toast.makeText(LoginActivity.this, "Invalid User Name or Password", Toast.LENGTH_SHORT).show();
                CustomToast.getInstance(CreateOutletOrderActivity.this).showSmallCustomToast("Something went wrong try again..");
//                text_signIn.setVisibility(View.VISIBLE);
                CustomProgress.hideProgress(activity);
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
//        System.out.println("parts " + parts[2]);
        System.out.println("totalPriceList " + totalPriceList);
        addProductModel.add(new AddProductModel(prod_id, prod_name, type_id, unitId, price, totalPriceList, qty.getText().toString()));
        updateAddProductAdapter("add", 0);
        product_name.setText("");
        productTypeData.clear();
        spin_name.setAdapter(null);
        txtPrice.setText("");
        qty.setText("");
    }


    public void totalPrice(List<AddProductModel> addProductModel1) {
        double price = 0;
        int qty = 0;

        for (int j = 0; j < addProductModel1.size(); j++) {
            price += Double.parseDouble(String.valueOf(addProductModel1.get(j).getTotalPrice()));
        }

        for (int i = 0; i < addProductModel1.size(); i++) {
            qty += Integer.valueOf(addProductModel1.get(i).getQty());

        }

        txtTotalQty.setText(String.valueOf(qty));
        txtTotalPrice.setText("\u20B9 " + price);
    }

    public void updateAddProductAdapter(String which, int pos) {
        System.out.println("possss " + pos);
        if (pos == 0) {
            auto_id = 1;
        } else {
            auto_id = pos;
        }
        if (addProductModel.size() > 0) {
            if (which.equals("remove")) {
                addProductModel.remove(pos);
                totalPrice(addProductModel);
            }
            totalPrice(addProductModel);

            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            adapterMain = new CreateOutletAdapter(CreateOutletOrderActivity.this, addProductModel);
            tableList.setLayoutManager(layoutManager);
            tableList.setAdapter(adapterMain);
            adapterMain.notifyDataSetChanged();

            Type baseType = new TypeToken<List<AddProductModel>>() {
            }.getType();
            Gson gson = new Gson();
            addProductJson = gson.toJson(addProductModel, baseType);
            System.out.println("addProductJson " + addProductJson);

        } else {
        }
    }

    public void productTypeApi(String prodId) {
        CustomProgress.showProgress(activity);

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
                        CustomProgress.hideProgress(activity);
                        productTypeData = productTypeModel.getData();

                        if (productTypeData != null) {
//                            ArrayList<String> list = new ArrayList<String>();
                            for (int i = 0; i < productTypeData.size(); i++) {
//                                list.add(productTypeData.get(i).getDescription());
                                unitItem = productTypeData.get(i).getDescription();
                                unitId = productTypeData.get(i).getProductUnit();
                            }

//                            unitValue = list.toArray(new String[list.size()]);

//                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, unitValue);
//                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                            spin_name.setAdapter(dataAdapter);
                            productAdapter = new ProductAdapter(CreateOutletOrderActivity.this, productTypeData);
                            spin_name.setAdapter(productAdapter);
                            productAdapter.notifyDataSetChanged();
                            qty.setEnabled(true);
                        }
                    } else {
                        CustomProgress.hideProgress(activity);
//                        CustomToast.getInstance(activity).showSmallCustomToast(productTypeModel.getMessage());
                    }

                } catch (Exception e) {
                    Log.d("Exception", e.getMessage());
                    CustomProgress.hideProgress(activity);
                }

            }

            @Override
            public void onFailure(@NonNull Call<ProductTypeModel> call, @NonNull Throwable t) {
                Log.d("Failure ", t.getMessage());
//                Toast.makeText(LoginActivity.this, "Invalid User Name or Password", Toast.LENGTH_SHORT).show();
                CustomToast.getInstance(activity).showSmallCustomToast("Something went wrong try again..");
//                text_signIn.setVisibility(View.VISIBLE);
                CustomProgress.hideProgress(activity);
            }
        });
    }

    public void updateAttendanceApi() {
        CustomProgress.showProgress(activity);
        String emp_id = sessionManagerSP.getEmployeeId();
        String assign_id=sessionManagerSP.getAssignId();


        Call<AddAttendanceModel> call = RetrofitClient
                .getInstance().getApi().updateAttendance("_updateAttendance", emp_id, store_id, lat_val,long_val, btn_Type_val, "", assign_id);

        call.enqueue(new Callback<AddAttendanceModel>() {
            @Override
            public void onResponse(@NonNull Call<AddAttendanceModel> call, @NonNull Response<AddAttendanceModel> response) {

                try {

                    Gson gson = new Gson();
                    String json = gson.toJson(response.body());

                    AddAttendanceModel attendanceTypeModel = gson.fromJson(json, AddAttendanceModel.class);

                    if (attendanceTypeModel.getStatus() == 1) {

                        CustomToast.getInstance(CreateOutletOrderActivity.this).showSmallCustomToast(attendanceTypeModel.getMessage());

                        CustomProgress.hideProgress(activity);
                        onBackPressed();
                    } else {
                        CustomProgress.hideProgress(activity);
                        CustomToast.getInstance(CreateOutletOrderActivity.this).showSmallCustomToast(attendanceTypeModel.getMessage());
                    }

                } catch (Exception e) {
                    Log.d("Exception", e.getMessage());
                    CustomProgress.hideProgress(activity);
                }

            }

            @Override
            public void onFailure(@NonNull Call<AddAttendanceModel> call, @NonNull Throwable t) {
                Log.d("Failure ", t.getMessage());
                CustomToast.getInstance(CreateOutletOrderActivity.this).showSmallCustomToast("Something went wrong try again..");
                CustomProgress.hideProgress(activity);
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
                    boolean isConnected = ConnectivityReceiver.isConnected();
                    if (isConnected) {
                        productTypeApi(prod_id);
                    } else {
                        CustomToast.getInstance(CreateOutletOrderActivity.this).showSmallCustomToast("Please check your internet connection");
                    }
                }
            }
        }
    }

    public void updateSales(String salesAgentName, String salesId) {
        sales_agent_name = salesAgentName;
        sales_agent_id = salesId;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}