package com.hr.foi.personalfinance;

import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;

import core.DataBuilder;
import core.DataInterface;
import pojo.Response;
import pojo.User;

/**
 * Created by dagy on 06.11.16..
 */

/**
 * Klasa LoginActivity za realizaciju prijave u sustav
 */
public class LoginActivity extends AppCompatActivity implements DataInterface{
    private Button loginButton, registerButton;
    private EditText korime, lozinka;
    private DataBuilder dataBulder = new DataBuilder(this);
    private SharedPreferences prefs; //= this.getSharedPreferences("login", 0);
    private SharedPreferences.Editor editor;// = prefs.edit();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.login_layout);

        prefs = this.getSharedPreferences("login", 0);
        editor = prefs.edit();

        loginButton = (Button) findViewById(R.id.login);
        registerButton = (Button) findViewById(R.id.register);

        super.onCreate(savedInstanceState);

        FlowManager.init(new FlowConfig.Builder(this).build());

        setListeners();
    }

    /**
     * Upravljaci dogadjaja za loginButton i registerButton gumbe
     */
    private void setListeners(){
        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                korime = (EditText) findViewById(R.id.korime);
                lozinka = (EditText) findViewById(R.id.lozinka);

                dataBulder.login(korime.getText().toString(), lozinka.getText().toString());
            }
        });
        String id = prefs.getString("id", "");
        if(!id.equals("")){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Postavlja user_id atribut u SharedPreferences
     * Sprema user_id u Log
     * Pokrece novu aktivnost ovisno o uspjesnosti prijave
     * @param data Odgovor web servisa
     */
    @Override
    public void buildData(Object data) {
        if(data instanceof User) {
            pojo.User user = (pojo.User) data;
            if (user != null) {
                editor.putString("id", user.getId());
                editor.putString("username", user.getUsername());
                editor.putString("name", user.getName());
                editor.putString("surname", user.getSurname());
                editor.putString("email", user.getEmail());
                editor.putString("password", user.getPassword());
                editor.commit();

                String id = prefs.getString("id", "Ne radi!");
                Log.w("user-id-prefs", id);

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                Toast.makeText(getApplicationContext(), "Uspješna prijava.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Neuspješna prijava.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Response response = (Response) data;
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
    }

}
