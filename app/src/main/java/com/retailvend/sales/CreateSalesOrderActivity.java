package com.retailvend.sales;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.retailvend.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CreateSalesOrderActivity extends AppCompatActivity {

    Toolbar toolbar;
    Menu menu;
    TextView mTitle;

    RecyclerView tableList;
//    ArrayList<CreatePurchaseModel> modelMains;
    private CreateSalesOrderAdapter adapterMain;

    public static TextView txtTotalPrice, txtTotalNos, txtTotalkg;

    Spinner spin_name;
    TextView txt_date, txt_invoice_mob, txt_invoice_address;
    Calendar c;
    int mYear, mMonth, mDay, mHour, mMinute;
//    InvoiceSupplierAdapter invoiceSupplierAdapter;
//    InvoiceCustomerAdapter invoiceCustomerAdapter;
//    List<Supplierlist> supplierlists;
//    List<Customerlist> customerlists;

    String supplierid = "";
    LinearLayout lin_submit;
    ProgressBar progress;
    LinearLayout lin_back;
//    List<InvoiceJsonArraymModel> invoiceJsonArraymModelList;
    TextView txt_dis_add_pay_toolbar, txt_dis_name;
    String which = "";
    AutoCompleteTextView autoCompleteTextViewProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_sales_order);


        if (android.os.Build.VERSION.SDK_INT >= 19) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimaryDark));
//            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
            window.setNavigationBarColor(this.getResources().getColor(R.color.white));
        }

        toolbar = findViewById(R.id.toolbar);
        mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mTitle.setText("Take Order");

        spin_name = findViewById(R.id.spin_name);
        txt_invoice_mob = findViewById(R.id.txt_invoice_mob);
        txt_invoice_address = findViewById(R.id.txt_invoice_address);
        txt_date = findViewById(R.id.txt_date);
        lin_submit = findViewById(R.id.lin_submit);
//        lin_back = findViewById(R.id.lin_back);
        progress = findViewById(R.id.progress);
//        txt_dis_add_pay_toolbar = findViewById(R.id.txt_dis_add_pay_toolbar);
        txt_dis_name = findViewById(R.id.txt_dis_name);
        autoCompleteTextViewProduct = findViewById(R.id.autoCompleteTextViewProduct);
        txtTotalPrice = (TextView) findViewById(R.id.txtTotalPrice);
        txtTotalNos = (TextView) findViewById(R.id.txtTotalNos);
        txtTotalkg = (TextView) findViewById(R.id.txtTotalkg);
        tableList = (RecyclerView) findViewById(R.id.tableList);

        c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy"); //Date and time
        String currentDate = sdf.format(c.getTime());
        txt_date.setText(currentDate);

        txt_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                c = Calendar.getInstance();
//                mYear = c.get(Calendar.YEAR);
//                mMonth = c.get(Calendar.MONTH);
//                mDay = c.get(Calendar.DAY_OF_MONTH);
//
//                System.out.println("Current Date  new " + mDay + "-" + mMonth + "-" + mYear);


                datePicker();
            }
        });

        tableList = (RecyclerView) findViewById(R.id.tableList);
        adapterMain = new CreateSalesOrderAdapter(CreateSalesOrderActivity.this);
        tableList.setAdapter(adapterMain);

        tableList.setLayoutManager(new LinearLayoutManager(this));


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
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

}