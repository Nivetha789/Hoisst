package com.retailvend.todayoutlet;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.retailvend.R;
import com.retailvend.model.outlets.AssignOutletsDatum;
import com.retailvend.model.outlets.AssignOutletsModel;
import com.retailvend.retrofit.RetrofitClient;
import com.retailvend.utills.CustomProgress;
import com.retailvend.utills.CustomToast;
import com.retailvend.utills.SharedPrefManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TodayOutletActivity extends AppCompatActivity {

    RecyclerView todayOutletRecycler;
    Activity activity;
    LinearLayoutManager mLayoutManager;
    TodayOutletAdapter todayOutletAdapter;
    ImageView leftArrow;
    List<AssignOutletsDatum> todayOutletsDatum;
    TextView nodata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today_outlet);
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


        todayOutletRecycler=findViewById(R.id.today_outlet_recycler);
        leftArrow=findViewById(R.id.left_arrow);
        nodata=findViewById(R.id.nodata);

        leftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        todayOutletListApi();
    }

    public void todayOutletListApi() {
        CustomProgress.showProgress(activity);
      String emp_id= SharedPrefManager.getInstance(TodayOutletActivity.this).getUser().getId();
        System.out.println("emmmpidd "+emp_id);

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
                        nodata.setVisibility(View.GONE);
                        todayOutletRecycler.setVisibility(View.VISIBLE);
                        nodata.setText("");
                        todayOutletsDatum = todayOutletList.getData();
//                        CustomToast.getInstance(TodayOutletActivity.this).showSmallCustomToast(todayOutletList.getMessage());


                        todayOutletAdapter = new TodayOutletAdapter(activity, todayOutletsDatum);
                        mLayoutManager = new LinearLayoutManager(activity);
                        //RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
                        todayOutletRecycler.setLayoutManager(mLayoutManager);
                        todayOutletRecycler.setItemAnimator(new DefaultItemAnimator());
                        todayOutletRecycler.setAdapter(todayOutletAdapter);

                      CustomProgress.hideProgress(activity);

                    } else {
                      CustomProgress.hideProgress(activity);
//                        CustomToast.getInstance(TodayOutletActivity.this).showSmallCustomToast(todayOutletList.getMessage());
                        nodata.setVisibility(View.VISIBLE);
                        todayOutletRecycler.setVisibility(View.GONE);
                        nodata.setText(todayOutletList.getMessage());
                    }

                } catch (Exception e) {
                    Log.d("Exception", e.getMessage());
                    CustomProgress.hideProgress(activity);
                    nodata.setVisibility(View.VISIBLE);
                    todayOutletRecycler.setVisibility(View.GONE);
                    nodata.setText("");
                }

            }

            @Override
            public void onFailure(Call<AssignOutletsModel> call, Throwable t) {
                Log.d("Failure ", t.getMessage());
//                CustomToast.getInstance(TodayOutletActivity.this).showSmallCustomToast("Something went wrong try again..");
//                text_signIn.setVisibility(View.VISIBLE);
                CustomProgress.hideProgress(activity);
                nodata.setVisibility(View.VISIBLE);
                nodata.setText("Something went wrong try again..");
                todayOutletRecycler.setVisibility(View.GONE);
            }
        });
    }
}