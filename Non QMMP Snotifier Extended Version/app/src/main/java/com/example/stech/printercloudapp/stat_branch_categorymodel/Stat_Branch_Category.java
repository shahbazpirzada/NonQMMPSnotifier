package com.example.stech.printercloudapp.stat_branch_categorymodel;

import com.example.stech.printercloudapp.branchmodel.Dim_Branch;
import com.example.stech.printercloudapp.stat_categorymodel.Stat_Category;
import com.orm.SugarRecord;

/**
 * Created by Stech on 1/15/2018.
 */

public class Stat_Branch_Category extends SugarRecord {


    int active ;
    Dim_Branch dim_branch;             //int branch_id;
    int category_no;
    String category_name;
    Stat_Category stat_category; //int category_id;
    int dirty_flag;


    public Stat_Branch_Category(){}

    public Stat_Branch_Category(int active, Dim_Branch dim_branch, int category_no, String category_name, Stat_Category stat_category,
                                int dirty_flag){

        this.active= active;
        this.dim_branch = dim_branch;
        this.category_no = category_no;
        this.category_name = category_name;
        this.stat_category = stat_category;
        this.dirty_flag = dirty_flag;

    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public Dim_Branch getDim_branch() {
        return dim_branch;
    }

    public void setDim_branch(Dim_Branch dim_branch) {
        this.dim_branch = dim_branch;
    }

    public int getCategory_no() {
        return category_no;
    }

    public void setCategory_no(int category_no) {
        this.category_no = category_no;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public Stat_Category getStat_category() {
        return stat_category;
    }

    public void setStat_category(Stat_Category stat_category) {
        this.stat_category = stat_category;
    }

    public int getDirty_flag() {
        return dirty_flag;
    }

    public void setDirty_flag(int dirty_flag) {
        this.dirty_flag = dirty_flag;
    }



}
