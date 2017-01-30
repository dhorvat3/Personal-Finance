package com.hr.foi.userinterface;

import android.app.Fragment;

/**
 * Created by dagy on 06.11.16..
 */

/**
 * Klasa Base Fragment za rad sa fragmentima kod menija
 */
public class BaseFragment extends Fragment{

    /**
     * Naziv fragmenta
     */
    public String fragmentName;

    /**
     * Postavlja naziv fragmenta
     * @param name Naziv fragmenta
     */
    public void setName(String name){
        this.fragmentName = name;
    }

    /**
     * Vraca naziv fragmenta
     * @return Naziv fragmenta
     */
    public String getName() {
        return this.fragmentName;
    }

    /**
     * Postavlja aktivni fragment na trenutnu aktivnost
     * @param fragment Aktivni fragment
     * @param backStack Zastavica
     * @param tag Oznaka fragmenta
     */
    public void switchFragment(Fragment fragment, boolean backStack, String tag){
        if(backStack){
            getFragmentManager().beginTransaction().addToBackStack(null)
                    .replace(R.id.container, fragment, tag)
                    .commit();
        } else {
            getFragmentManager().beginTransaction()
                    .replace(R.id.container, fragment, tag)
                    .commit();
        }
    }
}
