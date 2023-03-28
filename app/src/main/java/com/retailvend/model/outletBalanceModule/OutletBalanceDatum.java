package com.retailvend.model.outletBalanceModule;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OutletBalanceDatum {
    @SerializedName("auto_id")
    @Expose
    private String autoId;
    @SerializedName("distributor_id")
    @Expose
    private String distributorId;
    @SerializedName("outlet_id")
    @Expose
    private String outletId;
    @SerializedName("bill_no")
    @Expose
    private String billNo;
    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("bal_amt")
    @Expose
    private String balAmt;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("dis_code")
    @Expose
    private String disCode;
    @SerializedName("company_name")
    @Expose
    private String companyName;
    @SerializedName("store_name")
    @Expose
    private String storeName;
    @SerializedName("sales_type")
    @Expose
    private String salesType;

    public String getAutoId() {
        return autoId;
    }

    public void setAutoId(String autoId) {
        this.autoId = autoId;
    }

    public String getDistributorId() {
        return distributorId;
    }

    public void setDistributorId(String distributorId) {
        this.distributorId = distributorId;
    }

    public String getOutletId() {
        return outletId;
    }

    public void setOutletId(String outletId) {
        this.outletId = outletId;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getBalAmt() {
        return balAmt;
    }

    public void setBalAmt(String balAmt) {
        this.balAmt = balAmt;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDisCode() {
        return disCode;
    }

    public void setDisCode(String disCode) {
        this.disCode = disCode;
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

    public String getSalesType() {
        return salesType;
    }

    public void setSalesType(String salesType) {
        this.salesType = salesType;
    }
}
