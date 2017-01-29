package com.hr.foi.personalfinance.fragments;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.hr.foi.personalfinance.R;
import com.hr.foi.personalfinance.adapters.TaskListAdapter;
import com.hr.foi.userinterface.BaseFragment;
import com.hr.foi.userinterface.FragmentInterface;

import java.util.List;

import core.DataBuilder;
import core.DataInterface;
import pojo.Response;
import pojo.Task_;

/**
 * A simple {@link Fragment} subclass.
 */
public class Tasks extends BaseFragment implements FragmentInterface, DataInterface {

    private SharedPreferences prefs;
    private ProgressBar progress;
    private DataBuilder dataBuilder = new DataBuilder(this);
    private List<Task_> taskList;
    private Task_ task;
    private TaskListAdapter adapter;
    private ListView taskListView;
    private int itemForDelete = -1;

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

        ImageButton addTaskButton = (ImageButton) view.findViewById(R.id.add_task);

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
        getActivity().getActionBar().setTitle("Lista obveza");

        super.onResume();
    }

    @Override
    public void buildData(Object data) {
        if (data.getClass().getSimpleName().equals("Task")) {
            pojo.Task tasks = (pojo.Task) data;
            taskList = tasks.getTasks();

            if (tasks != null) {
                adapter = new TaskListAdapter(getActivity(), taskList);
                taskListView.setAdapter(adapter);

                progress.setVisibility(View.GONE);
            }
        } else if (data.getClass().getSimpleName().equals("Response")) {
            Response response = (Response) data;

            switch (response.getId()) {
                case "1":
                    taskList.remove(itemForDelete);
                    adapter.notifyDataSetChanged();

                    Toast.makeText(getActivity(), "Uspješno obrisano", Toast.LENGTH_SHORT).show();
                    break;
                case "-1":
                    Toast.makeText(getActivity(), "Pogreška", Toast.LENGTH_SHORT).show();
                    break;
                case "-2":
                    Toast.makeText(getActivity(), "Prazno polje", Toast.LENGTH_SHORT).show();
                    break;
            }

            itemForDelete = -1;
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
        final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()) {
            case 0:
                break;
            case 1:
                final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                final String itemId = String.valueOf(taskListView.getAdapter().getItemId(info.position));
                String itemTitle = ((Task_) taskListView.getAdapter().getItem(info.position)).getTitle();

                alert.setTitle("Brisanje: " + itemTitle);
                alert.setMessage("Jeste li sigurni da želite obrisati obvezu?");

                alert.setNegativeButton("Ne", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                alert.setPositiveButton("Da", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        itemForDelete = info.position;
                        dataBuilder.deleteTask(itemId);

                        dialog.dismiss();
                    }
                });

                alert.show();
                break;
        }

        return super.onContextItemSelected(item);
    }
}
