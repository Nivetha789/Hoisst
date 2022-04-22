package com.retailvend.model.delManModels.delCollection.invoiceDetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InvoiceProductDetail {
    @SerializedName("auto_id")
    @Expose
    public String autoId;
    @SerializedName("product_id")
    @Expose
    public String productId;
    @SerializedName("product_name")
    @Expose
    public String productName;
    @SerializedName("type_id")
    @Expose
    public String typeId;
    @SerializedName("type_name")
    @Expose
    public String typeName;
    @SerializedName("description")
    @Expose
    public String description;
    @SerializedName("hsn_code")
    @Expose
    public String hsnCode;
    @SerializedName("gst_val")
    @Expose
    public String gstVal;
    @SerializedName("unit_val")
    @Expose
    public String unitVal;
    @SerializedName("unit_name")
    @Expose
    public String unitName;
    @SerializedName("price")
    @Expose
    public String price;
    @SerializedName("order_qty")
    @Expose
    public String orderQty;
}
