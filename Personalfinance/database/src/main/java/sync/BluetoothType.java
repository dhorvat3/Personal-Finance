package sync;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.devpaul.bluetoothutillib.SimpleBluetooth;

import hr.foi.air.database.R;

/**
 * Created by dagy on 19.02.17..
 */

public class BluetoothType extends Dialog {
    public Context context;
    private Button server;
    private Button client;
    private Button cancel;
    private BTSyncer btSyncer;

    public BluetoothType(Context context, BTSyncer btSyncer) {
        super(context);
        this.context = context;
        this.btSyncer = btSyncer;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.type_selection);

        server = (Button) findViewById(R.id.server);
        client = (Button) findViewById(R.id.client);
        cancel = (Button) findViewById(R.id.cancel);

        server.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btSyncer.host();
                dismiss();
            }
        });

        client.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btSyncer.connect();
                dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }

}
