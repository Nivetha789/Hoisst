package com.retailvend.utills;

import android.content.Context;
import android.content.SharedPreferences;

import com.retailvend.model.login.LoginDatum;


public class SharedPrefManager {

    private static final String SHARED_PREF_NAME = "my_shared_preff";

    private static SharedPrefManager mInstance;
    private Context mCtx;

    private SharedPrefManager(Context mCtx) {
        this.mCtx = mCtx;
    }


    public static synchronized SharedPrefManager getInstance(Context mCtx) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(mCtx);
        }
        return mInstance;
    }


    public void saveUser(LoginDatum user) {

        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("id", user.getId());
        editor.putString("username", user.getUsername());
        editor.putString("mobile", user.getMobile());
        editor.putString("email", user.getEmail());
        editor.putString("address", user.getAddress());
        editor.putString("password", user.getPassword());
        editor.putString("log_type", user.getLogType());
        editor.putString("company_type", user.getCompanyType());
        editor.putString("company_id", user.getCompanyId());
        editor.putString("permission", user.getPermission());
        editor.putString("status", user.getStatus());
        editor.putString("published", user.getPublished());
        editor.putString("createdate", user.getCreatedate());
        editor.putString("updatedate", user.getUpdatedate());
        editor.apply();

    }

    public boolean isLoggedIn(LoginDatum user) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString("id", user.getId()) != user.getId();
    }

    public LoginDatum getUser() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new LoginDatum(
                sharedPreferences.getString("id", null),
                sharedPreferences.getString("username", null),
                sharedPreferences.getString("mobile", null),
                sharedPreferences.getString("email", null),
                sharedPreferences.getString("address", null),
                sharedPreferences.getString("password", null),
                sharedPreferences.getString("log_type", null),
                sharedPreferences.getString("company_type", null),
                sharedPreferences.getString("company_id", null),
                sharedPreferences.getString("permission", null),
                sharedPreferences.getString("status", null),
                sharedPreferences.getString("published", null),
                sharedPreferences.getString("createdate", null),
                sharedPreferences.getString("updatedate", null)
        );
    }


    public void clear() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

}