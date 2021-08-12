package com.retailvend.utills;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManagerSP {

    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "StaffSP";


    // Constructor
    public SessionManagerSP(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }



    public void setlogin(String authkey) {

        editor.putString("login", authkey);

        editor.commit();


    }

    public String getlogin() {


        return pref.getString("login", "");
    }


    public void setAppVersion(String authkey) {

        editor.putString("appversion", authkey);

        editor.commit();


    }

    public String getAppVersion() {


        return pref.getString("appversion", "");
    }


    public void setPincode(String authkey) {

        editor.putString("pincode", authkey);

        editor.commit();


    }

    public String getPincode() {


        return pref.getString("pincode", "");
    }





    public void setUserKey(String authkey) {

        editor.putString("key", authkey);

        editor.commit();


    }

    public String getUserKey() {


        return pref.getString("key", "");
    }


    public void setUserToken(String authkey) {

        editor.putString("token", authkey);

        editor.commit();


    }

    public String getUserToken() {


        return pref.getString("token", "");
    }



    //*************** Send to Mosambee Activity(Register Details) ******************

    public void clearall() {

        editor.clear();
        editor.commit();
    }


}
