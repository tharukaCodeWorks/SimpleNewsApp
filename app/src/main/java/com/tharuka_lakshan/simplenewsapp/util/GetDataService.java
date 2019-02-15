package com.tharuka_lakshan.simplenewsapp.util;



import com.tharuka_lakshan.simplenewsapp.models.MainResponceRetrofit;
import com.tharuka_lakshan.simplenewsapp.models.PostRetrofit;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GetDataService {

    @GET("top-headlines")
    Call<MainResponceRetrofit> getPost(@Query("apiKey") String api, @Query("pageSize") int page_id, @Query("page") int page, @Query("category") String category);

}
