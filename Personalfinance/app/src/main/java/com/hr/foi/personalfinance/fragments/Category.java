package com.hr.foi.personalfinance.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hr.foi.personalfinance.R;
import com.hr.foi.userinterface.BaseFragment;
import com.hr.foi.userinterface.FragmentInterface;

import java.util.ArrayList;

import entities.DataBuilder;
import entities.DataInterface;

/**
 * Created by Filip on 23.12.2016..
 */

public class Category extends BaseFragment implements FragmentInterface, DataInterface{
    private DataBuilder dataBuilder = new DataBuilder(this);

    public static final Category newInstance(String name){
        Category f = new Category();
        f.setName(name);

        return f;
    }

    @Override
    public BaseFragment getFragment() {   return this;    }

    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.category_layout, container, false);

        return view;
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void buildData(Object data) {
        pojo.Category category = (pojo.Category) data;
        if (category != null){
            System.out.println("bok");
        }
    }
}
