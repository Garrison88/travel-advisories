package com.thomas.garrison.traveladvisories

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.thomas.garrison.traveladvisories.MainActivity.Companion.database
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
        trips = database?.tripDao()?.getAllTrips() as MutableLiveData<List<Trip>>?
    }
}

//class WordViewModel(application: Application) : AndroidViewModel(application) {
//
////    private val mRepository: TrRepository
//
//    internal val allWords: LiveData<List<Trip>>
//
//    init {
////        mRepository = WordRepository(application)
//        allWords = mRepository.getAllWords()
//    }
//
//    fun insert(word: Word) {
//        mRepository.insert(word)
//    }
//}