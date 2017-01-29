package com.hr.foi.personalfinance.fragments;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.hr.foi.personalfinance.R;
import com.hr.foi.personalfinance.adapter.TaskListAdapter;
import com.hr.foi.userinterface.BaseFragment;
import com.hr.foi.userinterface.FragmentInterface;

import entities.DataBuilder;
import entities.DataInterface;
import pojo.Task_;

/**
 * A simple {@link Fragment} subclass.
 */
public class Tasks extends BaseFragment implements FragmentInterface, DataInterface {

    private SharedPreferences prefs;
    private ProgressBar progress;
    private DataBuilder dataBuilder = new DataBuilder(this);
    private Task_ task;
    private ListView taskListView;

    public static final Tasks newInstance(String name){
        Tasks f = new Tasks();
        f.setName(name);

        return f;
    }

    @Override
    public BaseFragment getFragment() {
        return this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tasks_layout, container, false);

        prefs = getActivity().getSharedPreferences("login", 0);

        progress = (ProgressBar) view.findViewById(R.id.loading);
        progress.setVisibility(View.VISIBLE);

        taskListView = (ListView) view.findViewById(R.id.tasks_list_view);

        registerForContextMenu(taskListView);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        dataBuilder.getTasks(prefs.getString("id", ""));

        Button addTaskButton = (Button) view.findViewById(R.id.add_task);

        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int fragmentId = ((ViewGroup)(getView().getParent())).getId();

                getFragmentManager().beginTransaction().replace(fragmentId, new TaskAdd()).addToBackStack(null).commit();
            }
        });

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        progress.setVisibility(View.VISIBLE);
        dataBuilder.getTasks(prefs.getString("id", ""));

        super.onResume();
    }

    @Override
    public void buildData(Object data) {
        pojo.Task tasksResponse = (pojo.Task) data;

        if (tasksResponse != null) {
            TaskListAdapter adapter = new TaskListAdapter(getActivity(), tasksResponse.getTasks());
            taskListView.setAdapter(adapter);

            progress.setVisibility(View.GONE);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        menu.add(0, 0, 0, "Uredi");
        menu.add(0, 1, 1, "Obriši");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()) {
            case 0:
                break;
            case 1:
                break;
        }

        return super.onContextItemSelected(item);
    }
}
