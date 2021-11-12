//package com.retailvend.payment;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.app.DatePickerDialog;
//import android.os.Build;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.view.Window;
//import android.view.WindowManager;
//import android.widget.AdapterView;
//import android.widget.DatePicker;
//import android.widget.EditText;
//import android.widget.LinearLayout;
//import android.widget.ProgressBar;
//import android.widget.Spinner;
//import android.widget.TextView;
//
//import com.google.gson.Gson;
//import com.retailvend.R;
//import com.retailvend.model.delCollection.paymentCollection.PaymentTypeModel;
//import com.retailvend.retrofit.RetrofitClient;
//import com.retailvend.utills.AddPaymentType;
//import com.retailvend.utills.CustomToast;
//
//import java.text.SimpleDateFormat;
//import java.util.Calendar;
//import java.util.List;
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
//public class AddPaymentActivity extends AppCompatActivity {
//
//    TextView txt_dis_add_pay_toolbar, txt_add_payment_name, txt_add_payment_bal_amt, txt_add_payment_date;
//    EditText edt_add_payment_amt, edt_add_payment_descrip;
//    Spinner spin_add_payment_type;
//    LinearLayout lin_add_payment, lin_back;
//    List<AddPaymentType> addPaymentTypeList;
//    AddPaymentTypeAdapter addPaymentTypeAdapter;
//    String payment_type = "";
//    Calendar c;
//    int mYear, mMonth, mDay, mHour, mMinute;
//    String assignId = "";
//    String balamt = "";
//    String cmpy = "";
//    ProgressBar progress;
//    String discount = "";
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_add_payment);
//
//        if (Build.VERSION.SDK_INT >= 19) {
//
//            Window window = getWindow();
//
//            getWindow().getDecorView().setSystemUiVisibility(
//                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
//                            | View.SYSTEM_UI_FLAG_LOW_PROFILE
//                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                            | View.SYSTEM_UI_FLAG_IMMERSIVE);
////            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
////            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//
//            window.setStatusBarColor(this.getResources().getColor(R.color.white));
////            window.setNavigationBarColor(this.getResources().getColor(R.color.black_overlay));
//        }
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
//            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
//        }
//
//
//
//        addPaymentTypeList.add(new AddPaymentType("Cash"));
//        addPaymentTypeList.add(new AddPaymentType("Cheque"));
//        addPaymentTypeList.add(new AddPaymentType("DD"));
//
//        addPaymentTypeAdapter = new AddPaymentTypeAdapter(AddPaymentActivity.this, addPaymentTypeList);
//        spin_add_payment_type.setAdapter(addPaymentTypeAdapter);
//        addPaymentTypeAdapter.notifyDataSetChanged();
//
//
//        spin_add_payment_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//
//                AddPaymentType addPaymentType = addPaymentTypeList.get(i);
//                payment_type = addPaymentType.getType();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });
//
//        c = Calendar.getInstance();
//        mYear = c.get(Calendar.YEAR);
//        mMonth = c.get(Calendar.MONTH);
//        mDay = c.get(Calendar.DAY_OF_MONTH);
//        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy"); //Date and time
//        String currentDate = sdf.format(c.getTime());
//        txt_add_payment_date.setText(currentDate);
//
//        txt_add_payment_date.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
////                c = Calendar.getInstance();
////                mYear = c.get(Calendar.YEAR);
////                mMonth = c.get(Calendar.MONTH);
////                mDay = c.get(Calendar.DAY_OF_MONTH);
////
////                System.out.println("Current Date  new " + mDay + "-" + mMonth + "-" + mYear);
//
//
//                datePicker();
//            }
//        });
//
//
//        if (balamt.length() > 0) {
//            if (balamt.equals("0")) {
//                lin_add_payment.setVisibility(View.GONE);
//                txt_add_payment_bal_amt.setText("0");
//            } else {
//                lin_add_payment.setVisibility(View.VISIBLE);
//                txt_add_payment_bal_amt.setText(balamt);
//            }
//        } else {
//            lin_add_payment.setVisibility(View.GONE);
//            txt_add_payment_bal_amt.setText("0");
//        }
//
//
//        lin_add_payment.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (Validate_Amt(edt_add_payment_amt)) {
//                    if (payment_type.length() > 0) {
//
//                        if (txt_add_payment_date.getText().toString().length() > 0) {
//
//                            if (edt_add_payment_descrip.getText().toString().length() > 0) {
//                                discount = edt_add_payment_descrip.getText().toString();
//                            } else {
//                                discount = "0";
//                            }
//
//                            if (which.equals("customer")) {
//
//                                boolean isConnected = ConnectivityReceiver.isConnected();
//                                if (isConnected) {
//
//                                    addCustomerPayment("3", userid, edt_add_payment_amt.getText().toString(),
//                                            balamt, txt_add_payment_date.getText().toString(),
//                                            payment_type, discount);
//
//                                } else {
//                                    CustomToast.getInstance(AddPaymentActivity.this).showSmallCustomToast("Please check your internet connection");
//                                }
//
//
//                            } else {
//
//                                boolean isConnected = ConnectivityReceiver.isConnected();
//                                if (isConnected) {
//
//                                    addSupplierPayment("3", userid, edt_add_payment_amt.getText().toString(),
//                                            balamt, txt_add_payment_date.getText().toString(),
//                                            payment_type, discount);
//
//                                } else {
//                                    CustomToast.getInstance(AddPaymentActivity.this).showSmallCustomToast("Please check your internet connection");
//                                }
//                            }
//
//                        } else {
//                            CustomToast.getInstance(AddPaymentActivity.this).showSmallCustomToast("Date missing");
//                        }
//
//                    } else {
//                        CustomToast.getInstance(AddPaymentActivity.this).showSmallCustomToast("Payment Type missing");
//                    }
//
//                }
//
//            }
//        });
//        lin_back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onBackPressed();
//            }
//        });
//
//
//    }
//
//    public void addCustomerPayment(String type, String userid, String amt, String balamt, String date, String p_type, String discount) {
//
//
//        progress.setVisibility(View.VISIBLE);
//        lin_add_payment.setVisibility(View.GONE);
//
//
//        Call<AddPaymentModel> call = RetrofitClient
//                .getInstance().getApi().addCustomerPayment(type, userid, amt, balamt, date, p_type, discount);
//
//
//        call.enqueue(new Callback<AddPaymentModel>() {
//            @Override
//            public void onResponse(Call<AddPaymentModel> call, Response<AddPaymentModel> response) {
//
//                try {
//
//                    Gson gson = new Gson();
//                    String json = gson.toJson(response.body());
//                    AddPaymentModel addPaymentModel = gson.fromJson(json, AddPaymentModel.class);
////                LoginModule loginModule = response.body();
//
//
//                    if (addPaymentModel.getCode() == 1) {
//
//                        progress.setVisibility(View.GONE);
//                        lin_add_payment.setVisibility(View.GONE);
//                        onBackPressed();
//                        CustomToast.getInstance(AddPaymentActivity.this).showSmallCustomToast(addPaymentModel.getMessage());
//
//
//                    } else {
//                        progress.setVisibility(View.GONE);
//                        lin_add_payment.setVisibility(View.VISIBLE);
//                        CustomToast.getInstance(AddPaymentActivity.this).showSmallCustomToast(addPaymentModel.getMessage());
//                    }
//
//                } catch (Exception e) {
//                    Log.d("Exception", e.getMessage());
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<AddPaymentModel> call, Throwable t) {
//                Log.d("Failure ", t.getMessage());
//                progress.setVisibility(View.GONE);
//                lin_add_payment.setVisibility(View.VISIBLE);
//                CustomToast.getInstance(AddPaymentActivity.this).showSmallCustomToast("Something went wrong try again..");
//
//            }
//        });
//
//    }
//
//    public void paymentTypeApi() {
//
//
//        progress.setVisibility(View.VISIBLE);
//        lin_add_payment.setVisibility(View.GONE);
//
//
//        Call<PaymentTypeModel> call = RetrofitClient
//                .getInstance().getApi().paymentTypeGet("_collectionType");
//
//
//        call.enqueue(new Callback<PaymentTypeModel>() {
//            @Override
//            public void onResponse(Call<PaymentTypeModel> call, Response<PaymentTypeModel> response) {
//
//                try {
//
//                    Gson gson = new Gson();
//                    String json = gson.toJson(response.body());
//                    PaymentTypeModel paymentTypeModel = gson.fromJson(json, PaymentTypeModel.class);
////                LoginModule loginModule = response.body();
//
//
//                    if (paymentTypeModel.getStatus() == 1) {
//
//                        progress.setVisibility(View.GONE);
//                        for(int i=0; i<paymentTypeModel.getData().size(); i++){
//                            addPaymentTypeList.addAll(i);
//                        }
////                        lin_add_payment.setVisibility(View.GONE);
////                        onBackPressed();
////                        CustomToast.getInstance(AddPaymentActivity.this).showSmallCustomToast(addPaymentModel.getMessage());
//
//
//                    } else {
//                        progress.setVisibility(View.GONE);
////                        lin_add_payment.setVisibility(View.VISIBLE);
//                        CustomToast.getInstance(AddPaymentActivity.this).showSmallCustomToast(paymentTypeModel.getMessage());
//                    }
//
//                } catch (Exception e) {
//                    Log.d("Exception", e.getMessage());
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<PaymentTypeModel> call, Throwable t) {
//                Log.d("Failure ", t.getMessage());
//                progress.setVisibility(View.GONE);
////                lin_add_payment.setVisibility(View.VISIBLE);
//                CustomToast.getInstance(AddPaymentActivity.this).showSmallCustomToast("Something went wrong try again..");
//
//            }
//        });
//
//    }
//
//    public void datePicker() {
//
//        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
//                new DatePickerDialog.OnDateSetListener() {
//
//                    @Override
//                    public void onDateSet(DatePicker view, int year,
//                                          int monthOfYear, int dayOfMonth) {
//
//                        String formattedDayOfMonth = "";
//                        String formattedMonth = "";
//
//                        if (dayOfMonth < 10) {
//
//                            formattedDayOfMonth = "0" + dayOfMonth;
//                        } else {
//                            formattedDayOfMonth = String.valueOf(dayOfMonth);
//                        }
//                        if ((monthOfYear + 1) < 10) {
//
//                            formattedMonth = "0" + (monthOfYear + 1);
//                        } else {
//                            formattedMonth = String.valueOf((monthOfYear + 1));
//                        }
//
//
////                        txt_date.setText(strDate);
//                        txt_add_payment_date.setText(formattedDayOfMonth + "-" + formattedMonth + "-" + year);
//
////                        select_spin_date = formattedDayOfMonth + "-" + formattedMonth + "-" + year;
//
////                        System.out.println("dayOfMonth  " + (dayOfMonth));
////                        System.out.println("monthOfYear " + (monthOfYear + 1));
////                        System.out.println("currreent " + select_spin_date);
//                    }
//                }, mYear, mMonth, mDay);
//        datePickerDialog.show();
//    }
//
//    private boolean Validate_Amt(EditText et) {
//        // Always assume false until proven otherwise
//        boolean bHasContent = false;
////        String MobilePattern = "[0-9]{10}";
//        String pass = et.getText().toString();
//        if (pass.length() > 0) {
//            // Got content
//            bHasContent = true;
//        } else {
//            et.setError("Enter Amount");
//
//        }
//        return bHasContent;
//    }
//
//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//    }
//}