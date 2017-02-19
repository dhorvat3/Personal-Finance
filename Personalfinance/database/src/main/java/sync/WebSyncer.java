package sync;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.raizlabs.android.dbflow.sql.language.Method;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import core.ApiMethods;
import pojo.Categories;
import pojo.Category;
import pojo.Category_Table;
import pojo.Record;
import pojo.Record_Table;
import pojo.Records;
import pojo.Task;
import pojo.Task_Table;
import pojo.Tasks;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by dagy on 13.02.17..
 */

public class WebSyncer implements SyncInterface {
    private String id;
    private Context context;
    private ApiMethods apiMethods = ApiMethods.retrofit.create(ApiMethods.class);

    @Override
    public boolean syncData(Context context, String id) {
        this.id = id;
        this.context = context;

        pullCategories();
        pullRecords();
        pullTasks();

        return false;
    }

    private void pullTasks(){
        Call<Tasks> retrofitCall = apiMethods.getEditedTasks(this.id, getLastEditedTask());
        retrofitCall.enqueue(new Callback<Tasks>() {
            @Override
            public void onResponse(Response<Tasks> response, Retrofit retrofit) {
                Tasks tasks = response.body();

                System.out.println(tasks.getTasks().get(0).getId());
                if(!tasks.getTasks().get(0).getId().equals("-1")){
                    Log.w("SYNC Task", "Server ahead");
                    for(Task task : tasks.getTasks()){
                        Log.w("fetched-task", task.getId());
                        if(SQLite.select(Method.count()).from(Task.class).where(Task_Table.id.is(task.getId())).count() > 0){
                            Task localTask = SQLite.select().from(Task.class).where(Task_Table.id.is(task.getId())).querySingle();
                            Log.w("pulled-task", task.getId());
                            localTask.setUserId(task.getUserId());
                            localTask.setLastEdited(task.getLastEdited());
                            localTask.setAktivan(task.getAktivan());
                            localTask.setNotice(task.getNotice());
                            localTask.setDate(task.getDate());
                            localTask.setNote(task.getNote());
                            localTask.setTitle(task.getTitle());
                            localTask.setId(task.getId());
                            localTask.save();
                        } else {
                            task.save();
                        }
                    }
                } else if(tasks.getTasks().get(0).getId().equals("-1")){
                    String lastEdited = tasks.getTasks().get(0).getLastEdited();
                    Log.w("SYNC Task", "Client is ahead " + lastEdited);
                    pushTasks(lastEdited);
                } else {
                    Log.w("SYNC Task", "Server and client synced");
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    private void pushTasks(String lastEdited){
        Tasks tasks = new Tasks();
        tasks.setTasks(SQLite.select().from(Task.class).where(Task_Table.lastEdited.greaterThan(lastEdited)).queryList());
        for(Task task : tasks.getTasks()){
            if(task.getId() == null){
                System.out.println("New task: " + task.getLocalId());
                newTask(task);
            } else if(task.getEdited() == 1){
                System.out.println("Edited task: " + task.getLocalId());
                editTask(task);
            } else if(task.getAktivan().equals("0")){
                System.out.println("Deleted task: " + task.getLocalId());
                deleteTask(task.getId());
            }
        }
    }

    private void newTask(final Task task){
        Call<pojo.Response> retrofitCall = apiMethods.newTask(task);
        retrofitCall.enqueue(new Callback<pojo.Response>() {
            @Override
            public void onResponse(Response<pojo.Response> response, Retrofit retrofit) {
                pojo.Response resp = response.body();
                Log.w("Pushed new task", resp.getResponse()+", "+resp.getId());
                task.setId(resp.getId());
                task.save();
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    private void editTask(final Task task){
        Call<pojo.Response> retrofitCall = apiMethods.editTask(task);
        retrofitCall.enqueue(new Callback<pojo.Response>() {
            @Override
            public void onResponse(Response<pojo.Response> response, Retrofit retrofit) {
                pojo.Response resp = response.body();
                Log.w("Pushed edit task", resp.getResponse()+", "+resp.getId());
                task.setEdited(0);
                task.save();
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    private void deleteTask(String id){
        Call<pojo.Response> retrofitCall = apiMethods.deleteTask(id);
        retrofitCall.enqueue(new Callback<pojo.Response>() {
            @Override
            public void onResponse(Response<pojo.Response> response, Retrofit retrofit) {
                pojo.Response resp = response.body();
                Log.w("Pushed delete task", resp.getResponse()+", "+resp.getId());
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    private void pullRecords(){
        Call<Records> retrofitCall = apiMethods.getEditedRecords(this.id, getLastEditedRecord());
        retrofitCall.enqueue(new Callback<Records>() {
            @Override
            public void onResponse(Response<Records> response, Retrofit retrofit) {
                Records records = response.body();

                if(!records.getRecord().get(0).getId().equals("-1")){
                    Log.w("SYNC Record", "Server is ahead");
                    for(Record record : records.getRecord()){
                        Log.w("fetched-record", record.getId());
                        if(SQLite.select(Method.count()).from(Record.class).where(Record_Table.id.is(record.getId())).count() > 0){
                            Record rec = SQLite.select().from(Record.class).where(Record_Table.id.is(record.getId())).querySingle();
                            Log.w("pulled-record", rec.getId());
                            rec.setUserId(record.getUserId());
                            rec.setVrsta(record.getVrsta());
                            rec.setAktivan(record.getAktivan());
                            rec.setCategoryId(record.getCategoryId());
                            rec.setLastEdited(record.getLastEdited());
                            rec.setDatum(record.getDatum());
                            rec.setIznos(record.getIznos());
                            rec.setNapomena(record.getNapomena());
                            rec.setId(rec.getId());
                            rec.save();
                        } else {
                            record.save();
                        }
                    }
                } else if(records.getRecord().get(0).getId().equals("-1")){
                    String lastEdited = records.getRecord().get(0).getLastEdited();
                    Log.w("SYNC Record", "Client is ahead" + lastEdited);

                    pushRecords(lastEdited);
                } else {
                    Log.w("Sync Record", "Server and client synced");
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    private void pushRecords(String lastEdited){
        Records records = new Records();

        records.setRecord(SQLite.select().from(Record.class).where(Record_Table.lastEdited.greaterThan(lastEdited)).queryList());
        for(Record record : records.getRecord()){
            if(record.getId() == null){
                System.out.println("New record: " + record.getLocalId());
                newRecord(record);
            } else if(record.getEdited() == 1){
                System.out.println("Edited record: " + record.getLocalId());
                editRecord(record);
            } else if(record.getAktivan().equals("0")){
                System.out.println("Deleted record: " + record.getLocalId());
                deleteRecord(record.getId());
            }
        }
    }

    private void newRecord(final Record record){
        Call<pojo.Response> retrofitCall = apiMethods.newRecord(record);
        retrofitCall.enqueue(new Callback<pojo.Response>() {
            @Override
            public void onResponse(Response<pojo.Response> response, Retrofit retrofit) {
                pojo.Response resp = response.body();
                Log.w("Pushed new record", resp.getResponse()+", "+resp.getId());
                record.setId(resp.getId());
                record.save();
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    private void editRecord(final Record record){
        Call<pojo.Response> retrofitCall = apiMethods.editRecord(record);
        retrofitCall.enqueue(new Callback<pojo.Response>() {
            @Override
            public void onResponse(Response<pojo.Response> response, Retrofit retrofit) {
                pojo.Response resp = response.body();
                Log.w("Pushed edit record", resp.getResponse()+", "+resp.getId());
                record.setEdited(0);
                record.save();
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    private void deleteRecord(String id){
        Call<pojo.Response> retrofitCall = apiMethods.deleteRecord(id);
        retrofitCall.enqueue(new Callback<pojo.Response>() {
            @Override
            public void onResponse(Response<pojo.Response> response, Retrofit retrofit) {
                pojo.Response resp = response.body();
                Log.w("Pushed delete record", resp.getResponse()+", "+resp.getId());
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    /**
     *
     */
    private void pullCategories(){
        Call<pojo.Categories> retrofitCall = apiMethods.getEditedCategories(this.id, getLastEditedCategory());
        retrofitCall.enqueue(new Callback<Categories>() {
            @Override
            public void onResponse(Response<pojo.Categories> response, Retrofit retrofit) {
                pojo.Categories categories = response.body();

                System.out.println(categories.getCategory().get(0).getId());
                if(!categories.getCategory().get(0).getId().equals("-1")) {
                    Log.w("SYNC Cat", "Server is ahead");
                    for (pojo.Category cat : categories.getCategory()) {
                        Log.w("fetched-cat", cat.getId());
                        //check if exitst
                        if (SQLite.select(Method.count()).from(pojo.Category.class).where(Category_Table.id.is(cat.getId())).count() > 0) {
                            //Update existing row
                            pojo.Category category = SQLite.select().from(pojo.Category.class).where(Category_Table.id.is(cat.getId())).querySingle();
                            Log.w("pulled-cat", category.getId());
                            category.setUserId(cat.getUserId());
                            category.setTitle(cat.getTitle());
                            category.setDescription(cat.getDescription());
                            category.setActive(cat.getActive());
                            category.setCategoryId(cat.getCategoryId());
                            category.setLastEdited(cat.getLastEdited());
                            category.setId(cat.getId());
                            category.save();
                        } else {
                            //Add new row
                            cat.save();
                        }
                    }
                } else if (categories.getCategory().get(0).getId().equals("-1")) {
                    String lastEdited = categories.getCategory().get(0).getLastEdited();

                    Log.w("SYNC Cat", "Client is ahead " + lastEdited);
                    pushCategories(lastEdited);
                } else {
                    Log.w("SYNC Cat", "Server and client synced");
                }
            }
            @Override
            public void onFailure(Throwable t) {
            }
        });
    }

    private void pushCategories(String lastEdited){
        //get categories edited after last edit
        Categories categories = new Categories();

        categories.setCategory(SQLite.select().from(Category.class).where(Category_Table.lastEdited.greaterThan(lastEdited)).queryList());
        for(Category category : categories.getCategory()){
            //get status
            if(category.getId() == null){
                System.out.println("New cat: " + category.getLocalId());
                newCategory(category);
            } else if(category.getEdited() == 1){
                System.out.println("Edited cat: " + category.getLocalId());
                editCategory(category);
            } else if(category.getActive().equals("0")){
                System.out.println("Deleted cat: " + category.getLocalId());
                deleteCategory(category.getId());
            }
        }
    }

    private void newCategory(final Category category){
        Call<pojo.Response> retrofitCall = apiMethods.newCategory(category);
        retrofitCall.enqueue(new Callback<pojo.Response>() {
            @Override
            public void onResponse(Response<pojo.Response> response, Retrofit retrofit) {
                pojo.Response resp = response.body();
                Log.w("Pushed new category", resp.getResponse()+", "+resp.getId());
                category.setId(resp.getId());
                category.save();
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    private void editCategory(final Category category){
        Call<pojo.Response> retrofitCall = apiMethods.editCategory(category);
        retrofitCall.enqueue(new Callback<pojo.Response>() {
            @Override
            public void onResponse(Response<pojo.Response> response, Retrofit retrofit) {
                pojo.Response resp = response.body();
                Log.w("Pushed edit category", resp.getResponse()+", "+resp.getId());
                category.setEdited(0);
                category.save();
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    private void deleteCategory(String id){
        Call<pojo.Response> restrofitCall = apiMethods.deleteCategory(id, this.id);
        restrofitCall.enqueue(new Callback<pojo.Response>() {
            @Override
            public void onResponse(Response<pojo.Response> response, Retrofit retrofit) {
                pojo.Response resp = response.body();
                Log.w("Pushed delete category", resp.getResponse()+", "+resp.getId());
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    /**
     *
     * @return
     */
    public String getLastEditedCategory(){
        String lastEdited;
        Categories categories = new Categories();

        if(SQLite.select().from(Category.class).orderBy(Category_Table.lastEdited, false).queryList().size() > 0) {
            categories.setCategory(SQLite.select().from(Category.class).orderBy(Category_Table.lastEdited, false).queryList());
            lastEdited = categories.getCategory().get(0).getLastEdited();
            System.out.println("Last edited category date: " + lastEdited);
        } else {
            lastEdited = "0";
        }

        return lastEdited;
    }

    public String getLastEditedRecord(){
        String lastEdited;
        Records records = new Records();

        if(SQLite.select().from(Record.class).orderBy(Record_Table.lastEdited, false).queryList().size() > 0) {
            records.setRecord(SQLite.select().from(Record.class).orderBy(Record_Table.lastEdited, false).queryList());
            lastEdited = records.getRecord().get(0).getLastEdited();
            System.out.println("Last edited record date: " + lastEdited);
        } else {
            lastEdited = "0";
        }

        return  lastEdited;
    }

    public String getLastEditedTask(){
        String lastEdited;
        Tasks tasks = new Tasks();

        if(SQLite.select().from(Task.class).orderBy(Task_Table.lastEdited, false).queryList().size() > 0) {
            tasks.setTasks(SQLite.select().from(Task.class).orderBy(Task_Table.lastEdited, false).queryList());
            lastEdited = tasks.getTasks().get(0).getLastEdited();
            System.out.println("Last edited task date: " + lastEdited);
        } else {
            lastEdited = "0";
        }

        return lastEdited;
    }
}
