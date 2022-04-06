package com.retailvend.model.targetDetailssales.EmployeeTargetDetailsData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class EmployeeTargetDetailsDatum {

    @SerializedName("overall_target")
    @Expose
    private EmployeeTargetOverallTarget overallTarget;
    @SerializedName("product_target")
    @Expose
    private List<EmployeeTargetProductTarget> productTarget = new ArrayList<>();
    @SerializedName("beat_target")
    @Expose
    private List<EmployeeTargetBeatTarget> beatTarget = new ArrayList<>();

    public EmployeeTargetOverallTarget getOverallTarget() {
        return overallTarget;
    }

    public void setOverallTarget(EmployeeTargetOverallTarget overallTarget) {
        this.overallTarget = overallTarget;
    }

    public List<EmployeeTargetProductTarget> getProductTarget() {
        return productTarget;
    }

    public void setProductTarget(List<EmployeeTargetProductTarget> productTarget) {
        this.productTarget = productTarget;
    }

    public List<EmployeeTargetBeatTarget> getBeatTarget() {
        return beatTarget;
    }

    public void setBeatTarget(List<EmployeeTargetBeatTarget> beatTarget) {
        this.beatTarget = beatTarget;
    }
}
