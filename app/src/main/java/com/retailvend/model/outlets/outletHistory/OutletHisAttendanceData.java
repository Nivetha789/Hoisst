package com.retailvend.model.outlets.outletHistory;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OutletHisAttendanceData {
    @SerializedName("attendance_id")
    @Expose
    private String attendanceId;
    @SerializedName("employee_name")
    @Expose
    private String employeeName;
    @SerializedName("attendance_type")
    @Expose
    private String attendanceType;
    @SerializedName("attendance_date")
    @Expose
    private String attendanceDate;
    @SerializedName("attendance_time")
    @Expose
    private String attendanceTime;

    public String getAttendanceId() {
        return attendanceId;
    }

    public void setAttendanceId(String attendanceId) {
        this.attendanceId = attendanceId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getAttendanceType() {
        return attendanceType;
    }

    public void setAttendanceType(String attendanceType) {
        this.attendanceType = attendanceType;
    }

    public String getAttendanceDate() {
        return attendanceDate;
    }

    public void setAttendanceDate(String attendanceDate) {
        this.attendanceDate = attendanceDate;
    }

    public String getAttendanceTime() {
        return attendanceTime;
    }

    public void setAttendanceTime(String attendanceTime) {
        this.attendanceTime = attendanceTime;
    }
}
