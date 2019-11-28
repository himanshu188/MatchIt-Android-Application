package com.example.matchit.ui.home.ui.post;

import com.google.gson.annotations.SerializedName;

public class SearchResult {
    @SerializedName("id")
    Integer id;

    @SerializedName("title")
    String title;

    @SerializedName("content")
    String content;
}

