package com.hr.foi.personalfinance;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import com.hr.foi.personalfinance.fragments.Flow;
import com.hr.foi.personalfinance.fragments.Profile;
import com.hr.foi.userinterface.MainMenu;

public class MainActivity extends Activity {

    private MainMenu menu = new MainMenu();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActionBar bar = getActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0665D6")));

        menu.initFrag(Flow.newInstance(getString(R.string.frag_flow)),
                Profile.newInstance(getString(R.string.frag_profile)));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.navdrawer, menu);
        ft.commit();
    }

    @Override
    public void onBackPressed(){
        menu.homeFragment();
    }
}
