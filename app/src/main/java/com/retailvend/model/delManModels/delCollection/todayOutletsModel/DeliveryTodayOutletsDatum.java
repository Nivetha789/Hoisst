package com.retailvend.model.delManModels.delCollection.todayOutletsModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeliveryTodayOutletsDatum {
    @SerializedName("invoice_id")
    @Expose
    private String invoiceId;
    @SerializedName("invoice_no")
    @Expose
    private String invoiceNo;
    @SerializedName("distributor_id")
    @Expose
    private String distributorId;
    @SerializedName("vendor_id")
    @Expose
    private String vendorId;
    @SerializedName("company_name")
    @Expose
    private String companyName;
    @SerializedName("store_name")
    @Expose
    private String storeName;
    @SerializedName("random_value")
    @Expose
    private String randomValue;
    @SerializedName("createdate")
    @Expose
    private String createdate;

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
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

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getRandomValue() {
        return randomValue;
    }

    public void setRandomValue(String randomValue) {
        this.randomValue = randomValue;
    }

    public String getCreatedate() {
        return createdate;
    }

    public void setCreatedate(String createdate) {
        this.createdate = createdate;
    }
}
