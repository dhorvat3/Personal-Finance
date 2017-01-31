package core;

import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import pojo.Categories;
import pojo.Category;
import pojo.Category_Table;
import pojo.Record;
import pojo.Records;
import pojo.Tasks;
import pojo.Task;
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
        Delete.table(Category.class);
        Delete.table(Record.class);
        Delete.table(Task.class);
        //get new data
        fetchCategories(this.userId);
        fetchRecords(this.userId);
        fetchTasks(this.userId);
    }

    public void truncateDatabase(){
        //empty local data
        Delete.table(Category.class);
        Delete.table(Record.class);
        Delete.table(Task.class);
    }

    public void fetchCategories(String userId){
        Call<Categories> retrofitCall = apiMethods.getCategories(userId);
        retrofitCall.enqueue(new Callback<Categories>() {
            @Override
            public void onResponse(Response<Categories> response, Retrofit retrofit) {
                if (response.body() != null) {
                    Categories category = response.body();

                    for(Category cat : category.getCategory()){
                        cat.save();
                    }

                    //TODO: remove in production
                    Categories category_ = new Categories();
                    category_.setCategory(SQLite.select().from(Category.class).queryList());
                    for(Category cat : category_.getCategory()){
                        System.out.println("Categories: " + cat.getId());
                    }
                } else {
                }
            }

            @Override
            public void onFailure(Throwable t) {
            }
        });
    }

    public Object getCategories(){
        Categories categories = new Categories();
        categories.setCategory(SQLite.select().from(Category.class).queryList());

        return categories;
    }

    public void deleteCategory(String id){
        Category category = SQLite.select().from(Category.class).where(Category_Table.id.is(id)).querySingle();
        category.delete();
        //TODO: remove in production
        Categories category_ = new Categories();
        category_.setCategory(SQLite.select().from(Category.class).queryList());
        for(Category cat : category_.getCategory()){
            System.out.println("Categories: " + cat.getId());
        }
    }

    public void newCategory(Category category){
        category.save();
    }

    public void editCategory(Category category){
        Category cat = SQLite.select().from(Category.class).where(Category_Table.id.is(category.getId())).querySingle();

        cat.setDescription(category.getDescription());
        cat.setTitle(category.getTitle());
        cat.save();
    }


    public void fetchRecords(String userId){
        Call<Records> retrofitCall = apiMethods.getRecords(userId);
        retrofitCall.enqueue(new Callback<Records>() {
            @Override
            public void onResponse(Response<Records> response, Retrofit retrofit) {
                if (response.body() != null) {
                    Records records = response.body();
                    for(Record record : records.getRecord()){
                        record.save();
                    }
                    Records record = new Records();

                    //TODO: remove in production
                    record.setRecord(SQLite.select().from(Record.class).queryList());
                    for(Record rec : record.getRecord()){
                        System.out.println("Records: " + rec.getId());
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
        Call<Tasks> retrofitCall = apiMethods.getTasks(userId);
        retrofitCall.enqueue(new Callback<Tasks>() {
            @Override
            public void onResponse(Response<Tasks> response, Retrofit retrofit) {
                if (response.body() != null) {
                    Tasks tasks = response.body();
                    for(Task task : tasks.getTasks()){
                        task.save();
                    }
                    //TODO: remove in production
                    Tasks task = new Tasks();
                    task.setTasks(SQLite.select().from(Task.class).queryList());
                    for(Task task_ : task.getTasks()){
                        System.out.println("Tasks: " + task_.getId());
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
