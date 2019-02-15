package com.tharuka_lakshan.simplenewsapp.models;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class PostRetrofit implements Serializable {

        @SerializedName("title")
        private String title;

        @SerializedName("author")
        private String author;

        @SerializedName("publishedAt")
        private String date;

        @SerializedName("urlToImage")
        private String image;

        @SerializedName("url")
        private String url;

        @SerializedName("content")
        private String content;

        @SerializedName("description")
        private String description;

        public String getTitle() {
            return title;
        }

        public String getAuthor() {
            return author;
        }

        public String getDate() {
            return date;
        }

        public String getImage() {
            return image;
        }

        public String getUrl() {
            return url;
        }

        public String getContent() {
            return content;
        }

        public String getDescription() {
            return description;
        }
}
