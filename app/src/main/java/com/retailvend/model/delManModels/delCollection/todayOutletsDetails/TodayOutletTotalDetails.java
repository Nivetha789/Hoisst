package com.retailvend.model.delManModels.delCollection.todayOutletsDetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TodayOutletTotalDetails {
    @SerializedName("total_qty")
    @Expose
    private String totalQty;
    @SerializedName("sub_total")
    @Expose
    private String subTotal;

    public String getTotalQty() {
        return totalQty;
    }

    public void setTotalQty(String totalQty) {
        this.totalQty = totalQty;
    }

    public String getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(String subTotal) {
        this.subTotal = subTotal;
    }
}
