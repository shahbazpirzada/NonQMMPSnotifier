package com.example.stech.printercloudapp.ipintervalmodel;

import com.orm.SugarRecord;

/**
 * Created by Stech on 11/20/2017.
 */

public class IpAddressInverval extends SugarRecord {


    public String IpAddress;
    public String Interval;

    public String getIpAddress() {
        return IpAddress;
    }

    public void setIpAddress(String ipAddress) {
        IpAddress = ipAddress;
    }

    public String getInterval() {
        return Interval;
    }

    public void setInterval(String interval) {
        Interval = interval;
    }

    public IpAddressInverval(){


    }
}
