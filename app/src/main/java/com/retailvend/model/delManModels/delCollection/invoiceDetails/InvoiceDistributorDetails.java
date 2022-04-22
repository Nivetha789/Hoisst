package com.retailvend.model.delManModels.delCollection.invoiceDetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InvoiceDistributorDetails {
    @SerializedName("company_name")
    @Expose
    public String companyName;
    @SerializedName("gst_no")
    @Expose
    public String gstNo;
    @SerializedName("contact_no")
    @Expose
    public String contactNo;
    @SerializedName("email")
    @Expose
    public String email;
    @SerializedName("tan_no")
    @Expose
    public String tanNo;
    @SerializedName("address")
    @Expose
    public String address;
    @SerializedName("state_name")
    @Expose
    public String stateName;
    @SerializedName("gst_code")
    @Expose
    public String gstCode;
    @SerializedName("account_name")
    @Expose
    public String accountName;
    @SerializedName("account_no")
    @Expose
    public String accountNo;
    @SerializedName("bank_name")
    @Expose
    public String bankName;
    @SerializedName("branch_name")
    @Expose
    public String branchName;
    @SerializedName("ifsc_code")
    @Expose
    public String ifscCode;
    @SerializedName("pincode")
    @Expose
    public String pincode;
}
