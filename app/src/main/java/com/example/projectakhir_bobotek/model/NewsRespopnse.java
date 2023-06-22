package com.example.projectakhir_bobotek.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NewsRespopnse {
    @SerializedName("status")
    private String status;

    @SerializedName("totalResults")
    private String totalResults;

    @SerializedName("articles")
    private List<News> articles;

    public List<News> getArticles() {
        return articles;
    }

    public String getStatus() {
        return status;
    }
}
