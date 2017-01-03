package com.hr.foi.personalfinance.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.hr.foi.personalfinance.R;
import com.hr.foi.userinterface.BaseFragment;
import com.hr.foi.userinterface.FragmentInterface;

import java.util.ArrayList;
import java.util.Arrays;

import entities.DataBuilder;
import entities.DataInterface;
import pojo.Category_;

/**
 * Created by Filip on 23.12.2016..
 */

public class Category extends BaseFragment implements FragmentInterface, DataInterface{
    private ListView listView;
    private EditText name;
    private  EditText description;
    private DataBuilder dataBuilder = new DataBuilder(this);
    private Category_ category;

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

                        category = new Category_();

                        category.setUserId("2");
                        category.setTitle(name.getText().toString());
                        category.setDescription(description.getText().toString());
                        dataBuilder.newCategory(category);
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



        dataBuilder.getCategories(1);
    }

    @Override
    public void buildData(Object data) {
        pojo.Category category1 = (pojo.Category) data;
        if (category1 != null) {
           listView = (ListView) getActivity().findViewById(R.id.kategorije);
            String[] items = {"prvi", "drugi", "treci", "prvi", "drugi", "treci", "prvi", "drugi", "treci", "prvi", "drugi", "treci"};
            ArrayList<Category_> categories = new ArrayList<Category_>();

            for (Category_ item : category1.getCategory()){
                categories.add(item);
            }

            String[] listItems = new String[categories.size()];
            for (int i=0; i<categories.size(); i++){
                listItems[i] = categories.get(i).getTitle();
            }

            System.out.println(category1);
            System.out.println(categories.size());
            ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, items);
            listView.setAdapter(adapter);
        }
    }
}
