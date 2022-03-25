package com.retailvend.model.outlets.outletHistory;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class OutletHistoryData {
    @SerializedName("attendance_data")
    @Expose
    private List<OutletHisAttendanceData> attendanceData = new ArrayList<>();
    @SerializedName("order_data")
    @Expose
    private List<OutletHisOrderData> orderData = new ArrayList<>();
    @SerializedName("payment_data")
    @Expose
    private List<OutletHisPaymentData> paymentData = new ArrayList<>();

    public List<OutletHisAttendanceData> getAttendanceData() {
        return attendanceData;
    }

    public void setAttendanceData(List<OutletHisAttendanceData> attendanceData) {
        this.attendanceData = attendanceData;
    }

    public List<OutletHisOrderData> getOrderData() {
        return orderData;
    }

    public void setOrderData(List<OutletHisOrderData> orderData) {
        this.orderData = orderData;
    }

    public List<OutletHisPaymentData> getPaymentData() {
        return paymentData;
    }

    public void setPaymentData(List<OutletHisPaymentData> paymentData) {
        this.paymentData = paymentData;
    }
}
