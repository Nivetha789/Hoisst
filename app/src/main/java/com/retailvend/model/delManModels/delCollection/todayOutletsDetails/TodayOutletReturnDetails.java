package com.retailvend.model.delManModels.delCollection.todayOutletsDetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TodayOutletReturnDetails {
    @SerializedName("return_total")
    @Expose
    private Integer returnTotal;

    public Integer getReturnTotal() {
        return returnTotal;
    }

    public void setReturnTotal(Integer returnTotal) {
        this.returnTotal = returnTotal;
    }
}
