package com.example.ankika.retrofitdemo2post;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Ankika on 9/23/2017.
 */

public interface APIService {
    @POST("/posts")
    Call<Post> savePost(@Body DataPost post);
}
