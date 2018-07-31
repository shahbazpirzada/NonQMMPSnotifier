package com.example.stech.printercloudapp.branchmodel;

import com.orm.SugarRecord;

/**
 * Created by Stech on 1/12/2018.
 */

public class Dim_Branch extends SugarRecord {
    int orgin_id;
    String name;
    int accessID;


    public Dim_Branch(int orgin_id, String name, int accessID)
    {

        this.orgin_id = orgin_id;
        this.name = name;
        this.accessID = accessID;
    }


    public Dim_Branch(){


    }

    public int getOrgin_id() {
        return orgin_id;
    }

    public void setOrgin_id(int orgin_id) {
        this.orgin_id = orgin_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAccessID() {
        return accessID;
    }

    public void setAccessID(int accessID) {
        this.accessID = accessID;
    }
}
