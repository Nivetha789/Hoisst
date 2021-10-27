package com.retailvend.model.sales;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SalesDetailsModel {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private SalesDetailsData data;

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

    public SalesDetailsData getData() {
        return data;
    }

    public void setData(SalesDetailsData data) {
        this.data = data;
    }
}
