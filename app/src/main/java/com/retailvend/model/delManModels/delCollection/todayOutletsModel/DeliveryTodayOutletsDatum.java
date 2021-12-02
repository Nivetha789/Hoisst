package com.retailvend.model.delManModels.delCollection.todayOutletsModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeliveryTodayOutletsDatum {
    @SerializedName("order_id")
    @Expose
    private String orderId;
    @SerializedName("bill_type")
    @Expose
    private String billType;
    @SerializedName("order_type")
    @Expose
    private String orderType;
    @SerializedName("invoice_no")
    @Expose
    private String invoiceNo;
    @SerializedName("emp_name")
    @Expose
    private String empName;
    @SerializedName("store_name")
    @Expose
    private String storeName;
    @SerializedName("zone_id")
    @Expose
    private String zoneId;
    @SerializedName("due_days")
    @Expose
    private String dueDays;
    @SerializedName("discount")
    @Expose
    private String discount;
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

    public String getBillType() {
        return billType;
    }

    public void setBillType(String billType) {
        this.billType = billType;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getZoneId() {
        return zoneId;
    }

    public void setZoneId(String zoneId) {
        this.zoneId = zoneId;
    }

    public String getDueDays() {
        return dueDays;
    }

    public void setDueDays(String dueDays) {
        this.dueDays = dueDays;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
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
