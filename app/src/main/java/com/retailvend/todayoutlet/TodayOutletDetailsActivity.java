package com.retailvend.todayoutlet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.retailvend.R;
import com.retailvend.model.outlets.AddAttendanceModel;
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
import java.util.Locale;

import at.markushi.ui.CircleButton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TodayOutletDetailsActivity extends AppCompatActivity {

    TextView takeOrder;
    AssignOutletsDatum assignOutletsDatum;
    TextView shop_name;
    TextView shop_number;
    TextView contact_name;
    TextView address,submit_btn;
    TextView mail;
    TextView gst;
    TextView pan;
    TextView check_in;
    TextView checked;
    Activity activity;
    EditText reason;
    List<AttendanceTypeDatum> attendanceTypeData;
    ButtonTypeAdapter buttonTypeAdapter;
    RecyclerView order_type_recycler;
    LinearLayoutManager mLayoutManager;
    String store_id="";
    String attendance_status="";
    String latitude="";
    String longitude="";
    ConstraintLayout order_type_constrain,reason_constrain;
    CircleButton fab;
    String type_id="";
    String type_val="";

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
        reason = findViewById(R.id.reason);
        submit_btn = findViewById(R.id.submit_btn);
        order_type_constrain = findViewById(R.id.order_type_constrain);
        order_type_recycler=findViewById(R.id.order_type_recycler);
        reason_constrain=findViewById(R.id.reason_constrain);
        fab=findViewById(R.id.fab);

        assignOutletsDatum = (AssignOutletsDatum) getIntent().getSerializableExtra("todayOutlet");
        String shop_name1 = assignOutletsDatum.getCompanyName();
        store_id = assignOutletsDatum.getStoreId();
        String shop_number1 = assignOutletsDatum.getMobile();
        String contact_name1 = assignOutletsDatum.getContactName();
        String address1 = assignOutletsDatum.getAddress();
        String mail1 = assignOutletsDatum.getEmail();
        String gst1 = assignOutletsDatum.getGstNo();
        String pan1 = assignOutletsDatum.getPanNo();
        attendance_status = assignOutletsDatum.getAttendanceStatus();
        latitude = assignOutletsDatum.getLatitude();
        longitude = assignOutletsDatum.getLongitude();
        System.out.println("attendance_status "+gst1+pan1);
        shop_name.setText(shop_name1);
        shop_number.setText(shop_number1);
        contact_name.setText(contact_name1);
        address.setText(address1);
        mail.setText(mail1);
        gst.setText(gst1);
        pan.setText(pan1);

        if(attendance_status.equals("1")){
            check_in.setVisibility(View.GONE);
            checked.setVisibility(View.VISIBLE);
            order_type_constrain.setVisibility(View.VISIBLE);

        }else{
            check_in.setVisibility(View.VISIBLE);
            checked.setVisibility(View.GONE);
            order_type_constrain.setVisibility(View.GONE);
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uri =  "http://maps.google.com/maps?q=loc:" + latitude + "," + longitude + " (" + "Map" + ")";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(intent);
            }
        });

        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!reason.getText().toString().isEmpty())
                {
                    updateAttendanceApi(type_id,type_val);
                }else{
                    CustomToast.getInstance(TodayOutletDetailsActivity.this).showSmallCustomToast("Enter Reason");
                }
            }
        });
        check_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addAttendanceApi();
            }
        });
//        checked.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                check_in.setVisibility(View.VISIBLE);
//                checked.setVisibility(View.GONE);
//                order_type_recycler.setVisibility(View.GONE);
//            }
//        });
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

    public void showReason(String typeId, String typeVal){
        reason_constrain.setVisibility(View.VISIBLE);
        type_id=typeId;
        type_val=typeVal;
    }

    public void updateAttendanceApi(String typeId, String typeVal) {
        final CustomProgress customProgress = new CustomProgress(this);
//        text_signIn.setVisibility(View.GONE);
        Loader.showLoad(customProgress, this, true);
        String emp_id= SharedPrefManager.getInstance(this).getUser().getId();

        Call<AddAttendanceModel> call = RetrofitClient
                .getInstance().getApi().updateAttendance("_updateAttendance",emp_id,store_id,latitude,longitude,typeVal,reason.getText().toString(),typeId);

        call.enqueue(new Callback<AddAttendanceModel>() {
            @Override
            public void onResponse(@NonNull Call<AddAttendanceModel> call, @NonNull Response<AddAttendanceModel> response) {

                try {

                    Gson gson = new Gson();
                    String json = gson.toJson(response.body());

                    AddAttendanceModel attendanceTypeModel = gson.fromJson(json, AddAttendanceModel.class);

                    if (attendanceTypeModel.getStatus()==1) {

                        CustomToast.getInstance(TodayOutletDetailsActivity.this).showSmallCustomToast(attendanceTypeModel.getMessage());

                        Loader.showLoad(customProgress, activity, false);

                    } else {
                        Loader.showLoad(customProgress, activity, false);
                        CustomToast.getInstance(TodayOutletDetailsActivity.this).showSmallCustomToast(attendanceTypeModel.getMessage());
                    }

                } catch (Exception e) {
                    Log.d("Exception", e.getMessage());
                    Loader.showLoad(customProgress, activity, false);
                }

            }

            @Override
            public void onFailure(@NonNull Call<AddAttendanceModel> call, @NonNull Throwable t) {
                Log.d("Failure ", t.getMessage());
                CustomToast.getInstance(TodayOutletDetailsActivity.this).showSmallCustomToast("Something went wrong try again..");
                Loader.showLoad(customProgress, activity, false);
            }
        });
    }

    public void addAttendanceApi() {
        final CustomProgress customProgress = new CustomProgress(this);
//        text_signIn.setVisibility(View.GONE);
        Loader.showLoad(customProgress, this, true);
        String emp_id= SharedPrefManager.getInstance(this).getUser().getId();

        Call<AddAttendanceModel> call = RetrofitClient
                .getInstance().getApi().addAttendance("_addAttendance",emp_id,store_id,latitude,longitude);

        call.enqueue(new Callback<AddAttendanceModel>() {
            @Override
            public void onResponse(@NonNull Call<AddAttendanceModel> call, @NonNull Response<AddAttendanceModel> response) {

                try {

                    Gson gson = new Gson();
                    String json = gson.toJson(response.body());

                    AddAttendanceModel attendanceTypeModel = gson.fromJson(json, AddAttendanceModel.class);

                    if (attendanceTypeModel.getStatus()==1) {

//                        CustomToast.getInstance(TodayOutletDetailsActivity.this).showSmallCustomToast(attendanceTypeModel.getMessage());
                        check_in.setVisibility(View.GONE);
                        checked.setVisibility(View.VISIBLE);
                        order_type_recycler.setVisibility(View.VISIBLE);
                        Loader.showLoad(customProgress, activity, false);

                    } else {
                        Loader.showLoad(customProgress, activity, false);
                        CustomToast.getInstance(TodayOutletDetailsActivity.this).showSmallCustomToast(attendanceTypeModel.getMessage());
                    }

                } catch (Exception e) {
                    Log.d("Exception", e.getMessage());
                    Loader.showLoad(customProgress, activity, false);
                }

            }

            @Override
            public void onFailure(@NonNull Call<AddAttendanceModel> call, @NonNull Throwable t) {
                Log.d("Failure ", t.getMessage());
                CustomToast.getInstance(TodayOutletDetailsActivity.this).showSmallCustomToast("Something went wrong try again..");
                Loader.showLoad(customProgress, activity, false);
            }
        });
    }
}