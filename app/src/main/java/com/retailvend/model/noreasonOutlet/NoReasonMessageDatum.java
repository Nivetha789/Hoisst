package com.retailvend.model.noreasonOutlet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NoReasonMessageDatum {
    @SerializedName("message_id")
    @Expose
    private String messageId;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("published")
    @Expose
    private String published;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("createdate")
    @Expose
    private String createdate;

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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
