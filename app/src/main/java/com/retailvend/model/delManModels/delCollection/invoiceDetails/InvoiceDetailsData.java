package com.retailvend.model.delManModels.delCollection.invoiceDetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class InvoiceDetailsData {

    @SerializedName("bill_details")
    @Expose
    public InvoiceBillDetails billDetails;
    @SerializedName("buyer_details")
    @Expose
    public InvoiceBuyerDetails buyerDetails;
    @SerializedName("distributor_details")
    @Expose
    public InvoiceDistributorDetails distributorDetails;
    @SerializedName("store_details")
    @Expose
    public InvoiceStoreDetails storeDetails;
    @SerializedName("product_details")
    @Expose
    public List<InvoiceProductDetail> productDetails = null;
    @SerializedName("tax_details")
    @Expose
    public List<InvoiceTaxDetail> taxDetails = null;
    @SerializedName("total_details")
    @Expose
    public InvoiceTotalDetails totalDetails;
    @SerializedName("return_details")
    @Expose
    public InvoiceReturnDetails returnDetails;
}
