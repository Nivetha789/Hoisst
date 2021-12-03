package com.retailvend.model.delManModels.delCollection.todayOutletsDetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TodayOutletDetailsModel {
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private TodayOutletDetailsData data;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public TodayOutletDetailsData getData() {
        return data;
    }

    public void setData(TodayOutletDetailsData data) {
        this.data = data;
    }
}
