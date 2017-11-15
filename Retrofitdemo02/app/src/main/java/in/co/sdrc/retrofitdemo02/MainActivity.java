package in.co.sdrc.retrofitdemo02;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
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

        String BASE_URL = "https://jsonplaceholder.typicode.com";
        SOService service;
        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(1000, TimeUnit.SECONDS)
                .connectTimeout(1000, TimeUnit.SECONDS)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        service = retrofit.create(SOService.class);
        Call<List<Example>> listCall = service.getList();
        listCall.enqueue(new Callback<List<Example>>() {
            @Override
            public void onResponse(Call<List<Example>> call, Response<List<Example>> response) {
                if (response.code()  == 200){
                    Log.d("data",response.toString());
                }
            }

            @Override
            public void onFailure(Call<List<Example>> call, Throwable t) {

            }
        });

        OkHttpClient client1 = new OkHttpClient.Builder()
                .readTimeout(1000, TimeUnit.SECONDS)
                .connectTimeout(1000, TimeUnit.SECONDS)
                .build();
        Retrofit retrofit1 = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client1)
                .build();

        SOService service1 = retrofit1.create(SOService.class);
        Call<Example> call = service1.getExample(1);
        call.enqueue(new Callback<Example>() {
            @Override
            public void onResponse(Call<Example> call, Response<Example> response) {
                if (response.code()  == 200){
                    Log.d("data",response.toString());
                }
            }

            @Override
            public void onFailure(Call<Example> call, Throwable t) {

            }
        });

    }
}
