package com.example.youtubeapp.api;

import com.example.youtubeapp.model.hintsearch.HintSearch;
import com.example.youtubeapp.model.listchannelsfromchannel.ChannelsList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiServiceHintSearch {
    Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .create();

    ApiServiceHintSearch apiListHint = new Retrofit.Builder()
            .baseUrl("http://suggestqueries.google.com/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiServiceHintSearch.class);

    @GET("complete/search")
    Call<String> listHint(
            @Query("client") String youtube,
            @Query("ds") String yt,
            @Query("client") String firefox,
            @Query("q") String q);
}
