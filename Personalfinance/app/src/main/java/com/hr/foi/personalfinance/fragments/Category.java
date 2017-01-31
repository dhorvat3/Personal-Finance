package com.hr.foi.personalfinance.fragments;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.hr.foi.personalfinance.R;
import com.hr.foi.personalfinance.adapters.MyListAdapter;
import com.hr.foi.personalfinance.info.DetailInfo;
import com.hr.foi.personalfinance.info.HeaderInfo;
import com.hr.foi.userinterface.BaseFragment;
import com.hr.foi.userinterface.FragmentInterface;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import core.DataBuilder;
import core.DataInterface;
import pojo.Categories;
import pojo.Response;

/**
 * Created by Filip on 23.12.2016..
 */

/**
 * Klasa za rad s kategorijama. Sadrzi metode za kreiranje novih te azuriranje, brisanje, dohvacanje
 * i prikaz postojecih kategorija.
 */
public class Category extends BaseFragment implements FragmentInterface, DataInterface{

    /**
     * ExpandableListView za prikaz kategorija
     */
    private ExpandableListView listView;

    /**
     * Naziv kategorije
     */
    private EditText name;

    /**
     * Opis kategorije
     */
    private EditText description;

    /**
     * Za rad s bazom podataka
     */
    private DataBuilder dataBuilder = new DataBuilder(this);

    /**
     * Jedna instanca kategorije za tip liste
     */
    private pojo.Category category;
    private LinkedHashMap<String, HeaderInfo> myCategories = new LinkedHashMap<String, HeaderInfo>();
    private ArrayList<HeaderInfo> deptList = new ArrayList<HeaderInfo>();
    private MyListAdapter listAdapter;

    /**
     * Korisnicke opcije. Za dohvacanje user_id atributa
     */
    private SharedPreferences preferences;

    /**
     * Redni broj u listi
     */
    private int seqInt;

    /**
     * Za dodavanje nove kategorije
     */
    private Dialog dialog;

    /**
     * Lista kategorija tipa Category
     */
    private  ArrayList<pojo.Category> categories;

    /**
     * Brojac
     */
    private int sequence = 0;

    int mCurrentScrollState;
    int mCurrentVisibleItemCount;

    /**
     * Konstruktor
     * @param name Naziv
     * @return Kategorija
     */
    public static final Category newInstance(String name){
        Category f = new Category();
        f.setName(name);

        return f;
    }

    @Override
    public BaseFragment getFragment() {   return this;    }

    /**
     * Upravljac dogadjaja za dodavanje nove kategorije
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.category_layout, container, false);

        Button button = (Button) view.findViewById(R.id.addCategory);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(getActivity());
                dialog.setTitle("Nova kategorija");
                dialog.setContentView(R.layout.category_input_layout);
                dialog.show();

                Button cancel = (Button) dialog.findViewById(R.id.category_cancel);
                Button submit = (Button) dialog.findViewById(R.id.category_ok);
                cancel.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });
                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        name = (EditText) dialog.findViewById(R.id.category_name);
                        description = (EditText) dialog.findViewById(R.id.category_description);
                        boolean valid = true;

                        List<EditText> fields = Arrays.asList(name, description);

                        for (Iterator<EditText> i = fields.iterator(); i.hasNext();) {
                            EditText field = i.next();

                            if (field.getText().toString().isEmpty()) {
                                field.setError("Obavezno polje");

                                valid = false;
                            }
                        }
                        if (valid) {
                            category = new pojo.Category();
                            category.setUserId(userID());
                            category.setTitle(name.getText().toString());
                            category.setDescription(description.getText().toString());
                            dataBuilder.newCategory(category);

                            sequence++;
                        }

                        name.setText("");
                        description.setText("");
                        dialog.cancel();
                    }
                });

            }
        });

        return view;
    }

    /**
     * Dohvacanje liste kategorija iz baze podataka za aktivnog korisnika
     * @param view
     * @param savedInstanceState
     */
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        String userId = userID();

        dataBuilder.getCategories(userId);
    }

    /**
     * Priprema i obrada podataka iz odgovora web servisa
     * @param data Odgovor web servisa
     */
    @Override
    public void buildData(Object data) {
        if(data instanceof Categories) {
            Categories category1 = (Categories) data;
            if (category1 != null) {

                listView = (ExpandableListView) getActivity().findViewById(R.id.kategorije);


                categories = new ArrayList<pojo.Category>();

                myCategories.clear();
                deptList.clear();


                for (pojo.Category item : category1.getCategory()) {
                    categories.add(item);
                }

                for (int i = 0; i < categories.size(); i++) {
                    sequence = i;
                    addCategory(categories.get(i).getTitle(), categories.get(i).getDescription());
                }
                listAdapter = new MyListAdapter(getActivity(), deptList);

                listView.setAdapter(listAdapter);
                listAdapter.notifyDataSetChanged();
                listView.invalidateViews();
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
        if(data instanceof Response) {
            Response response = (Response) data;
            if(Integer.parseInt(response.getId()) > 0){
                dataBuilder.getCategories(userID());
                Toast.makeText(getActivity(), "Uspješno", Toast.LENGTH_SHORT).show();
            }

            switch (response.getId()){
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
     * Upravljaci dogadjaja za brisanje i azuriranje kategorija
     * Prikaz detalja o kategoriji
     */
    private ExpandableListView.OnChildClickListener myListItemClicked = new ExpandableListView.OnChildClickListener() {
        @Override
        public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

            LinearLayout linearLayout =(LinearLayout)  getActivity().findViewById(R.id.update_delete_category);
            linearLayout.setVisibility(View.VISIBLE);

            Button button = (Button) getActivity().findViewById(R.id.addCategory);

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            params.addRule(RelativeLayout.ABOVE, R.id.update_delete_category);
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
                    dialog.setTitle("Ažuriranje kategorija");
                    dialog.setContentView(R.layout.category_item_layout);
                    dialog.show();

                    final EditText naslov = (EditText) dialog.findViewById(R.id.title);
                    final EditText opis = (EditText) dialog.findViewById(R.id.description);

                    naslov.setText(categories.get(seqInt).getTitle());
                    opis.setText(categories.get(seqInt).getDescription());

                    Button save = (Button) dialog.findViewById(R.id.save);
                    Button close = (Button) dialog.findViewById(R.id.close);

                    save.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            boolean valid = true;
                            List<EditText> fieldsE = Arrays.asList(naslov, opis);

                            pojo.Category category = new pojo.Category();

                            for (Iterator<EditText> i = fieldsE.iterator(); i.hasNext(); ) {
                                EditText field = i.next();

                                if (field.getText().toString().isEmpty()) {
                                    field.setError("Obavezno polje");
                                    valid = false;
                                }
                            }

                            if (valid){
                                category.setId(categories.get(seqInt).getId());
                                category.setTitle(naslov.getText().toString());
                                category.setDescription(opis.getText().toString());

                                dataBuilder.editCategory(category);
                                dialog.cancel();
                            }
                        }
                    });
                    close.setOnClickListener(new View.OnClickListener() {
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
                            dataBuilder.deleteCategory(categories.get(seqInt).getId(),userID());
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
     * Dodavanje nove kategorije
     * @param name Naziv kategorije
     * @param description Opis kategorije
     * @return Indeks kategorije
     */
    private int addCategory(String name, String description){
        int groupPosition = 0;

        HeaderInfo headerInfo = myCategories.get(name);

        if(headerInfo == null){
            headerInfo = new HeaderInfo();
            headerInfo.setName(name);
            myCategories.put(name, headerInfo);
            deptList.add(headerInfo);
        }

        ArrayList<DetailInfo> categoryList = headerInfo.getCategoryList();

        DetailInfo detailInfo = new DetailInfo();
        detailInfo.setSequence(String.valueOf(sequence));
        detailInfo.setName(description);
        categoryList.add(detailInfo);
        headerInfo.setCategoryList(categoryList);

        groupPosition = deptList.indexOf(headerInfo);
        return groupPosition;
    }

    /**
     * Dohvacanje user_id atributa iz SharedPreferences
     */
    private String userID(){
        preferences = getActivity().getSharedPreferences("login", 0);
        String status = preferences.getString("id", "");
        return  status;
    }
}
