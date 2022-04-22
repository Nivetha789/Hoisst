package com.retailvend.model.delManModels.delCollection.invoiceDetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InvoiceReturnDetails {

    @SerializedName("return_total")
    @Expose
    public Integer returnTotal;
}
