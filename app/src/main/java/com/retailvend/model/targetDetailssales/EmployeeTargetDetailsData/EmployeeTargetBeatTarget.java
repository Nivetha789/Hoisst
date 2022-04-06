package com.retailvend.model.targetDetailssales.EmployeeTargetDetailsData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EmployeeTargetBeatTarget {
    @SerializedName("zone_name")
    @Expose
    private String zoneName;
    @SerializedName("beat_target_val")
    @Expose
    private String beatTargetVal;
    @SerializedName("beat_achieve_val")
    @Expose
    private String beatAchieveVal;
    @SerializedName("beat_achieve_per")
    @Expose
    private Integer beatAchievePer;

    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }

    public String getBeatTargetVal() {
        return beatTargetVal;
    }

    public void setBeatTargetVal(String beatTargetVal) {
        this.beatTargetVal = beatTargetVal;
    }

    public String getBeatAchieveVal() {
        return beatAchieveVal;
    }

    public void setBeatAchieveVal(String beatAchieveVal) {
        this.beatAchieveVal = beatAchieveVal;
    }

    public Integer getBeatAchievePer() {
        return beatAchievePer;
    }

    public void setBeatAchievePer(Integer beatAchievePer) {
        this.beatAchievePer = beatAchievePer;
    }
}
