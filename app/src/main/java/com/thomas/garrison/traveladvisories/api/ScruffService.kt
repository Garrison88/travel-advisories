package com.thomas.garrison.traveladvisories.api

import com.thomas.garrison.traveladvisories.Advisory
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ScruffService {

    @GET("index.json")
    fun advisories(): Call<CountriesWithAdvisories>

    @GET("{code}/index.json")
    fun advisoryByCode(@Path("code") code: String): Call<Advisory>

    companion object {
        const val BASE_URL = "https://scruff.com/advisories/"
    }

}