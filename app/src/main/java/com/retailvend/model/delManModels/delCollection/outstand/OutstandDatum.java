package com.retailvend.model.delManModels.delCollection.outstand;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OutstandDatum {

    @SerializedName("assign_id")
    @Expose
    private String assignId;
    @SerializedName("distributor_id")
    @Expose
    private String distributorId;
    @SerializedName("outlet_id")
    @Expose
    private String outletId;
    @SerializedName("outlet_name")
    @Expose
    private String outletName;
    @SerializedName("pre_bal")
    @Expose
    private String preBal;
    @SerializedName("cur_bal")
    @Expose
    private String curBal;
    @SerializedName("update_date")
    @Expose
    private String updateDate;

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

    public String getOutletName() {
        return outletName;
    }

    public void setOutletName(String outletName) {
        this.outletName = outletName;
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

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }
}
