package com.example.projectakhir_bobotek.model;

import com.google.gson.annotations.SerializedName;

public class News {
    @SerializedName("author")
    private String author;

    @SerializedName("title")
    private String title;

    @SerializedName("url")
    private String url;

    @SerializedName("publishedAt")
    private String publishedAt;

    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public String getPublishedAt() {
        return publishedAt;
    }
}
