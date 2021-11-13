package com.retailvend;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.retailvend.broadcast.ConnectivityReceiver;
import com.retailvend.model.login.LoginDatum;
import com.retailvend.model.login.LoginResModel;
import com.retailvend.retrofit.RetrofitClient;
import com.retailvend.utills.CustomProgress;
import com.retailvend.utills.CustomToast;
import com.retailvend.utills.SessionManagerSP;
import com.retailvend.utills.SharedPrefManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    TextView text_signIn;
    TextView txt_forgot;
    EditText edt_mob_number;
    ProgressBar progress;
    EditText edt_password;
    List<LoginDatum> loginDataModelList;
    LoginDatum loginDataModel;
    SessionManagerSP sessionManagerSP;
    Activity activity;
    LinearLayout lin_eye,lin_eye_inv;
    String login_type="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        text_signIn=findViewById(R.id.txt_signIn);
        txt_forgot=findViewById(R.id.txt_forgot);
        edt_mob_number=findViewById(R.id.edt_mob_number);
        edt_password=findViewById(R.id.edt_pass);
        progress = findViewById(R.id.progressBar);
        lin_eye = findViewById(R.id.lin_eye);
        lin_eye_inv = findViewById(R.id.lin_eye_inv);
        sessionManagerSP = new SessionManagerSP(LoginActivity.this);

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

        text_signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Validate_Email(edt_mob_number) && Validate_Pass(edt_password)) {

                    checkConnection(edt_mob_number.getText().toString(), edt_password.getText().toString());
                }
            }
        });
        txt_forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, ForgetPasswordActivity.class);
                startActivity(i);
            }
        });

        lin_eye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edt_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                lin_eye_inv.setVisibility(View.VISIBLE);
                lin_eye.setVisibility(View.GONE);
            }
        });
        lin_eye_inv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edt_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                lin_eye_inv.setVisibility(View.GONE);
                lin_eye.setVisibility(View.VISIBLE);
            }
        });
    }

    private void checkConnection(String mobNo, String pass) {
        boolean isConnected = ConnectivityReceiver.isConnected();
        if (isConnected) {
//            Intent i = new Intent(LoginActivity.this,DashboardActivity.class);
//            startActivity(i);
            userLogin(mobNo, pass);

        } else {
//            Toast.makeText(this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
            CustomToast.getInstance(LoginActivity.this).showSmallCustomToast("Please check your internet connection");
        }

    }

    public void userLogin(String mobNo, String pass) {

        CustomProgress.showProgress(this);

        Call<LoginResModel> call = RetrofitClient
                .getInstance().getApi().userlogin("_employeeLogin",mobNo, pass);

        call.enqueue(new Callback<LoginResModel>() {
            @Override
            public void onResponse(@NonNull Call<LoginResModel> call, @NonNull Response<LoginResModel> response) {

                try {

                    Gson gson = new Gson();
                    String json = gson.toJson(response.body());
                    LoginResModel loginModule = gson.fromJson(json, LoginResModel.class);
//                LoginModule loginModule = response.body();
                    String s = loginModule.getMessage();
                    System.out.println("ssssss "+s);


                    if (loginModule.getStatus()) {

                        loginDataModelList = loginModule.getData();
                        CustomToast.getInstance(LoginActivity.this).showSmallCustomToast(loginModule.getMessage());


                        for (int i = 0; i < loginDataModelList.size(); i++) {
                            loginDataModel = loginDataModelList.get(i);
                        }
                        login_type=loginDataModel.getLogType();
                            SharedPrefManager.getInstance(LoginActivity.this)
                                    .saveUser(loginDataModel);

                            sessionManagerSP.setLoginType(login_type);
                            sessionManagerSP.setPhonelogin("1");
                            sessionManagerSP.setMobile(mobNo);
                            sessionManagerSP.setPass(pass);
                            sessionManagerSP.setPhonelogin("1");
                            sessionManagerSP.setDistributorId(loginDataModel.getCompanyId());

                            Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        CustomProgress.hideProgress(activity);
                    } else {
                        CustomProgress.hideProgress(activity);
                        CustomToast.getInstance(LoginActivity.this).showSmallCustomToast("Invalid User Name or Password");
                    }

                } catch (Exception e) {
                    Log.d("Exception", e.getMessage());
                    CustomProgress.hideProgress(activity);
                }

            }

            @Override
            public void onFailure(@NonNull Call<LoginResModel> call, @NonNull Throwable t) {
                Log.d("Failure ", t.getMessage());
                CustomToast.getInstance(LoginActivity.this).showSmallCustomToast("Something went wrong try again..");
                CustomProgress.hideProgress(activity);
            }
        });
    }

    private boolean Validate_Email(EditText et) {
        // Always assume false until proven otherwise

        boolean bHasContent = false;
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        String email = et.getText().toString();

        if (email.length() > 0) {
            // Got content
            bHasContent = true;
        } else {
            et.setError("Enter Mobile Number");

        }
        return bHasContent;
    }

    private boolean Validate_Pass(EditText et) {
        // Always assume false until proven otherwise
        boolean bHasContent = false;
//        String MobilePattern = "[0-9]{10}";
        String pass = et.getText().toString();
        if (pass.length() > 0) {
            // Got content
            bHasContent = true;
        } else {
            et.setError("Enter password");

        }
        return bHasContent;
    }
}