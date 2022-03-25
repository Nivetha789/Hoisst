package com.retailvend.model.outlets.outletHistory;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OutletHisPaymentData {
    @SerializedName("payment_id")
    @Expose
    private String paymentId;
    @SerializedName("distributor_name")
    @Expose
    private String distributorName;
    @SerializedName("bill_code")
    @Expose
    private String billCode;
    @SerializedName("bill_no")
    @Expose
    private String billNo;
    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("discount")
    @Expose
    private String discount;
    @SerializedName("amt_type")
    @Expose
    private String amtType;
    @SerializedName("collection_type")
    @Expose
    private String collectionType;
    @SerializedName("date")
    @Expose
    private String date;

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getDistributorName() {
        return distributorName;
    }

    public void setDistributorName(String distributorName) {
        this.distributorName = distributorName;
    }

    public String getBillCode() {
        return billCode;
    }

    public void setBillCode(String billCode) {
        this.billCode = billCode;
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

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getAmtType() {
        return amtType;
    }

    public void setAmtType(String amtType) {
        this.amtType = amtType;
    }

    public String getCollectionType() {
        return collectionType;
    }

    public void setCollectionType(String collectionType) {
        this.collectionType = collectionType;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
