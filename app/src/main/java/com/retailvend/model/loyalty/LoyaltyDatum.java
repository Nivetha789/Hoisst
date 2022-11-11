package com.retailvend.model.loyalty;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class LoyaltyDatum implements Serializable{
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("inv_count")
    @Expose
    private String invCount;
    @SerializedName("dis_value")
    @Expose
    private String disValue;
    @SerializedName("date")
    @Expose
    private String date;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInvCount() {
        return invCount;
    }

    public void setInvCount(String invCount) {
        this.invCount = invCount;
    }

    public String getDisValue() {
        return disValue;
    }

    public void setDisValue(String disValue) {
        this.disValue = disValue;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
