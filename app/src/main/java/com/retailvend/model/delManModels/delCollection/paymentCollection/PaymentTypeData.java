package com.retailvend.model.delManModels.delCollection.paymentCollection;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PaymentTypeData {
    @SerializedName("type_id")
    @Expose
    private String typeId;
    @SerializedName("type_val")
    @Expose
    private String typeVal;

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getTypeVal() {
        return typeVal;
    }

    public void setTypeVal(String typeVal) {
        this.typeVal = typeVal;
    }
}
