package com.hr.foi.personalfinance.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hr.foi.personalfinance.LoginActivity;
import com.hr.foi.personalfinance.R;
import com.hr.foi.userinterface.BaseFragment;

import entities.DataBuilder;
import entities.DataInterface;
import entities.User;

/**
 * Created by Filip on 9.11.2016..
 */

public class account extends BaseFragment implements DataInterface {
    private Button loginButton;
    private EditText korime, lozinka;
    private DataBuilder dataBulder = new DataBuilder(this);

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.login_layout, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        loginButton = (Button) getActivity().findViewById(R.id.login);

        loginButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                korime = (EditText) getActivity().findViewById(R.id.korime);
                lozinka = (EditText) getActivity().findViewById(R.id.lozinka);

                dataBulder.login(korime.getText().toString(), lozinka.getText().toString());
            }
        });

    }
    @Override
    public void buildData(Object data) {
        User user = (User) data;
        if (user.getId()!=0){
            Toast.makeText(getActivity(), "Sucessfull login.", Toast.LENGTH_SHORT).show();

        }
        else {
            Toast.makeText(getActivity(), "Wrong credentials!", Toast.LENGTH_SHORT).show();
        }
    }
}
