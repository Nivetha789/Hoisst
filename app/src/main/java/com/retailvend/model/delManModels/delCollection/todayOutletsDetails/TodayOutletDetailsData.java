package com.retailvend.model.delManModels.delCollection.todayOutletsDetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class TodayOutletDetailsData {
    @SerializedName("bill_details")
    @Expose
    private TodayOutletDetailsBillDetails billDetails;
    @SerializedName("buyer_details")
    @Expose
    private TodayOutletDetailsBuyerDetails buyerDetails;
    @SerializedName("distributor_details")
    @Expose
    private TodayOutletDetailsDistributorDetails distributorDetails;
    @SerializedName("store_details")
    @Expose
    private TodayOutletDetailsStoreDetails storeDetails;
    @SerializedName("product_details")
    @Expose
    private List<TodayOutletDetailsProductDetail> productDetails = new ArrayList<>();
    @SerializedName("tax_details")
    @Expose
    private List<TodayOutletDetailsTaxDetail> taxDetails = new ArrayList<>();

    public TodayOutletDetailsBillDetails getBillDetails() {
        return billDetails;
    }

    public void setBillDetails(TodayOutletDetailsBillDetails billDetails) {
        this.billDetails = billDetails;
    }

    public TodayOutletDetailsBuyerDetails getBuyerDetails() {
        return buyerDetails;
    }

    public void setBuyerDetails(TodayOutletDetailsBuyerDetails buyerDetails) {
        this.buyerDetails = buyerDetails;
    }

    public TodayOutletDetailsDistributorDetails getDistributorDetails() {
        return distributorDetails;
    }

    public void setDistributorDetails(TodayOutletDetailsDistributorDetails distributorDetails) {
        this.distributorDetails = distributorDetails;
    }

    public TodayOutletDetailsStoreDetails getStoreDetails() {
        return storeDetails;
    }

    public void setStoreDetails(TodayOutletDetailsStoreDetails storeDetails) {
        this.storeDetails = storeDetails;
    }

    public List<TodayOutletDetailsProductDetail> getProductDetails() {
        return productDetails;
    }

    public void setProductDetails(List<TodayOutletDetailsProductDetail> productDetails) {
        this.productDetails = productDetails;
    }

    public List<TodayOutletDetailsTaxDetail> getTaxDetails() {
        return taxDetails;
    }

    public void setTaxDetails(List<TodayOutletDetailsTaxDetail> taxDetails) {
        this.taxDetails = taxDetails;
    }
}
