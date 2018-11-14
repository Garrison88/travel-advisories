package com.thomas.garrison.traveladvisories.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ScruffService {

    @GET("index.json")
    fun getAllAdvisories(): Call<CountriesWithAdvisories>

    @GET("{code}/index.json")
    fun getAdvisoryByCountryCode(@Path("code") code: String): Call<Advisory>

    companion object {
        const val BASE_URL = "https://scruff.com/advisories/"
    }
}