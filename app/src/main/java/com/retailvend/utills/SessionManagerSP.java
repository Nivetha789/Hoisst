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


    public void setPhonelogin(String authkey) {

        editor.putString("Phonelogin", authkey);

        editor.commit();


    }

    public String getPhonelogin() {


        return pref.getString("Phonelogin", "");
    }

    public void setSociallogin(String authkey) {

        editor.putString("SocialLogin", authkey);

        editor.commit();


    }

    public String getSociallogin() {


        return pref.getString("SocialLogin", "");
    }



    public void setSalesName(String authkey) {

        editor.putString("salesName", authkey);

        editor.commit();


    }

    public String getSalesName() {

        return pref.getString("salesName", "");
    }
    public void setSalesNameId(String authkey) {

        editor.putString("salesNameId", authkey);

        editor.commit();


    }

    public String getSalesNameId() {

        return pref.getString("salesNameId", "");
    }





    //*************** Send to Mosambee Activity(Register Details) ******************

    public void clearall() {

        editor.clear();
        editor.commit();
    }
}
