package com.retailvend.todayoutlet;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;

import static java.lang.Math.random;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.retailvend.R;
import com.retailvend.broadcast.ConnectivityReceiver;
import com.retailvend.createOutlet.salesManCollection.SalesManCollectionActivity;
import com.retailvend.model.noreasonOutlet.NoReasonMessageDatum;
import com.retailvend.model.noreasonOutlet.NoReasonMessageModel;
import com.retailvend.model.outlets.AddAttendanceData;
import com.retailvend.model.outlets.AddAttendanceModel;
import com.retailvend.model.outlets.AssignOutletsDatum;
import com.retailvend.model.outlets.AttendanceTypeDatum;
import com.retailvend.model.outlets.AttendanceTypeModel;
import com.retailvend.retrofit.RetrofitClient;
import com.retailvend.utills.CustomProgress;
import com.retailvend.utills.CustomToast;
import com.retailvend.utills.ReasonBaseAdapter;
import com.retailvend.utills.SessionManagerSP;
import com.sromku.simple.storage.SimpleStorage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
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
    LinearLayout outlet_his_Constrain, out_collection_Constrain, image_layout;
    String store_id = "";
    String shop_name1 = "";
    String attendance_status = "";
    String upload_status = "";
    String latitude = "";
    String longitude = "";
    ConstraintLayout order_type_constrain, reason_constrain, location_constrain,call_constrain;
    String type_id = "";
    String type_val = "";
    SessionManagerSP sessionManagerSP;
    ImageView left_arrow, captured_icon, captured_img;
    LocationManager locationManager;
    String reasonTxt = "";
    String reasonId = "";

    String assign_id = "";
    String desLatitude = "";
    String desLongitude = "";

    private boolean locationget;

    List<AddAttendanceData> addAttendanceData;

    List<NoReasonMessageDatum> noReasonMessageData;
    ReasonBaseAdapter reasonBaseAdapter;

    AlertDialog.Builder builder;

    String mobile="";


    File file1;
    private int REQUEST_IMAGE_CAPTURE = 100;

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

        captured_icon = findViewById(R.id.captured_icon);
        captured_img = findViewById(R.id.captured_img);
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
        outlet_his_Constrain = findViewById(R.id.outlet_his_Constrain);
        out_collection_Constrain = findViewById(R.id.out_collection_Constrain);
        image_layout = findViewById(R.id.image_layout);
        call_constrain = findViewById(R.id.call_constrain);

        builder = new AlertDialog.Builder(this);

        assignOutletsDatum = (AssignOutletsDatum) getIntent().getSerializableExtra("todayOutlet");
        shop_name1 = assignOutletsDatum.getCompanyName();
        store_id = assignOutletsDatum.getStoreId();
        String shop_number1 = assignOutletsDatum.getMobile();
        String contact_name1 = assignOutletsDatum.getContactName();
        String address1 = assignOutletsDatum.getAddress();
        String mail1 = assignOutletsDatum.getEmail();
        String gst1 = assignOutletsDatum.getGstNo();
        String pan1 = assignOutletsDatum.getPanNo();
        attendance_status = assignOutletsDatum.getAttendanceStatus();
        upload_status = assignOutletsDatum.getUploadStatus();
        System.out.println("upload_status : "+upload_status);
        shop_name.setText(shop_name1);
        shop_number.setText(shop_number1);
        contact_name.setText(contact_name1);
        address.setText(address1);
        mail.setText(mail1);
//        gst.setText(gst1);
//        pan.setText(pan1);


        desLatitude = sessionManagerSP.getOutletLat();
        desLongitude = sessionManagerSP.getOutletLong();

        checkGPSON();
        getLocation();

        if (upload_status.equals("2")) {
            image_layout.setVisibility(View.VISIBLE);
        } else {
            image_layout.setVisibility(View.GONE);
        }

        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(TodayOutletDetailsActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);

        }


        boolean isConnected = ConnectivityReceiver.isConnected();
        if (isConnected) {
            attendanceListApi();
        } else {
//            Toast.makeText(this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
            CustomToast.getInstance(TodayOutletDetailsActivity.this).showSmallCustomToast("Please check your internet connection");
        }

        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(TodayOutletDetailsActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
        }

        left_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        call_constrain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+shop_number1));
                startActivity(intent);
            }
        });

        image_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        });

        if (attendance_status.equals("1")) {
//            System.out.println("attendance_statusdddss " + attendance_status);
            check_in.setVisibility(View.GONE);
            checked.setVisibility(View.VISIBLE);
            order_type_constrain.setVisibility(View.VISIBLE);

        } else {
            check_in.setVisibility(View.VISIBLE);
            checked.setVisibility(View.GONE);
            order_type_constrain.setVisibility(View.GONE);
//            System.out.println("attendance_status111 " + attendance_status);
        }

        location_constrain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (TextUtils.isEmpty(latitude) && TextUtils.isEmpty(longitude)) {
//
//                    Toast.makeText(TodayOutletDetailsActivity.this, "Location is not updated, Try Again", Toast.LENGTH_SHORT).show();
//                } else {

                if (TextUtils.isEmpty(desLatitude) && TextUtils.isEmpty(desLongitude)) {
                    Toast.makeText(TodayOutletDetailsActivity.this, "Designation location empty", Toast.LENGTH_SHORT).show();
                } else {


                    String strUri = "http://maps.google.com/maps?q=loc:" + desLatitude + "," + desLongitude + " (" + address1 + ")";
                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(strUri));

                    intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");

                    startActivity(intent);
                }

//                }
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
                    if (type_val.equals("Sales Order")) {
                        type_val = "1";
                    } else {
                        type_val = "2";
                    }
                    updateAttendanceApi(type_val);
                } else {
                    CustomToast.getInstance(TodayOutletDetailsActivity.this).showSmallCustomToast("Select Reason");
                }
            }
        });
        check_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.app.AlertDialog.Builder builder1 = new android.app.AlertDialog.Builder(activity);
                builder1.setMessage("Check In").setTitle("Check In");
                builder1.setMessage("Are you sure you want to Check In?");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                                    ActivityCompat.requestPermissions(TodayOutletDetailsActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);

                                }
                                checkGPSON();
                                getLocation();
                                System.out.println("latitudenewww " + latitude);
                                System.out.println("longitudenewww " + longitude);
                                if (!TextUtils.isEmpty(latitude) && !TextUtils.isEmpty(longitude)) {

                                    if (upload_status.equals("2")) {
                                        if (!file1.getPath().isEmpty()) {
                                            boolean isConnected = ConnectivityReceiver.isConnected();
                                            if (isConnected) {
                                                addAttendanceApi(latitude, longitude);
                                            } else {
                                                CustomToast.getInstance(TodayOutletDetailsActivity.this).showSmallCustomToast("Please check your internet connection");
                                            }
                                        } else {
                                            CustomToast.getInstance(TodayOutletDetailsActivity.this).showSmallCustomToast("Please Upload image");
                                        }
                                    } else {
                                        boolean isConnected = ConnectivityReceiver.isConnected();
                                        if (isConnected) {
                                            addAttendanceApi(latitude, longitude);
                                        } else {
                                            CustomToast.getInstance(TodayOutletDetailsActivity.this).showSmallCustomToast("Please check your internet connection");
                                        }
                                    }

                                } else {
                                    Toast.makeText(TodayOutletDetailsActivity.this, "Location is not updated, Try Again", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                builder1.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                android.app.AlertDialog alert11 = builder1.create();
                alert11.show();
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

        outlet_his_Constrain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent outletIntent = new Intent(activity, OutletHistoryActivity.class);
                outletIntent.putExtra("outlet_id", store_id);
                outletIntent.putExtra("outlet_name", shop_name1);
                activity.startActivity(outletIntent);
            }
        });

        out_collection_Constrain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent outletIntent = new Intent(activity, SalesManCollectionActivity.class);
                outletIntent.putExtra("outlet_id", store_id);
                activity.startActivity(outletIntent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (attendance_status.equals("1")) {
            check_in.setVisibility(View.GONE);
            checked.setVisibility(View.VISIBLE);
            order_type_constrain.setVisibility(View.VISIBLE);

        } else {
            check_in.setVisibility(View.VISIBLE);
            checked.setVisibility(View.GONE);
            order_type_constrain.setVisibility(View.GONE);
        }
        desLatitude = sessionManagerSP.getOutletLat();
        desLongitude = sessionManagerSP.getOutletLong();
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

        desLatitude = sessionManagerSP.getOutletLat();
        desLongitude = sessionManagerSP.getOutletLong();
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
        Toast.makeText(TodayOutletDetailsActivity.this, "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(String provider) {

    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        captured_img.setVisibility(View.VISIBLE);
        captured_icon.setVisibility(View.GONE);
        Bitmap bitmap =  ImageUtils.getBitmapFromIntent(this, data);
        captured_img.setImageBitmap(bitmap);// mImage is a ImageView which is bind previously.
        String imgPath = ImageUtils.createFile(this, bitmap);
        file1= new File(imgPath);

        Glide.with(TodayOutletDetailsActivity.this).load(bitmap).into(captured_img);
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        desLatitude = sessionManagerSP.getOutletLat();
        desLongitude = sessionManagerSP.getOutletLong();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public static class ImageUtils {

        public static Bitmap getBitmapFromIntent(Context context, Intent data) {
            Bitmap bitmap = null;

            if (data.getData() == null) {
                bitmap = (Bitmap) data.getExtras().get("data");
            } else {
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), data.getData());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return bitmap;
        }


        public static String createFile(Context context, Bitmap data) {
            Uri selectedImage = getImageUri(context,data);
            String[] filePath = {MediaStore.Images.Media.DATA};
            Cursor c = context.getContentResolver().query(selectedImage, filePath, null, null, null);
            c.moveToFirst();
            c.getColumnIndex(filePath[0]);
            int columnIndex = c.getColumnIndex(filePath[0]);
            String picturePath = c.getString(columnIndex);
            c.close();

            return picturePath;
        }

        public static Uri getImageUri(Context context, Bitmap inImage) {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, "Pet_Image", null);
            return Uri.parse(path);
        }

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
                    System.out.println("attadanceecev " + response.body());

                    if (attendanceTypeModel.getStatus() == 1) {
                        attendanceTypeData = attendanceTypeModel.getData();
//                        CustomToast.getInstance(TodayOutletActivity.this).showSmallCustomToast(todayOutletList.getMessage());

                        buttonTypeAdapter = new ButtonTypeAdapter(activity, attendanceTypeData, store_id);
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
        boolean isConnected = ConnectivityReceiver.isConnected();
        if (isConnected) {
            getReasonApi();
        } else {
            CustomToast.getInstance(TodayOutletDetailsActivity.this).showSmallCustomToast("Please check your internet connection");
        }
        reason_constrain.setVisibility(View.VISIBLE);
        type_id = typeId;
        type_val = typeVal;
    }

    public void hideReason(String typeId, String typeVal) {
        reason_constrain.setVisibility(View.GONE);
        type_id = typeId;
        type_val = typeVal;
    }

    public void updateAttendanceApi(String typeVal) {
        CustomProgress.showProgress(activity);
        String emp_id = sessionManagerSP.getEmployeeId();
        String assign_ID = sessionManagerSP.getAssignId();

        Call<AddAttendanceModel> call = RetrofitClient
                .getInstance().getApi().updateAttendance("_updateAttendance", emp_id, store_id, latitude, longitude, typeVal, reasonTxt, assign_ID);

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
        MultipartBody.Part body1;

        if(file1!=null){
            RequestBody reqFile1 = RequestBody.create(MediaType.parse("image/*"), file1);
            body1 = MultipartBody.Part.createFormData("c_image", file1.getName(), reqFile1);
        }else{
            RequestBody reqFile1 = RequestBody.create(MediaType.parse("image/*"), "");
            body1 = MultipartBody.Part.createFormData("c_image", "", reqFile1);
        }

        RequestBody method = RequestBody.create(MediaType.parse("text/plain"), "_addAttendance");
        RequestBody emp_idd = RequestBody.create(MediaType.parse("text/plain"), emp_id);
        RequestBody store_Id = RequestBody.create(MediaType.parse("text/plain"), store_id);
        RequestBody latitude1 = RequestBody.create(MediaType.parse("text/plain"), latitude);
        RequestBody longitude1 = RequestBody.create(MediaType.parse("text/plain"), longitude);
        RequestBody upload_Status = RequestBody.create(MediaType.parse("text/plain"), upload_status);
        System.out.println("upload_status777777 "+upload_status);

        Call<AddAttendanceModel> call = RetrofitClient
                .getInstance().getApi().addAttendance(method, emp_idd, store_Id, latitude1, longitude1, upload_Status, body1);

        call.enqueue(new Callback<AddAttendanceModel>() {
            @Override
            public void onResponse(@NonNull Call<AddAttendanceModel> call, @NonNull Response<AddAttendanceModel> response) {

                try {

                    Gson gson = new Gson();
                    String json = gson.toJson(response.body());

                    AddAttendanceModel attendanceTypeModel = gson.fromJson(json, AddAttendanceModel.class);

                    if (attendanceTypeModel.getStatus() == 1) {
                        addAttendanceData = attendanceTypeModel.getData();
                        assign_id = addAttendanceData.get(0).getAttendanceId();
                        System.out.println("attandaceID " + assign_id);
                        sessionManagerSP.setAssignId(assign_id);
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
                Log.d("Failuresffssf ", t.getMessage());
                CustomToast.getInstance(TodayOutletDetailsActivity.this).showSmallCustomToast("Something went wrong try again..");
                CustomProgress.hideProgress(activity);
            }
        });
    }

    public void getReasonApi() {
//        CustomProgress.showProgress(activity);

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

                        noReasonMessageData = noReasonMessageModel.getData();
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
//                        CustomProgress.hideProgress(activity);

                    } else {
//                        CustomProgress.hideProgress(activity);
                        CustomToast.getInstance(TodayOutletDetailsActivity.this).showSmallCustomToast(noReasonMessageModel.getMessage());
                    }

                } catch (Exception e) {
                    Log.d("Exception", e.getMessage());
//                    CustomProgress.hideProgress(activity);
                }

            }

            //
            @Override
            public void onFailure(@NonNull Call<NoReasonMessageModel> call, @NonNull Throwable t) {
                Log.d("Failure ", t.getMessage());
                CustomToast.getInstance(TodayOutletDetailsActivity.this).showSmallCustomToast("Something went wrong try again..");
//                CustomProgress.hideProgress(activity);
            }
        });
    }
}