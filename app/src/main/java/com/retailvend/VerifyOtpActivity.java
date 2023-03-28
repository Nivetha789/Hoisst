package com.retailvend;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chaos.view.PinView;
import com.google.gson.Gson;
import com.retailvend.broadcast.ConnectivityReceiver;
import com.retailvend.model.SendOtpModel;
import com.retailvend.model.VerifyOtpModel;
import com.retailvend.model.order.CreateOrderModel;
import com.retailvend.model.outlets.AddAttendanceModel;
import com.retailvend.retrofit.RetrofitClient;
import com.retailvend.utills.CustomProgress;
import com.retailvend.utills.CustomToast;
import com.retailvend.utills.SessionManagerSP;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerifyOtpActivity extends AppCompatActivity {

    Activity activity;

    TextView txt_timer;
    PinView otp_pinView;

    CountDownTimer cTimer = null;

    LinearLayout lin_recent,lin_login;

    String otp = "";
    String store_id = "";
    String order_id = "";
    String bill_type = "";
    String discount = "";
    String due_days = "";
    String addProductJson = "";
    String lat_val = "";
    String long_val = "";
    String btn_Type_val = "";

    ImageView left_arrow;
    SessionManagerSP sessionManagerSP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);
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

        sessionManagerSP = new SessionManagerSP(VerifyOtpActivity.this);

        txt_timer=findViewById(R.id.txt_timer);
        lin_recent=findViewById(R.id.lin_recent);
        otp_pinView=findViewById(R.id.otp_pinView);
        left_arrow=findViewById(R.id.left_arrow);
        lin_login=findViewById(R.id.lin_login);

        VerifyOtpActivity VerifyOtpActivity = new VerifyOtpActivity();

        Intent data = getIntent();
        if (data != null) {
            store_id = getIntent().getStringExtra("store_id");
            bill_type = getIntent().getStringExtra("bill_type");
            discount = getIntent().getStringExtra("discount");
            due_days = getIntent().getStringExtra("due_days");
            addProductJson = getIntent().getStringExtra("addProductJson");
            btn_Type_val = getIntent().getStringExtra("btn_Type_val");
        }


        startTimer();
        otp_pinView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d(TAG, "onTextChanged() called with: s = [" + s + "], start = [" + start + "], before = [" + before + "], count = [" + count + "]");

//                if (s.length() == 0){
//                    otp_pinView.setLineColor(Color.parseColor("#60ba62"));
//                }
//                else if (s.length() == 1){
//                    otp_pinView.setLineColor(Color.parseColor("#60ba62"));
//                }


                if (s.length() > 3 && count == 1) {

                    otp = s.toString();
//                    lin_btn_otp.setBackgroundResource(R.drawable.lin_btn_default_login_back);

                } else {
//                    lin_btn_otp.setBackgroundResource(R.drawable.lin_btn_default_lite_clr_back);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        left_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        lin_recent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isConnected = ConnectivityReceiver.isConnected();
                if (isConnected) {

                    sendOtpApi();

                } else {
                    CustomToast.getInstance(VerifyOtpActivity.this).showSmallCustomToast("Please check your internet connection");

                }

            }
        });

        lin_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(otp)){
                    boolean isConnected = ConnectivityReceiver.isConnected();
                    if (isConnected) {
                        verifyOtpApi();
                    } else {
                        CustomToast.getInstance(VerifyOtpActivity.this).showSmallCustomToast("Please check your internet connection");
                    }
                }else {
                    CustomToast.getInstance(VerifyOtpActivity.this).showSmallCustomToast("Enter OTP");
                }
            }
        });
    }


    //send otp
    public void sendOtpApi() {
        CustomProgress.showProgress(activity);

        String emp_id = sessionManagerSP.getEmployeeId();
        String attandance_id = sessionManagerSP.getAssignId();

        Call<SendOtpModel> call = RetrofitClient
                .getInstance().getApi().sendOtp("_sendOtp", store_id,emp_id,"");

        call.enqueue(new Callback<SendOtpModel>() {
            @Override
            public void onResponse(@NonNull Call<SendOtpModel> call, @NonNull Response<SendOtpModel> response) {

                try {

                    Gson gson = new Gson();
                    String json = gson.toJson(response.body());

                    SendOtpModel sendOtpModel = gson.fromJson(json, SendOtpModel.class);
                    String s = sendOtpModel.getMessage();

                    if (sendOtpModel.getStatus() == 1) {

//                        text_signIn.setVisibility(View.VISIBLE);
                        CustomToast.getInstance(VerifyOtpActivity.this).showSmallCustomToast(sendOtpModel.getMessage());

                    } else {
//                        text_signIn.setVisibility(View.VISIBLE);
                        CustomProgress.hideProgress(activity);
                        //                        Toast.makeText(LoginActivity.this, "Invalid User Name or Password", Toast.LENGTH_SHORT).show();
                        CustomToast.getInstance(VerifyOtpActivity.this).showSmallCustomToast(sendOtpModel.getMessage());
//                    Toast.makeText(LoginActivity.this, "Invalid User Name or Password", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    Log.d("Exception", e.getMessage());
                    CustomProgress.hideProgress(activity);
                }

            }

            @Override
            public void onFailure(@NonNull Call<SendOtpModel> call, @NonNull Throwable t) {
                Log.d("Failure ", t.getMessage());
//                Toast.makeText(LoginActivity.this, "Invalid User Name or Password", Toast.LENGTH_SHORT).show();
                CustomToast.getInstance(VerifyOtpActivity.this).showSmallCustomToast("Something went wrong try again..");
//                text_signIn.setVisibility(View.VISIBLE);
                CustomProgress.hideProgress(activity);
            }
        });
    }

    //verify otp
    public void verifyOtpApi() {
        CustomProgress.showProgress(activity);

        String emp_id = sessionManagerSP.getEmployeeId();

        Call<VerifyOtpModel> call = RetrofitClient
                .getInstance().getApi().verifyOtp("_verifyOtp", store_id,emp_id,otp);

        call.enqueue(new Callback<VerifyOtpModel>() {
            @Override
            public void onResponse(@NonNull Call<VerifyOtpModel> call, @NonNull Response<VerifyOtpModel> response) {

                try {

                    Gson gson = new Gson();
                    String json = gson.toJson(response.body());

                    VerifyOtpModel verifyOtpModel = gson.fromJson(json, VerifyOtpModel.class);
                    String s = verifyOtpModel.getMessage();

                    if (verifyOtpModel.getStatus() == 1) {
                        CustomProgress.hideProgress(activity);
//                        text_signIn.setVisibility(View.VISIBLE);
                        CustomToast.getInstance(VerifyOtpActivity.this).showSmallCustomToast(verifyOtpModel.getMessage());
                        createOrderApi();
                    } else {
//                        text_signIn.setVisibility(View.VISIBLE);
                        CustomProgress.hideProgress(activity);
                        //                        Toast.makeText(LoginActivity.this, "Invalid User Name or Password", Toast.LENGTH_SHORT).show();
                        CustomToast.getInstance(VerifyOtpActivity.this).showSmallCustomToast(verifyOtpModel.getMessage());
//                    Toast.makeText(LoginActivity.this, "Invalid User Name or Password", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    Log.d("Exception", e.getMessage());
                    CustomProgress.hideProgress(activity);
                }

            }

            @Override
            public void onFailure(@NonNull Call<VerifyOtpModel> call, @NonNull Throwable t) {
                Log.d("Failure ", t.getMessage());
//                Toast.makeText(LoginActivity.this, "Invalid User Name or Password", Toast.LENGTH_SHORT).show();
                CustomToast.getInstance(VerifyOtpActivity.this).showSmallCustomToast("Something went wrong try again..");
//                text_signIn.setVisibility(View.VISIBLE);
                CustomProgress.hideProgress(activity);
            }
        });
    }


    public void createOrderApi() {
        CustomProgress.showProgress(activity);

        String emp_id = sessionManagerSP.getEmployeeId();
        String attandance_id = sessionManagerSP.getAssignId();

        Call<CreateOrderModel> call = RetrofitClient
                .getInstance().getApi().createOrder("_addSalesOrder", emp_id, store_id, bill_type, discount, due_days, "1", attandance_id, addProductJson);

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
                        CustomToast.getInstance(VerifyOtpActivity.this).showSmallCustomToast(createOrderModel.getMessage());

                        CustomProgress.hideProgress(activity);
                        boolean isConnected = ConnectivityReceiver.isConnected();
                        if (isConnected) {
                            updateAttendanceApi();
                        } else {
//            Toast.makeText(this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
                            CustomToast.getInstance(VerifyOtpActivity.this).showSmallCustomToast("Please check your internet connection");
                        }

                    } else {
//                        text_signIn.setVisibility(View.VISIBLE);
                        CustomProgress.hideProgress(activity);
                        //                        Toast.makeText(LoginActivity.this, "Invalid User Name or Password", Toast.LENGTH_SHORT).show();
                        CustomToast.getInstance(VerifyOtpActivity.this).showSmallCustomToast(createOrderModel.getMessage());
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
                CustomToast.getInstance(VerifyOtpActivity.this).showSmallCustomToast("Something went wrong try again..");
//                text_signIn.setVisibility(View.VISIBLE);
                CustomProgress.hideProgress(activity);
            }
        });
    }

    public void updateAttendanceApi() {
        CustomProgress.showProgress(activity);
        String emp_id = sessionManagerSP.getEmployeeId();
        String assign_id = sessionManagerSP.getAssignId();
        String invoice_id = sessionManagerSP.getInvoiceId();
        lat_val = sessionManagerSP.getLat();
        long_val = sessionManagerSP.getLong();


        Call<AddAttendanceModel> call = RetrofitClient
                .getInstance().getApi().updateAttendance("_updateAttendance", emp_id, store_id, lat_val, long_val, btn_Type_val, "", assign_id,invoice_id);

        call.enqueue(new Callback<AddAttendanceModel>() {
            @Override
            public void onResponse(@NonNull Call<AddAttendanceModel> call, @NonNull Response<AddAttendanceModel> response) {

                try {

                    Gson gson = new Gson();
                    String json = gson.toJson(response.body());

                    AddAttendanceModel attendanceTypeModel = gson.fromJson(json, AddAttendanceModel.class);

                    if (attendanceTypeModel.getStatus() == 1) {

//                        CustomToast.getInstance(VerifyOtpActivity.this).showSmallCustomToast(attendanceTypeModel.getMessage());

                        CustomProgress.hideProgress(activity);
                        Intent intent =new Intent(VerifyOtpActivity.this,DashboardActivity.class);
                        startActivity(intent);
                    } else {
                        CustomProgress.hideProgress(activity);
                        CustomToast.getInstance(VerifyOtpActivity.this).showSmallCustomToast(attendanceTypeModel.getMessage());
                    }

                } catch (Exception e) {
                    Log.d("Exception", e.getMessage());
                    CustomProgress.hideProgress(activity);
                }

            }

            @Override
            public void onFailure(@NonNull Call<AddAttendanceModel> call, @NonNull Throwable t) {
                Log.d("Failure ", t.getMessage());
                CustomToast.getInstance(VerifyOtpActivity.this).showSmallCustomToast("Something went wrong try again..");
                CustomProgress.hideProgress(activity);
            }
        });
    }


    void startTimer() {
        cTimer = new CountDownTimer(90000, 1000) {
            public void onTick(long millisUntilFinished) {
                txt_timer.setText(millisUntilFinished / 1000 + "s");

            }

            public void onFinish() {
                txt_timer.setVisibility(View.GONE);
                lin_recent.setVisibility(View.VISIBLE);
            }
        };
        cTimer.start();
    }
}