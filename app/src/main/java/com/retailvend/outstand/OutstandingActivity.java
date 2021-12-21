package com.retailvend.outstand;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.retailvend.LoginActivity;
import com.retailvend.R;
import com.retailvend.broadcast.ConnectivityReceiver;
import com.retailvend.model.outlets.AssignOutletsDatum;
import com.retailvend.model.outlets.AssignOutletsModel;
import com.retailvend.retrofit.RetrofitClient;
import com.retailvend.todayoutlet.TodayOutletActivity;
import com.retailvend.todayoutlet.TodayOutletAdapter;
import com.retailvend.utills.CustomProgress;
import com.retailvend.utills.CustomToast;
import com.retailvend.utills.SessionManagerSP;
import com.retailvend.utills.SharedPrefManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OutstandingActivity extends AppCompatActivity {

    RecyclerView outstandingRecycler;
    Activity activity;
    LinearLayoutManager mLayoutManager;
    OutstandingAdapter outstandingAdapter;
    ImageView leftArrow;
    Toolbar toolbar;
    List<AssignOutletsDatum> todayOutletsDatum;
    TextView total_amount;
    SessionManagerSP sessionManagerSP;
    TextView nodata_txt;
    ConstraintLayout no_data_constrain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outstanding);
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

        toolbar = findViewById(R.id.toolbar);
        outstandingRecycler = findViewById(R.id.outstanding_recyclerView);
        leftArrow = findViewById(R.id.left_arrow);
        total_amount=findViewById(R.id.total_amount);
        nodata_txt=findViewById(R.id.nodata_txt);
        no_data_constrain=findViewById(R.id.no_data_constrain);
        sessionManagerSP=new SessionManagerSP(OutstandingActivity.this);


//        swipeRefresh.setOnRefreshListener(this);
//        salesRecycler.setHasFixedSize(true);
//        saleslists = new ArrayList<>();

        boolean isConnected = ConnectivityReceiver.isConnected();
        if (isConnected) {
//            Intent i = new Intent(LoginActivity.this,DashboardActivity.class);
//            startActivity(i);
            outstandListApi();

        } else {
//            Toast.makeText(this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
            CustomToast.getInstance(OutstandingActivity.this).showSmallCustomToast("Please check your internet connection");
        }

        leftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
//        refreshActivity();
//        RegistorSharedPreManager.getInstance(DetailsActivity.this).clear();
        super.onBackPressed();
    }

    public void outstandListApi() {
        CustomProgress.showProgress(activity);
        String emp_id= sessionManagerSP.getEmployeeId();
//        System.out.println("emmmpidd "+emp_id);

        Call<AssignOutletsModel> call = RetrofitClient
                .getInstance().getApi().todayOutletList("_employeeWiseList",emp_id);

        call.enqueue(new Callback<AssignOutletsModel>() {
            @Override
            public void onResponse(Call<AssignOutletsModel> call, Response<AssignOutletsModel> response) {

                try {

                    Gson gson = new Gson();
                    String json = gson.toJson(response.body());
                    System.out.println("responseOutletsss "+response.body());

                    AssignOutletsModel todayOutletList = gson.fromJson(json, AssignOutletsModel.class);
                    String s = todayOutletList.getMessage();

                    if (todayOutletList.getStatus()==1) {
                        no_data_constrain.setVisibility(View.GONE);
                        outstandingRecycler.setVisibility(View.VISIBLE);
                        nodata_txt.setText("");
                        todayOutletsDatum = todayOutletList.getData();
//                        CustomToast.getInstance(TodayOutletActivity.this).showSmallCustomToast(todayOutletList.getMessage());

                        String total=todayOutletsDatum.get(1).getAvailableLimit();
                        total_amount.setText(total);
                        outstandingAdapter = new OutstandingAdapter(activity,todayOutletsDatum);
                        mLayoutManager = new LinearLayoutManager(activity);
                        //RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
                        outstandingRecycler.setLayoutManager(mLayoutManager);
                        outstandingRecycler.setItemAnimator(new DefaultItemAnimator());
                        outstandingRecycler.setAdapter(outstandingAdapter);

                        CustomProgress.hideProgress(activity);

                    } else {
                        CustomProgress.hideProgress(activity);
//                        CustomToast.getInstance(OutstandingActivity.this).showSmallCustomToast(todayOutletList.getMessage());
//                    Toast.makeText(LoginActivity.this, "Invalid User Name or Password", Toast.LENGTH_SHORT).show();
                        no_data_constrain.setVisibility(View.VISIBLE);
                        outstandingRecycler.setVisibility(View.GONE);
                        nodata_txt.setText(todayOutletList.getMessage());
                    }

                } catch (Exception e) {
                    Log.d("Exception", e.getMessage());
                    CustomProgress.hideProgress(activity);
                    no_data_constrain.setVisibility(View.VISIBLE);
                    outstandingRecycler.setVisibility(View.GONE);
                    nodata_txt.setText("");
                }

            }

            @Override
            public void onFailure(Call<AssignOutletsModel> call, Throwable t) {
                Log.d("Failure ", t.getMessage());
//                Toast.makeText(LoginActivity.this, "Invalid User Name or Password", Toast.LENGTH_SHORT).show();
//                CustomToast.getInstance(OutstandingActivity.this).showSmallCustomToast("Something went wrong try again..");
//                text_signIn.setVisibility(View.VISIBLE);
                CustomProgress.hideProgress(activity);
                no_data_constrain.setVisibility(View.VISIBLE);
                nodata_txt.setText("Something went wrong try again..");
                outstandingRecycler.setVisibility(View.GONE);
            }
        });
    }
}