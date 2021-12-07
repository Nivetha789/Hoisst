package com.retailvend.model.delManModels.delCollection.invoiceHistory;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InvoiceHistoryDatum {

    @SerializedName("bill_no")
    @Expose
    private String billNo;
    @SerializedName("pre_bal")
    @Expose
    private String preBal;
    @SerializedName("cur_bal")
    @Expose
    private String curBal;
    @SerializedName("date")
    @Expose
    private String date;

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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}