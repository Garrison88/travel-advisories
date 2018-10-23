package com.thomas.garrison.traveladvisories.api

import com.google.gson.annotations.SerializedName

class CountriesWithAdvisories(
    @SerializedName("Africa")
    val africa: List<String>,
    @SerializedName("Asia")
    val asia: List<String>,
    @SerializedName("Latin America & Caribbean")
    val latinAmericaAndCaribbean: List<String>,
    @SerializedName("Oceania")
    val oceania: List<String>,
    @SerializedName("Europe")
    val europe: List<String>
)