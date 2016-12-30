package com.hr.foi.userinterface;

import android.app.ActionBar;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;

import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.ikimuhendis.ldrawer.ActionBarDrawerToggle;
import com.ikimuhendis.ldrawer.DrawerArrowDrawable;

import java.util.ArrayList;

/**
 * Created by dagy on 06.11.16..
 */

public class MainMenu extends BaseFragment {
    private DrawerLayout drawerLayout;
    private ListView drawerList;
    private DrawerArrowDrawable drawerArrow;
    private ActionBarDrawerToggle drawerToggle;

    private ArrayList<String> fragmentNames = new ArrayList<String>();
    private ArrayList<FragmentInterface> fragments = new ArrayList<FragmentInterface>();
    private String currentFrag;

    /**
     * Set names and fragment objects.
     * @param params
     */
    public void initFrag(FragmentInterface... params){
        for(FragmentInterface param : params){
            fragmentNames.add(param.getFragment().getName());
            this.fragments.add(param);
        }
    }

    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setHasOptionsMenu(true);
        this.homeFragment();

        ActionBar ab = getActivity().getActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeButtonEnabled(true);

        drawerLayout = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
        drawerList = (ListView) getActivity().findViewById(R.id.navdrawer);

        drawerArrow = new DrawerArrowDrawable(getActivity()){
          @Override
          public boolean isLayoutRtl(){return false;}
        };

        drawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, drawerArrow, R.string.drawer_open, R.string.drawer_close){
            public void onDrawerClosed(View view){
                super.onDrawerClosed(view);
                refreshName();
            }

            public void onDrawerOpened(View drawerView){
                super.onDrawerOpened(drawerView);
                //getActivity().getActionBar().setTitle("ui");
                getActivity().invalidateOptionsMenu();
            }
        };

        drawerLayout.setDrawerListener(drawerToggle);
        drawerToggle.syncState();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1, fragmentNames);
        drawerList.setAdapter(adapter);

        drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currentFrag = fragments.get(position).getFragment().getName();

                switchFragment(fragments.get(position).getFragment(), false, currentFrag);
                drawerLayout.closeDrawer(drawerList);

            }
        });
    }

    /**
     * Set first fragment as active.
     */
    public void homeFragment(){
        currentFrag = fragments.get(0).getFragment().getName();
        refreshName();
        switchFragment(fragments.get(0).getFragment(), false, currentFrag);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId() == android.R.id.home){
            if(drawerLayout.isDrawerOpen(drawerList)){
                drawerLayout.closeDrawer(drawerList);
            } else {
                drawerLayout.openDrawer(drawerList);
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig){
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    /**
     * Refresh title.
     */
    public void refreshName(){
        getActivity().getActionBar().setTitle(currentFrag);
    }
}
