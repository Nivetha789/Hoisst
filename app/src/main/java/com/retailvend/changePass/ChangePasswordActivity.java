package com.retailvend.changePass;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.retailvend.R;
import com.retailvend.broadcast.ConnectivityReceiver;
import com.retailvend.collection.CollectionActivity;
import com.retailvend.model.changePassword.ChangePasswordModel;
import com.retailvend.model.outlets.AssignOutletsModel;
import com.retailvend.outstand.OutstandingAdapter;
import com.retailvend.retrofit.RetrofitClient;
import com.retailvend.utills.CustomProgress;
import com.retailvend.utills.CustomToast;
import com.retailvend.utills.SessionManagerSP;

import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends AppCompatActivity {

    Activity activity;
    SessionManagerSP sessionManagerSP;
    EditText new_pwd,confirm_pwd;
    Button update_btn;
    ImageView left_arrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        ButterKnife.bind(this);
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

        sessionManagerSP=new SessionManagerSP(ChangePasswordActivity.this);

        new_pwd=findViewById(R.id.new_pwd);
        confirm_pwd=findViewById(R.id.confirm_pwd);
        update_btn=findViewById(R.id.update_btn);
        left_arrow=findViewById(R.id.left_arrow);

        left_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isConnected = ConnectivityReceiver.isConnected();
                if (isConnected) {
                    changePasswordApi(new_pwd.getText().toString(),confirm_pwd.getText().toString());
                } else {
                    CustomToast.getInstance(ChangePasswordActivity.this).showSmallCustomToast("Please check your internet connection");
                }
            }
        });
    }


    public void changePasswordApi(String newPass,String conPass) {
        CustomProgress.showProgress(activity);
        String emp_id= sessionManagerSP.getEmployeeId();
//        System.out.println("emmmpidd "+emp_id);

        Call<ChangePasswordModel> call = RetrofitClient
                .getInstance().getApi().changePassword("_changePassword",emp_id,newPass,conPass);

        call.enqueue(new Callback<ChangePasswordModel>() {
            @Override
            public void onResponse(Call<ChangePasswordModel> call, Response<ChangePasswordModel> response) {

                try {

                    Gson gson = new Gson();
                    String json = gson.toJson(response.body());
                    System.out.println("responseOutletsss "+response.body());

                    ChangePasswordModel todayOutletList = gson.fromJson(json, ChangePasswordModel.class);
                    String s = todayOutletList.getMessage();

                    if (todayOutletList.getStatus()==1) {
                        new_pwd.setEnabled(false);
                        confirm_pwd.setEnabled(false);
                        CustomToast.getInstance(ChangePasswordActivity.this).showSmallCustomToast("Password changed successfully");
                        CustomProgress.hideProgress(activity);
                        onBackPressed();

                    } else {
                        CustomProgress.hideProgress(activity);
                        CustomToast.getInstance(ChangePasswordActivity.this).showSmallCustomToast(todayOutletList.getMessage());
//                    Toast.makeText(LoginActivity.this, "Invalid User Name or Password", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    Log.d("Exception", e.getMessage());
                    CustomProgress.hideProgress(activity);
                }

            }

            @Override
            public void onFailure(Call<ChangePasswordModel> call, Throwable t) {
                Log.d("Failure ", t.getMessage());
//                Toast.makeText(LoginActivity.this, "Invalid User Name or Password", Toast.LENGTH_SHORT).show();
                CustomToast.getInstance(ChangePasswordActivity.this).showSmallCustomToast("Something went wrong try again..");
//                text_signIn.setVisibility(View.VISIBLE);
                CustomProgress.hideProgress(activity);
            }
        });
    }
}