package com.retailvend.model.collateralsmodule;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CollateralsListDatum {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("created_date")
    @Expose
    private String createdDate;
    @SerializedName("random_val")
    @Expose
    private String randomVal;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getRandomVal() {
        return randomVal;
    }

    public void setRandomVal(String randomVal) {
        this.randomVal = randomVal;
    }
}
