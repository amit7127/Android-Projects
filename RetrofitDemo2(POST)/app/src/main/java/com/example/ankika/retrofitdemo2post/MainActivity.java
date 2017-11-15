package com.example.ankika.retrofitdemo2post;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DataPost dataPost = new DataPost();
        dataPost.setBody("Amit");
        dataPost.setTitle("001");
        dataPost.setUserId(1);

        ServerController serverController = new ServerController();
        serverController.startService(dataPost);

    }
}
