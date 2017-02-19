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

        Button webSync = (Button) view.findViewById(R.id.webSync);
        Button bluetoothSync = (Button) view.findViewById(R.id.bluetoothSync);
        prefs = getActivity().getSharedPreferences("login", 0);


        webSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataSyncer.syncData(getActivity(), prefs.getString("id", ""), 1);
            }
        });

        bluetoothSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataSyncer.syncData(getActivity(), prefs.getString("id", ""), 2);
            }
        });

        return view;
    }
}
