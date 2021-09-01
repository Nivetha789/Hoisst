package com.retailvend.todayoutlet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.gson.Gson;
import com.retailvend.R;
import com.retailvend.model.outlets.AssignOutletsDatum;
import com.retailvend.model.outlets.AssignOutletsModel;
import com.retailvend.model.outlets.AttendanceTypeDatum;
import com.retailvend.model.outlets.AttendanceTypeModel;
import com.retailvend.retrofit.RetrofitClient;
import com.retailvend.utills.CustomProgress;
import com.retailvend.utills.CustomToast;
import com.retailvend.utills.Loader;
import com.retailvend.utills.SharedPrefManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TodayOutletDetailsActivity extends AppCompatActivity {

    TextView takeOrder;
    AssignOutletsDatum assignOutletsDatum;
    TextView shop_name;
    TextView shop_number;
    TextView contact_name;
    TextView address;
    TextView mail;
    TextView gst;
    TextView pan;
    TextView check_in;
    TextView checked;
    Activity activity;
    List<AttendanceTypeDatum> attendanceTypeData;
    ButtonTypeAdapter buttonTypeAdapter;
    RecyclerView order_type_recycler;
    LinearLayoutManager mLayoutManager;
    String store_id="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today_outlet_details);
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
        attendanceListApi();
        shop_name = findViewById(R.id.shop_name);
        shop_number = findViewById(R.id.shop_number);
        contact_name = findViewById(R.id.contact_name);
        address = findViewById(R.id.address);
        mail = findViewById(R.id.mail);
        gst = findViewById(R.id.gst);
        pan = findViewById(R.id.pan);
        check_in = findViewById(R.id.check_in);
        checked = findViewById(R.id.checked);
        order_type_recycler=findViewById(R.id.order_type_recycler);

        assignOutletsDatum = (AssignOutletsDatum) getIntent().getSerializableExtra("todayOutlet");
        String shop_name1 = assignOutletsDatum.getCompanyName();
        store_id = assignOutletsDatum.getStoreId();
        System.out.println("fbfffggg "+store_id);
        String shop_number1 = assignOutletsDatum.getMobile();
        String contact_name1 = assignOutletsDatum.getContactName();
        String address1 = assignOutletsDatum.getAddress();
        String mail1 = assignOutletsDatum.getEmail();
        String gst1 = assignOutletsDatum.getGstNo();
        String pan1 = assignOutletsDatum.getPanNo();
        System.out.println("gstPan "+gst1+pan1);
        shop_name.setText(shop_name1);
        shop_number.setText(shop_number1);
        contact_name.setText(contact_name1);
        address.setText(address1);
        mail.setText(mail1);
        gst.setText(gst1);
        pan.setText(pan1);

        check_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_in.setVisibility(View.GONE);
                checked.setVisibility(View.VISIBLE);
                order_type_recycler.setVisibility(View.VISIBLE);
            }
        });
        checked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_in.setVisibility(View.VISIBLE);
                checked.setVisibility(View.GONE);
                order_type_recycler.setVisibility(View.GONE);
            }
        });
    }

    public void attendanceListApi() {
        final CustomProgress customProgress = new CustomProgress(this);
//        text_signIn.setVisibility(View.GONE);
        Loader.showLoad(customProgress, this, true);

        Call<AttendanceTypeModel> call = RetrofitClient
                .getInstance().getApi().attendanceList("_attendanceType");

        call.enqueue(new Callback<AttendanceTypeModel>() {
            @Override
            public void onResponse(@NonNull Call<AttendanceTypeModel> call, @NonNull Response<AttendanceTypeModel> response) {

                try {

                    Gson gson = new Gson();
                    String json = gson.toJson(response.body());

                    AttendanceTypeModel attendanceTypeModel = gson.fromJson(json, AttendanceTypeModel.class);

                    if (attendanceTypeModel.getStatus()==1) {

                        attendanceTypeData = attendanceTypeModel.getData();
//                        CustomToast.getInstance(TodayOutletActivity.this).showSmallCustomToast(todayOutletList.getMessage());

                        buttonTypeAdapter = new ButtonTypeAdapter(activity, attendanceTypeData,store_id);
                        order_type_recycler.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
                        mLayoutManager = new LinearLayoutManager(activity);
                        //RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
                        order_type_recycler.setItemAnimator(new DefaultItemAnimator());
                        order_type_recycler.setAdapter(buttonTypeAdapter);

//                        text_signIn.setVisibility(View.VISIBLE);
                        Loader.showLoad(customProgress, activity, false);

                    } else {
//                        text_signIn.setVisibility(View.VISIBLE);
                        Loader.showLoad(customProgress, activity, false);
                        //                        Toast.makeText(LoginActivity.this, "Invalid User Name or Password", Toast.LENGTH_SHORT).show();
                        CustomToast.getInstance(TodayOutletDetailsActivity.this).showSmallCustomToast(attendanceTypeModel.getMessage());
//                    Toast.makeText(LoginActivity.this, "Invalid User Name or Password", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    Log.d("Exception", e.getMessage());
                    Loader.showLoad(customProgress, activity, false);
                }

            }

            @Override
            public void onFailure(@NonNull Call<AttendanceTypeModel> call, @NonNull Throwable t) {
                Log.d("Failure ", t.getMessage());
//                Toast.makeText(LoginActivity.this, "Invalid User Name or Password", Toast.LENGTH_SHORT).show();
                CustomToast.getInstance(TodayOutletDetailsActivity.this).showSmallCustomToast("Something went wrong try again..");
//                text_signIn.setVisibility(View.VISIBLE);
                Loader.showLoad(customProgress, activity, false);
            }
        });
    }
}