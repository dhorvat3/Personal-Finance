package com.hr.foi.personalfinance.fragments;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hr.foi.personalfinance.R;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import core.DataBuilder;
import core.DataInterface;
import pojo.Response;
import pojo.User;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileEdit extends Fragment implements DataInterface {

    private SharedPreferences prefs;
    private DataBuilder dataBuilder = new DataBuilder(this);
    private User user = new User();

    public ProfileEdit() {
        // Required empty public constructor
    }


    /**
     * Used for setting values while creating view.
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().getActionBar().setTitle("Uređivanje profila");

        prefs = getActivity().getSharedPreferences("login", 0);

        View view = inflater.inflate(R.layout.profile_edit_layout, container, false);
        EditText firstName = (EditText) view.findViewById(R.id.first_name);
        EditText lastName = (EditText) view.findViewById(R.id.last_name);
        EditText email = (EditText) view.findViewById(R.id.email);

        firstName.setText(prefs.getString("name", ""));
        lastName.setText(prefs.getString("surname", ""));
        email.setText(prefs.getString("email", ""));

        return view;
    }

    /**
     * Used for setting values after view is created.
     *
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        Button cancelButton = (Button) view.findViewById(R.id.profile_edit_cancel_button);
        Button submitButton = (Button) view.findViewById(R.id.profile_edit_submit_button);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText firstName = (EditText) view.findViewById(R.id.first_name);
                EditText lastName = (EditText) view.findViewById(R.id.last_name);
                EditText email = (EditText) view.findViewById(R.id.email);
                String password = ((EditText) view.findViewById(R.id.password)).getText().toString();
                boolean valid = true;

                List<EditText> fields = Arrays.asList(firstName, lastName, email);

                for (Iterator<EditText> i = fields.iterator(); i.hasNext();) {
                    EditText field = i.next();

                    if (field.getText().toString().isEmpty()) {
                        field.setError("Obavezno polje");

                        valid = false;
                    }
                }

                if (valid) {
                    if (password.isEmpty()) {
                        password = prefs.getString("password", "");
                    }

                    user.setId(prefs.getString("id", ""));
                    user.setUsername(prefs.getString("username", ""));
                    user.setName(firstName.getText().toString());
                    user.setSurname(lastName.getText().toString());
                    user.setEmail(email.getText().toString());
                    user.setPassword(password);

                    dataBuilder.editUser(user);
                }
            }
        });

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void buildData(Object data) {
        Response response = (Response) data;

        switch (response.getId()) {
            case "1":
                SharedPreferences.Editor editor = prefs.edit();

                editor.putString("id", user.getId());
                editor.putString("username", user.getUsername());
                editor.putString("name", user.getName());
                editor.putString("surname", user.getSurname());
                editor.putString("email", user.getEmail());
                editor.putString("password", user.getPassword());
                editor.putBoolean("profile-edited", true);
                editor.commit();

                Toast.makeText(getActivity(), "Uspješna izmjena", Toast.LENGTH_SHORT).show();

                getFragmentManager().popBackStack();
                break;
            case "-1":
                Toast.makeText(getActivity(), "Pogreška", Toast.LENGTH_SHORT).show();
                break;
            case "-2":
                Toast.makeText(getActivity(), "Prazno polje", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
