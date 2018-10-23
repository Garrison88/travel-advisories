package com.thomas.garrison.traveladvisories.database

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
data class Trip(
    @PrimaryKey(autoGenerate = true)
    val tid: Long,
    val country: String = "",
    val countryCode: String = "",
    val startDate: String = "",
    val endDate: String = "",
    val hasAdvisory: Boolean
)