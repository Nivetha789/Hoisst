package com.retailvend.productModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddProductModel {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("unit")
    @Expose
    private String unit;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("qty")
    @Expose
    private String qty;

    public AddProductModel(String name, String unit, String price, String qty) {
        this.name = name;
        this.unit = unit;
        this.price = price;
        this.qty = qty;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }
}
