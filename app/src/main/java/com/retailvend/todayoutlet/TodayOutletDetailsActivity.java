package com.retailvend.todayoutlet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.retailvend.R;
import com.retailvend.model.noreasonOutlet.NoReasonMessageDatum;
import com.retailvend.model.noreasonOutlet.NoReasonMessageModel;
import com.retailvend.model.outlets.AddAttendanceModel;
import com.retailvend.model.outlets.AssignOutletsDatum;
import com.retailvend.model.outlets.AttendanceTypeDatum;
import com.retailvend.model.outlets.AttendanceTypeModel;
import com.retailvend.model.outlets.ProductTypeDatum;
import com.retailvend.retrofit.RetrofitClient;
import com.retailvend.utills.CustomProgress;
import com.retailvend.utills.CustomToast;
import com.retailvend.utills.ProductAdapter;
import com.retailvend.utills.ReasonBaseAdapter;
import com.retailvend.utills.SessionManagerSP;
import com.retailvend.utills.SharedPrefManager;

import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TodayOutletDetailsActivity extends AppCompatActivity implements LocationListener {

    AssignOutletsDatum assignOutletsDatum;
    TextView shop_name;
    TextView shop_number;
    TextView contact_name;
    TextView address, submit_btn;
    TextView mail;
    TextView check_in;
    TextView checked;
    Activity activity;
    Spinner reason;
    List<AttendanceTypeDatum> attendanceTypeData;
    ButtonTypeAdapter buttonTypeAdapter;
    RecyclerView order_type_recycler;
    LinearLayoutManager mLayoutManager;
    String store_id = "";
    String attendance_status = "";
    String latitude = "";
    String longitude = "";
    ConstraintLayout order_type_constrain, reason_constrain, location_constrain;
    String type_id = "";
    String type_val = "";
    SessionManagerSP sessionManagerSP;
    ImageView left_arrow;
    LocationManager locationManager;
    String reasonTxt="";
    String reasonId="";

    private boolean locationget;

    List<NoReasonMessageDatum> noReasonMessageData;
    ReasonBaseAdapter reasonBaseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today_outlet_details);
        activity = this;
        sessionManagerSP = new SessionManagerSP(TodayOutletDetailsActivity.this);

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
        getReasonApi();

        shop_name = findViewById(R.id.shop_name);
        shop_number = findViewById(R.id.shop_number);
        contact_name = findViewById(R.id.contact_name);
        address = findViewById(R.id.address);
        mail = findViewById(R.id.mail);
        check_in = findViewById(R.id.check_in);
        checked = findViewById(R.id.checked);
        reason = findViewById(R.id.reason);
        submit_btn = findViewById(R.id.submit_btn);
        order_type_constrain = findViewById(R.id.order_type_constrain);
        order_type_recycler = findViewById(R.id.order_type_recycler);
        reason_constrain = findViewById(R.id.reason_constrain);
        location_constrain = findViewById(R.id.location_constrain);
        left_arrow = findViewById(R.id.left_arrow);

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
//        latitude = assignOutletsDatum.getLatitude();
//        longitude = assignOutletsDatum.getLongitude();
//        System.out.println("latitude "+latitude);
//        System.out.println("longitude "+longitude);
        shop_name.setText(shop_name1);
        shop_number.setText(shop_number1);
        contact_name.setText(contact_name1);
        address.setText(address1);
        mail.setText(mail1);
//        gst.setText(gst1);
//        pan.setText(pan1);
        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(TodayOutletDetailsActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);

        }

        checkGPSON();
        getLocation();

        left_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if(attendance_status.equals("1")){
            check_in.setVisibility(View.GONE);
            checked.setVisibility(View.VISIBLE);
            order_type_constrain.setVisibility(View.VISIBLE);

        }else{
            check_in.setVisibility(View.VISIBLE);
            checked.setVisibility(View.GONE);
            order_type_constrain.setVisibility(View.GONE);
        }

        location_constrain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uri = "http://maps.google.com/maps?q=loc:" + latitude + "," + longitude + " (" + "Map" + ")";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(intent);
            }
        });

        reason.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                NoReasonMessageDatum reasonMessageDatum = noReasonMessageData.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!reasonTxt.isEmpty()) {
                    updateAttendanceApi(type_id, type_val);
                } else {
                    CustomToast.getInstance(TodayOutletDetailsActivity.this).showSmallCustomToast("Enter Reason");
                }
            }
        });
        check_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(TodayOutletDetailsActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);

                }
                checkGPSON();
                getLocation();

                if (!TextUtils.isEmpty(latitude) && !TextUtils.isEmpty(longitude)) {
                    addAttendanceApi(latitude, longitude);
                } else {
                    Toast.makeText(TodayOutletDetailsActivity.this, "Location is not updated, Try Again", Toast.LENGTH_SHORT).show();
                }
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

    public boolean checkGPSON() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (manager != null) {
            if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                displayPromptForEnablingGPS(this);
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    public void displayPromptForEnablingGPS(final Activity activity) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        final String action = Settings.ACTION_LOCATION_SOURCE_SETTINGS;
        final String message = "Kindly turn ON Location Settings. GPS is required. Do you want open GPS setting?";

        builder.setMessage(message)
                .setPositiveButton("Location Settings",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface d, int id) {
                                activity.startActivity(new Intent(action));
                                d.dismiss();
                            }
                        });
//                .setNegativeButton("Cancel",
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface d, int id) {
//                                d.cancel();
//                                //saveEntry.setEnabled(false);
//                                // progressBar.setVisibility(View.INVISIBLE);
//                            }
//                        });

        builder.setCancelable(false);

        builder.create().show();
    }



    void getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, (LocationListener) this);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    private boolean isLocationEnabled() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    @Override
    public void onLocationChanged(Location location) {

        latitude = String.valueOf(location.getLatitude());
        longitude = String.valueOf(location.getLongitude());
        locationget = true;
        //  Toast.makeText(this, "Latitudetttt: " + latitude + "\n Longitude: " + longitude, Toast.LENGTH_SHORT).show();
        System.out.println("Latitudetttt: " + latitude + " Longitude: " + longitude);


        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

            String address = addresses.get(0).getAddressLine(0) + ", " +
                    addresses.get(0).getAddressLine(1);

            System.out.println(" address " + addresses.get(0).getAddressLine(0) + ", " +
                    addresses.get(0).getAddressLine(1) + ", " + addresses.get(0).getAddressLine(2));
        } catch (Exception e) {

        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(TodayOutletDetailsActivity.this, "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(String provider) {

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void attendanceListApi() {
        CustomProgress.showProgress(activity);
        Call<AttendanceTypeModel> call = RetrofitClient
                .getInstance().getApi().attendanceList("_attendanceType");

        call.enqueue(new Callback<AttendanceTypeModel>() {
            @Override
            public void onResponse(@NonNull Call<AttendanceTypeModel> call, @NonNull Response<AttendanceTypeModel> response) {

                try {

                    Gson gson = new Gson();
                    String json = gson.toJson(response.body());

                    AttendanceTypeModel attendanceTypeModel = gson.fromJson(json, AttendanceTypeModel.class);

                    if (attendanceTypeModel.getStatus() == 1) {

                        attendanceTypeData = attendanceTypeModel.getData();
//                        CustomToast.getInstance(TodayOutletActivity.this).showSmallCustomToast(todayOutletList.getMessage());

                        buttonTypeAdapter = new ButtonTypeAdapter(activity, attendanceTypeData, store_id, latitude, longitude);
                        order_type_recycler.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
                        mLayoutManager = new LinearLayoutManager(activity);
                        //RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
                        order_type_recycler.setItemAnimator(new DefaultItemAnimator());
                        order_type_recycler.setAdapter(buttonTypeAdapter);

//                        text_signIn.setVisibility(View.VISIBLE);
                        CustomProgress.hideProgress(activity);

                    } else {
//                        text_signIn.setVisibility(View.VISIBLE);
                        CustomProgress.hideProgress(activity);
                        //                        Toast.makeText(LoginActivity.this, "Invalid User Name or Password", Toast.LENGTH_SHORT).show();
                        CustomToast.getInstance(TodayOutletDetailsActivity.this).showSmallCustomToast(attendanceTypeModel.getMessage());
//                    Toast.makeText(LoginActivity.this, "Invalid User Name or Password", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    Log.d("Exception", e.getMessage());
                    CustomProgress.hideProgress(activity);
                }

            }

            @Override
            public void onFailure(@NonNull Call<AttendanceTypeModel> call, @NonNull Throwable t) {
                Log.d("Failure ", t.getMessage());
//                Toast.makeText(LoginActivity.this, "Invalid User Name or Password", Toast.LENGTH_SHORT).show();
                CustomToast.getInstance(TodayOutletDetailsActivity.this).showSmallCustomToast("Something went wrong try again..");
//                text_signIn.setVisibility(View.VISIBLE);
                CustomProgress.hideProgress(activity);
            }
        });
    }

    public void showReason(String typeId, String typeVal) {
        reason_constrain.setVisibility(View.VISIBLE);
        type_id = typeId;
        type_val = typeVal;
    }

    public void hideReason(String typeId, String typeVal) {
        reason_constrain.setVisibility(View.GONE);
        type_id = typeId;
        type_val = typeVal;
    }

    public void updateAttendanceApi(String typeId, String typeVal) {
        CustomProgress.showProgress(activity);
        String emp_id = sessionManagerSP.getEmployeeId();

        Call<AddAttendanceModel> call = RetrofitClient
                .getInstance().getApi().updateAttendance("_updateAttendance", emp_id, store_id, latitude, longitude, typeVal, reasonTxt, typeId);

        call.enqueue(new Callback<AddAttendanceModel>() {
            @Override
            public void onResponse(@NonNull Call<AddAttendanceModel> call, @NonNull Response<AddAttendanceModel> response) {

                try {

                    Gson gson = new Gson();
                    String json = gson.toJson(response.body());

                    AddAttendanceModel attendanceTypeModel = gson.fromJson(json, AddAttendanceModel.class);

                    if (attendanceTypeModel.getStatus() == 1) {

                        CustomToast.getInstance(TodayOutletDetailsActivity.this).showSmallCustomToast(attendanceTypeModel.getMessage());

                        CustomProgress.hideProgress(activity);
                        onBackPressed();
                    } else {
                        CustomProgress.hideProgress(activity);
                        CustomToast.getInstance(TodayOutletDetailsActivity.this).showSmallCustomToast(attendanceTypeModel.getMessage());
                    }

                } catch (Exception e) {
                    Log.d("Exception", e.getMessage());
                    CustomProgress.hideProgress(activity);
                }

            }

            @Override
            public void onFailure(@NonNull Call<AddAttendanceModel> call, @NonNull Throwable t) {
                Log.d("Failure ", t.getMessage());
                CustomToast.getInstance(TodayOutletDetailsActivity.this).showSmallCustomToast("Something went wrong try again..");
                CustomProgress.hideProgress(activity);
            }
        });
    }

    public void addAttendanceApi(String latitude, String longitude) {
        CustomProgress.showProgress(activity);
        String emp_id = sessionManagerSP.getEmployeeId();

        Call<AddAttendanceModel> call = RetrofitClient
                .getInstance().getApi().addAttendance("_addAttendance", emp_id, store_id, latitude, longitude);

        call.enqueue(new Callback<AddAttendanceModel>() {
            @Override
            public void onResponse(@NonNull Call<AddAttendanceModel> call, @NonNull Response<AddAttendanceModel> response) {

                try {

                    Gson gson = new Gson();
                    String json = gson.toJson(response.body());

                    AddAttendanceModel attendanceTypeModel = gson.fromJson(json, AddAttendanceModel.class);

                    if (attendanceTypeModel.getStatus() == 1) {

//                        CustomToast.getInstance(TodayOutletDetailsActivity.this).showSmallCustomToast(attendanceTypeModel.getMessage());
                        check_in.setVisibility(View.GONE);
                        checked.setVisibility(View.VISIBLE);
                        order_type_constrain.setVisibility(View.VISIBLE);
                        CustomProgress.hideProgress(activity);

                    } else {
                        CustomProgress.hideProgress(activity);
                        CustomToast.getInstance(TodayOutletDetailsActivity.this).showSmallCustomToast(attendanceTypeModel.getMessage());
                    }

                } catch (Exception e) {
                    Log.d("Exception", e.getMessage());
                    CustomProgress.hideProgress(activity);
                }

            }

            @Override
            public void onFailure(@NonNull Call<AddAttendanceModel> call, @NonNull Throwable t) {
                Log.d("Failure ", t.getMessage());
                CustomToast.getInstance(TodayOutletDetailsActivity.this).showSmallCustomToast("Something went wrong try again..");
                CustomProgress.hideProgress(activity);
            }
        });
    }

    public void getReasonApi() {
        CustomProgress.showProgress(activity);

        Call<NoReasonMessageModel> call = RetrofitClient
                .getInstance().getApi().noReason("_listMessage");

        call.enqueue(new Callback<NoReasonMessageModel>() {
            @Override
            public void onResponse(@NonNull Call<NoReasonMessageModel> call, @NonNull Response<NoReasonMessageModel> response) {

                try {

                    Gson gson = new Gson();
                    String json = gson.toJson(response.body());

                    NoReasonMessageModel noReasonMessageModel = gson.fromJson(json, NoReasonMessageModel.class);

                    if (noReasonMessageModel.getStatus() == 1) {

                        noReasonMessageData=noReasonMessageModel.getData();
                        if (noReasonMessageData != null) {
//                            ArrayList<String> list = new ArrayList<String>();
                            for (int i = 0; i < noReasonMessageData.size(); i++) {
//                                list.add(productTypeData.get(i).getDescription());
                                reasonTxt = noReasonMessageData.get(i).getMessage();
                                reasonId = noReasonMessageData.get(i).getMessageId();
                            }

//                            unitValue = list.toArray(new String[list.size()]);

//                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, unitValue);
//                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                            spin_name.setAdapter(dataAdapter);
                            reasonBaseAdapter = new ReasonBaseAdapter(TodayOutletDetailsActivity.this, noReasonMessageData);
                            reason.setAdapter(reasonBaseAdapter);
                            reasonBaseAdapter.notifyDataSetChanged();
                        }
                        CustomProgress.hideProgress(activity);

                    } else {
                        CustomProgress.hideProgress(activity);
                        CustomToast.getInstance(TodayOutletDetailsActivity.this).showSmallCustomToast(noReasonMessageModel.getMessage());
                    }

                } catch (Exception e) {
                    Log.d("Exception", e.getMessage());
                    CustomProgress.hideProgress(activity);
                }

            }

            @Override
            public void onFailure(@NonNull Call<NoReasonMessageModel> call, @NonNull Throwable t) {
                Log.d("Failure ", t.getMessage());
                CustomToast.getInstance(TodayOutletDetailsActivity.this).showSmallCustomToast("Something went wrong try again..");
                CustomProgress.hideProgress(activity);
            }
        });
    }
}