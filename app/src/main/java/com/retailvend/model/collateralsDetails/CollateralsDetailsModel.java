package com.retailvend.model.collateralsDetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CollateralsDetailsModel {
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private CollateralsDetailsData data;

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

    public CollateralsDetailsData getData() {
        return data;
    }

    public void setData(CollateralsDetailsData data) {
        this.data = data;
    }
}
