package com.hr.foi.personalfinance.info;

import java.util.ArrayList;

/**
 * Created by Filip on 4.1.2017..
 */

public class HeaderInfo {
    private String name;
    private ArrayList<DetailInfo> categoryList = new ArrayList<DetailInfo>();;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public ArrayList<DetailInfo> getCategoryList() {
        return categoryList;
    }
    public void setCategoryList(ArrayList<DetailInfo> categoryList) {
        this.categoryList = categoryList;
    }
}
