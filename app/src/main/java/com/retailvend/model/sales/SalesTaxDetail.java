package com.retailvend.model.sales;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SalesTaxDetail {
    @SerializedName("hsn_code")
    @Expose
    private String hsnCode;
    @SerializedName("gst_val")
    @Expose
    private String gstVal;
    @SerializedName("price_val")
    @Expose
    private String priceVal;

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

    public String getPriceVal() {
        return priceVal;
    }

    public void setPriceVal(String priceVal) {
        this.priceVal = priceVal;
    }
}
