package com.hr.foi.personalfinance.fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.support.v7.app.AlertDialog;
import android.util.Log;
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
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

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
    private RadioButton prihod, rashod;
    private ArrayList<Record_> records;
    private ArrayList<pojo.Category_> catList;
    private Dialog dialog;
    private Spinner spinner;
    private int seqInt;
    private Category_ myItem = null;
    private ArrayAdapter<Category_> adapter;
    private int sequence = 0;



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
        dialog = new Dialog(getActivity());

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //final Dialog dialog = new Dialog(getActivity());
                dialog.setTitle("Nova Prihod/rashod");
                dialog.setContentView(R.layout.income_expense_item_layout);
                dialog.show();

                Button submit = (Button) dialog.findViewById(R.id.ok);
                Button cancel = (Button) dialog.findViewById(R.id.cancel);

                spinner = (Spinner) dialog.findViewById(R.id.sp_kategorija);
                dataBuilder.getCategories(userID());
                //String[] days ={"pon", "uto", "sri"};
                //ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item,days);
                //spinner.setAdapter(adapter);


                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String userId = userID();
                        napomena = (EditText) dialog.findViewById(R.id.napomena);
                        datum = (EditText) dialog.findViewById(R.id.datum);
                        iznos = (EditText) dialog.findViewById(R.id.iznos);
                        prihod = (RadioButton) dialog.findViewById(R.id.prihod);
                        rashod = (RadioButton) dialog.findViewById(R.id.rashod);

                        Category_ catId = (Category_) spinner.getSelectedItem();
                        Log.w("CatID", catId.getId());

                        record = new Record_();

                        if (prihod.isChecked()){
                            System.out.println("prihod");
                            record.setVrsta("1");
                        }
                        else if(rashod.isChecked()){
                            System.out.println("rashod");
                            record.setVrsta("0");
                        }

                        record.setNapomena(napomena.getText().toString());
                        record.setUserId(userId);
                        record.setAktivan("1");
                        record.setCatgoryId(catId.getId());
                        record.setDatum("2001-12-01 00:00:00");
                        record.setIznos(iznos.getText().toString());

                        dataBuilder.newRecord(record);

                        sequence++;
                        int groupPosition = addRecord(record.getDatum(),record.getIznos());
                        listAdapter.notifyDataSetChanged();
                        listView.setSelectedGroup(groupPosition);

                        dataBuilder.getRecords(userId);

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

        if(data instanceof pojo.Category){
            pojo.Category categories = (pojo.Category) data;
            catList = new ArrayList<>();
            for(Category_ item: categories.getCategory()){
                catList.add(item);
            }

            adapter = new ArrayAdapter<Category_>(getActivity(), android.R.layout.simple_spinner_dropdown_item, catList);
            spinner.setAdapter(adapter);
            //find selected cat
            String catID = records.get(seqInt).getCatgoryId();
            //Category_ myItem = null;
            for(int i=0; i < adapter.getCount(); i++){
                Category_ item = adapter.getItem(i);

                if(item.getId().equals(catID)){
                    myItem = item;
                    break;
                }
            }
            spinner.setSelection(adapter.getPosition(myItem));
        }
        if(data instanceof pojo.Record) {
            pojo.Record record = (pojo.Record) data;
            if (record != null) {

                listView = (ExpandableListView) getActivity().findViewById(R.id.zapisi);

                myRecords.clear();
                deptList.clear();

                records = new ArrayList<Record_>();

                for (Record_ item : record.getRecord()) {
                    records.add(item);
                }

                for (int i = 0; i < records.size(); i++) {
                    sequence = i;
                    addRecord(records.get(i).getDatum().substring(0, 10), records.get(i).getIznos());
                }
                listAdapter = new MyListAdapter(getActivity(), deptList);
                listView.setAdapter(listAdapter);


                listView.setOnChildClickListener(myListItemClicked);
            }
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

            parent.getExpandableListAdapter().getChild(groupPosition, childPosition);
            final EditText seq = (EditText) v.findViewById(R.id.sequence);

            update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //final Dialog dialog = new Dialog(getActivity());
                    dialog.setTitle("Ažuriranje prihoda/rashoda");
                    dialog.setContentView(R.layout.income_expense_item_layout);
                    dialog.show();

                    int vrsta;
                    seqInt=  Integer.parseInt(String.valueOf(seq.getText()));

                    spinner = (Spinner) dialog.findViewById(R.id.sp_kategorija);
                    dataBuilder.getCategories(userID());



                    napomena = (EditText) dialog.findViewById(R.id.napomena);
                    datum = (EditText) dialog.findViewById(R.id.datum);
                    iznos = (EditText) dialog.findViewById(R.id.iznos);
                    prihod = (RadioButton) dialog.findViewById(R.id.prihod);
                    rashod = (RadioButton) dialog.findViewById(R.id.rashod);

                    napomena.setText(records.get(seqInt).getNapomena());
                    datum.setText(records.get(seqInt).getDatum());
                    iznos.setText(records.get(seqInt).getIznos());
                    vrsta = Integer.parseInt(records.get(seqInt).getVrsta());

                    if (vrsta == 0){
                        rashod.setChecked(true);
                    }
                    else if(vrsta == 1){
                        rashod.setChecked(true);
                    }


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
        detailInfo.setSequence(String.valueOf(sequence));
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
