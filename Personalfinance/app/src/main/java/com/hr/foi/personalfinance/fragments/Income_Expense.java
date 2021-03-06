package com.hr.foi.personalfinance.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.hr.foi.personalfinance.R;
import com.hr.foi.personalfinance.adapters.MyListAdapter;
import com.hr.foi.personalfinance.info.DetailInfo;
import com.hr.foi.personalfinance.info.HeaderInfo;
import com.hr.foi.userinterface.BaseFragment;
import com.hr.foi.userinterface.FragmentInterface;
import com.raizlabs.android.dbflow.sql.language.Condition;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import core.DataBuilder;
import core.DataInterface;
import pojo.*;
import pojo.Category;

/**
 * Created by Filip on 22.12.2016..
 */

/**
 * Klasa Income_Expense za rad s popisom korisnickih zapisa iz DB
 */
public class Income_Expense extends BaseFragment implements FragmentInterface, DataInterface {

    /**
     * Jedan zapis tipa Record
     */
    private Record record;

    /**
     * Za rad s bazom podataka
     */
    private DataBuilder dataBuilder = new DataBuilder(this);

    /**
     * Korisnicke opcije
     */
    private SharedPreferences preferences;

    /**
     * Za prikaz liste zapisa s detaljima
     */
    private ExpandableListView listView;

    /**
     * Zapisi specificnog korisnika
     */
    private LinkedHashMap<String, HeaderInfo> myRecords = new LinkedHashMap<String, HeaderInfo>();

    /**
     * Za prikaz na ExpandableListView
     */
    private ArrayList<HeaderInfo> deptList = new ArrayList<HeaderInfo>();

    /**
     * Za prikaz na ExpandableListView
     */
    private MyListAdapter listAdapter;

    /**
     * GUI elementi
     */
    private EditText napomena, datum, iznos;

    /**
     * GUI elementi za odabir tipa
     */
    private RadioButton prihod, rashod;

    /**
     * Lista zapisa tipa Record
     */
    private ArrayList<Record> records;

    /**
     * Lista zapisa u odgovoru web servisa
     */
    private ArrayList<Category> catList;

    /**
     * Za azuriranje i brisanje zapisa
     */
    private Dialog dialog;

    /**
     * Za odabir kategorije
     */
    private Spinner spinner;

    /**
     * Indeks za dohvacanje kategorije iz liste
     */
    private int seqInt;

    /**
     * Instanca kategorije
     */
    private Category myItem = null;

    /**
     * Priprema podataka za MyListAdapter
     */
    private ArrayAdapter<Category> adapter;

    /**
     * Odabir datuma
     */
    private DatePickerDialog datePickerDialog;

    /**
     * Razlicito formatiranje datuma za DB i aplikaciju
     */
    private SimpleDateFormat userFormat, databaseFormat;

    /**
     * Pomocna varijabla
     */
    private String dateAndTime;

    /**
     * Brojac
     */
    private int sequence = 0;

    /**
     * Formiranje datuma
     */
    private String godina, mjesec, dan, dat;

    int mCurrentScrollState;
    int mCurrentVisibleItemCount;



    public static final Income_Expense newInstance(String name){
        Income_Expense f = new Income_Expense();
        f.setName(name);

        return f;
    }
    @Override
    public BaseFragment getFragment() {
        return this;
    }

    /**
     * Dohvacanje GUI elemnata i postavljanje upravljaca dogadjaja na gumbe
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
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

                datum = (EditText) dialog.findViewById(R.id.datum);
                datum.setFocusable(false);
                datePickerSetter();

                datum.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        datePickerDialog.show();
                    }
                });

                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        napomena = (EditText) dialog.findViewById(R.id.napomena);
                        iznos = (EditText) dialog.findViewById(R.id.iznos);
                        prihod = (RadioButton) dialog.findViewById(R.id.prihod);
                        rashod = (RadioButton) dialog.findViewById(R.id.rashod);
                        boolean valid = true;
                        List<EditText> fieldsE = Arrays.asList(napomena, iznos, datum);

                        Category catId = (Category) spinner.getSelectedItem();
                        //Log.w("CatID", catId.getId());

                        record = new Record();

                        if (prihod.isChecked()){
                            System.out.println("prihod");
                            record.setVrsta("1");
                            prihod.setError(null);
                        }
                        else if(rashod.isChecked()){
                            System.out.println("rashod");
                            record.setVrsta("0");
                            prihod.setError(null);
                        }
                        else {
                            prihod.setError("morate odabrati prihod ili rashod");
                            valid =false;
                        }

                        for (Iterator<EditText> i = fieldsE.iterator(); i.hasNext();) {
                            EditText field = i.next();

                            if (field.getText().toString().isEmpty()) {
                                field.setError("Obavezno polje");
                                valid = false;
                            }
                        }
                        if(valid) {
                            record.setNapomena(napomena.getText().toString());
                            record.setUserId(userID());
                            record.setAktivan("1");
                            if(catId.getId() == null) {
                                record.setCatgoryId(null);
                            }
                            else {
                                record.setCatgoryId(catId.getId());
                            }
                            record.setDatum(dateAndTime + " 00:00:00");
                            //record.setDatum("2001-12-01 00:00:00");
                            //System.out.println(record.getCatgoryId());
                            record.setIznos(iznos.getText().toString());

                            dataBuilder.newRecord(record);

                            sequence++;
                            int groupPosition = addRecord(record.getDatum(), record.getIznos());
                            listView.setSelectedGroup(groupPosition);
                            dialog.cancel();
                        }
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

    /**
     * Dohvacanje korisnickih zapisa i kategorija iz baze podataka
     * @param view
     * @param savedInstanceState
     */
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        String userId = userID();

        dataBuilder.getRecords(userId);
        dataBuilder.getCategories(userId);
    }


    /**
     * Obrada odogovora web servisa
     * Dohvacanje kategorij za spinner
     * Dohvacanje zapisa za listView
     * @param data Odgovor web servisa
     */
    @Override
    public void buildData(Object data) {

        if(data instanceof Categories){
            Categories categories = (Categories) data;
            catList = new ArrayList<>();

            Category noCat = new Category();
            noCat.setTitle("(NEMA)");
            noCat.setId(null);
            catList.add(noCat);

            for(Category item: categories.getCategory()){
                catList.add(item);
            }
            if(spinner != null) {
                adapter = new ArrayAdapter<Category>(getActivity(), android.R.layout.simple_spinner_dropdown_item, catList);
                spinner.setAdapter(adapter);

                if (myRecords.size() != 0){
                    String catID = records.get(seqInt).getCatgoryId();
                    if (catID != null) {
                        for (int i = 1; i < adapter.getCount(); i++) {
                            Category item = adapter.getItem(i);

                            if (item.getId().equals(catID)) {
                                myItem = item;
                                break;
                            }
                        }
                        spinner.setSelection(adapter.getPosition(myItem));
                    }
                    else{
                        spinner.setSelection(0);
                    }
                }
            }
        }

        if(data instanceof Records) {
            Records record = (Records) data;
            if (record != null) {
                listView = (ExpandableListView) getActivity().findViewById(R.id.zapisi);

                myRecords.clear();
                deptList.clear();

                records = new ArrayList<Record>();

                for (Record item : record.getRecord()) {
                    records.add(item);
                }

                String setRecordType = new String();
                for (int i = 0; i < records.size(); i++) {
                    sequence = i;
                    godina = records.get(i).getDatum().substring(0, 4);
                    mjesec = records.get(i).getDatum().substring(5, 7);
                    dan = records.get(i).getDatum().substring(8, 10);
                    dat = dan+"."+mjesec+"."+godina+".";
                    if (records.get(i).getVrsta().equals("1")){
                        setRecordType = records.get(i).getIznos()+" kn";
                    }
                    else if (records.get(i).getVrsta().equals("0")){
                        setRecordType ="-"+ records.get(i).getIznos()+" kn";
                    }
                    addRecord(dat, setRecordType);

                }
                listAdapter = new MyListAdapter(getActivity(), deptList);
                listView.invalidateViews();
                listView.setAdapter(listAdapter);


                listView.setOnChildClickListener(myListItemClicked);

                listView.setOnScrollListener(new AbsListView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(AbsListView view, int scrollState) {
                        mCurrentScrollState = scrollState;
                        if(mCurrentVisibleItemCount > 0 && mCurrentScrollState == SCROLL_STATE_IDLE){
                            RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                            params2.addRule(RelativeLayout.ABOVE, R.id.update_delete);
                            listView.setLayoutParams(params2);
                        }
                    }

                    @Override
                    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                        mCurrentVisibleItemCount = visibleItemCount;
                    }
                });
            }
        }
        if(data instanceof Response){
            Response response = (Response) data;
            switch (response.getId()){
                case "1":
                    dataBuilder.getRecords(userID());
                    Toast.makeText(getActivity(), "Uspješno", Toast.LENGTH_SHORT).show();
                    break;
                case "-1":
                    Toast.makeText(getActivity(), "Pogreška", Toast.LENGTH_SHORT).show();
                    break;
                case "-2":
                    Toast.makeText(getActivity(), "Prazno polje", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    /**
     * Upravljac dogadjaja za ExpandableListView
     * Azuriranje zapisa i spremanje u DB
     * Brisanje zapisa iz DB
     */
    private ExpandableListView.OnChildClickListener myListItemClicked =  new ExpandableListView.OnChildClickListener() {

        public boolean onChildClick(ExpandableListView parent, View v, final int groupPosition, final int childPosition, final long id) {

            LinearLayout linearLayout =(LinearLayout)  getActivity().findViewById(R.id.update_delete);
            linearLayout.setVisibility(View.VISIBLE);

            Button button = (Button) getActivity().findViewById(R.id.AddIncomeExpense);

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            params.addRule(RelativeLayout.ABOVE, R.id.update_delete);
            button.setLayoutParams(params);

            Button update = (Button) linearLayout.findViewById(R.id.update);
            Button delete = (Button) linearLayout.findViewById(R.id.delete);

            parent.getExpandableListAdapter().getChild(groupPosition, childPosition);
            final EditText seq = (EditText) v.findViewById(R.id.sequence);
            seqInt=  Integer.parseInt(String.valueOf(seq.getText()));

            update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog = new Dialog(getActivity());
                    dialog.setTitle("Ažuriranje prihoda/rashoda");
                    dialog.setContentView(R.layout.income_expense_item_layout);
                    dialog.show();



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
                    int vrsta = Integer.parseInt( records.get(seqInt).getVrsta());

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


                    Button ok = (Button) dialog.findViewById(R.id.ok);
                    Button cancel = (Button) dialog.findViewById(R.id.cancel);
                    ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            boolean valid = true;
                            List<EditText> fieldsE = Arrays.asList(napomena, iznos, datum);

                            Record record = new Record();

                            for (Iterator<EditText> i = fieldsE.iterator(); i.hasNext(); ) {
                                EditText field = i.next();

                                if (field.getText().toString().isEmpty()) {
                                    field.setError("Obavezno polje");
                                    valid = false;
                                }
                            }
                            if (valid) {
                                Log.w("recordID", String.valueOf(records.get(seqInt).getLocalId()));
                                Log.w("recordDate", datum.getText().toString());
                                record.setLocalId(records.get(seqInt).getLocalId());
                                record.setAktivan("1");

                                Category catId = (Category) spinner.getSelectedItem();
                                //record.setCatgoryId(catId.getId());
                                if (catId.getId() == null) {
                                    record.setCatgoryId(null);
                                } else {
                                    record.setCatgoryId(catId.getId());
                                }

                                String dat1 = datum.getText().toString();
                                dan = dat1.substring(0, 2);
                                mjesec = dat1.substring(3, 5);
                                godina = dat1.substring(6, 10);
                                dat = godina + "-" + mjesec + "-" + dan;

                                Log.w("datum1", dat);
                                Log.w("datum", datum.getText().toString());

                                record.setDatum(dat + " 00:00:00");
                                record.setIznos(iznos.getText().toString());
                                record.setNapomena(napomena.getText().toString());

                                if (rashod.isChecked()) {
                                    record.setVrsta("0");
                                } else {
                                    record.setVrsta("1");
                                }

                                dataBuilder.editRecord(record);
                                dialog.cancel();
                            }
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
                            dataBuilder.deleteRecord(String.valueOf(records.get(seqInt).getLocalId()));
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

    /**
     * Dodavanje novog zapisa u listu
     * @param name Naziv
     * @param description Opis
     * @return Indeks grupe
     */
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

    /**
     * Odabir datuma
     * Format zapisa
     */
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

    /**
     * Korisnicki user_id atribut iz SharedPreferences
     * @return user_id
     */
    private String userID(){
        preferences = getActivity().getSharedPreferences("login", 0);
        String status = preferences.getString("id", "");
        return  status;
    }

}
