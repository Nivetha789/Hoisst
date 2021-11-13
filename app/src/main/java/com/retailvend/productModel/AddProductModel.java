package com.retailvend.productModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddProductModel {

    @SerializedName("auto_id")
    @Expose
    private int auto_id;
    @SerializedName("product_id")
    @Expose
    private String product_id;
    @SerializedName("product_name")
    @Expose
    private String product_name;
    @SerializedName("type_id")
    @Expose
    private String type_id;
    @SerializedName("unit_id")
    @Expose
    private String unit_id;
    @SerializedName("unit_val")
    @Expose
    private String unit_val;
    @SerializedName("hsn_code")
    @Expose
    private String hsn_code;
    @SerializedName("gst_val")
    @Expose
    private String gst_val;
    @SerializedName("qty")
    @Expose
    private String qty;
    @SerializedName("price")
    @Expose
    private String price;

    public AddProductModel(int autoId, String prodId, String prodName, String typeId,String unitId,String unitVal,String hsn,String gst, String qty,String price) {
        this.auto_id = autoId;
        this.product_id = prodId;
        this.product_name = prodName;
        this.type_id = typeId;
        this.unit_id = unitId;
        this.unit_val = unitVal;
        this.hsn_code = hsn;
        this.gst_val = gst;
        this.qty = qty;
        this.price = price;
    }

    public int getAuto_id() {
        return auto_id;
    }

    public void setAuto_id(int auto_id) {
        this.auto_id = auto_id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }
    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_id = product_name;
    }

    public String getType_id() {
        return type_id;
    }

    public void setType_id(String type_id) {
        this.type_id = type_id;
    }

    public String getUnit_id() {
        return unit_id;
    }

    public void setUnit_id(String unit) {
        this.unit_id = unit;
    }

    public String getUnit() {
        return unit_val;
    }

    public void setUnit(String unit) {
        this.unit_val = unit;
    }

    public String getHsn_code() {
        return hsn_code;
    }

    public void setHsn_code(String hsn_code) {
        this.hsn_code = hsn_code;
    }

    public String getGst_val() {
        return gst_val;
    }

    public void setGst_val(String gst_val) {
        this.gst_val = gst_val;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
