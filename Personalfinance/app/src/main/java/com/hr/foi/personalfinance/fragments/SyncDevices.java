package com.hr.foi.personalfinance.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.hr.foi.personalfinance.R;
import com.hr.foi.userinterface.BaseFragment;
import com.hr.foi.userinterface.FragmentInterface;

import sync.DataSyncer;


/**
 * Created by dagy on 31.01.17..
 */

public class SyncDevices extends BaseFragment implements FragmentInterface {

    private SharedPreferences prefs;
    private DataSyncer dataSyncer = new DataSyncer();

    /**
     * Konstruktor
     * @param name Naziv
     * @return SyncDevices
     */
    public static final SyncDevices newInstance(String name){
        SyncDevices f = new SyncDevices();
        f.setName(name);

        return f;
    }

    @Override
    public BaseFragment getFragment() {
        return this;
    }

    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.sync_layout, container, false);

        Button button = (Button) view.findViewById(R.id.webSync);
        prefs = getActivity().getSharedPreferences("login", 0);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataSyncer.syncData(prefs.getString("id", ""), 1);
            }
        });

        return view;
    }
}
