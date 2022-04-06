package com.retailvend.model.targetDetailssales.EmployeeTargetDetailsData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class EmployeeTargetDetailsModel {
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private List<EmployeeTargetDetailsDatum> data = new ArrayList<>();

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

    public List<EmployeeTargetDetailsDatum> getData() {
        return data;
    }

    public void setData(List<EmployeeTargetDetailsDatum> data) {
        this.data = data;
    }
}
