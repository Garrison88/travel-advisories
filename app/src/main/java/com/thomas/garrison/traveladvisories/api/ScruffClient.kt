package com.thomas.garrison.traveladvisories.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ScruffClient {

    val BASE_URL = "https://aviation-safety.net/"
    var retrofit: Retrofit? = null


    fun getClient(): Retrofit? {
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
        }
        return retrofit
    }

}