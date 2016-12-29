package entities;

import pojo.*;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 * Created by Filip on 9.11.2016..
 */

public interface ApiMethods {
    @GET("/user_by_name.php/")
    //void login(@Query("username") String username,  Callback<pojo.User> callback);
    Call<pojo.User> login(@Query("username") String username);

    @POST("/new_user.php")
    Call<pojo.Response> newUser(@Body pojo.User user);

    @GET("/category_by_user.php/")
    Call<pojo.Category> getCategories(@Query("id") Integer userId);

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://finance2015.3eeweb.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

}
