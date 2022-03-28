package com.retailvend.model.targetDetailssales;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TargetDetailsModel {
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private TargetDetailsData data;

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

    public TargetDetailsData getData() {
        return data;
    }

    public void setData(TargetDetailsData data) {
        this.data = data;
    }
}
