package entities;

import pojo.*;
import pojo.User;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by Filip on 9.11.2016..
 */

public class DataBuilder {
    private ApiMethods apiMethods = ApiMethods.retrofit.create(ApiMethods.class);
    private DataInterface call;
    private Object data;

    public DataBuilder(DataInterface caller) {
        call = caller;
    }

    public void login(String user){
        Call<pojo.User> retrofitCall = apiMethods.login(user);
        retrofitCall.enqueue(new Callback<pojo.User>() {
            @Override
            public void onResponse(Response<pojo.User> response, Retrofit retrofit) {
                if(response.body() != null) {
                    data = response.body();
                } else {
                    data = null;
                }
                call.buildData(data);
            }

            @Override
            public void onFailure(Throwable t) {
                data = null;
                call.buildData(data);
            }
        });
    }

    public void newUser(pojo.User user){
        Call<pojo.Response> retrofitCall = apiMethods.newUser(user);
        retrofitCall.enqueue(new Callback<pojo.Response>() {
            @Override
            public void onResponse(Response<pojo.Response> response, Retrofit retrofit) {
                call.buildData(response.body());
            }

            @Override
            public void onFailure(Throwable t) {
                call.buildData(null);
            }
        });
    }

    public void getCategories(Integer userId){
        Call<pojo.Category> retrofitCall = apiMethods.getCategories(1);
        retrofitCall.enqueue((new Callback<pojo.Category>() {
            @Override
            public void onResponse(Response<pojo.Category> response, Retrofit retrofit) {
                if(response.body() != null) {
                    data = response.body();
                } else {
                    data = null;
                }
                call.buildData(data);
            }

            @Override
            public void onFailure(Throwable t) {
                data = null;
                call.buildData(data);
            }
        }));
    }

    public void newCategory(pojo.Category category){
        Call<pojo.Category> retrofitCall = apiMethods.newCategory(category);
        retrofitCall.enqueue(new Callback<pojo.Category>() {
            @Override
            public void onResponse(Response<pojo.Category> response, Retrofit retrofit) {
                call.buildData(response.body());
            }

            @Override
            public void onFailure(Throwable t) {
                call.buildData(null);
            }
        });
    }
}
