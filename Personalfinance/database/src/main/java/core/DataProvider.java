package core;

import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import pojo.Categories;
import pojo.Category;
import pojo.Category_Table;
import pojo.Record;
import pojo.Record_Table;
import pojo.Records;
import pojo.Task_Table;
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

    public void refrashDatabase(DataBuilder dataBuilder){

        //empty local data
        Delete.table(Category.class);
        Delete.table(Record.class);
        Delete.table(Task.class);
        //get new data
        fetchCategories(this.userId, dataBuilder);
        fetchRecords(this.userId, dataBuilder);
        fetchTasks(this.userId, dataBuilder);
    }

    public void truncateDatabase(){
        //empty local data
        Delete.table(Category.class);
        Delete.table(Record.class);
        Delete.table(Task.class);
    }

    public void fetchCategories(String userId, final DataBuilder dataBuilder){
        Call<Categories> retrofitCall = apiMethods.getCategories(userId);
        retrofitCall.enqueue(new Callback<Categories>() {
            @Override
            public void onResponse(Response<Categories> response, Retrofit retrofit) {
                if (response.body() != null) {
                    Categories category = response.body();

                    for(Category cat : category.getCategory()){
                        cat.save();
                    }
                    dataBuilder.dataReady(1);
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


    public void fetchRecords(String userId, final DataBuilder dataBuilder){
        Call<Records> retrofitCall = apiMethods.getRecords(userId);
        retrofitCall.enqueue(new Callback<Records>() {
            @Override
            public void onResponse(Response<Records> response, Retrofit retrofit) {
                if (response.body() != null) {
                    Records records = response.body();
                    for(Record record : records.getRecord()){
                        record.save();
                    }
                    dataBuilder.dataReady(3);
                }
            }

            @Override
            public void onFailure(Throwable t) {
            }
        });
    }

    public Object getRecords(){
        Records records = new Records();
        records.setRecord(SQLite.select().from(Record.class).queryList());
        return  records;
    }

    public void deleteRecord(String id){
        Record record = SQLite.select().from(Record.class).where(Record_Table.id.is(id)).querySingle();
        record.delete();
    }

    public void newRecord(Record record){
        record.save();
    }

    public void editRecord(Record record){
        Record rec = SQLite.select().from(Record.class).where(Record_Table.id.is(record.getId())).querySingle();
        rec.setVrsta(record.getVrsta());
        rec.setNapomena(record.getNapomena());
        rec.setDatum(record.getDatum());
        rec.setCategoryId(record.getCatgoryId());
        rec.setIznos(record.getIznos());
        rec.save();
    }

    public void fetchTasks(String userId, final DataBuilder dataBuilder){
        Call<Tasks> retrofitCall = apiMethods.getTasks(userId);
        retrofitCall.enqueue(new Callback<Tasks>() {
            @Override
            public void onResponse(Response<Tasks> response, Retrofit retrofit) {
                if (response.body() != null) {
                    Tasks tasks = response.body();
                    for(Task task : tasks.getTasks()){
                        task.save();
                    }
                    dataBuilder.dataReady(2);
                }
            }

            @Override
            public void onFailure(Throwable t) {
            }
        });
    }

    public Object getTasks(){
        Tasks tasks = new Tasks();
        tasks.setTasks(SQLite.select().from(Task.class).queryList());
        return tasks;
    }

    public void deleteTask(String id){
        Task task = SQLite.select().from(Task.class).where(Task_Table.id.is(id)).querySingle();
        task.delete();
    }

    public void newTask(Task task){
        task.setDate(task.getDate() + ":00");
        task.setNotice(task.getNotice() + ":00");
        task.save();
    }

    public void editTask(Task task){
        Task tas = SQLite.select().from(Task.class).where(Task_Table.id.is(task.getId())).querySingle();
        tas.setTitle(task.getTitle());
        tas.setNote(task.getNote());
        tas.setDate(task.getDate() + ":00");
        tas.setNotice(task.getNotice() + ":00");
        tas.save();
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
