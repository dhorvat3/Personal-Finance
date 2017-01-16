package com.hr.foi.personalfinance.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.hr.foi.personalfinance.R;
import com.hr.foi.personalfinance.info.DetailInfo;
import com.hr.foi.personalfinance.info.HeaderInfo;

import java.util.ArrayList;

/**
 * Created by Filip on 4.1.2017..
 */

public class MyListAdapter extends BaseExpandableListAdapter{
    private Context context;
    private ArrayList<HeaderInfo> deptList;
    private ArrayList<HeaderInfo> originalList;

    public MyListAdapter(Context context, ArrayList<HeaderInfo> deptList) {
        this.context = context;
        this.deptList = new ArrayList<HeaderInfo>();
        this.deptList.addAll(deptList);
        this.originalList = new ArrayList<HeaderInfo>();
        this.originalList.addAll(deptList);
    }

    @Override
    public int getGroupCount() {
        return deptList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        ArrayList<DetailInfo> categoryList = deptList.get(groupPosition).getCategoryList();
        return categoryList.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return deptList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        ArrayList<DetailInfo> categoryList = deptList.get(groupPosition).getCategoryList();
        return categoryList.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View view, ViewGroup parent) {
        HeaderInfo headerInfo = (HeaderInfo) getGroup(groupPosition);
        if (view == null) {
            LayoutInflater inf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inf.inflate(R.layout.parent_layout, null);
        }

        TextView heading = (TextView) view.findViewById(R.id.heading);
        heading.setText(headerInfo.getName().trim());

        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View view, ViewGroup parent) {


            DetailInfo detailInfo = (DetailInfo) getChild(groupPosition, childPosition);
            if (view == null) {
                LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = infalInflater.inflate(R.layout.child_layout, null);
            }

            EditText sequence = (EditText) view.findViewById(R.id.sequence);
            sequence.setText(detailInfo.getSequence().trim());
            TextView childItem = (TextView) view.findViewById(R.id.childItem);
            childItem.setText(detailInfo.getName().trim());

            return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
    public void filterData(String query){
        deptList.clear();
        if (query.isEmpty()){
            deptList.addAll(deptList);
        }
        else{

            for (HeaderInfo headerInfo: originalList){
                ArrayList<HeaderInfo> newList = new ArrayList<>();
                    if (headerInfo.getName().contains(query)){
                        newList.add(headerInfo);
                    }
                if (newList.size() > 0){
                    deptList.add(headerInfo);
                }
            }

        }
        notifyDataSetChanged();
    }
}
