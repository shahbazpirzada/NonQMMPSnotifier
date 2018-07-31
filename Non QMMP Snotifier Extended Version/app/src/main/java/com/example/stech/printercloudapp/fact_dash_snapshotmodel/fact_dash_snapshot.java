package com.example.stech.printercloudapp.fact_dash_snapshotmodel;

import com.orm.SugarRecord;

/**
 * Created by Stech on 1/15/2018.
 */

public class fact_dash_snapshot extends SugarRecord {


    int branch_id;
    int category_id;
    int served;
    int waiting;
    int est_wait;
    int avg_wait;
    int avg_trt;
    int open_counter;
    int waiting_above_sl;

    public fact_dash_snapshot() {
    }

    public int getBranch_id() {
        return branch_id;
    }

    public void setBranch_id(int branch_id) {
        this.branch_id = branch_id;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public int getServed() {
        return served;
    }

    public void setServed(int served) {
        this.served = served;
    }

    public int getWaiting() {
        return waiting;
    }

    public void setWaiting(int waiting) {
        this.waiting = waiting;
    }

    public int getEst_wait() {
        return est_wait;
    }

    public void setEst_wait(int est_wait) {
        this.est_wait = est_wait;
    }

    public int getAvg_wait() {
        return avg_wait;
    }

    public void setAvg_wait(int avg_wait) {
        this.avg_wait = avg_wait;
    }

    public int getAvg_trt() {
        return avg_trt;
    }

    public void setAvg_trt(int avg_trt) {
        this.avg_trt = avg_trt;
    }

    public int getOpen_counter() {
        return open_counter;
    }

    public void setOpen_counter(int open_counter) {
        this.open_counter = open_counter;
    }

    public int getWaiting_above_sl() {
        return waiting_above_sl;
    }

    public void setWaiting_above_sl(int waiting_above_sl) {
        this.waiting_above_sl = waiting_above_sl;
    }



    public fact_dash_snapshot(int branch_id, int category_id, int served, int waiting, int est_wait, int avg_wait, int avg_trt, int open_counter, int waiting_above_sl) {
        this.branch_id = branch_id;
        this.category_id = category_id;
        this.served = served;
        this.waiting = waiting;
        this.est_wait = est_wait;
        this.avg_wait = avg_wait;
        this.avg_trt = avg_trt;
        this.open_counter = open_counter;
        this.waiting_above_sl = waiting_above_sl;
    }
}
