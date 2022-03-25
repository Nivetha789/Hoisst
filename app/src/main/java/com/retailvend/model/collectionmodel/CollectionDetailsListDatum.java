package com.retailvend.model.collectionmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CollectionDetailsListDatum {
    @SerializedName("distributor_id")
    @Expose
    private String distributorId;
    @SerializedName("distributor_name")
    @Expose
    private String distributorName;
    @SerializedName("bill_no")
    @Expose
    private String billNo;
    @SerializedName("invoice_no")
    @Expose
    private String invoiceNo;
    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("discount")
    @Expose
    private String discount;
    @SerializedName("amount_type")
    @Expose
    private String amountType;
    @SerializedName("payment_date")
    @Expose
    private String paymentDate;

    public String getDistributorId() {
        return distributorId;
    }

    public void setDistributorId(String distributorId) {
        this.distributorId = distributorId;
    }

    public String getDistributorName() {
        return distributorName;
    }

    public void setDistributorName(String distributorName) {
        this.distributorName = distributorName;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
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

    public String getAmountType() {
        return amountType;
    }

    public void setAmountType(String amountType) {
        this.amountType = amountType;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }
}
