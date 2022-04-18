package com.retailvend.model.endTempSales;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EndTempOutletAttendance {
    @SerializedName("in_time")
    @Expose
    private String inTime;
    @SerializedName("out_time")
    @Expose
    private String outTime;
    @SerializedName("attendance_type")
    @Expose
    private String attendanceType;
    @SerializedName("order_no")
    @Expose
    private String orderNo;
    @SerializedName("order_total")
    @Expose
    private String orderTotal;
    @SerializedName("reason")
    @Expose
    private String reason;

    public String getInTime() {
        return inTime;
    }

    public void setInTime(String inTime) {
        this.inTime = inTime;
    }

    public String getOutTime() {
        return outTime;
    }

    public void setOutTime(String outTime) {
        this.outTime = outTime;
    }

    public String getAttendanceType() {
        return attendanceType;
    }

    public void setAttendanceType(String attendanceType) {
        this.attendanceType = attendanceType;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOrderTotal() {
        return orderTotal;
    }

    public void setOrderTotal(String orderTotal) {
        this.orderTotal = orderTotal;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
