package com.hr.foi.personalfinance.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TabHost;

import com.hr.foi.personalfinance.R;
import com.hr.foi.personalfinance.adapter.MyListAdapter;
import com.hr.foi.personalfinance.info.DetailInfo;
import com.hr.foi.personalfinance.info.HeaderInfo;
import com.hr.foi.userinterface.BaseFragment;
import com.hr.foi.userinterface.FragmentInterface;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import entities.DataBuilder;
import entities.DataInterface;
import pojo.Category_;
import pojo.Record;
import pojo.Record_;
import pojo.Response;

/**
 * Created by Filip on 22.12.2016..
 */

public class Daybook extends BaseFragment implements FragmentInterface, DataInterface {
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
    private ArrayList<Category_> catList;
    private Dialog dialog;
    private Spinner spinner;
    private int seqInt;
    private Category_ myItem = null;
    private ArrayAdapter<Category_> adapter;
    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat userFormat, databaseFormat;
    private String dateAndTime;
    private int sequence = 0;
    private String godina, mjesec, dan, dat;
    private android.widget.SearchView search;
    private boolean dateOrCategorySpinnerSelection = false;
    private LinkedHashMap<String, String> catIdName = new LinkedHashMap<String, String>();

    public static final Daybook newInstance(String name){
        Daybook f = new Daybook();
        f.setName(name);

        return f;
    }
    @Override
    public BaseFragment getFragment() {
        return this;
    }

    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.daybook_layout, container, false);

        search = (android.widget.SearchView) view.findViewById(R.id.search_view);

        final Spinner dateOrCategory = (Spinner) view.findViewById(R.id.dateOrCat);
        ArrayAdapter<CharSequence> DoCadapter = ArrayAdapter.createFromResource(getActivity(),R.array.dateOrCat_array, android.R.layout.simple_spinner_item);
        DoCadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dateOrCategory.setAdapter(DoCadapter);

        dateOrCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedValue = dateOrCategory.getSelectedItem().toString();
                //System.out.println(selectedValue);
                if (selectedValue.equals("Datum")){
                    dateOrCategorySpinnerSelection = false;
                    search.setQueryHint("Datum...");
                }
                else if(selectedValue.equals("Kategorije")){
                    dateOrCategorySpinnerSelection = true;
                    search.setQueryHint("Kategorije...");
                }
                dataBuilder.getRecords(userID());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




        search.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

            }
        });

        search.setOnCloseListener(new android.widget.SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                dataBuilder.getRecords(userID());
                listAdapter.notifyDataSetChanged();
                //listAdapter.filterData("");
                return false;
            }
        });

        search.setOnQueryTextListener(new android.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                listAdapter.filterData(query);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                listAdapter.filterData(query);

                return false;
            }
        });

        return view;
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        dataBuilder.getRecords(userID());
    }



    @Override
    public void buildData(Object data) {

        if(data instanceof pojo.Category){
            pojo.Category categories = (pojo.Category) data;
            catList = new ArrayList<>();
            for(Category_ item: categories.getCategory()){
                catList.add(item);
                catIdName.put(item.getId(), item.getTitle());
            }

            adapter = new ArrayAdapter<Category_>(getActivity(), android.R.layout.simple_spinner_dropdown_item, catList);
            spinner.setAdapter(adapter);

            System.out.println("kategorija ne radi: ");
            if (myRecords.size() != 0){
                //find selected cat
                String catID = records.get(seqInt).getCatgoryId();
                System.out.println("kategorija: "+ catID);
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
        }
        if(data instanceof Record) {
            Record record = (Record) data;
            if (record != null) {
                listView = (ExpandableListView) getActivity().findViewById(R.id.zapisi);

                myRecords.clear();
                deptList.clear();

                records = new ArrayList<Record_>();

                for (Record_ item : record.getRecord()) {
                    records.add(item);
                }
                if (!dateOrCategorySpinnerSelection)   {
                    for (int i = 0; i < records.size(); i++) {
                        sequence = i;
                        godina = records.get(i).getDatum().substring(0, 4);
                        mjesec = records.get(i).getDatum().substring(5, 7);
                        dan = records.get(i).getDatum().substring(8, 10);
                        dat = dan+"."+mjesec+"."+godina+".";
                        addRecord(dat, records.get(i).getIznos()+" kn");
                    }
                }
                else {
                    String catName = new String();
                    for (int i = 0; i < records.size(); i++) {
                        sequence = i;
                        catName = catIdName.get(records.get(i).getCatgoryId());
                        addRecord(catName, records.get(i).getIznos()+" kn");
                    }
                }
                listAdapter = new MyListAdapter(getActivity(), deptList);
                listView.setAdapter(listAdapter);


                listView.setOnChildClickListener(myListItemClicked);
            }
        }
        if(data instanceof Response){
            Response response = (Response) data;
            Log.w("response", response.getResponse());
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
            seqInt=  Integer.parseInt(String.valueOf(seq.getText()));

            update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //final Dialog dialog = new Dialog(getActivity());
                    dialog = new Dialog(getActivity());
                    dialog.setTitle("Ažuriranje prihoda/rashoda");
                    dialog.setContentView(R.layout.income_expense_item_layout);
                    dialog.show();

                    int vrsta;

                    spinner = (Spinner) dialog.findViewById(R.id.sp_kategorija);
                    dataBuilder.getCategories(userID());

                    napomena = (EditText) dialog.findViewById(R.id.napomena);
                    datum = (EditText) dialog.findViewById(R.id.datum);
                    iznos = (EditText) dialog.findViewById(R.id.iznos);
                    prihod = (RadioButton) dialog.findViewById(R.id.prihod);
                    rashod = (RadioButton) dialog.findViewById(R.id.rashod);

                    godina = records.get(seqInt).getDatum().substring(0, 4);
                    mjesec = records.get(seqInt).getDatum().substring(5, 7);
                    dan = records.get(seqInt).getDatum().substring(8, 10);
                    dat = dan+"."+mjesec+"."+godina+".";

                    napomena.setText(records.get(seqInt).getNapomena());
                    datum.setText(dat);
                    iznos.setText(records.get(seqInt).getIznos());
                    vrsta = Integer.parseInt(records.get(seqInt).getVrsta());

                    datePickerSetter();

                    datum.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            datePickerDialog.show();
                        }
                    });

                    System.out.println("recordId: "+records.get(seqInt).getId());

                    if (vrsta == 0){
                        rashod.setChecked(true);
                    }
                    else if(vrsta == 1){
                        prihod.setChecked(true);
                    }
                    System.out.println("edit record: "+seqInt);


                    Button ok = (Button) dialog.findViewById(R.id.ok);
                    Button cancel = (Button) dialog.findViewById(R.id.cancel);
                    ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Record_ record = new Record_();
                            Log.w("recordID", records.get(seqInt).getId());
                            Log.w("recordDate", datum.getText().toString());
                            record.setId(records.get(seqInt).getId());
                            record.setAktivan("1");

                            Category_ catId = (Category_) spinner.getSelectedItem();
                            record.setCatgoryId(catId.getId());

                           String dat1 = datum.getText().toString();
                            dan = dat1.substring(0,2);
                            mjesec = dat1.substring(3,5);
                            godina = dat1.substring(6,10);
                            dat = godina+"-"+mjesec+"-"+dan;

                            Log.w("datum1", dat);
                            Log.w("datum", datum.getText().toString());

                            record.setDatum(dat+ " 00:00:00");
                            record.setIznos(iznos.getText().toString());
                            record.setNapomena(napomena.getText().toString());

                            if(rashod.isChecked()){
                                record.setVrsta("false");
                            } else {
                                record.setVrsta("true");
                            }

                            dataBuilder.editRecord(record);
                            listAdapter.notifyDataSetChanged();
                            dataBuilder.getRecords(userID());
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
                    System.out.println("delete: "+seqInt);

                    Button ok = (Button) dialog.findViewById(R.id.ok);
                    Button cancel = (Button) dialog.findViewById(R.id.cancel);
                    ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dataBuilder.deleteRecord(records.get(seqInt).getId());
                            listAdapter.notifyDataSetChanged();
                            dataBuilder.getRecords(userID());
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

    private void datePickerSetter(){
        Calendar calendar = Calendar.getInstance();
        userFormat = new SimpleDateFormat("dd.MM.yyyy");
        databaseFormat = new SimpleDateFormat("yyyy-MM-dd");


        datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar date = Calendar.getInstance();
                date.set(year, month, dayOfMonth);
                datum.setText(userFormat.format(date.getTime()));
                dateAndTime = databaseFormat.format(date.getTime());
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
    }

    private String userID(){
        preferences = getActivity().getSharedPreferences("login", 0);
        String status = preferences.getString("id", "");
        return  status;
    }

}
