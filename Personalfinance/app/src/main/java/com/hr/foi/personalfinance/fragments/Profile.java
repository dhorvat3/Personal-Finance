package com.hr.foi.personalfinance.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

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
        TextView profileFullName = (TextView) view.findViewById(R.id.profile_full_name);
        TextView profileEmail = (TextView) view.findViewById(R.id.profile_email);

        SharedPreferences prefs = this.getActivity().getSharedPreferences("login", 0);

        profileFullName.setText(prefs.getString("name", "") + " " + prefs.getString("surname", ""));
        profileEmail.setText(prefs.getString("email", ""));

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
        ImageButton editButton = (ImageButton) view.findViewById(R.id.profile_edit);

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int fragmentId = ((ViewGroup)(getView().getParent())).getId();

                getFragmentManager().beginTransaction().replace(fragmentId, new ProfileEdit()).commit();
            }
        });

        super.onViewCreated(view, savedInstanceState);
    }

}
