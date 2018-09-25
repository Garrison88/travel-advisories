package com.thomas.garrison.traveladvisories

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import com.thomas.garrison.traveladvisories.database.Trip
import com.thomas.garrison.traveladvisories.ui.MainActivity.Companion.database


class TripViewModel : ViewModel() {
    private var trips: LiveData<List<Trip>>? = null
    fun getTrips(): LiveData<List<Trip>> {
        if (trips == null) {
//            trips = LiveData()
            loadTrips()
        }
        return trips as LiveData<List<Trip>>
    }

    private fun loadTrips() {
        trips = database?.tripDao()?.getAllTrips()
    }
}