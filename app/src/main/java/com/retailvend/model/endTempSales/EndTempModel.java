package com.retailvend.model.endTempSales;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EndTempModel {
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private EndTempData data;

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

    public EndTempData getData() {
        return data;
    }

    public void setData(EndTempData data) {
        this.data = data;
    }

}
