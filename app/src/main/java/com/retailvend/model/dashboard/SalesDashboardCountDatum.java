package com.retailvend.model.dashboard;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SalesDashboardCountDatum {
    @SerializedName("total_invoice")
    @Expose
    private String totalInvoice;
    @SerializedName("visit_invoice")
    @Expose
    private String visitInvoice;
    @SerializedName("pending_invoice")
    @Expose
    private String pendingInvoice;
    @SerializedName("total_outlet")
    @Expose
    private String totalOutlet;
    @SerializedName("visit_outlet")
    @Expose
    private String visitOutlet;
    @SerializedName("pending_outlet")
    @Expose
    private String pendingOutlet;
    @SerializedName("target_value")
    @Expose
    private String targetValue;
    @SerializedName("achievement")
    @Expose
    private String achievement;
    @SerializedName("order_count")
    @Expose
    private String orderCount;
    @SerializedName("order_total")
    @Expose
    private String orderTotal;

    public String getTotalInvoice() {
        return totalInvoice;
    }

    public void setTotalInvoice(String totalInvoice) {
        this.totalInvoice = totalInvoice;
    }

    public String getVisitInvoice() {
        return visitInvoice;
    }

    public void setVisitInvoice(String visitInvoice) {
        this.visitInvoice = visitInvoice;
    }

    public String getPendingInvoice() {
        return pendingInvoice;
    }

    public void setPendingInvoice(String pendingInvoice) {
        this.pendingInvoice = pendingInvoice;
    }

    public String getTotalOutlet() {
        return totalOutlet;
    }

    public void setTotalOutlet(String totalOutlet) {
        this.totalOutlet = totalOutlet;
    }

    public String getVisitOutlet() {
        return visitOutlet;
    }

    public void setVisitOutlet(String visitOutlet) {
        this.visitOutlet = visitOutlet;
    }

    public String getPendingOutlet() {
        return pendingOutlet;
    }

    public void setPendingOutlet(String pendingOutlet) {
        this.pendingOutlet = pendingOutlet;
    }

    public String getTargetValue() {
        return targetValue;
    }

    public void setTargetValue(String targetValue) {
        this.targetValue = targetValue;
    }

    public String getAchievement() {
        return achievement;
    }

    public void setAchievement(String achievement) {
        this.achievement = achievement;
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
}
