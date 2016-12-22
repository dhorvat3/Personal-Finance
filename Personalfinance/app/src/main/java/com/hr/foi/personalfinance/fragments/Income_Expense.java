package com.hr.foi.personalfinance.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hr.foi.personalfinance.R;
import com.hr.foi.userinterface.BaseFragment;
import com.hr.foi.userinterface.FragmentInterface;

/**
 * Created by Filip on 22.12.2016..
 */

public class Income_Expense extends BaseFragment implements FragmentInterface {

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

        return view;
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
    }
}
