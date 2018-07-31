package com.example.stech.printercloudapp.ticketalertmodel;

import com.orm.SugarRecord;

/**
 * Created by Stech on 11/14/2017.
 */

public class Ticket extends SugarRecord {

    int track_no;
    int time_no;
    int ticket_no;
    int branch_id;
    int position_in_queue;

    public Ticket(int track_no,int time_no,int ticket_no,int branch_id,int position_in_queue ){
        this.track_no = track_no;
        this.time_no =time_no;
        this.ticket_no = ticket_no;
        this.branch_id = branch_id;
        this.position_in_queue = position_in_queue;

    }

    //defualt Constrctor
    public  Ticket(){


    }

    public int getTrack_no() {
        return track_no;
    }

    public void setTrack_no(int track_no) {
        this.track_no = track_no;
    }

    public int getTime_no() {
        return time_no;
    }

    public void setTime_no(int time_no) {
        this.time_no = time_no;
    }

    public int getTicket_no() {
        return ticket_no;
    }

    public void setTicket_no(int ticket_no) {
        this.ticket_no = ticket_no;
    }

    public int getBranch_id() {
        return branch_id;
    }

    public void setBranch_id(int branch_id) {
        this.branch_id = branch_id;
    }

    public int getPosition_in_queue() {
        return position_in_queue;
    }

    public void setPosition_in_queue(int position_in_queue) {
        this.position_in_queue = position_in_queue;
    }


}
