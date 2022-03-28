package com.retailvend.model.endTempSales;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class EndTempData {

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
    @SerializedName("old_outlet")
    @Expose
    private String oldOutlet;
    @SerializedName("order_count")
    @Expose
    private String orderCount;
    @SerializedName("order_total")
    @Expose
    private String orderTotal;
    @SerializedName("close_time")
    @Expose
    private String closeTime;
    @SerializedName("outlet_list")
    @Expose
    private List<Object> outletList = new ArrayList<>();

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

    public String getOldOutlet() {
        return oldOutlet;
    }

    public void setOldOutlet(String oldOutlet) {
        this.oldOutlet = oldOutlet;
    }

    public String getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(String orderCount) {
        this.orderCount = orderCount;
    }

    public String getOrderTotal() {
        return orderTotal;
    }

    public void setOrderTotal(String orderTotal) {
        this.orderTotal = orderTotal;
    }

    public String getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(String closeTime) {
        this.closeTime = closeTime;
    }

    public List<Object> getOutletList() {
        return outletList;
    }

    public void setOutletList(List<Object> outletList) {
        this.outletList = outletList;
    }
}
