package com.retailvend.model.delManModels.delCollection.invoiceDetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
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
    public List<InvoiceProductDetail> productDetails = new ArrayList<>();
    @SerializedName("tax_details")
    @Expose
    public List<InvoiceTaxDetail> taxDetails = new ArrayList<>();
    @SerializedName("total_details")
    @Expose
    public InvoiceTotalDetails totalDetails;
    @SerializedName("return_details")
    @Expose
    public InvoiceReturnDetails returnDetails;
    @SerializedName("print_invoice")
    @Expose
    private String printInvoice;

    public InvoiceBillDetails getBillDetails() {
        return billDetails;
    }

    public void setBillDetails(InvoiceBillDetails billDetails) {
        this.billDetails = billDetails;
    }

    public InvoiceBuyerDetails getBuyerDetails() {
        return buyerDetails;
    }

    public void setBuyerDetails(InvoiceBuyerDetails buyerDetails) {
        this.buyerDetails = buyerDetails;
    }

    public InvoiceDistributorDetails getDistributorDetails() {
        return distributorDetails;
    }

    public void setDistributorDetails(InvoiceDistributorDetails distributorDetails) {
        this.distributorDetails = distributorDetails;
    }

    public InvoiceStoreDetails getStoreDetails() {
        return storeDetails;
    }

    public void setStoreDetails(InvoiceStoreDetails storeDetails) {
        this.storeDetails = storeDetails;
    }

    public List<InvoiceProductDetail> getProductDetails() {
        return productDetails;
    }

    public void setProductDetails(List<InvoiceProductDetail> productDetails) {
        this.productDetails = productDetails;
    }

    public List<InvoiceTaxDetail> getTaxDetails() {
        return taxDetails;
    }

    public void setTaxDetails(List<InvoiceTaxDetail> taxDetails) {
        this.taxDetails = taxDetails;
    }

    public InvoiceTotalDetails getTotalDetails() {
        return totalDetails;
    }

    public void setTotalDetails(InvoiceTotalDetails totalDetails) {
        this.totalDetails = totalDetails;
    }

    public InvoiceReturnDetails getReturnDetails() {
        return returnDetails;
    }

    public void setReturnDetails(InvoiceReturnDetails returnDetails) {
        this.returnDetails = returnDetails;
    }

    public String getPrintInvoice() {
        return printInvoice;
    }

    public void setPrintInvoice(String printInvoice) {
        this.printInvoice = printInvoice;
    }
}
