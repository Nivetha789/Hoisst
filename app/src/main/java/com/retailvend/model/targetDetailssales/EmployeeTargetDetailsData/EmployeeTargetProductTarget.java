package com.retailvend.model.targetDetailssales.EmployeeTargetDetailsData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EmployeeTargetProductTarget {
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("pdt_target_val")
    @Expose
    private String pdtTargetVal;
    @SerializedName("pdt_achieve_val")
    @Expose
    private String pdtAchieveVal;
    @SerializedName("pdt_achieve_per")
    @Expose
    private Integer pdtAchievePer;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPdtTargetVal() {
        return pdtTargetVal;
    }

    public void setPdtTargetVal(String pdtTargetVal) {
        this.pdtTargetVal = pdtTargetVal;
    }

    public String getPdtAchieveVal() {
        return pdtAchieveVal;
    }

    public void setPdtAchieveVal(String pdtAchieveVal) {
        this.pdtAchieveVal = pdtAchieveVal;
    }

    public Integer getPdtAchievePer() {
        return pdtAchievePer;
    }

    public void setPdtAchievePer(Integer pdtAchievePer) {
        this.pdtAchievePer = pdtAchievePer;
    }

}
