package com.hr.foi.personalfinance.fragments;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.hr.foi.personalfinance.R;
import com.hr.foi.personalfinance.adapter.MyListAdapter;
import com.hr.foi.personalfinance.info.DetailInfo;
import com.hr.foi.personalfinance.info.HeaderInfo;
import com.hr.foi.userinterface.BaseFragment;
import com.hr.foi.userinterface.FragmentInterface;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import entities.DataBuilder;
import entities.DataInterface;
import pojo.Category_;
import pojo.Record_;

/**
 * Created by Filip on 22.12.2016..
 */

public class Income_Expense extends BaseFragment implements FragmentInterface, DataInterface {
    private EditText napomena, datum, iznos;
    private RadioButton vrsta;
    private Record_ record;
    private DataBuilder dataBuilder = new DataBuilder(this);
    private SharedPreferences preferences;
    private ExpandableListView listView;
    private LinkedHashMap<String, HeaderInfo> myRecords = new LinkedHashMap<String, HeaderInfo>();
    private ArrayList<HeaderInfo> deptList = new ArrayList<HeaderInfo>();
    private MyListAdapter listAdapter;



    public static final Income_Expense newInstance(String name){
        Income_Expense f = new Income_Expense();
        f.setName(name);

        return f;
    }
    @Override
    public BaseFragment getFragment() {
        return this;
    }

    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.income_expense_layout, container, false);

        Button button = (Button) view.findViewById(R.id.AddIncomeExpense);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(getActivity());
                dialog.setTitle("Nova Prihod/rashod");
                dialog.setContentView(R.layout.income_expense_item_layout);
                dialog.show();

                Button submit = (Button) dialog.findViewById(R.id.ok);
                Button cancel = (Button) dialog.findViewById(R.id.cancel);

                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        record = new Record_();
                        String userId = userID();
                        record.setUserId(userId);
                        record.setAktivan("1");
                        record.setCatgoryId("2");
                        record.setDatum("2017-28-01 05:05:05");
                        record.setId("1");
                        record.setIznos("150");
                        record.setVrsta("1");
                        dataBuilder.newRecord(record);

                        int groupPosition = addRecord(record.getDatum(),record.getIznos());
                        listAdapter.notifyDataSetChanged();
                        listView.setSelectedGroup(groupPosition);

                    }
                });

                cancel.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });
            }
        });

        return view;
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        String userId = userID();

        dataBuilder.getRecords(userId);
    }



    @Override
    public void buildData(Object data) {
        pojo.Record record = (pojo.Record) data;
        if (record !=null){
            listView = (ExpandableListView) getActivity().findViewById(R.id.zapisi);
            ArrayList<Record_> records = new ArrayList<Record_>();

            for (Record_ item : record.getRecord()){
                records.add(item);
            }

            String[] listItems = new String[records.size()];
            for (int i=0; i<records.size(); i++){
                listItems[i] = records.get(i).getIznos();
                addRecord(records.get(i).getDatum(), records.get(i).getIznos());
            }

            System.out.println(records.size());

            //ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, listItems);
            listAdapter = new MyListAdapter(getActivity(), deptList);
            //ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, listItems);
            listView.setAdapter(listAdapter);


            listView.setOnChildClickListener(myListItemClicked);
            listView.setOnGroupClickListener(myListGroupClicked);
        }
    }

    private ExpandableListView.OnChildClickListener myListItemClicked =  new ExpandableListView.OnChildClickListener() {

        public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

            HeaderInfo headerInfo = deptList.get(groupPosition);
            DetailInfo detailInfo =  headerInfo.getCategoryList().get(childPosition);
            return false;
        }
    };
    private ExpandableListView.OnGroupClickListener myListGroupClicked =  new ExpandableListView.OnGroupClickListener() {

        public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {

            HeaderInfo headerInfo = deptList.get(groupPosition);
            return false;
        }

    };

    private int addRecord(String name, String description){
        int groupPosition = 0;

        HeaderInfo headerInfo = myRecords.get(name);

        if(headerInfo == null){
            headerInfo = new HeaderInfo();
            headerInfo.setName(name);
            myRecords.put(name, headerInfo);
            deptList.add(headerInfo);
        }

        ArrayList<DetailInfo> categoryList = headerInfo.getCategoryList();

        int listSize = categoryList.size();

        listSize++;

        DetailInfo detailInfo = new DetailInfo();
        detailInfo.setSequence(String.valueOf(listSize));
        detailInfo.setName(description);
        categoryList.add(detailInfo);
        headerInfo.setCategoryList(categoryList);

        groupPosition = deptList.indexOf(headerInfo);
        return groupPosition;
    }

    private String userID(){
        preferences = getActivity().getSharedPreferences("login", 0);
        String status = preferences.getString("id", "");
        return  status;
    }
}
