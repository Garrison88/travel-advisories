package com.thomas.garrison.traveladvisories.database

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.support.annotation.NonNull

@Entity
data class Trip(
    @PrimaryKey(autoGenerate = true)
    val tid: Long,
    val country: String = "",
    val startDate: String = "",
    val endDate: String = ""
)