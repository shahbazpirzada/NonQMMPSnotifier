package com.example.stech.printercloudapp.appconfig;

import android.app.Application;

import com.orm.SugarContext;

/**
 * Created by Stech on 11/14/2017.
 */

public class AppConfig extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        SugarContext.init(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        SugarContext.terminate();
    }
}
