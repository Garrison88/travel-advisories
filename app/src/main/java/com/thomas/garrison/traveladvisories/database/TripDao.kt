package com.thomas.garrison.traveladvisories.database

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*

@Dao
interface TripDao {

    @Query("SELECT * FROM trip")
    fun getAllTrips(): LiveData<List<Trip>>

    @Update()
    fun update(trip : Trip)

    @Delete()
    fun delete(trip : Trip)

    @Insert
    fun insert(trip: Trip)
}
