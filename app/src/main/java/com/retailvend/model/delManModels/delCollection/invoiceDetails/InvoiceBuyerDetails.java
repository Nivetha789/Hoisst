package com.retailvend.model.delManModels.delCollection.invoiceDetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InvoiceBuyerDetails {
    @SerializedName("order_no")
    @Expose
    public String orderNo;
    @SerializedName("_ordered")
    @Expose
    public String ordered;
}
