package com.retailvend.model.delManModels.delCollection.todayOutletsDetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TodayOutletDetailsBillDetails {
    @SerializedName("invoice_id")
    @Expose
    private String invoiceId;
    @SerializedName("bill_type")
    @Expose
    private String billType;
    @SerializedName("order_id")
    @Expose
    private String orderId;
    @SerializedName("invoice_no")
    @Expose
    private String invoiceNo;
    @SerializedName("distributor_id")
    @Expose
    private String distributorId;
    @SerializedName("vendor_id")
    @Expose
    private String vendorId;
    @SerializedName("store_id")
    @Expose
    private String storeId;
    @SerializedName("due_days")
    @Expose
    private String dueDays;
    @SerializedName("discount")
    @Expose
    private String discount;
    @SerializedName("outlet_type")
    @Expose
    private String outletType;
    @SerializedName("createdate")
    @Expose
    private String createdate;

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getBillType() {
        return billType;
    }

    public void setBillType(String billType) {
        this.billType = billType;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getDistributorId() {
        return distributorId;
    }

    public void setDistributorId(String distributorId) {
        this.distributorId = distributorId;
    }

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
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

    public String getOutletType() {
        return outletType;
    }

    public void setOutletType(String outletType) {
        this.outletType = outletType;
    }

    public String getCreatedate() {
        return createdate;
    }

    public void setCreatedate(String createdate) {
        this.createdate = createdate;
    }
}
