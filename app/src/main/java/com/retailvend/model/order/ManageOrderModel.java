package com.retailvend.model.order;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ManageOrderModel {
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private ManageOrderDataModel data;

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

    public ManageOrderDataModel getData() {
        return data;
    }

    public void setData(ManageOrderDataModel data) {
        this.data = data;
    }
}
