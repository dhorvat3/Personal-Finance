package com.hr.foi.personalfinance;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import com.hr.foi.personalfinance.fragments.Category;
import com.hr.foi.personalfinance.fragments.Daybook;
import com.hr.foi.personalfinance.fragments.Income_Expense;
import com.hr.foi.personalfinance.fragments.Profile;
import com.hr.foi.personalfinance.fragments.Statistics;
import com.hr.foi.personalfinance.fragments.SyncDevices;
import com.hr.foi.personalfinance.fragments.Tasks;
import com.hr.foi.userinterface.MainMenu;

/**
 * Klasa MainActivity za realizaciju glavne aktivnosti
 * */
public class MainActivity extends Activity {

    /**
     * Meni (userInterface modul)
     */
    private MainMenu menu = new MainMenu();

    /**
     * Postavljanje fragmenata u meni
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActionBar bar = getActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3F51B5")));
        bar.setDisplayShowHomeEnabled(false);

        menu.initFrag(Income_Expense.newInstance("Prihodi/Rashodi"),
                Category.newInstance("Kategorije"),
                Tasks.newInstance("Lista obveza"),
                Statistics.newInstance("Statistika"),
                Daybook.newInstance("Dnevnik"),
                Profile.newInstance("Moj profil"),
                SyncDevices.newInstance("Upari uređaj"));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.navdrawer, menu);
        ft.commit();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if (intent != null) {
            String redirect = intent.getStringExtra("redirect");

            if (redirect != null && redirect.equals("TasksFragment")) {
                getFragmentManager().beginTransaction().replace(R.id.container, new Tasks()).addToBackStack(null).commit();
            }
        }
    }

    /**
     * Povratak u pocetni fragment u navigaciji
     */
    @Override
    public void onBackPressed(){
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            menu.homeFragment();
        }
    }
}
