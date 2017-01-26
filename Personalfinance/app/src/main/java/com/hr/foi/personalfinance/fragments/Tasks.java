package com.hr.foi.personalfinance.fragments;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.hr.foi.personalfinance.R;
import com.hr.foi.userinterface.BaseFragment;
import com.hr.foi.userinterface.FragmentInterface;

import java.util.List;

import entities.DataBuilder;
import entities.DataInterface;
import pojo.Task_;

/**
 * A simple {@link Fragment} subclass.
 */
public class Tasks extends BaseFragment implements FragmentInterface, DataInterface {

    private SharedPreferences prefs;
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
        View view = inflater.inflate(R.layout.fragment_tasks, container, false);

        prefs = getActivity().getSharedPreferences("login", 0);

        taskListView = (ListView) view.findViewById(R.id.tasks_list_view);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        dataBuilder.getTasks(prefs.getString("id", ""));
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void buildData(Object data) {
        pojo.Task tasksResponse = (pojo.Task) data;

        if (tasksResponse != null) {
            List<Task_> tasks = tasksResponse.getTasks();
            String[] listItems = new String[tasks.size()];

            for (int i = 0; i < tasks.size(); i++) {
                listItems[i] = tasks.get(i).getTitle();
            }

            ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, listItems);
            taskListView.setAdapter(adapter);
        }
    }
}
