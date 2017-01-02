package com.hr.foi.personalfinance.fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hr.foi.personalfinance.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileEdit extends Fragment {


    public ProfileEdit() {
        // Required empty public constructor
    }


    /**
     * Used for setting values after view is created.
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_edit_layout, container, false);

        getActivity().getActionBar().setTitle("Uređivanje profila");

        return view;
    }

}
