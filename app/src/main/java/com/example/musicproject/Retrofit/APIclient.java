package com.example.musicproject.Retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIclient {

    private static Retrofit retrofit;

    public static final String BASE_URL = "http://starlord.hackerearth.com";

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create()) //buat convert json to java object
                    .build();
        }
        return retrofit;
    }
    //api client done
}
