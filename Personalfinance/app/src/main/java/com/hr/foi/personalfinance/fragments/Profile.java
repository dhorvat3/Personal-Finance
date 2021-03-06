package com.hr.foi.personalfinance.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.hr.foi.personalfinance.LoginActivity;
import com.hr.foi.personalfinance.R;
import com.hr.foi.userinterface.BaseFragment;
import com.hr.foi.userinterface.FragmentInterface;

/**
 * Created by dagy on 06.11.16..
 */

/**
 * Klasa za rad s korisnickim profilom.
 * Azuriranje podataka i odjava sa sustava.
 */
public class Profile extends BaseFragment implements FragmentInterface {

    /**
     * Korisnicke postavke
     */
    private SharedPreferences prefs;

    public static final Profile newInstance(String name){
        Profile f = new Profile();
        f.setName(name);

        return f;
    }

    @Override
    public BaseFragment getFragment() {
        return this;
    }

    /**
     * Postavljanje podataka o korisniku
     * Implementacija odjave sa sustava
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        prefs = this.getActivity().getSharedPreferences("login", 0);

        View view = inflater.inflate(R.layout.profile_layout, container, false);
        TextView profileFullName = (TextView) view.findViewById(R.id.profile_full_name);
        TextView profileEmail = (TextView) view.findViewById(R.id.profile_email);
        ImageButton logout = (ImageButton) view.findViewById(R.id.logout);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prefs.edit().clear().commit();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });

        profileFullName.setText(prefs.getString("name", "") + " " + prefs.getString("surname", ""));
        profileEmail.setText(prefs.getString("email", ""));

        return view;
    }

    /**
     * Azuriranje korisnika
     * @param view
     * @param savedInstanceState
     */
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        ImageButton editButton = (ImageButton) view.findViewById(R.id.profile_edit);

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int fragmentId = ((ViewGroup)(getView().getParent())).getId();

                getFragmentManager().beginTransaction().replace(fragmentId, new ProfileEdit()).addToBackStack(null).commit();
            }
        });

        super.onViewCreated(view, savedInstanceState);
    }

    /**
     * Postavljanje podataka o korisniku
     */
    @Override
    public void onResume() {
        getActivity().getActionBar().setTitle("Moj profil");

        if (prefs.contains("profile-edited")) {
            TextView profileFullName = (TextView) getView().findViewById(R.id.profile_full_name);
            TextView profileEmail = (TextView) getView().findViewById(R.id.profile_email);

            profileFullName.setText(prefs.getString("name", "") + " " + prefs.getString("surname", ""));
            profileEmail.setText(prefs.getString("email", ""));

            SharedPreferences.Editor editor = prefs.edit();

            editor.remove("profile-edited");
            editor.commit();
        }

        super.onResume();
    }
}
