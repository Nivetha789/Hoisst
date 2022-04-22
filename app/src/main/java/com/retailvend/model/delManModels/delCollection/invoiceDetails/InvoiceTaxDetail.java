package com.retailvend.model.delManModels.delCollection.invoiceDetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InvoiceTaxDetail {
    @SerializedName("hsn_code")
    @Expose
    public String hsnCode;
    @SerializedName("gst_val")
    @Expose
    public String gstVal;
    @SerializedName("gst_value")
    @Expose
    public String gstValue;
    @SerializedName("price_value")
    @Expose
    public String priceValue;
}
