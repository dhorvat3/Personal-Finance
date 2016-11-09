package entities;

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

    public void login(final String user, String pass){
        apiMethods.login(user, pass, new Callback<User>() {
            @Override
            public void onResponse(Response<User> response, Retrofit retrofit) {

            }

            @Override
            public void onFailure(Throwable t) {
                data = null;
                call.buildData(data);

            }
        });
    }
}
