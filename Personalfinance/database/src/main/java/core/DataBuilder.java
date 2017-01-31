package core;

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
    private DataProvider dataProvider;

    public DataBuilder(DataInterface caller) {
        call = caller;
        dataProvider = new DataProvider();
    }

    public void login(String user, String pass){
        Call<pojo.User> retrofitCall = apiMethods.login(user, pass);
        retrofitCall.enqueue(new Callback<pojo.User>() {
            @Override
            public void onResponse(Response<pojo.User> response, Retrofit retrofit) {
                if(response.body() != null) {
                    data = response.body();
                    pojo.User user = response.body();
                    dataProvider.setUserId(user.getId());
                    dataProvider.refrashDatabase();
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
        Object data = dataProvider.getCategories();
        pojo.Response response = new pojo.Response();
        if(data != null){
            call.buildData(data);
        } else {
            response.setId("-1");
            call.buildData(response);
        }
    }

    public void newCategory(final Category category){
        Call<pojo.Response> retrofitCall = apiMethods.newCategory(category);
        retrofitCall.enqueue(new Callback<pojo.Response>() {
            @Override
            public void onResponse(Response<pojo.Response> response, Retrofit retrofit) {
                pojo.Response resp = response.body();
                if(resp.getId().equals("1")){
                    dataProvider.newCategory(category);
                }

                call.buildData(response.body());
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    public void getRecords(String userId){
        Call<Records> retrofitCall = apiMethods.getRecords(userId);
        retrofitCall.enqueue(new Callback<Records>() {
            @Override
            public void onResponse(Response<Records> response, Retrofit retrofit) {
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

    public void newRecord(Record record){
        Call<pojo.Response> retrofitCall = apiMethods.newRecord(record);
        retrofitCall.enqueue(new Callback<pojo.Response>() {
            @Override
            public void onResponse(Response<pojo.Response> response, Retrofit retrofit) {
                call.buildData(response.body());
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    public void editRecord(Record record){
        Call<pojo.Response> retrofitCall = apiMethods.editRecord(record);
        retrofitCall.enqueue(new Callback<pojo.Response>() {
            @Override
            public void onResponse(Response<pojo.Response> response, Retrofit retrofit) {
                call.buildData(response.body());
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    public void deleteRecord(String id){
        Call<pojo.Response> retrofitCall = apiMethods.deleteRecord(id);
        retrofitCall.enqueue(new Callback<pojo.Response>() {
            @Override
            public void onResponse(Response<pojo.Response> response, Retrofit retrofit) {
                call.buildData(response.body());
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    public void newTask(Task task){
        Call<pojo.Response> retrofitCall = apiMethods.newTask(task);
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

    public void getTasks(String userId){
        Call<Tasks> retrofitCall = apiMethods.getTasks(userId);
        retrofitCall.enqueue(new Callback<Tasks>() {
            @Override
            public void onResponse(Response<Tasks> response, Retrofit retrofit) {
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

    public void editTask(Task task){
        Call<pojo.Response> retrofitCall = apiMethods.editTask(task);
        retrofitCall.enqueue(new Callback<pojo.Response>() {
            @Override
            public void onResponse(Response<pojo.Response> response, Retrofit retrofit) {
                call.buildData(response.body());
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    public void deleteTask(String id){
        Call<pojo.Response> restrofitCall = apiMethods.deleteTask(id);
        restrofitCall.enqueue(new Callback<pojo.Response>() {
            @Override
            public void onResponse(Response<pojo.Response> response, Retrofit retrofit) {
                call.buildData(response.body());
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    public void editCategory(final Category category){
        Call<pojo.Response> retrofitCall = apiMethods.editCategory(category);
        retrofitCall.enqueue(new Callback<pojo.Response>() {
            @Override
            public void onResponse(Response<pojo.Response> response, Retrofit retrofit) {
                pojo.Response resp = response.body();

                if(resp.getId().equals("1")){
                    dataProvider.editCategory(category);
                }

                call.buildData(response.body());
            }

            @Override
            public void onFailure(Throwable t) {
                pojo.Response response = new pojo.Response();
                response.setId("-1");
                call.buildData(response);
            }
        });
    }

    public void deleteCategory(final String id, String user_id){
        Call<pojo.Response> retrofitCall = apiMethods.deleteCategory(id, user_id);
        retrofitCall.enqueue(new Callback<pojo.Response>() {
            @Override
            public void onResponse(Response<pojo.Response> response, Retrofit retrofit) {
                pojo.Response resp = response.body();

                if(resp.getId().equals("1")){
                    dataProvider.deleteCategory(id);
                }

                call.buildData(response.body());
            }

            @Override
            public void onFailure(Throwable t) {
                pojo.Response response = new pojo.Response();
                response.setId("-1");
                call.buildData(response);
            }
        });
    }
}
