package com.hr.foi.personalfinance.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hr.foi.personalfinance.R;
import com.hr.foi.userinterface.BaseFragment;
import com.hr.foi.userinterface.FragmentInterface;

/**
 * Created by dagy on 06.11.16..
 */

public class Profile extends BaseFragment implements FragmentInterface {

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
     * Used for setting values before view is created.
     *
     * **
     * ** Checking user login status.
     * **
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.profile_layout, container, false);

        return view;
    }

    /**
     * Used for setting values after view is created.
     * **
     * ** Setting listeners for ui elements.
     * **
     * @param view
     * @param savedInstanceState
     */
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
    }
}
