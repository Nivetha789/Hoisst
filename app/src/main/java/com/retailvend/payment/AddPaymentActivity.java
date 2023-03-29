package com.retailvend.payment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.retailvend.R;
import com.retailvend.broadcast.ConnectivityReceiver;
import com.retailvend.deliveryman.collection.InvoiceNoSpinAdapter;
import com.retailvend.model.delManModels.delCollection.DetailOutletInvAmntBillDatum;
import com.retailvend.model.delManModels.delCollection.DetailOutletInvAmntBillModel;
import com.retailvend.model.delManModels.delCollection.addpayment.AddPaymentModel;
import com.retailvend.model.delManModels.delCollection.paymentCollection.InvoiceTypeDatum;
import com.retailvend.model.delManModels.delCollection.paymentCollection.InvoiceTypeModel;
import com.retailvend.model.delManModels.delCollection.paymentCollection.PaymentTypeData;
import com.retailvend.model.delManModels.delCollection.paymentCollection.PaymentTypeModel;
import com.retailvend.retrofit.RetrofitClient;
import com.retailvend.utills.CustomToast;
import com.retailvend.utills.SessionManagerSP;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddPaymentActivity extends AppCompatActivity {

    TextView txt_dis_add_pay_toolbar, txt_add_payment_name, txt_add_payment_date, cheque_date;
    EditText edt_add_payment_amt, edt_add_payment_descrip, type_description, cheque_no, bank_name;
    Spinner spin_add_payment_type, spin_invoice_num;
    LinearLayout lin_add_payment, lin_back, description_linearLayout, cheque_linearLayout, bank_linearLayout, cheque_date_linearLayout;
    List<PaymentTypeData> addPaymentTypeList;
    List<InvoiceTypeDatum> invoiceTypeDatumList;
    List<DetailOutletInvAmntBillDatum> detailOutletInvAmntBillData;
    AddPaymentTypeAdapter addPaymentTypeAdapter;
    InvoiceNoSpinAdapter invoiceNoSpinAdapter;
    String payment_type = "";
    String payment_typeVal = "";
    String invoice_num = "";
    String invoice_no_id = "";
    String pay_id = "";
    Calendar c;
    int mYear, mMonth, mDay, mHour, mMinute;
    String assignId = "";
    String employeeId = "";
    String balamt = "";
    String name = "";
    ProgressBar progress;
    String discount = "";
    String distributorId = "";
    String outletId = "";
    String paymentId = "";

    SessionManagerSP sessionManagerSP;
    Typeface font;

    String currentDate="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_payment);

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

        txt_dis_add_pay_toolbar = findViewById(R.id.txt_dis_add_pay_toolbar);
        txt_add_payment_name = findViewById(R.id.txt_add_payment_name);
        txt_add_payment_date = findViewById(R.id.txt_add_payment_date);
        //EditText
        edt_add_payment_amt = findViewById(R.id.edt_add_payment_amt);
        edt_add_payment_descrip = findViewById(R.id.edt_add_payment_descrip);
        //Spinner
        spin_add_payment_type = findViewById(R.id.spin_add_payment_type);
        spin_invoice_num = findViewById(R.id.spin_invoice_num);
        //LinearLayout
        lin_add_payment = findViewById(R.id.lin_add_payment);
        lin_back = findViewById(R.id.lin_back);
        progress = findViewById(R.id.progress);
        description_linearLayout = findViewById(R.id.description_linearLayout);
        type_description = findViewById(R.id.type_description);
        cheque_linearLayout = findViewById(R.id.cheque_linearLayout);
        bank_linearLayout = findViewById(R.id.bank_linearLayout);
        cheque_date_linearLayout = findViewById(R.id.cheque_date_linearLayout);
        cheque_no = findViewById(R.id.cheque_no);
        bank_name = findViewById(R.id.bank_name);
        cheque_date = findViewById(R.id.cheque_date);

        sessionManagerSP = new SessionManagerSP(AddPaymentActivity.this);

        description_linearLayout.setVisibility(View.GONE);
        invoiceTypeDatumList=new ArrayList<>();

        distributorId = sessionManagerSP.getDistributorId();
        Intent iin = getIntent();
        Bundle b = iin.getExtras();

        if (b != null) {
            assignId = (String) b.get("assign_id");
            name = (String) b.get("name");
            outletId = (String) b.get("outletId");
            paymentId = (String) b.get("paymentId");
        }

        boolean isConnected = ConnectivityReceiver.isConnected();
        if (isConnected) {
            listOutletPaymentBillApi();
            paymentTypeApi();
        } else {
            CustomToast.getInstance(AddPaymentActivity.this).showSmallCustomToast("Please check your internet connection");
        }

        spin_add_payment_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                payment_type = addPaymentTypeList.get(i).getTypeId();
                payment_typeVal = addPaymentTypeList.get(i).getTypeVal();
                if (payment_typeVal.equals("Cash")) {
                    description_linearLayout.setVisibility(View.GONE);
                    cheque_linearLayout.setVisibility(View.GONE);
                    bank_linearLayout.setVisibility(View.GONE);
                    cheque_date_linearLayout.setVisibility(View.GONE);
                } else if (payment_typeVal.equals("Cheque")) {
                    description_linearLayout.setVisibility(View.GONE);
                    cheque_linearLayout.setVisibility(View.VISIBLE);
                    bank_linearLayout.setVisibility(View.VISIBLE);
                    cheque_date_linearLayout.setVisibility(View.VISIBLE);
                } else {
                    description_linearLayout.setVisibility(View.VISIBLE);
                    cheque_linearLayout.setVisibility(View.GONE);
                    bank_linearLayout.setVisibility(View.GONE);
                    cheque_date_linearLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spin_invoice_num.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if(i!=0){
                    invoice_num = invoiceTypeDatumList.get(i).getBillNo();
                    invoice_no_id = invoiceTypeDatumList.get(i).getBillId();
                    pay_id = invoiceTypeDatumList.get(i).getPayId();
                    detailOutletPaymentBillApi(invoiceTypeDatumList.get(i).getPayId());
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy"); //Date and time
        currentDate = sdf.format(c.getTime());
        txt_add_payment_date.setText(currentDate);

        txt_add_payment_date.setOnClickListener(new View.OnClickListener() {
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
        cheque_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //                c = Calendar.getInstance();
                //                mYear = c.get(Calendar.YEAR);
                //                mMonth = c.get(Calendar.MONTH);
                //                mDay = c.get(Calendar.DAY_OF_MONTH);
                //
                //                System.out.println("Current Date  new " + mDay + "-" + mMonth + "-" + mYear);


                datePickerCollectDate();
            }
        });


        lin_add_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Validate_Amt(edt_add_payment_amt)) {
                    if (payment_type.length() > 0) {

                        if (txt_add_payment_date.getText().toString().length() > 0) {

                            if (edt_add_payment_descrip.getText().toString().length() > 0) {
                                discount = edt_add_payment_descrip.getText().toString();
                            } else {
                                discount = "0";
                            }
                            boolean isConnected = ConnectivityReceiver.isConnected();
                            if (isConnected) {
                                addPayment(assignId, distributorId, outletId, edt_add_payment_amt.getText().toString(), discount,
                                        payment_type, type_description.getText().toString(), bank_name.getText().toString(), cheque_no.getText().toString());
                            } else {
                                CustomToast.getInstance(AddPaymentActivity.this).showSmallCustomToast("Please check your internet connection");
                            }
                        } else {
                            CustomToast.getInstance(AddPaymentActivity.this).showSmallCustomToast("Date missing");
                        }

                    } else {
                        CustomToast.getInstance(AddPaymentActivity.this).showSmallCustomToast("Payment Type missing");
                    }

                }

            }
        });
        lin_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


    }

    public void addPayment(String assignId, String distributorID, String outletId, String amt, String discount, String amt_type, String description, String bankName, String chequeNo) {
        employeeId = sessionManagerSP.getEmployeeId();

        progress.setVisibility(View.VISIBLE);


        Call<AddPaymentModel> call = RetrofitClient
                .getInstance().getApi().addPayment("_addOutletPayment", assignId, pay_id, employeeId, distributorID, outletId, amt, discount, amt_type, "", bankName, chequeNo, "2",currentDate,cheque_date.getText().toString(),cheque_date.getText().toString());


        call.enqueue(new Callback<AddPaymentModel>() {
            @Override
            public void onResponse(Call<AddPaymentModel> call, Response<AddPaymentModel> response) {

                try {

                    Gson gson = new Gson();
                    String json = gson.toJson(response.body());
                    AddPaymentModel addPaymentModel = gson.fromJson(json, AddPaymentModel.class);
//                LoginModule loginModule = response.body();


                    if (addPaymentModel.getStatus() == 1) {
                        progress.setVisibility(View.GONE);
                        CustomToast.getInstance(AddPaymentActivity.this).showSmallCustomToast(addPaymentModel.getMessage());
                        onBackPressed();

                    } else {
                        progress.setVisibility(View.GONE);
                        CustomToast.getInstance(AddPaymentActivity.this).showSmallCustomToast(addPaymentModel.getMessage());
                    }
                } catch (Exception e) {
                    Log.d("Exception", e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<AddPaymentModel> call, Throwable t) {
                Log.d("Failure ", t.getMessage());
                progress.setVisibility(View.GONE);
                CustomToast.getInstance(AddPaymentActivity.this).showSmallCustomToast("Something went wrong try again..");
            }
        });

    }

    public void listOutletPaymentBillApi() {


        progress.setVisibility(View.VISIBLE);


        Call<InvoiceTypeModel> call = RetrofitClient
                .getInstance().getApi().listOutletPaymentBill("_listOutletPaymentBill", assignId);


        call.enqueue(new Callback<InvoiceTypeModel>() {
            @Override
            public void onResponse(Call<InvoiceTypeModel> call, Response<InvoiceTypeModel> response) {

                try {

                    Gson gson = new Gson();
                    String json = gson.toJson(response.body());
                    InvoiceTypeModel invoiceTypeModel = gson.fromJson(json, InvoiceTypeModel.class);
//                LoginModule loginModule = response.body();


                    if (invoiceTypeModel.getStatus() == 1) {
                        progress.setVisibility(View.GONE);

                        InvoiceTypeDatum invoiceTypeDatum = new InvoiceTypeDatum();
                        invoiceTypeDatum.setBillNo("Select Invoice");
                        invoiceTypeDatumList.add(invoiceTypeDatum);

                        invoiceTypeDatumList.addAll(invoiceTypeModel.getData()) ;

                        for(int i=0; i<invoiceTypeDatumList.size(); i++) {
                            if (paymentId.equals(invoiceTypeDatumList.get(i).getPayId())){
                                invoiceNoSpinAdapter = new InvoiceNoSpinAdapter(AddPaymentActivity.this, invoiceTypeDatumList);
                                spin_invoice_num.setAdapter(invoiceNoSpinAdapter);
                                invoiceNoSpinAdapter.notifyDataSetChanged();
                            }else{
                                invoiceNoSpinAdapter = new InvoiceNoSpinAdapter(AddPaymentActivity.this, invoiceTypeDatumList);
                                spin_invoice_num.setAdapter(invoiceNoSpinAdapter);
                                invoiceNoSpinAdapter.notifyDataSetChanged();
                            }
                        }
                    } else {
                        progress.setVisibility(View.GONE);
//                        CustomToast.getInstance(AddPaymentActivity.this).showSmallCustomToast(invoiceTypeModel.getMessage());
                    }

                } catch (Exception e) {
                    Log.d("ExceptionInvoice", e.getMessage());
                }

            }

            @Override
            public void onFailure(Call<InvoiceTypeModel> call, Throwable t) {
                Log.d("Failure ", t.getMessage());
                progress.setVisibility(View.GONE);
                CustomToast.getInstance(AddPaymentActivity.this).showSmallCustomToast("Something went wrong try again..");

            }
        });

    }

    public void detailOutletPaymentBillApi(String payId) {
        progress.setVisibility(View.VISIBLE);

        Call<DetailOutletInvAmntBillModel> call = RetrofitClient
                .getInstance().getApi().detailOutletPaymentBill("_detailOutletPaymentBill", payId);


        call.enqueue(new Callback<DetailOutletInvAmntBillModel>() {
            @Override
            public void onResponse(@NonNull Call<DetailOutletInvAmntBillModel> call, @NonNull Response<DetailOutletInvAmntBillModel> response) {

                try {

                    Gson gson = new Gson();
                    String json = gson.toJson(response.body());
                    DetailOutletInvAmntBillModel detailOutletInvAmntBillModel = gson.fromJson(json, DetailOutletInvAmntBillModel.class);

                    if (detailOutletInvAmntBillModel.getStatus() == 1) {
                        if (detailOutletInvAmntBillModel.getData() != null) {
                            detailOutletInvAmntBillData = detailOutletInvAmntBillModel.getData();
                            for (int i = 0; i < detailOutletInvAmntBillData.size(); i++) {
                                edt_add_payment_amt.setText(detailOutletInvAmntBillData.get(i).getBalAmt());
                            }
                            progress.setVisibility(View.GONE);
                        } else {
                            progress.setVisibility(View.GONE);
                            CustomToast.getInstance(AddPaymentActivity.this).showSmallCustomToast(detailOutletInvAmntBillModel.getMessage());
                        }
                    } else {
                        progress.setVisibility(View.GONE);
                        CustomToast.getInstance(AddPaymentActivity.this).showSmallCustomToast(detailOutletInvAmntBillModel.getMessage());
                    }
                } catch (Exception e) {
                    Log.d("Exception", e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<DetailOutletInvAmntBillModel> call, Throwable t) {
                Log.d("Failure ", t.getMessage());
                progress.setVisibility(View.GONE);
                CustomToast.getInstance(AddPaymentActivity.this).showSmallCustomToast("Something went wrong try again..");

            }
        });

    }

    public void paymentTypeApi() {
        progress.setVisibility(View.VISIBLE);

        Call<PaymentTypeModel> call = RetrofitClient
                .getInstance().getApi().paymentTypeGet("_collectionType");


        call.enqueue(new Callback<PaymentTypeModel>() {
            @Override
            public void onResponse(@NonNull Call<PaymentTypeModel> call, @NonNull Response<PaymentTypeModel> response) {

                try {

                    Gson gson = new Gson();
                    String json = gson.toJson(response.body());
                    PaymentTypeModel paymentTypeModel = gson.fromJson(json, PaymentTypeModel.class);
//                LoginModule loginModule = response.body();


                    if (paymentTypeModel.getStatus() == 1) {

                        progress.setVisibility(View.GONE);
//                        CustomToast.getInstance(AddPaymentActivity.this).showSmallCustomToast(paymentTypeModel.getMessage());

                        addPaymentTypeList = paymentTypeModel.getData();
                        addPaymentTypeAdapter = new AddPaymentTypeAdapter(AddPaymentActivity.this, addPaymentTypeList);
                        spin_add_payment_type.setAdapter(addPaymentTypeAdapter);
                        addPaymentTypeAdapter.notifyDataSetChanged();
                        //                        onBackPressed();


                    } else {
                        progress.setVisibility(View.GONE);
                        CustomToast.getInstance(AddPaymentActivity.this).showSmallCustomToast(paymentTypeModel.getMessage());
                    }

                } catch (Exception e) {
                    Log.d("Exception", e.getMessage());
                }

            }

            @Override
            public void onFailure(@NonNull Call<PaymentTypeModel> call, @NonNull Throwable t) {
                Log.d("Failure ", t.getMessage());
                progress.setVisibility(View.GONE);
                CustomToast.getInstance(AddPaymentActivity.this).showSmallCustomToast("Something went wrong try again..");

            }
        });

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
                        txt_add_payment_date.setText(formattedDayOfMonth + "-" + formattedMonth + "-" + year);
                        txt_add_payment_date.setEnabled(false);

//                        select_spin_date = formattedDayOfMonth + "-" + formattedMonth + "-" + year;

//                        System.out.println("dayOfMonth  " + (dayOfMonth));
//                        System.out.println("monthOfYear " + (monthOfYear + 1));
//                        System.out.println("currreent " + select_spin_date);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    public void datePickerCollectDate() {

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
                        cheque_date.setText(formattedDayOfMonth + "-" + formattedMonth + "-" + year);
                        cheque_date.setEnabled(false);

//                        select_spin_date = formattedDayOfMonth + "-" + formattedMonth + "-" + year;

//                        System.out.println("dayOfMonth  " + (dayOfMonth));
//                        System.out.println("monthOfYear " + (monthOfYear + 1));
//                        System.out.println("currreent " + select_spin_date);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    private boolean Validate_Amt(EditText et) {
        // Always assume false until proven otherwise
        boolean bHasContent = false;
//        String MobilePattern = "[0-9]{10}";
        String pass = et.getText().toString();
        if (pass.length() > 0) {
            // Got content
            bHasContent = true;
        } else {
            et.setError("Enter Amount");

        }
        return bHasContent;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}