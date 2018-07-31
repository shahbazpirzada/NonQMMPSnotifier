package com.example.stech.printercloudapp.stat_categorymodel;

import com.example.stech.printercloudapp.branchmodel.Dim_Branch;
import com.orm.SugarRecord;

/**
 * Created by Stech on 1/12/2018.
 */

public class Stat_Category extends SugarRecord {

    String name;
    int locked;
    Dim_Branch dim_branch;

    public Stat_Category(){}

    public Stat_Category(String name, int locked, Dim_Branch dim_branch){

        this.name = name;
        this.locked = locked;
        this.dim_branch = dim_branch;

    }




    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLocked() {
        return locked;
    }

    public void setLocked(int locked) {
        this.locked = locked;
    }

    public Dim_Branch getDim_branch() {
        return dim_branch;
    }

    public void setDim_branch(Dim_Branch dim_branch) {
        this.dim_branch = dim_branch;
    }




}
