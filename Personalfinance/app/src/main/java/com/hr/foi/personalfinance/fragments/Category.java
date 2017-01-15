package com.hr.foi.personalfinance.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.icu.util.TimeZone;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Spinner;

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

import entities.DataBuilder;
import entities.DataInterface;
import entities.User;
import pojo.Category_;

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

                            dataBuilder.getCategories(userID());
                            int groupPosition = addCategory(name.getText().toString(), description.getText().toString());
                            listAdapter.notifyDataSetChanged();
                            listView.setSelectedGroup(groupPosition);
                        }

                        name.setText("");
                        description.setText("");
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
        pojo.Category category1 = (pojo.Category) data;
        if (category1 != null) {
           listView = (ExpandableListView) getActivity().findViewById(R.id.kategorije);

            ArrayList<Category_> categories = new ArrayList<Category_>();

            myCategories.clear();
            deptList.clear();


            for (Category_ item : category1.getCategory()){
                categories.add(item);
            }

            for (int i=0; i<categories.size(); i++){
                addCategory(categories.get(i).getTitle(), categories.get(i).getDescription());
            }
            listAdapter = new MyListAdapter(getActivity(), deptList);
            listView.setAdapter(listAdapter);
        }
    }

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
