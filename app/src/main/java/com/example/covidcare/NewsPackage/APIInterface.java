package com.example.covidcare.NewsPackage;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface APIInterface {

    @GET("everything")
    Call<List<Headlines>> getEverything (
            @Query("q") String keyword, @Query("apiKey") String apiKey );

}
