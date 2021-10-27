package com.retailvend;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
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

public class ProfileActivity extends AppCompatActivity {

    ImageView left_arrow;
    ImageView logout;
    SessionManagerSP sessionManagerSP;
    List<LoginDatum> loginDataModelList;
    Activity activity;
    TextView profiler_name,email,mobile_no,address;
    String mobNo,pass="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        activity=this;

        left_arrow=findViewById(R.id.back);
        profiler_name=findViewById(R.id.profiler_name);
        email=findViewById(R.id.email);
        mobile_no=findViewById(R.id.mobile_no);
        address=findViewById(R.id.address);
//        logout=findViewById(R.id.logout);

        sessionManagerSP = new SessionManagerSP(ProfileActivity.this);
        left_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
//        logout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                sessionManagerSP.setPhonelogin("0");
//                SharedPrefManager.getInstance(getApplicationContext()).clear();
//                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
//                finish();
//            }
//        });

       mobNo= sessionManagerSP.getMobile();
       pass= sessionManagerSP.getPass();

        profile(mobNo,pass);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void profile(String mobNo, String pass) {

        CustomProgress.showProgress(this);

        Call<LoginResModel> call = RetrofitClient
                .getInstance().getApi().userlogin("_employeeLogin",mobNo, pass);

        call.enqueue(new Callback<LoginResModel>() {
            @Override
            public void onResponse(Call<LoginResModel> call, Response<LoginResModel> response) {

                try {

                    Gson gson = new Gson();
                    String json = gson.toJson(response.body());
                    LoginResModel loginModule = gson.fromJson(json, LoginResModel.class);

                    if (loginModule.getStatus()) {

                        loginDataModelList = loginModule.getData();
                        profiler_name.setText(loginDataModelList.get(0).getUsername());
                        email.setText(loginDataModelList.get(0).getEmail());
                        mobile_no.setText(loginDataModelList.get(0).getMobile());
                        address.setText(loginDataModelList.get(0).getAddress());

                        CustomProgress.hideProgress(activity);
                    } else {
                        CustomProgress.hideProgress(activity);
                        CustomToast.getInstance(ProfileActivity.this).showSmallCustomToast("Invalid User Name or Password");
                    }

                } catch (Exception e) {
                    Log.d("Exception", e.getMessage());
                    CustomProgress.hideProgress(activity);
                }

            }

            @Override
            public void onFailure(@NonNull Call<LoginResModel> call, @NonNull Throwable t) {
                Log.d("Failure ", t.getMessage());
                CustomToast.getInstance(ProfileActivity.this).showSmallCustomToast("Something went wrong try again..");
                CustomProgress.hideProgress(activity);
            }
        });
    }

}