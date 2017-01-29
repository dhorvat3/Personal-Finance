package com.hr.foi.personalfinance.fragments;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.hr.foi.personalfinance.R;
import com.hr.foi.personalfinance.adapter.MyListAdapter;
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
import pojo.Category_;
import pojo.Response;

/**
 * Created by Filip on 23.12.2016..
 */

public class Category extends BaseFragment implements FragmentInterface, DataInterface{
    private ExpandableListView listView;
    private EditText name;
    private EditText description;
    private DataBuilder dataBuilder = new DataBuilder(this);
    private Category_ category;
    private LinkedHashMap<String, HeaderInfo> myCategories = new LinkedHashMap<String, HeaderInfo>();
    private ArrayList<HeaderInfo> deptList = new ArrayList<HeaderInfo>();
    private MyListAdapter listAdapter;
    private SharedPreferences preferences;
    private int seqInt;
    private Dialog dialog;
    private  ArrayList<Category_> categories;
    private int sequence = 0;


    public static final Category newInstance(String name){
        Category f = new Category();
        f.setName(name);

        return f;
    }

    @Override
    public BaseFragment getFragment() {   return this;    }

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
                            category = new Category_();
                            category.setUserId(userID());
                            category.setTitle(name.getText().toString());
                            category.setDescription(description.getText().toString());
                            dataBuilder.newCategory(category);

                            sequence++;
                            dataBuilder.getCategories(userID());
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

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        String userId = userID();

        dataBuilder.getCategories(userId);
    }

    @Override
    public void buildData(Object data) {
        if(data instanceof pojo.Category) {
            pojo.Category category1 = (pojo.Category) data;
            if (category1 != null) {
                listView = (ExpandableListView) getActivity().findViewById(R.id.kategorije);

                categories = new ArrayList<Category_>();

                myCategories.clear();
                deptList.clear();


                for (Category_ item : category1.getCategory()) {
                    categories.add(item);
                }

                for (int i = 0; i < categories.size(); i++) {
                    sequence = i;
                    addCategory(categories.get(i).getTitle(), categories.get(i).getDescription());
                }
                listAdapter = new MyListAdapter(getActivity(), deptList);
                listView.setAdapter(listAdapter);
                listView.setOnChildClickListener(myListItemClicked);
            }
        }
        if(data instanceof Response) {
            Response response = (Response) data;
            switch (response.getId()){
                case "1":
                    dataBuilder.getCategories(userID());
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

    private ExpandableListView.OnChildClickListener myListItemClicked = new ExpandableListView.OnChildClickListener() {
        @Override
        public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
            HeaderInfo headerInfo = deptList.get(groupPosition);
            DetailInfo detailInfo =  headerInfo.getCategoryList().get(childPosition);

            LinearLayout linearLayout =(LinearLayout)  getActivity().findViewById(R.id.update_delete_category);
            linearLayout.setVisibility(View.VISIBLE);

            ImageButton update = (ImageButton) linearLayout.findViewById(R.id.update);
            ImageButton delete = (ImageButton) linearLayout.findViewById(R.id.delete);

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

                            Category_ category = new Category_();

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

    private String userID(){
        preferences = getActivity().getSharedPreferences("login", 0);
        String status = preferences.getString("id", "");
        return  status;
    }
}
