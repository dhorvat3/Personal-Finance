package com.hr.foi.personalfinance;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import entities.DataBuilder;
import entities.DataInterface;
import pojo.User;

/**
 * Created by Valentina on 28.12.2016..
 */

public class RegisterActivity extends AppCompatActivity implements DataInterface {
    private EditText name;
    private EditText surname;
    private EditText email;
    private EditText username;
    private EditText pass;
    private Button register;
    private User user;
    private DataBuilder dataBuilder = new DataBuilder(this);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);

        name = (EditText) findViewById(R.id.name);
        surname = (EditText) findViewById(R.id.surname);
        email = (EditText) findViewById(R.id.email);
        username = (EditText) findViewById(R.id.username);
        pass = (EditText) findViewById(R.id.pass);
        register = (Button) findViewById(R.id.register);

        setListeners();
    }

    @Override
    public void buildData(Object data) {
        pojo.Response response = (pojo.Response) data;

        Toast.makeText(this, response.getResponse(), Toast.LENGTH_LONG).show();
        if(response.getId() != null){
            Log.w("user-response", response.getId());
        }
    }

    private void setListeners(){
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = new User();

                user.setName(name.getText().toString());
                user.setSurname(surname.getText().toString());
                user.setEmail(email.getText().toString());
                user.setUsername(username.getText().toString());
                user.setPassword(pass.getText().toString());

                Log.w("user", user.getName());
                Log.w("user", user.getSurname());
                Log.w("user", user.getEmail());
                Log.w("user", user.getUsername());
                Log.w("user", user.getPassword());

                dataBuilder.newUser(user);
            }
        });
    }
}
