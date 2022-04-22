package com.retailvend.model.delManModels.delCollection.invoiceDetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InvoiceBillDetails {
    @SerializedName("invoice_id")
    @Expose
    public String invoiceId;
    @SerializedName("bill_type")
    @Expose
    public String billType;
    @SerializedName("order_id")
    @Expose
    public String orderId;
    @SerializedName("invoice_no")
    @Expose
    public String invoiceNo;
    @SerializedName("distributor_id")
    @Expose
    public String distributorId;
    @SerializedName("vendor_id")
    @Expose
    public String vendorId;
    @SerializedName("store_id")
    @Expose
    public String storeId;
    @SerializedName("due_days")
    @Expose
    public String dueDays;
    @SerializedName("discount")
    @Expose
    public String discount;
    @SerializedName("outlet_type")
    @Expose
    public String outletType;
    @SerializedName("createdate")
    @Expose
    public String createdate;
}
