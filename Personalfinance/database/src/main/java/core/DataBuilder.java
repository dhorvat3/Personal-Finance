package core;

import pojo.*;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by Filip on 9.11.2016..
 */

/**
 * Klasa koja poziva potrebne metode za dohvat objekata iz baze podataka
 */
public class DataBuilder {
    /**
     * PHP skripte web servisa
     */
    private ApiMethods apiMethods = ApiMethods.retrofit.create(ApiMethods.class);

    /**
     * Sucelje za poziv metoda
     */
    private DataInterface call;

    /**
     * Za pohranjivanje odgovora web servisa
     */
    private Object data;

    /**
     * sadrzi metode za dohvacanje svih tipova objekata iz baze podataka
     */
    private DataProvider dataProvider;

    /**
     * Pozivatelj metoda
     */
    private DataBuilder dataBuilder;

    /**
     * Kontrolne zastavice
     */
    private boolean catReady = false, taskReady = false, recordReady = false;

    /**
     * Konstruktor
     * @param caller
     */
    public DataBuilder(DataInterface caller) {
        call = caller;
        dataProvider = new DataProvider();
        dataBuilder = this;
    }

    /**
     * Provjera zastavica
     * @param type
     */
    public void dataReady(int type){
        if(type == 1){
            catReady = true;
        }
        if(type == 2){
            taskReady = true;
        }
        if(type == 3){
            recordReady = true;
        }

        if(catReady && taskReady && recordReady){
            call.buildData(data);
        }
    }

    /**
     * Metoda za osvježavanje lokalne baze
     * @param id
     */
    public void refrashDatabase(String id){
        dataProvider.setUserId(id);
        pojo.Response response = new pojo.Response();
        response.setId("1");
        data = response;
        dataProvider.refrashDatabase(dataBuilder);
    }

    /**
     * Metoda za prijavu na sustav
     * @param user Korisnicko ime
     * @param pass Korisnicka lozinka
     */
    public void login(String user, String pass){
        Call<pojo.User> retrofitCall = apiMethods.login(user, pass);
        retrofitCall.enqueue(new Callback<pojo.User>() {
            @Override
            public void onResponse(Response<pojo.User> response, Retrofit retrofit) {
                if(response.body() != null) {
                    data = response.body();
                    pojo.User user = response.body();
                    dataProvider.setUserId(user.getId());
                    dataProvider.refrashDatabase(dataBuilder);
                } else {
                    data = null;
                }
                //call.buildData(data);
            }

            @Override
            public void onFailure(Throwable t) {
                data = null;
                call.buildData(data);
            }
        });
    }

    /**
     * Metoda za registraciju novog korisnika u sustav
     * @param user Objekt tipa User
     */
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

    /**
     * Metoda za azuriranje korisnika
     * @param user Objekt tipa User
     */
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

    /**
     * Metoda za dohvacanje korisnickih kategorija
     * @param userId Korisnicki user_id atribut
     */
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

    /**
     * Metoda za dodavanje nove korisnicke kategorije
     * @param category Objekt tipa Category
     */
    public void newCategory(final Category category){
        Call<pojo.Response> retrofitCall = apiMethods.newCategory(category);
        retrofitCall.enqueue(new Callback<pojo.Response>() {
            @Override
            public void onResponse(Response<pojo.Response> response, Retrofit retrofit) {
                pojo.Response resp = response.body();
                if(Integer.parseInt(resp.getId()) > 0){
                    category.setId(resp.getId());
                    dataProvider.newCategory(category);
                }

                call.buildData(response.body());
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    /**
     * Metoda za dohvacanje korisnickih zapisa
     * @param userId Korisnicki user_id atribut
     */
    public void getRecords(String userId){
        Object data = dataProvider.getRecords();
        pojo.Response response = new pojo.Response();
        if(data != null){
            call.buildData(data);
        } else {
            response.setId("-1");
            call.buildData(response);
        }
    }

    /**
     * Metoda za dodavanje novog zapisa
     * @param record Objekt tipa Record
     */
    public void newRecord(final Record record){
        Call<pojo.Response> retrofitCall = apiMethods.newRecord(record);
        retrofitCall.enqueue(new Callback<pojo.Response>() {
            @Override
            public void onResponse(Response<pojo.Response> response, Retrofit retrofit) {
                pojo.Response resp = response.body();
                if(resp.getId().equals("1")){
                    dataProvider.newRecord(record);
                }

                call.buildData(response.body());
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    /**
     * Metoda za azuriranje korisnickih zapisa
     * @param record Objekt tipa Record
     */
    public void editRecord(final Record record){
        Call<pojo.Response> retrofitCall = apiMethods.editRecord(record);
        retrofitCall.enqueue(new Callback<pojo.Response>() {
            @Override
            public void onResponse(Response<pojo.Response> response, Retrofit retrofit) {
                pojo.Response resp = response.body();
                if(resp.getId().equals("1")){
                    dataProvider.editRecord(record);
                }

                call.buildData(response.body());
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    /**
     * Metoda za brisanje korisnickih zapisa
     * @param id id zapisa
     */
    public void deleteRecord(final String id){
        Call<pojo.Response> retrofitCall = apiMethods.deleteRecord(id);
        retrofitCall.enqueue(new Callback<pojo.Response>() {
            @Override
            public void onResponse(Response<pojo.Response> response, Retrofit retrofit) {
                pojo.Response resp = response.body();
                if(resp.getId().equals("1")){
                    dataProvider.deleteRecord(id);
                }

                call.buildData(response.body());
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    /**
     * Metoda za dodavanje nove obveze
     * @param task Objekt tipa Task
     */
    public void newTask(final Task task){
        Call<pojo.Response> retrofitCall = apiMethods.newTask(task);
        retrofitCall.enqueue(new Callback<pojo.Response>() {
            @Override
            public void onResponse(Response<pojo.Response> response, Retrofit retrofit) {
                pojo.Response resp = response.body();
                int id = Integer.parseInt(resp.getId());

                if(id > 0){
                    dataProvider.newTask(task);
                }

                call.buildData(response.body());
            }

            @Override
            public void onFailure(Throwable t) {
                call.buildData(null);
            }
        });
    }

    /**
     * Metoda za dohvacanje korisnickih obveza
     * @param userId Korisnicki user_id atribut
     */
    public void getTasks(String userId){
        Object data = dataProvider.getTasks();
        pojo.Response response = new pojo.Response();
        if(data != null){
            call.buildData(data);
        } else {
            response.setId("-1");
            call.buildData(response);
        }
    }

    /**
     * Metoda za azuriranje korisnickih obveza
     * @param task Objekt tipa Task
     */
    public void editTask(final Task task){
        Call<pojo.Response> retrofitCall = apiMethods.editTask(task);
        retrofitCall.enqueue(new Callback<pojo.Response>() {
            @Override
            public void onResponse(Response<pojo.Response> response, Retrofit retrofit) {
                pojo.Response resp = response.body();
                if(resp.getId().equals("1")){
                    dataProvider.editTask(task);
                }

                call.buildData(response.body());
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    /**
     * Metoda za brisanje korisnickih obveza
     * @param id id obveze
     */
    public void deleteTask(final String id){
        Call<pojo.Response> restrofitCall = apiMethods.deleteTask(id);
        restrofitCall.enqueue(new Callback<pojo.Response>() {
            @Override
            public void onResponse(Response<pojo.Response> response, Retrofit retrofit) {
                pojo.Response resp = response.body();
                if(resp.getId().equals("1")){
                    dataProvider.deleteTask(id);
                }

                call.buildData(response.body());
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    /**
     * Metoda za azuriranje korisnickih kategorija
     * @param category Objekt tipa Category
     */
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

    /**
     * Metoda za brisanje korisnickih kategorija
     * @param id id kategorije
     * @param user_id Korisnicki user_id atribut
     */
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
