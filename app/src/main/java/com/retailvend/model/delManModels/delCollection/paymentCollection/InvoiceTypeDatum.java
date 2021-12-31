package com.retailvend.model.delManModels.delCollection.paymentCollection;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InvoiceTypeDatum {
    @SerializedName("pay_id")
    @Expose
    private String payId="";
    @SerializedName("bill_id")
    @Expose
    private String billId="";
    @SerializedName("bill_no")
    @Expose
    private String billNo="";
    @SerializedName("pre_bal")
    @Expose
    private String preBal="";
    @SerializedName("cur_bal")
    @Expose
    private String curBal="";
    @SerializedName("amount")
    @Expose
    private String amount="";


    public String getPayId() {
        return payId;
    }

    public void setPayId(String payId) {
        this.payId = payId;
    }

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getPreBal() {
        return preBal;
    }

    public void setPreBal(String preBal) {
        this.preBal = preBal;
    }

    public String getCurBal() {
        return curBal;
    }

    public void setCurBal(String curBal) {
        this.curBal = curBal;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
