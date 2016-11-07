package com.hr.foi.personalfinance;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

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
