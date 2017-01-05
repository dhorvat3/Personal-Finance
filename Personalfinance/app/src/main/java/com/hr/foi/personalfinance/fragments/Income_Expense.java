package com.hr.foi.personalfinance.fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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
import pojo.*;

/**
 * Created by Filip on 22.12.2016..
 */

public class Income_Expense extends BaseFragment implements FragmentInterface, DataInterface {
    private Record_ record;
    private DataBuilder dataBuilder = new DataBuilder(this);
    private SharedPreferences preferences;
    private ExpandableListView listView;
    private LinkedHashMap<String, HeaderInfo> myRecords = new LinkedHashMap<String, HeaderInfo>();
    private ArrayList<HeaderInfo> deptList = new ArrayList<HeaderInfo>();
    private MyListAdapter listAdapter;
    private EditText napomena, datum, iznos;
    private ArrayList<Record_> records;



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

                Spinner spinner = (Spinner) dialog.findViewById(R.id.sp_kategorija);

                String[] days ={"pon", "uto", "sri"};
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item,days);
                spinner.setAdapter(adapter);


                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String userId = userID();
                        napomena = (EditText) dialog.findViewById(R.id.napomena);
                        datum = (EditText) dialog.findViewById(R.id.napomena);
                        iznos = (EditText) dialog.findViewById(R.id.napomena);

                        record = new Record_();
                        record.setNapomena(napomena.getText().toString());
                        record.setUserId(userId);
                        record.setAktivan("1");
                        record.setCatgoryId("1");
                        record.setDatum("2001-12-01 00:00:00");
                        record.setIznos(iznos.getText().toString());
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

            myRecords.clear();
            deptList.clear();

            records = new ArrayList<Record_>();

            for (Record_ item : record.getRecord()){
                records.add(item);
            }

            for (int i=0; i<records.size(); i++){
                addRecord(records.get(i).getDatum(), records.get(i).getIznos());
            }
            listAdapter = new MyListAdapter(getActivity(), deptList);
            listView.setAdapter(listAdapter);


            listView.setOnChildClickListener(myListItemClicked);
        }
    }

    private ExpandableListView.OnChildClickListener myListItemClicked =  new ExpandableListView.OnChildClickListener() {

        public boolean onChildClick(ExpandableListView parent, View v, final int groupPosition, final int childPosition, final long id) {

            HeaderInfo headerInfo = deptList.get(groupPosition);
            DetailInfo detailInfo =  headerInfo.getCategoryList().get(childPosition);

            LinearLayout linearLayout =(LinearLayout)  getActivity().findViewById(R.id.update_delete);
            linearLayout.setVisibility(View.VISIBLE);

            ImageButton update = (ImageButton) linearLayout.findViewById(R.id.update);
            ImageButton delete = (ImageButton) linearLayout.findViewById(R.id.delete);

            update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Dialog dialog = new Dialog(getActivity());
                    dialog.setTitle("Ažuriranje prihoda/rashoda");
                    dialog.setContentView(R.layout.income_expense_item_layout);
                    dialog.show();

                    napomena = (EditText) dialog.findViewById(R.id.napomena);
                    napomena.setText(records.get(childPosition).getNapomena());

                    System.out.println(v.toString());
                    System.out.println(groupPosition);
                    System.out.println(childPosition);
                    System.out.println(id);

                    Button ok = (Button) dialog.findViewById(R.id.ok);
                    Button cancel = (Button) dialog.findViewById(R.id.cancel);
                    ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.cancel();
                        }
                    });

                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.cancel();
                        }
                    });
                }
            });

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Dialog dialog = new Dialog(getActivity());
                    dialog.setTitle("Sigurno želite obrisati?");
                    dialog.setContentView(R.layout.income_expense_delete_layout);
                    dialog.show();

                    Button ok = (Button) dialog.findViewById(R.id.ok);
                    Button cancel = (Button) dialog.findViewById(R.id.cancel);
                    ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.cancel();
                        }
                    });

                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.cancel();
                        }
                    });
                }
            });

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

        ArrayList<DetailInfo> recordList = headerInfo.getCategoryList();

        DetailInfo detailInfo = new DetailInfo();
        detailInfo.setName(description);
        recordList.add(detailInfo);
        headerInfo.setCategoryList(recordList);

        groupPosition = deptList.indexOf(headerInfo);
        return groupPosition;
    }

    private String userID(){
        preferences = getActivity().getSharedPreferences("login", 0);
        String status = preferences.getString("id", "");
        return  status;
    }
}
