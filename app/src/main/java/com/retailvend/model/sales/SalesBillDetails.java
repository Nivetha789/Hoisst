package com.retailvend.model.sales;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SalesBillDetails {

    @SerializedName("order_id")
    @Expose
    private String orderId;
    @SerializedName("order_no")
    @Expose
    private String orderNo;
    @SerializedName("emp_name")
    @Expose
    private String empName;
    @SerializedName("store_name")
    @Expose
    private String storeName;
    @SerializedName("contact_name")
    @Expose
    private String contactName;
    @SerializedName("order_status")
    @Expose
    private String orderStatus;
    @SerializedName("_ordered")
    @Expose
    private String ordered;
    @SerializedName("_processing")
    @Expose
    private String processing;
    @SerializedName("_shiped")
    @Expose
    private String shiped;
    @SerializedName("_canceled")
    @Expose
    private String canceled;
    @SerializedName("_delivery")
    @Expose
    private String delivery;
    @SerializedName("published")
    @Expose
    private String published;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("createdate")
    @Expose
    private String createdate;

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

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
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

    public String getProcessing() {
        return processing;
    }

    public void setProcessing(String processing) {
        this.processing = processing;
    }

    public String getShiped() {
        return shiped;
    }

    public void setShiped(String shiped) {
        this.shiped = shiped;
    }

    public String getCanceled() {
        return canceled;
    }

    public void setCanceled(String canceled) {
        this.canceled = canceled;
    }

    public String getDelivery() {
        return delivery;
    }

    public void setDelivery(String delivery) {
        this.delivery = delivery;
    }

    public String getPublished() {
        return published;
    }

    public void setPublished(String published) {
        this.published = published;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedate() {
        return createdate;
    }

    public void setCreatedate(String createdate) {
        this.createdate = createdate;
    }
}
