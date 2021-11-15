package com.retailvend.productModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddProductModel {

    @SerializedName("product_id")
    @Expose
    private String product_id;
    @SerializedName("product_name")
    @Expose
    private String product_name;
    @SerializedName("type_id")
    @Expose
    private String type_id;
    @SerializedName("unit_val")
    @Expose
    private String unit_val;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("qty")
    @Expose
    private String qty;


    public AddProductModel(String prodId, String productName,String typeId, String unitVal, String price, String qty) {
        this.product_id = prodId;
        this.product_name = productName;
        this.type_id = typeId;
        this.unit_val = unitVal;
        this.qty = qty;
        this.price = price;
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
        this.product_name = product_name;
    }

    public String getType_id() {
        return type_id;
    }

    public void setType_id(String type_id) {
        this.type_id = type_id;
    }

    public String getUnit() {
        return unit_val;
    }

    public void setUnit(String unit) {
        this.unit_val = unit;
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
