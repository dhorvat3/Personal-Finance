package com.hr.foi.personalfinance;

import android.content.Intent;
import android.icu.text.SymbolTable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.List;

import entities.User;
import helper.MockData;

/**
 * Created by dagy on 06.11.16..
 */

public class LoginActivity extends AppCompatActivity {
    private Button loginButton;
    private EditText korime;
    private EditText lozinka;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.login_layout);

        loginButton = (Button) findViewById(R.id.login);
        korime = (EditText) findViewById(R.id.korime);
        lozinka = (EditText) findViewById(R.id.lozinka);

        super.onCreate(savedInstanceState);

        FlowManager.init(new FlowConfig.Builder(this).build());

        setListeners();
    }

    private void setListeners(){
        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(SQLite.select().from(User.class).queryList().isEmpty()){
                    MockData.writeAll();
                }
                List<User> users = SQLite.select().from(User.class).queryList();
                for (User user : users){
                   if (korime.getText().toString().equals(user.getKorime()) && lozinka.getText().toString().equals(user.getLozinka())){
                       Toast.makeText(getApplicationContext(), "Uspjesno!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    }
                   else {
                       Toast.makeText(getApplicationContext(), "Krivo!", Toast.LENGTH_SHORT).show();
                    }
               }

            }
        });
    }
}
