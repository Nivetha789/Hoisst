package com.retailvend.model.outlets.outletHistory;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OutletHisOrderData {
    @SerializedName("order_id")
    @Expose
    private String orderId;
    @SerializedName("order_no")
    @Expose
    private String orderNo;
    @SerializedName("emp_name")
    @Expose
    private String empName;
    @SerializedName("order_status")
    @Expose
    private String orderStatus;
    @SerializedName("_ordered")
    @Expose
    private String ordered;
    @SerializedName("random_value")
    @Expose
    private String randomValue;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrdered() {
        return ordered;
    }

    public void setOrdered(String ordered) {
        this.ordered = ordered;
    }

    public String getRandomValue() {
        return randomValue;
    }

    public void setRandomValue(String randomValue) {
        this.randomValue = randomValue;
    }

}
