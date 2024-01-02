package com.example.musicproject.Retrofit;


import com.example.musicproject.Model.SongModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface APIinterface {

    @GET("studio")
    Call<List<SongModel>> getStudio();
}
