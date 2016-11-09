package entities;

import retrofit.Callback;
import retrofit.Retrofit;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by Filip on 9.11.2016..
 */

public interface ApiMethods {
    @GET("/login.php")
    void login(@Query("username") String username, @Query("password") String password, Callback<User> callback);

    Retrofit retrofit = new Retrofit.Builder().baseUrl("http://cortex.foi.hr/mtl/courses/air/").build();

}
