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

    public void setLoginType(String authkey) {

        editor.putString("loginType", authkey);

        editor.commit();


    }

    public String getLoginType() {


        return pref.getString("loginType", "");
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

    public String getMobile() {

        return pref.getString("mobile", "");
    }


    public void setMobile(String authkey) {
        editor.putString("mobile", authkey);
        editor.commit();
    }

    public String getPass() {

        return pref.getString("pass", "");
    }


    public void setPass(String authkey) {
        editor.putString("pass", authkey);
        editor.commit();
    }

    public String getDistributorId() {

        return pref.getString("DistributorId", "");
    }


    public void setDistributorId(String authkey) {
        editor.putString("DistributorId", authkey);
        editor.commit();
    }

    public String getEmployeeId() {

        return pref.getString("EmployeeId", "");
    }


    public void setEmployeeId(String authkey) {
        editor.putString("EmployeeId", authkey);
        editor.commit();
    }

    public String getLat() {

        return pref.getString("Lat", "");
    }


    public void setLat(String authkey) {
        editor.putString("Lat", authkey);
        editor.commit();
    }

    public String getOutletLat() {

        return pref.getString("OutletLat", "");
    }


    public void setOutletLat(String authkey) {
        editor.putString("OutletLat", authkey);
        editor.commit();
    }

    public String getOutletLong() {

        return pref.getString("OutletLong", "");
    }


    public void setOutletLong(String authkey) {
        editor.putString("OutletLong", authkey);
        editor.commit();
    }

    public String getLong() {

        return pref.getString("Long", "");
    }


    public void setLong(String authkey) {
        editor.putString("Long", authkey);
        editor.commit();
    }

    public String getAttendanceId() {

        return pref.getString("AttendanceID", "");
    }


    public void setAttendanceId(String authkey) {
        editor.putString("AttendanceID", authkey);
        editor.commit();
    }

    public String getAssignId() {

        return pref.getString("AssignID", "");
    }


    public void setAssignId(String authkey) {
        editor.putString("AssignID", authkey);
        editor.commit();
    }



    //*************** Send to Mosambee Activity(Register Details) ******************

    public void clearall() {

        editor.clear();
        editor.commit();
    }
}
