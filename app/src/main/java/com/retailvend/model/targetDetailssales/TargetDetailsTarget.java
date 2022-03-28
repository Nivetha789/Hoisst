package com.retailvend.model.targetDetailssales;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TargetDetailsTarget {
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("value")
    @Expose
    private Integer value;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
