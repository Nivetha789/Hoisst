package com.retailvend.model.outlets;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductNameResData {
    @SerializedName("product_id")
    @Expose
    private String productId;
    @SerializedName("product_name")
    @Expose
    private String productName;
    @SerializedName("hsn_code")
    @Expose
    private String hsnCode;
    @SerializedName("gst_val")
    @Expose
    private String gstVal;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getHsnCode() {
        return hsnCode;
    }

    public void setHsnCode(String hsnCode) {
        this.hsnCode = hsnCode;
    }

    public String getGstVal() {
        return gstVal;
    }

    public void setGstVal(String gstVal) {
        this.gstVal = gstVal;
    }
}
