package com.thomas.garrison.traveladvisories

import com.google.gson.annotations.SerializedName

class Advisory (
        @SerializedName("region_name")
            val country: String,
        @SerializedName("iso_2")
            val countryCode: String,
        @SerializedName("legal_code_body")
            val comments: String
)