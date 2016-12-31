package com.hr.foi.personalfinance.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.icu.text.AlphabeticIndex;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.hr.foi.personalfinance.MainActivity;
import com.hr.foi.personalfinance.R;
import com.hr.foi.userinterface.BaseFragment;
import com.hr.foi.userinterface.FragmentInterface;
import com.squareup.okhttp.Response;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import entities.DataBuilder;
import entities.DataInterface;

/**
 * Created by Filip on 23.12.2016..
 */

public class Category extends BaseFragment implements FragmentInterface, DataInterface{
    private DataBuilder dataBuilder = new DataBuilder(this);
    private ListView listView;

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

        dataBuilder.getCategories(1);
    }

    @Override
    public void buildData(Object data) {
        pojo.Category category = (pojo.Category) data;
        if (category != null){
            listView = (ListView) getActivity().findViewById(R.id.kategorije);
            String[] items = {"prvi", "drugi", "treci", "prvi", "drugi", "treci", "prvi", "drugi", "treci", "prvi", "drugi", "treci",};
            ArrayList arrayList = new ArrayList(Arrays.asList(((pojo.Category) data).getTitle()));
            ArrayAdapter adapter = new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1, arrayList);
            listView.setAdapter(adapter);

        }
    }

}
