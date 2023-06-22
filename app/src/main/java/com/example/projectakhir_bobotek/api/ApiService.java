package com.example.projectakhir_bobotek.api;

import com.example.projectakhir_bobotek.model.News;
import com.example.projectakhir_bobotek.model.NewsRespopnse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    @GET("top-headlines?country=id&category=health&apiKey=afb66cb11b7d45729907d6926c4920b3")
    Call<NewsRespopnse> getNews();
}
