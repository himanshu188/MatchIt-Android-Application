package com.example.matchit.ui.gallery;

import com.google.gson.annotations.SerializedName;

public class ProfileResult {
    @SerializedName("name")
    public String name;

    @SerializedName("tag")
    public String tag;

    @SerializedName("interest")
    public String interest;

    @SerializedName("city")
    public String city;

    @SerializedName("movies")
    public String movies;

    @SerializedName("sports")
    public String sports;

    @SerializedName("food")
    public String food;

    @SerializedName("age")
    public Integer age;

    @SerializedName("social_media")
    public String social_media;

    @SerializedName("photo")
    public String photo;
}
