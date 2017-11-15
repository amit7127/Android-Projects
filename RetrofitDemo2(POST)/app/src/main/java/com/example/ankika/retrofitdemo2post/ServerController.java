package com.example.ankika.retrofitdemo2post;

import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Ankika on 9/23/2017.
 */

public class ServerController {

    public void startService(DataPost dataPost){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://jsonplaceholder.typicode.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        APIService gerritAPI = retrofit.create(APIService.class);

        Call<Post> call = gerritAPI.savePost(dataPost);

        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                if (response.code()  == 200){
                    Log.d("data",response.toString());
                }
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                Log.d("data",t.toString());
            }
        });

    }

}
