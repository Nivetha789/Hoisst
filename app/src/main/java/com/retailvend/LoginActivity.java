package com.retailvend;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.retailvend.broadcast.ConnectivityReceiver;
import com.retailvend.utills.CustomToast;

public class LoginActivity extends AppCompatActivity {

    TextView text_signIn;
    TextView txt_forgot;
    EditText edt_mob_number;
    EditText edt_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        text_signIn=findViewById(R.id.txt_signIn);
        txt_forgot=findViewById(R.id.txt_forgot);
        edt_mob_number=findViewById(R.id.edt_mob_number);
        edt_password=findViewById(R.id.edt_pass);

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
                Intent i = new Intent(LoginActivity.this,ForgetPasswordActivity.class);
                startActivity(i);
            }
        });
    }

    private void checkConnection(String username, String pass) {
        boolean isConnected = ConnectivityReceiver.isConnected();
        if (isConnected) {
            Intent i = new Intent(LoginActivity.this,DashboardActivity.class);
            startActivity(i);
//            userLogin(username, pass);

        } else {
//            Toast.makeText(this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
            CustomToast.getInstance(LoginActivity.this).showSmallCustomToast("Please check your internet connection");
        }

    }

//    public void userLogin(String username, String pass) {
//
//        btn_login.setVisibility(View.GONE);
//        progress.setVisibility(View.VISIBLE);
//
//        Call<LoginModel> call = RetrofitClient
//                .getInstance().getApi().userlogin(username, pass);
//
//
//        call.enqueue(new Callback<LoginModel>() {
//            @Override
//            public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
//
//                try {
//
//                    Gson gson = new Gson();
//                    String json = gson.toJson(response.body());
//                    LoginModel loginModule = gson.fromJson(json, LoginModel.class);
////                LoginModule loginModule = response.body();
//                    String s = loginModule.getMessage();
//
//
//                    if (loginModule.getCode() == 1) {
//
//                        loginDataModelList = loginModule.getData();
//
//                        for (int i = 0; i < loginDataModelList.size(); i++) {
//                            loginDataModel = loginDataModelList.get(i);
//                        }
//
//                        SharedPrefManager.getInstance(LoginActivity.this)
//                                .saveUser(loginDataModel);
//
//
//                        sessionManagerSP.setPhonelogin("1");
//
//                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                        startActivity(intent);
//                        finish();
//
//                        btn_login.setVisibility(View.VISIBLE);
//                        progress.setVisibility(View.GONE);
//
//
//                    } else {
//                        btn_login.setVisibility(View.VISIBLE);
//                        progress.setVisibility(View.GONE);
////                        Toast.makeText(LoginActivity.this, "Invalid User Name or Password", Toast.LENGTH_SHORT).show();
//                        CustomToast.getInstance(LoginActivity.this).showSmallCustomToast("Invalid User Name or Password");
////                    Toast.makeText(LoginActivity.this, "Invalid User Name or Password", Toast.LENGTH_SHORT).show();
//                    }
//
//                } catch (Exception e) {
//                    Log.d("Exception", e.getMessage());
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<LoginModel> call, Throwable t) {
//                Log.d("Failure ", t.getMessage());
////                Toast.makeText(LoginActivity.this, "Invalid User Name or Password", Toast.LENGTH_SHORT).show();
//                CustomToast.getInstance(LoginActivity.this).showSmallCustomToast("Something went wrong try again..");
//                btn_login.setVisibility(View.VISIBLE);
//                progress.setVisibility(View.GONE);
//
//            }
//        });
//
//
//    }

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