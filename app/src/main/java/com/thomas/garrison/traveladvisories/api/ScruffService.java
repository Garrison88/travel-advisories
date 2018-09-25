package com.thomas.garrison.traveladvisories.api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ScruffService {

    String BASE_URL = "https://www.scruff.com/gaytravel/";

    @GET("advisories/index")
    Call<List<TravelAdvisory>> getAdvisories();

}