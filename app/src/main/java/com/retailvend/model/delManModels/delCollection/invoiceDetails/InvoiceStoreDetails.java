package com.retailvend.model.delManModels.delCollection.invoiceDetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InvoiceStoreDetails {
    @SerializedName("company_name")
    @Expose
    public String companyName;
    @SerializedName("contact_name")
    @Expose
    public String contactName;
    @SerializedName("mobile")
    @Expose
    public String mobile;
    @SerializedName("email")
    @Expose
    public String email;
    @SerializedName("gst_no")
    @Expose
    public String gstNo;
    @SerializedName("address")
    @Expose
    public String address;
    @SerializedName("country_id")
    @Expose
    public String countryId;
    @SerializedName("state_id")
    @Expose
    public String stateId;
    @SerializedName("city_id")
    @Expose
    public String cityId;
    @SerializedName("zone_id")
    @Expose
    public String zoneId;
    @SerializedName("state_name")
    @Expose
    public String stateName;
    @SerializedName("gst_code")
    @Expose
    public String gstCode;
    @SerializedName("outlet_type")
    @Expose
    public String outletType;
    @SerializedName("pincode")
    @Expose
    public String pincode;
}
