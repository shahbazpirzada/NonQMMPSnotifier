package com.example.stech.printercloudapp;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stech.printercloudapp.ServiceClass.JobServiceClass;
import com.example.stech.printercloudapp.branchmodel.Dim_Branch;
import com.example.stech.printercloudapp.fact_dash_snapshotmodel.fact_dash_snapshot;
import com.example.stech.printercloudapp.ipintervalmodel.IpAddressInverval;
import com.example.stech.printercloudapp.stat_branch_categorymodel.Stat_Branch_Category;
import com.example.stech.printercloudapp.stat_categorymodel.Stat_Category;
import com.example.stech.printercloudapp.ticketalertmodel.Ticket;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.Base64;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;

import javax.net.ssl.HttpsURLConnection;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.protocol.HTTP;
import cz.msebera.android.httpclient.util.EntityUtils;
import io.hypertrack.smart_scheduler.Job;
import io.hypertrack.smart_scheduler.SmartScheduler;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.internal.DiskLruCache;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final int JOB_ID = 1;

    String Ip = "";
    String Interval = "";

    TextView PrinterStatus, PrinterMsg, CloudStatus, CloudMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        PrinterStatus = (TextView) findViewById(R.id.PrinterStatus);
        PrinterMsg = (TextView) findViewById(R.id.PrinterMsg);
        CloudStatus = (TextView) findViewById(R.id.CloudStatus);
        CloudMsg = (TextView) findViewById(R.id.CloudMsg);


        //nevigations
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        //get the interval values from Database
        LoadDatabaseData();

    }


    public static void fillDatabase() {

        Dim_Branch dim_branch = new Dim_Branch(1, "Aun", 0);
        dim_branch.save();


        Dim_Branch dim_branch1 = Dim_Branch.findById(Dim_Branch.class, 1);


        Stat_Category stat_category = new Stat_Category("Transfer", 0, dim_branch1);
        stat_category.save();

        stat_category = new Stat_Category("Account", 0, dim_branch1);
        stat_category.save();

        stat_category = new Stat_Category("Plot Verfication", 0, dim_branch1);
        stat_category.save();

        stat_category = new Stat_Category("Maintenace", 0, dim_branch1);
        stat_category.save();


        Stat_Category statCategory = Stat_Category.findById(Stat_Category.class, 1);
        Stat_Category statCategory2 = Stat_Category.findById(Stat_Category.class, 2);
        Stat_Category statCategory3 = Stat_Category.findById(Stat_Category.class, 3);
        Stat_Category statCategory4 = Stat_Category.findById(Stat_Category.class, 4);


        Stat_Branch_Category sbc = new Stat_Branch_Category(1, dim_branch1, 1, "Transfer", statCategory, 0);
        sbc.save();
        sbc = new Stat_Branch_Category(1, dim_branch1, 2, "Account", statCategory2, 0);
        sbc.save();
        sbc = new Stat_Branch_Category(1, dim_branch1, 3, "Plot Verfication", statCategory3, 0);
        sbc.save();
        sbc = new Stat_Branch_Category(1, dim_branch1, 4, "Maintenace", statCategory4, 0);
        sbc.save();
//
//
//        fact_dash_snapshot fds = new fact_dash_snapshot();
//        fds.save();

        //Toast.makeText(MainActivity.class, "Data Successfully Added", Toast.LENGTH_SHORT).show();
    }

    //Delete All Data
    public static void emptyDatabase() {
        Dim_Branch.deleteAll(Dim_Branch.class);
        fact_dash_snapshot.deleteAll(fact_dash_snapshot.class);
        Stat_Branch_Category.deleteAll(Stat_Branch_Category.class);
        Stat_Category.deleteAll(Stat_Category.class);


    }


    public void LoadDatabaseData() {
        List<IpAddressInverval> listIp = IpAddressInverval.listAll(IpAddressInverval.class);
        for (IpAddressInverval ips : listIp) {
            Ip = ips.getIpAddress();
            Interval = ips.getInterval();
            System.out.println("Ip " + Ip);
            System.out.println("Interval is " + Interval);
        }
    }


    public void gotoServicePage(View view) {
        //get the interval values from Database
        LoadDatabaseData();


        if (Ip.matches("") || Interval.matches("")) {

            Toast.makeText(this, "Please Fill the Parametters", Toast.LENGTH_SHORT).show();
        }

        if (Interval.matches("")) {

            Toast.makeText(this, "Please Fill the Parametters", Toast.LENGTH_SHORT).show();

        } else {

            startService(new Intent(this, JobServiceClass.class));
        }


    }

    private void removePeriodicJob() {

        SmartScheduler jobScheduler = SmartScheduler.getInstance(this);
        if (!jobScheduler.contains(JOB_ID)) {
            Toast.makeText(MainActivity.this, "No job exists with JobID: " + JOB_ID, Toast.LENGTH_SHORT).show();
            return;
        }

        if (jobScheduler.removeJob(JOB_ID)) {
            Toast.makeText(MainActivity.this, "Job successfully removed!", Toast.LENGTH_SHORT).show();
        }
    }

    //Navigation
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.newItem1) {
            Intent intent = new Intent(this, SecondActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void onResetSchedulerClick(MenuItem item) {
        SmartScheduler smartScheduler = SmartScheduler.getInstance(getApplicationContext());
        smartScheduler.removeJob(JOB_ID);
        Toast.makeText(this, "Service has been Stoped!", Toast.LENGTH_SHORT).show();
        //        Button btnstart = (Button) findViewById(R.id.button);
        //        btnstart.setVisibility(View.VISIBLE);


        //        smartJobButton.setText(getString(R.string.schedule_job_btn));
        //        smartJobButton.setEnabled(true);
        //        smartJobButton.setAlpha(1.0f);
    }

    @Override
    protected void onResume() {

        // Action2 to filter
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(JobServiceClass.my_intent_service1);
        intentFilter.addAction(JobServiceClass.my_intent_service2);

        registerReceiver(receiver, intentFilter);
        super.onResume();

    }

    @Override
    public void onPause(){

        super.onPause();

        ActivityManager activityManager = (ActivityManager) getApplicationContext()
                .getSystemService(Context.ACTIVITY_SERVICE);

        activityManager.moveTaskToFront(getTaskId(), 0);
        unregisterReceiver(receiver);

    }

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(JobServiceClass.my_intent_service1)){
                Bundle bundle = intent.getExtras();
                if (bundle != null) {
                    int resultCode = bundle.getInt(JobServiceClass.Result);
                    String Message = bundle.getString("Name");

                    if (resultCode == RESULT_OK) {

                        PrinterStatus.setText("Connected");
                        PrinterStatus.setTextColor(Color.parseColor("#008000"));
                        PrinterMsg.setText(Message);


                    } else {

                        PrinterStatus.setText("Disconnected");
                        PrinterStatus.setTextColor(Color.parseColor("#FF0000"));
                        PrinterMsg.setText(Message);

                    }
                }

            }
            else if(intent.getAction().equals(JobServiceClass.my_intent_service2)){

                Bundle bundle = intent.getExtras();
                if (bundle != null) {
                    int resultCode = bundle.getInt(JobServiceClass.Result);
                    String Message = bundle.getString("Name");

                    if (resultCode == RESULT_OK) {

                        CloudStatus.setText("Connected");
                        CloudStatus.setTextColor(Color.parseColor("#008000"));
                        CloudMsg.setText(Message);


                    } else {

                        CloudStatus.setText("Disconnected");
                        CloudStatus.setTextColor(Color.parseColor("#FF0000"));
                        CloudMsg.setText(Message);

                    }
                }

            }
        }
    };

}
