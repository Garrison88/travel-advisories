package com.thomas.garrison.traveladvisories.api;

public class TravelAdvisory {

    public TravelAdvisory(String region_name) {
        this.region_name = region_name;
    }

//    @SerializedName("/iso_2")
    private String region_name;

    public String getRegionName() {
        return region_name;
    }

}