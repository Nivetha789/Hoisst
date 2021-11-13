package com.retailvend.utills;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

@SuppressWarnings("unused")
public class Preference {

    private Context context;

    private final static String PREFS_NAME = "Hoisst";

    private Preference(Context context){
        this.context = context;
    }

    @SuppressLint("StaticFieldLeak")
    private static Preference yourPreference;

    private SharedPreferences sharedPreferences;

    public static Preference getInstance(Context context) {
        if (yourPreference == null) {
            yourPreference = new Preference(context);
        }
        return yourPreference;
    }

    public void setInt( String key, int value) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public int getInt(String key) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(key, 0);
    }

    public void setStr(String key, String value) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getStr(String key) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(key,"");
    }

    public void setlogin(String value) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Login", value);
        editor.apply();
    }

    public String getLogin() {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString("Login","");
    }
}
