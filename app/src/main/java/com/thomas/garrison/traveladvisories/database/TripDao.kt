package com.thomas.garrison.traveladvisories.database

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*

@Dao
interface TripDao {

    @Query("SELECT * FROM trip")
    fun getAllTrips(): LiveData<List<Trip>>

    @Update()
    fun updateTrip(trip : Trip)

    @Delete()
    fun deleteTrip(trip : Trip)

    @Insert
    fun insertTrip(trip: Trip)

}
