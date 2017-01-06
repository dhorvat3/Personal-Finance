package entities;

import pojo.*;
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

    public void editUser(pojo.User user){
        Call<pojo.Response> retrofitCall = apiMethods.editUser(user);
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

    public void getCategories(String userId) {

        Call<pojo.Category> retrofitCall = apiMethods.getCategories(userId);
        retrofitCall.enqueue(new Callback<Category>() {
            @Override
            public void onResponse(Response<pojo.Category> response, Retrofit retrofit) {
                if (response.body() != null) {
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

    public void newCategory(Category_ category){
        Call<Category_> retrofitCall = apiMethods.newCategory(category);
        retrofitCall.enqueue(new Callback<Category_>() {
            @Override
            public void onResponse(Response<Category_> response, Retrofit retrofit) {
                call.buildData(response.body());
            }

            @Override
            public void onFailure(Throwable t) {
                call.buildData(null);
            }
        });
    }

    public void getRecords(String userId){
        Call<pojo.Record> retrofitCall = apiMethods.getRecords(userId);
        retrofitCall.enqueue(new Callback<Record>() {
            @Override
            public void onResponse(Response<Record> response, Retrofit retrofit) {
                if (response.body() != null) {
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

    public void newRecord(Record_ record){
        Call<pojo.Response> retrofitCall = apiMethods.newRecord(record);
        retrofitCall.enqueue(new Callback<pojo.Response>() {
            @Override
            public void onResponse(Response<pojo.Response> response, Retrofit retrofit) {

            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }
}
