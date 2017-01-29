package entities;

import pojo.*;
import retrofit.Call;
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
    Call<pojo.User> login(@Query("username") String username, @Query("pass") String pass);

    @POST("/new_user.php")
    Call<pojo.Response> newUser(@Body pojo.User user);
    
    @POST("/edit_user.php")
    Call<pojo.Response> editUser(@Body pojo.User user);

    @GET("/category_by_user.php/")
    Call<pojo.Category> getCategories(@Query("id") String userId);

    @POST("/add_category.php")
    Call<Category_> newCategory(@Body Category_ category);

    @GET("/records_by_user.php/")
    Call<pojo.Record> getRecords(@Query("id") String userId);

    @POST("/new_record.php")
    Call<pojo.Response> newRecord(@Body pojo.Record_ record);

    @GET("/delete_record.php")
    Call<pojo.Response> deleteRecord(@Query("id") String recordId);

    @POST("/edit_record.php")
    Call<pojo.Response> editRecord(@Body pojo.Record_ record);

    @POST("/new_task.php")
    Call<pojo.Response> newTask(@Body pojo.Task_ task);

    @GET("/tasks_by_user.php")
    Call<pojo.Task> getTasks(@Query("id") String userId);

    @GET("/delete_task.php")
    Call<pojo.Response> deleteTask(@Query("id") String id);

    @POST("/edit_task.php")
    Call<pojo.Response> editTask(@Body pojo.Task_ task);

    @POST("/edit_category.php")
    Call<pojo.Response> editCategory(@Body pojo.Category_ category);

    @GET("/delete_category.php")
    Call<pojo.Response> deleteCategory(@Query("id") String id ,@Query("user_id") String user_id);


    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://finance2016.000webhostapp.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

}
