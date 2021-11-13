package com.retailvend.model.outlets;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SalesAgentData {
    @SerializedName("agents_id")
    @Expose
    private String agentsId;
    @SerializedName("company_name")
    @Expose
    private String companyName;
    @SerializedName("contact_name")
    @Expose
    private String contactName;
    @SerializedName("mobile")
    @Expose
    private String mobile;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("gst_no")
    @Expose
    private String gstNo;
    @SerializedName("pan_no")
    @Expose
    private String panNo;
    @SerializedName("published")
    @Expose
    private String published;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("createdate")
    @Expose
    private String createdate;

    public String getAgentsId() {
        return agentsId;
    }

    public void setAgentsId(String agentsId) {
        this.agentsId = agentsId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGstNo() {
        return gstNo;
    }

    public void setGstNo(String gstNo) {
        this.gstNo = gstNo;
    }

    public String getPanNo() {
        return panNo;
    }

    public void setPanNo(String panNo) {
        this.panNo = panNo;
    }

    public String getPublished() {
        return published;
    }

    public void setPublished(String published) {
        this.published = published;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedate() {
        return createdate;
    }

    public void setCreatedate(String createdate) {
        this.createdate = createdate;
    }
}
