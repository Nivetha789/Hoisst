package com.retailvend.model.createOutSales.collectionModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CollectionResDatum {
    @SerializedName("payment_id")
    @Expose
    private String paymentId;
    @SerializedName("assign_id")
    @Expose
    private String assignId;
    @SerializedName("distributor_id")
    @Expose
    private String distributorId;
    @SerializedName("outlet_id")
    @Expose
    private String outletId;
    @SerializedName("company_name")
    @Expose
    private String companyName;
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

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getAssignId() {
        return assignId;
    }

    public void setAssignId(String assignId) {
        this.assignId = assignId;
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

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
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
}
