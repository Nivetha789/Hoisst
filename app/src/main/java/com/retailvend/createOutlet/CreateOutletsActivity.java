package com.retailvend.createOutlet;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.retailvend.R;
import com.retailvend.broadcast.ConnectivityReceiver;
import com.retailvend.collection.CollectionActivity;
import com.retailvend.model.createOutSales.CreateOutSalesModel;
import com.retailvend.model.createOutletModule.CreateOutletBeatListDatum;
import com.retailvend.model.createOutletModule.CreateOutletBeatListModel;
import com.retailvend.model.noreasonOutlet.NoReasonMessageDatum;
import com.retailvend.retrofit.RetrofitClient;
import com.retailvend.todayoutlet.CreateOutletOrderActivity;
import com.retailvend.todayoutlet.TodayOutletDetailsActivity;
import com.retailvend.utills.CustomProgress;
import com.retailvend.utills.CustomToast;
import com.retailvend.utills.ReasonBaseAdapter;
import com.retailvend.utills.SessionManagerSP;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateOutletsActivity extends AppCompatActivity implements LocationListener{

    Spinner spin_beat_name;
    EditText store_name,contact_name,mob_no_text,address_text;
    private boolean isLoading = false;
    Activity activity;
    SessionManagerSP sessionManagerSP;
    String emp_id="";
    List<CreateOutletBeatListDatum> createOutletBeatListData;
    BeatAdapter beatAdapter;
    String stateId,cityId,zoneId="";
    String lat_val = "";
    String long_val = "";
    LinearLayout lin_create;

    LocationManager locationManager;
    String desLatitude="";
    String desLongitude="";
    String latitude = "";
    String longitude = "";
    private boolean locationget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_outlets);
        activity=this;

        sessionManagerSP=new SessionManagerSP(CreateOutletsActivity.this);

        spin_beat_name=findViewById(R.id.spin_beat_name);
        store_name=findViewById(R.id.store_name);
        contact_name=findViewById(R.id.contact_name);
        mob_no_text=findViewById(R.id.mob_no_text);
        address_text=findViewById(R.id.address_text);
        lin_create=findViewById(R.id.lin_create);

        createOutletBeatListData=new ArrayList<>();

        emp_id= sessionManagerSP.getEmployeeId();
        lat_val = sessionManagerSP.getLat();
        long_val = sessionManagerSP.getLong();

        checkGPSON();
        getLocation();

        boolean isConnected = ConnectivityReceiver.isConnected();
        if (isConnected) {
            createOutletBeatApi();
        } else {
            CustomToast.getInstance(CreateOutletsActivity.this).showSmallCustomToast("Please check your internet connection");
        }

        spin_beat_name.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                CreateOutletBeatListDatum createOutletBeatListDatum = createOutletBeatListData.get(i);
                if(createOutletBeatListDatum!=null){
                    stateId=createOutletBeatListDatum.getStateId();
                    cityId=createOutletBeatListDatum.getCityId();
                    zoneId=createOutletBeatListDatum.getZoneId();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        lin_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(spin_beat_name!=null){
                    if(!store_name.getText().toString().isEmpty()){
                        if(!contact_name.getText().toString().isEmpty()){
                            if(!mob_no_text.getText().toString().isEmpty()){
                                if(!address_text.getText().toString().isEmpty()){
                                    boolean isConnected = ConnectivityReceiver.isConnected();
                                    if (isConnected) {
                                        createOutletApi();
                                    } else {
                                        CustomToast.getInstance(CreateOutletsActivity.this).showSmallCustomToast("Please check your internet connection");
                                    }
                                }else{
                                    CustomToast.getInstance(CreateOutletsActivity.this).showSmallCustomToast("Address should not be empty");
                                }
                            }else{
                                CustomToast.getInstance(CreateOutletsActivity.this).showSmallCustomToast("Mobile number should not be empty");
                            }
                        }else{
                            CustomToast.getInstance(CreateOutletsActivity.this).showSmallCustomToast("Contact Name should not be empty");
                        }
                    }else{
                        CustomToast.getInstance(CreateOutletsActivity.this).showSmallCustomToast("Store Name should not be empty");
                    }
                }else{
                    CustomToast.getInstance(CreateOutletsActivity.this).showSmallCustomToast("Select Beat");
                }
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



    private boolean isLocationEnabled() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }
    void getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, (LocationListener) this);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, (LocationListener) this);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(Location location) {

        desLatitude=sessionManagerSP.getOutletLat();
        desLongitude=sessionManagerSP.getOutletLong();
        latitude = String.valueOf(location.getLatitude());
        longitude = String.valueOf(location.getLongitude());
        sessionManagerSP.setLat(latitude);
        sessionManagerSP.setLong(longitude);
        locationget = true;
        //  Toast.makeText(this, "Latitudetttt: " + latitude + "\n Longitude: " + longitude, Toast.LENGTH_SHORT).show();
        System.out.println("Latitudetttt: " + latitude + " Longitude: " + longitude);


//        try {
//            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
//            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
//
//            String address = addresses.get(0).getAddressLine(0) + ", " +
//                    addresses.get(0).getAddressLine(1);
//
//            System.out.println(" address " + addresses.get(0).getAddressLine(0) + ", " +
//                    addresses.get(0).getAddressLine(1) + ", " + addresses.get(0).getAddressLine(2));
//        } catch (Exception e) {
//
//        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(CreateOutletsActivity.this, "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        desLatitude=sessionManagerSP.getOutletLat();
        desLongitude=sessionManagerSP.getOutletLong();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


    //beat name api
    public void createOutletBeatApi() {

        CustomProgress.showProgress(activity);

        Call<CreateOutletBeatListModel> call = RetrofitClient
                .getInstance().getApi().createOutletBeat("_employeeWiseBeat",emp_id);

        call.enqueue(new Callback<CreateOutletBeatListModel>() {
            @Override
            public void onResponse(Call<CreateOutletBeatListModel> call, Response<CreateOutletBeatListModel> response) {

                try {

                    Gson gson = new Gson();
                    String json = gson.toJson(response.body());
                    CreateOutletBeatListModel loginModule = gson.fromJson(json, CreateOutletBeatListModel.class);

                    if (loginModule.getStatus()==1) {
                        createOutletBeatListData = loginModule.getData();
                        beatAdapter = new BeatAdapter(CreateOutletsActivity.this, createOutletBeatListData);
                        spin_beat_name.setAdapter(beatAdapter);
                        beatAdapter.notifyDataSetChanged();

                        CustomProgress.hideProgress(activity);
                    } else {
                        CustomProgress.hideProgress(activity);
                        CustomToast.getInstance(CreateOutletsActivity.this).showSmallCustomToast(loginModule.getMessage());
                    }

                } catch (Exception e) {
                    Log.d("Exception", e.getMessage());
                    CustomProgress.hideProgress(activity);
                }

            }

            @Override
            public void onFailure(@NonNull Call<CreateOutletBeatListModel> call, @NonNull Throwable t) {
                Log.d("Failure ", t.getMessage());
                CustomToast.getInstance(CreateOutletsActivity.this).showSmallCustomToast("Something went wrong try again..");
                CustomProgress.hideProgress(activity);
            }
        });
    }

    //create outlet api
    public void createOutletApi() {

        CustomProgress.showProgress(activity);

        Call<CreateOutSalesModel> call = RetrofitClient
                .getInstance().getApi().createOutletDist("_newOutlets",emp_id,store_name.getText().toString(),contact_name.getText().toString(),mob_no_text.getText().toString(),
                        address_text.getText().toString(),stateId,cityId,zoneId,lat_val,long_val);

        call.enqueue(new Callback<CreateOutSalesModel>() {
            @Override
            public void onResponse(Call<CreateOutSalesModel> call, Response<CreateOutSalesModel> response) {

                try {

                    Gson gson = new Gson();
                    String json = gson.toJson(response.body());
                    CreateOutSalesModel loginModule = gson.fromJson(json, CreateOutSalesModel.class);

                    if (loginModule.getStatus()==1) {
                        CustomToast.getInstance(CreateOutletsActivity.this).showSmallCustomToast(loginModule.getMessage());
                        CustomProgress.hideProgress(activity);
                        onBackPressed();
                    } else {
                        CustomProgress.hideProgress(activity);
                        CustomToast.getInstance(CreateOutletsActivity.this).showSmallCustomToast(loginModule.getMessage());
                    }

                } catch (Exception e) {
                    Log.d("Exception", e.getMessage());
                    CustomProgress.hideProgress(activity);
                }

            }

            @Override
            public void onFailure(@NonNull Call<CreateOutSalesModel> call, @NonNull Throwable t) {
                Log.d("Failure ", t.getMessage());
                CustomToast.getInstance(CreateOutletsActivity.this).showSmallCustomToast("Something went wrong try again..");
                CustomProgress.hideProgress(activity);
            }
        });
    }
}