package com.hr.foi.userinterface;

import android.app.Fragment;

/**
 * Created by dagy on 06.11.16..
 */

public class BaseFragment extends Fragment{
    public String fragmentName;

    /**
     * Set fragment name.
     * @param name
     */
    public void setName(String name){
        this.fragmentName = name;
    }

    /**
     * Get fragment name
     * @return Fragment name
     */
    public String getName() {
        return this.fragmentName;
    }

    /**
     * Switch active fragment on current activity
     * @param fragment
     * @param backStack
     * @param tag
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
