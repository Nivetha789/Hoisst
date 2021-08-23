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
                sharedPreferences.getString("password", null)
        );
    }


    public void clear() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

}