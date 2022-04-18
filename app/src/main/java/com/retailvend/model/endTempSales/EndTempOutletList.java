package com.retailvend.model.endTempSales;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class EndTempOutletList {
    @SerializedName("outlet_id")
    @Expose
    private String outletId;
    @SerializedName("company_name")
    @Expose
    private String companyName;
    @SerializedName("mobile")
    @Expose
    private String mobile;
    @SerializedName("attendance_list")
    @Expose
    private List<EndTempOutletAttendance> attendanceList = new ArrayList<>();

    public String getOutletId() {
        return outletId;
    }

    public void setOutletId(String outletId) {
        this.outletId = outletId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public List<EndTempOutletAttendance> getAttendanceList() {
        return attendanceList;
    }

    public void setAttendanceList(List<EndTempOutletAttendance> attendanceList) {
        this.attendanceList = attendanceList;
    }
}
