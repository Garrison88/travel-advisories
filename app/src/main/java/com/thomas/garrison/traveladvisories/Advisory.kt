package com.thomas.garrison.traveladvisories

import android.arch.persistence.room.Entity

@Entity
data class Advisory (
        val country: String = "",
        val countryCode: String = ""
)