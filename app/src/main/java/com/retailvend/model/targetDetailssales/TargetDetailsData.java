package com.retailvend.model.targetDetailssales;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class TargetDetailsData {

    @SerializedName("target_val")
    @Expose
    private String targetVal;
    @SerializedName("achieve_val")
    @Expose
    private String achieveVal;
    @SerializedName("target_list")
    @Expose
    private List<TargetDetailsTarget> targetList = new ArrayList<>();

    public String getTargetVal() {
        return targetVal;
    }

    public void setTargetVal(String targetVal) {
        this.targetVal = targetVal;
    }

    public String getAchieveVal() {
        return achieveVal;
    }

    public void setAchieveVal(String achieveVal) {
        this.achieveVal = achieveVal;
    }

    public List<TargetDetailsTarget> getTargetList() {
        return targetList;
    }

    public void setTargetList(List<TargetDetailsTarget> targetList) {
        this.targetList = targetList;
    }
}
