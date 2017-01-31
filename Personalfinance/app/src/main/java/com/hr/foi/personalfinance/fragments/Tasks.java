package com.hr.foi.personalfinance.fragments;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import core.DataBuilder;
import core.DataInterface;
import pojo.Response;
import pojo.Task;

/**
 * Klasa Tasks za realizaciju korisnickih obveza
 */
public class Tasks extends BaseFragment implements FragmentInterface, DataInterface {

    private SharedPreferences prefs;
    private ProgressBar progress;
    private DataBuilder dataBuilder = new DataBuilder(this);
    private SwipeRefreshLayout swipeContainer;
    private List<Task> taskList;
    private Task task;
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

    /**
     * Dohvacanje GUI elemenata
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tasks_layout, container, false);

        prefs = getActivity().getSharedPreferences("login", 0);
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.tasks_swipe_container);
        progress = (ProgressBar) view.findViewById(R.id.loading);
        taskListView = (ListView) view.findViewById(R.id.tasks_list_view);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                dataBuilder.getTasks(prefs.getString("id", ""));
            }
        });

        swipeContainer.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), android.R.color.holo_blue_bright),
                ContextCompat.getColor(getActivity(), android.R.color.holo_green_light),
                ContextCompat.getColor(getActivity(), android.R.color.holo_orange_light),
                ContextCompat.getColor(getActivity(), android.R.color.holo_red_light)
        );

        progress.setVisibility(View.VISIBLE);
        registerForContextMenu(taskListView);

        return view;
    }

    /**
     * Dohvacanje postojecih korisnickih obveza iz baze podataka
     * @param view
     * @param savedInstanceState
     */
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

    /**
     * Obrada odogovora web servisa
     * Ispis rezultata obrade u Toast
     * @param data Odgovor web servisa
     */
    @Override
    public void buildData(Object data) {
        if (data.getClass().getSimpleName().equals("Tasks")) {
            pojo.Tasks tasks = (pojo.Tasks) data;
            taskList = tasks.getTasks();

            if (tasks != null) {
                adapter = new TaskListAdapter(getActivity(), taskList);
                taskListView.setAdapter(adapter);
                swipeContainer.setRefreshing(false);

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

    /**
     * Izmjena ili brisanje obveze
     * @param item
     * @return
     */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final String itemId = String.valueOf(taskListView.getAdapter().getItemId(info.position));
        String itemTitle = ((Task) taskListView.getAdapter().getItem(info.position)).getTitle();
        String itemNote = ((Task) taskListView.getAdapter().getItem(info.position)).getNote();
        String itemDate = ((Task) taskListView.getAdapter().getItem(info.position)).getDate();
        String itemNotice = ((Task) taskListView.getAdapter().getItem(info.position)).getNotice();

        switch (item.getItemId()) {
            case 0:
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy. HH:mm");
                SimpleDateFormat noticeFormat = new SimpleDateFormat("'Obavijesti me 'dd.MM.yyyy.' u 'HH:mm");
                SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                String editTaskDate = "";
                String editTaskNotice = "";

                try {
                    editTaskDate = dateFormat.format(outputFormat.parse(itemDate.substring(0, itemDate.length() - 3)));
                    editTaskNotice = noticeFormat.format(outputFormat.parse(itemNotice.substring(0, itemNotice.length() - 3)));
                } catch (ParseException e) {
                    Log.w("date-parser", e.getMessage());
                }

                SharedPreferences.Editor editor = prefs.edit();

                editor.putString("edit-task-id", itemId);
                editor.putString("edit-task-title", itemTitle);
                editor.putString("edit-task-note", itemNote);
                editor.putString("edit-task-date", editTaskDate);
                editor.putString("edit-task-notice", editTaskNotice);
                editor.commit();

                int fragmentId = ((ViewGroup)(getView().getParent())).getId();

                getFragmentManager().beginTransaction().replace(fragmentId, new TaskEdit()).addToBackStack(null).commit();
                break;
            case 1:
                final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());

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
