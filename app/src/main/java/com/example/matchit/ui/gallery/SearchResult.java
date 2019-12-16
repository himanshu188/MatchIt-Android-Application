package com.example.matchit.ui.gallery;

import com.google.gson.annotations.SerializedName;

// This is the structure where the post content will be stored
public class SearchResult {
    @SerializedName("id")
    Integer id;

    @SerializedName("title")
    String title;

    @SerializedName("content")
    String content;

    @SerializedName("photo")
    String photo;
}

