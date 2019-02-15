package com.tharuka_lakshan.simplenewsapp.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MainResponceRetrofit {
    @SerializedName("articles")
    private List<PostRetrofit> posts;

    public List<PostRetrofit> getPosts() {
        return posts;
    }
}
