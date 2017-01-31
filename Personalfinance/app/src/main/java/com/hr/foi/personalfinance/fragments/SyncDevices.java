package com.hr.foi.personalfinance.fragments;

import android.content.SharedPreferences;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.hr.foi.personalfinance.R;
import com.hr.foi.userinterface.BaseFragment;
import com.hr.foi.userinterface.FragmentInterface;

import java.nio.charset.Charset;

/**
 * Created by dagy on 31.01.17..
 */

public class SyncDevices extends BaseFragment implements FragmentInterface, NfcAdapter.CreateNdefMessageCallback, NfcAdapter.OnNdefPushCompleteCallback {

    private NfcAdapter nfcAdapter;
    private SharedPreferences prefs;

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

        Button button = (Button) view.findViewById(R.id.sync);
        prefs = getActivity().getSharedPreferences("login", 0);

        //setup nfc adapter
        nfcAdapter = NfcAdapter.getDefaultAdapter(getActivity());
        if(nfcAdapter == null){
            Toast.makeText(getActivity(), "NFC nije dostupan", Toast.LENGTH_SHORT).show();
        } else {
            nfcAdapter.setNdefPushMessageCallback(this, getActivity());
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return view;
    }

    @Override
    public NdefMessage createNdefMessage(NfcEvent event) {
        String name = prefs.getString("name", "");
        String surname = prefs.getString("surname", "");
        String email = prefs.getString("email", "");
        String id = prefs.getString("id", "");


        if(id != "") {
            String text = name + "\n" + surname +"\n" + "\n" + email + "\n" + id;
            byte[] bytesOut = text.getBytes();

            NdefRecord ndefRecord = new NdefRecord(
                    NdefRecord.TNF_MIME_MEDIA,
                    "text/plain".getBytes(),
                    new byte[] {},
                    bytesOut
            );


            NdefMessage ndefMessage = new NdefMessage(ndefRecord);

            return ndefMessage;
        }

        return null;
    }

    @Override
    public void onNdefPushComplete(NfcEvent event) {

    }

}
