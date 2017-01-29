package core;

import android.content.SharedPreferences;
import android.provider.ContactsContract;

import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import pojo.Category;
import pojo.Category_;
import pojo.Record;
import pojo.Record_;
import pojo.Task;
import pojo.Task_;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by dagy on 29.01.17..
 */

public class DataProvider {
    private ApiMethods apiMethods = ApiMethods.retrofit.create(ApiMethods.class);
    private String userId;

    public DataProvider(){

    }

    public DataProvider(String userId){
        this.userId = userId;
    }

    public void refrashDatabase(){

        //empty local data
        Delete.table(Category_.class);
        Delete.table(Record_.class);
        Delete.table(Task_.class);
        //get new data
        fetchCategories(this.userId);
        fetchRecords(this.userId);
        fetchTasks(this.userId);
    }

    public void truncateDatabase(){
        //empty local data
        Delete.table(Category_.class);
        Delete.table(Record_.class);
        Delete.table(Task_.class);
    }

    public void fetchCategories(String userId){
        Call<pojo.Category> retrofitCall = apiMethods.getCategories(userId);
        retrofitCall.enqueue(new Callback<Category>() {
            @Override
            public void onResponse(Response<pojo.Category> response, Retrofit retrofit) {
                if (response.body() != null) {
                    pojo.Category category = response.body();

                    for(pojo.Category_ cat : category.getCategory()){
                        cat.save();
                    }

                    //TODO: remove in production
                    pojo.Category category_ = new Category();
                    category_.setCategory(SQLite.select().from(pojo.Category_.class).queryList());
                    for(Category_ cat : category_.getCategory()){
                        System.out.println("Category: " + cat.getId());
                    }
                } else {
                }
            }

            @Override
            public void onFailure(Throwable t) {
            }
        });

    }

    public void fetchRecords(String userId){
        Call<Record> retrofitCall = apiMethods.getRecords(userId);
        retrofitCall.enqueue(new Callback<Record>() {
            @Override
            public void onResponse(Response<Record> response, Retrofit retrofit) {
                if (response.body() != null) {
                    pojo.Record records = response.body();
                    for(pojo.Record_ record : records.getRecord()){
                        record.save();
                    }
                    pojo.Record record = new Record();

                    //TODO: remove in production
                    record.setRecord(SQLite.select().from(pojo.Record_.class).queryList());
                    for(Record_ rec : record.getRecord()){
                        System.out.println("Record: " + rec.getId());
                    }
                } else {
                }
            }

            @Override
            public void onFailure(Throwable t) {
            }
        });
    }

    public void fetchTasks(String userId){
        Call<pojo.Task> retrofitCall = apiMethods.getTasks(userId);
        retrofitCall.enqueue(new Callback<Task>() {
            @Override
            public void onResponse(Response<Task> response, Retrofit retrofit) {
                if (response.body() != null) {
                    pojo.Task tasks = response.body();
                    for(pojo.Task_ task : tasks.getTasks()){
                        task.save();
                    }
                    //TODO: remove in production
                    pojo.Task task = new Task();
                    task.setTasks(SQLite.select().from(pojo.Task_.class).queryList());
                    for(Task_ task_ : task.getTasks()){
                        System.out.println("Task: " + task_.getId());
                    }
                } else {
                }
            }

            @Override
            public void onFailure(Throwable t) {
            }
        });
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
