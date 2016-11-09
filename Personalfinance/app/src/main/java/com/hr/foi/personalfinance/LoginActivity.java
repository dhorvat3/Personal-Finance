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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.login_layout);

        loginButton = (Button) findViewById(R.id.login);


        super.onCreate(savedInstanceState);

        FlowManager.init(new FlowConfig.Builder(this).build());

        setListeners();
    }

    private void setListeners(){
        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);

            }
        });
    }
}
