package com.retailvend.model.sales;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class SalesDetailsData {

    @SerializedName("bill_details")
    @Expose
    private SalesBillDetails billDetails;
    @SerializedName("store_details")
    @Expose
    private SalesStoreDetails storeDetails;
    @SerializedName("product_details")
    @Expose
    private List<SalesProductDetail> productDetails = new ArrayList<>();
    @SerializedName("tax_details")
    @Expose
    private List<SalesTaxDetail> taxDetails = new ArrayList<>();

    public SalesBillDetails getBillDetails() {
        return billDetails;
    }

    public void setBillDetails(SalesBillDetails billDetails) {
        this.billDetails = billDetails;
    }

    public SalesStoreDetails getStoreDetails() {
        return storeDetails;
    }

    public void setStoreDetails(SalesStoreDetails storeDetails) {
        this.storeDetails = storeDetails;
    }

    public List<SalesProductDetail> getProductDetails() {
        return productDetails;
    }

    public void setProductDetails(List<SalesProductDetail> productDetails) {
        this.productDetails = productDetails;
    }

    public List<SalesTaxDetail> getTaxDetails() {
        return taxDetails;
    }

    public void setTaxDetails(List<SalesTaxDetail> taxDetails) {
        this.taxDetails = taxDetails;
    }
}