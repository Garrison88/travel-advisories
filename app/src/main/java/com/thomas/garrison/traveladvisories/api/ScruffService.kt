package com.thomas.garrison.traveladvisories.api

import retrofit2.Call
import retrofit2.http.GET

interface ScruffService {

    @get:GET("index.json")
    val advisories: Call<CountriesWithAdvisories>

    companion object {
        const val BASE_URL = "https://scruff.com/advisories/"
    }

}