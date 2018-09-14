package com.thomas.garrison.traveladvisories

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import com.thomas.garrison.traveladvisories.database.Trip


class TripViewModel : ViewModel() {
    private var trips: MutableLiveData<List<Trip>>? = null
    fun getTrips(): LiveData<List<Trip>> {
        if (trips == null) {
            trips = MutableLiveData()
            loadTrips()
        }
        return trips as MutableLiveData<List<Trip>>
    }

    private fun loadTrips() {
//        com.thomas.garrison.traveladvisories.database.tripDao().getAllTrips()
    }
}