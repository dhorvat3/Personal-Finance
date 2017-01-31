package com.hr.foi.personalfinance.adapters;

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

/**
 * Klasa MyListAdapter za rad s ExpandableList
 */
public class MyListAdapter extends BaseExpandableListAdapter{

    /**
     * Sustavske informacije
     */
    private Context context;

    /**
     * Za listu u ExpandableList
     */
    private ArrayList<HeaderInfo> deptList;

    /**
     * Za listu u ExpandableList
     */
    private ArrayList<HeaderInfo> originalList;

    /**
     * Konstruktor
     * @param context Android context
     * @param deptList Podaci o elementu
     */
    public MyListAdapter(Context context, ArrayList<HeaderInfo> deptList) {
        this.context = context;
        this.deptList = new ArrayList<HeaderInfo>();
        this.deptList.addAll(deptList);
        this.originalList = new ArrayList<HeaderInfo>();
        this.originalList.addAll(deptList);
    }

    /**
     * Vraca broj elemenata za prikazati u ExpandableList
     * @return Broj elemenata
     */
    @Override
    public int getGroupCount() {
        return deptList.size();
    }

    /**
     * Vraca broj elemenata liste
     * @param groupPosition Indeks elementa u listi
     * @return Broj elemenata liste
     */
    @Override
    public int getChildrenCount(int groupPosition) {
        ArrayList<DetailInfo> categoryList = deptList.get(groupPosition).getCategoryList();
        return categoryList.size();
    }

    /**
     * Vraca element liste
     * @param groupPosition Indeks elementa
     * @return Grupa
     */
    @Override
    public Object getGroup(int groupPosition) {
        return deptList.get(groupPosition);
    }

    /**
     * Dohvaca dijete za ExpandableList
     * @param groupPosition Indeks grupe
     * @param childPosition Indeks djeteta grupe
     * @return Dijete
     */
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

    /**
     * Dohvaca grupu
     * @param groupPosition Indeks grupe
     * @param isExpanded Je li grupa prosirena
     * @param view Glavni pogled
     * @param parent Roditelj djeteta
     * @return Pogled grupe
     */
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

    /**
     * Dohvaca dijete
     * @param groupPosition Indeks grupe
     * @param childPosition Indeks djeteta
     * @param isLastChild Je li zadnji u listi
     * @param view Glavni pogled
     * @param parent Roditelj djeteta
     * @return Pogled djeteta
     */
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

    /**
     * Filtriranje podataka prema uvjetu
     * @param query Uvjet
     */
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
