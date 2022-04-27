package com.retailvend.model.outlets.outletHistory;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OutletHisInvoiceData {
    @SerializedName("invoice_no")
    @Expose
    private String invoiceNo;
    @SerializedName("distributor_name")
    @Expose
    private String distributorName;
    @SerializedName("delivery_employee")
    @Expose
    private String deliveryEmployee;
    @SerializedName("invoice_date")
    @Expose
    private String invoiceDate;
    @SerializedName("delivery_date")
    @Expose
    private String deliveryDate;

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getDistributorName() {
        return distributorName;
    }

    public void setDistributorName(String distributorName) {
        this.distributorName = distributorName;
    }

    public String getDeliveryEmployee() {
        return deliveryEmployee;
    }

    public void setDeliveryEmployee(String deliveryEmployee) {
        this.deliveryEmployee = deliveryEmployee;
    }

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }
}
