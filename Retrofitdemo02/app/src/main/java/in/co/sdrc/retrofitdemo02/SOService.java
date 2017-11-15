package in.co.sdrc.retrofitdemo02;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Amit Kumar Sahoo(amit@sdrc.co.in) on 13-09-2017.
 */

public interface SOService {
    @GET("/posts")
    Call<List<Example>> getList();

    @GET("/posts/{id}")
    Call<Example> getExample(@Path("id") int id);
}
