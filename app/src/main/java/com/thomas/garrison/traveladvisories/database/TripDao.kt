package com.thomas.garrison.traveladvisories.database

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query

@Dao
interface TripDao {

    @Query("SELECT * FROM trip")
    fun getAllTrips(): List<Trip>

    @Insert
    fun insert(trip: Trip)
}
