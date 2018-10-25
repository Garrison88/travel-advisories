package com.thomas.garrison.traveladvisories

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel

class AdvisoryViewModel : ViewModel() {
    private var advisories: LiveData<List<Advisory>>? = null
    fun getAdvisories(): LiveData<List<Advisory>> {
        if (advisories == null) {
            loadAdvisories()
        }
        return advisories as LiveData<List<Advisory>>
    }

    private fun loadAdvisories() {
//        trips = MainActivity.database?.tripDao()?.getAllTrips()
    }
}