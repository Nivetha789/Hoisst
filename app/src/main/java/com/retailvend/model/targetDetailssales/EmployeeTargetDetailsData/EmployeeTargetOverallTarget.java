package com.retailvend.model.targetDetailssales.EmployeeTargetDetailsData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EmployeeTargetOverallTarget {
    @SerializedName("overall_target_val")
    @Expose
    private String overallTargetVal;
    @SerializedName("overall_achieve_val")
    @Expose
    private String overallAchieveVal;
    @SerializedName("overall_achieve_per")
    @Expose
    private Integer overallAchievePer;

    public String getOverallTargetVal() {
        return overallTargetVal;
    }

    public void setOverallTargetVal(String overallTargetVal) {
        this.overallTargetVal = overallTargetVal;
    }

    public String getOverallAchieveVal() {
        return overallAchieveVal;
    }

    public void setOverallAchieveVal(String overallAchieveVal) {
        this.overallAchieveVal = overallAchieveVal;
    }

    public Integer getOverallAchievePer() {
        return overallAchievePer;
    }

    public void setOverallAchievePer(Integer overallAchievePer) {
        this.overallAchievePer = overallAchievePer;
    }
}
