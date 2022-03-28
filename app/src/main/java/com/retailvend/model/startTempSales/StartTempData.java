package com.retailvend.model.startTempSales;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StartTempData {
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("beat")
    @Expose
    private String beat;
    @SerializedName("total_outlet")
    @Expose
    private String totalOutlet;
    @SerializedName("new_outlet")
    @Expose
    private String newOutlet;
    @SerializedName("start_time")
    @Expose
    private String startTime;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getBeat() {
        return beat;
    }

    public void setBeat(String beat) {
        this.beat = beat;
    }

    public String getTotalOutlet() {
        return totalOutlet;
    }

    public void setTotalOutlet(String totalOutlet) {
        this.totalOutlet = totalOutlet;
    }

    public String getNewOutlet() {
        return newOutlet;
    }

    public void setNewOutlet(String newOutlet) {
        this.newOutlet = newOutlet;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
}