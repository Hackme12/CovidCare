package com.example.covidcare.NewsPackage;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
///////////aca29107206e402b84441499b1505d74
public class Client {

    private static final String URL= "https://newsapi.org/v2/";

    private static Retrofit retrofit;
   // private static Client client;


    public static Retrofit getApiClient(){
        if(retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit;
    }


}
