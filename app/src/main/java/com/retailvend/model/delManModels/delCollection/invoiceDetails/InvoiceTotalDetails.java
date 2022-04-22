package com.retailvend.model.delManModels.delCollection.invoiceDetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InvoiceTotalDetails {
    @SerializedName("total_qty")
    @Expose
    public String totalQty;
    @SerializedName("sub_total")
    @Expose
    public String subTotal;
}
