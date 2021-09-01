package com.retailvend.model.outlets;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddAttendanceModel {
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private AddAttendanceData data;

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

    public AddAttendanceData getData() {
        return data;
    }

    public void setData(AddAttendanceData data) {
        this.data = data;
    }

}
