package com.example.stech.printercloudapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

public class MainActivity extends AppCompatActivity implements SmartScheduler.JobScheduledCallback, NavigationView.OnNavigationItemSelectedListener {


    String str;
    ProgressBar pb;
    TextView textView;
    TextView updateDisp;

    private static final int JOB_ID = 1;
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String JOB_PERIODIC_TASK_TAG = "io.hypertrack.android_scheduler_demo.JobPeriodicTask";

    String Ip = "";
    String Interval = "";

    private String intervalInMillisEditText;

    JSONArray jsonArray;

    static final String UrlPost = "http://notifier.stech.com.pk:2337/save?format=json";

    // final String TenantId = "STECH-TICKET-ALERT-53763ec7-c3c8-4209-b597-a63f4eabdcc4";
    final String TenantId = "NORTHWINDNS-72e6d769-023f-435f-9786-4032dbea8710";

    private Activity activity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Progress bar code is here
        pb = (ProgressBar) findViewById(R.id.progressBar);
        pb.setVisibility(View.GONE);

        //for the textView of loading...
        textView = (TextView) findViewById(R.id.textView);
        updateDisp = (TextView) findViewById(R.id.textView5);
        updateDisp.setVisibility(View.GONE);
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

        //   emptyDatabase();
        //fillDatabase();


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

        //        Button btnstart = (Button) findViewById(R.id.button);
        //        btnstart.setVisibility(View.GONE);

        if (Ip.matches("") || Interval.matches("")) {

            Toast.makeText(this, "Please Fill the Parametters", Toast.LENGTH_SHORT).show();
        }

        //        if(Interval.matches("")){
        //
        //            Toast.makeText(this, "Please Fill the Parametters", Toast.LENGTH_SHORT).show();
        //
        //        }
        else {

            // CallApi();

            SmartScheduler jobScheduler = SmartScheduler.getInstance(this);

            // Check if any periodic job is currently scheduled
            if (jobScheduler.contains(JOB_ID)) {
                removePeriodicJob();
                return;
            }

            // Create a new job with specified params
            Job job = createJob();
            if (job == null) {
                Toast.makeText(MainActivity.this, "Invalid paramteres specified. " +
                        "Please try again with correct job params.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Schedule current created job
            if (jobScheduler.addJob(job)) {
                Toast.makeText(MainActivity.this, "Job successfully added!", Toast.LENGTH_SHORT).show();

            }

        }


    }


    //call the api and get the data

    public void CallApi() {

        //String AddressIp = "http://"+Ip+":5700/api/values";
        String AddressIp = "http://" + Ip + "/login/login.action?username=InstallAdmin&password=ulan&page=/touchscreens/getDisplayInfo3.qsp?display=1";


        new AsyncHttpClient().get(AddressIp, new TextHttpResponseHandler() {


            @Override
            public void onStart() {
                //  pb.setVisibility(View.VISIBLE);
                textView.setVisibility(View.VISIBLE);
                textView.setText("Loading....");

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                Toast.makeText(MainActivity.this, responseString, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {


                str = responseString;
                //   Toast.makeText(MainActivity.this, "Data Received", Toast.LENGTH_SHORT).show();

                try {
                    SpliterStringMethod(str);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                pb.setVisibility(View.GONE);
                textView.setVisibility(View.GONE);
            }


        });


    }

    //spli the strinh into the array
    public void SpliterStringMethod(String values) throws IOException, JSONException {

        fact_dash_snapshot.deleteAll(fact_dash_snapshot.class);

        if (values != null && !values.isEmpty()) {

            //unquote the string for the data..
            values = values.replaceAll("^\"|\"$", "").replace("\n", "").replace("\r", "").trim();

            //split on the baseis of '#'
            String[] lineString = values.split("#");

            for (int i = 0; i < lineString.length; i++) {
                double idTicket = 0;
                System.out.println(lineString[i]);

                String[] valuesString = lineString[i].split("\\|");
                System.out.println("inner loop");


                fact_dash_snapshot fds = new fact_dash_snapshot(Integer.parseInt(valuesString[0]),
                        Integer.parseInt(valuesString[1]),
                        Integer.parseInt(valuesString[2]),
                        Integer.parseInt(valuesString[3]),
                        Integer.parseInt(valuesString[4]),
                        Integer.parseInt(valuesString[5]),
                        Integer.parseInt(valuesString[6]),
                        Integer.parseInt(valuesString[7]),
                        Integer.parseInt(valuesString[8]));
                fds.save();
            }
        }


        // getTableData();
    }


    //fetch the data from the Database

    public void getTableData() {
        //call the ticket table to get the values

        List<fact_dash_snapshot> fdsAll = fact_dash_snapshot.listAll(fact_dash_snapshot.class);
        List<Dim_Branch> dm = Dim_Branch.listAll(Dim_Branch.class);
        List<Stat_Branch_Category> sbc = Stat_Branch_Category.listAll(Stat_Branch_Category.class);

        jsonArray = new JSONArray();
        JSONArray jsonArray1 = new JSONArray();
        JSONArray jsonArray2 = new JSONArray();
        JSONArray jsonArray3 = new JSONArray();

        for (int i = 0; i < fdsAll.size(); i++) {
            // JSONObject jGroup1 = new JSONObject();
            Map jGroup1 = new LinkedHashMap();
            jGroup1.put("branchid", fdsAll.get(i).getBranch_id());
            jGroup1.put("categoryid", fdsAll.get(i).getCategory_id());
            jGroup1.put("served", fdsAll.get(i).getServed());
            jGroup1.put("waiting", fdsAll.get(i).getWaiting());
            jGroup1.put("est_wait", fdsAll.get(i).getEst_wait());
            jGroup1.put("avg_wait", fdsAll.get(i).getAvg_wait());
            jGroup1.put("avg_trt", fdsAll.get(i).getAvg_trt());
            jGroup1.put("open_counters", fdsAll.get(i).getOpen_counter());
//            jGroup1.put("waiting_above_sl", fdsAll.get(i).getWaiting_above_sl());

            jsonArray1.put(new Gson().toJson(jGroup1, Map.class));

        }

        for (int i = 0; i < dm.size(); i++) {
            // JSONObject jGroup1 = new JSONObject();
            Map jGroup1 = new LinkedHashMap();
//            jGroup1.put("orgin_id", dm.get(i).getOrgin_id());
//            jGroup1.put("name", dm.get(i).getName());

            jGroup1.put("branchid", dm.get(i).getOrgin_id());
            jGroup1.put("branchname", dm.get(i).getName());

            // jGroup1.put("accessID", dm.get(i).getAccessID());


            jsonArray2.put(new Gson().toJson(jGroup1, Map.class));

        }


        for (int i = 0; i < sbc.size(); i++) {
            // JSONObject jGroup1 = new JSONObject();
            Map jGroup1 = new LinkedHashMap();

            jGroup1.put("categoryid", sbc.get(i).getCategory_no());
            jGroup1.put("branchid", sbc.get(i).getDim_branch().getId());
            jGroup1.put("categoryname", sbc.get(i).getCategory_name());


            jsonArray3.put(new Gson().toJson(jGroup1, Map.class));

        }

        jsonArray.put(jsonArray1);
        jsonArray.put(jsonArray2);
        jsonArray.put(jsonArray3);


        String str1 = POST(UrlPost, "");

    }


    public String POST(String url, String json) {
        enableStrictMode();
        HttpResponse response = null;
        HttpClient httpclient = null;
        httpclient = new DefaultHttpClient();

        HttpGet request = new HttpGet();


        InputStream inputStream = null;
        String result = "";
        try {

            // 2. make POST request to the given URL
            HttpPost httpPost = new HttpPost();

            // 4. convert JSONObject to JSON to String
            json = jsonArray.toString();
            json = json.replace("\\", "").replace("\"{", "{").replace("}\"", "}");

            String userName = "admin";
            String password = "@Stech78324";
            String base64EncodedCredentials = "Basic " + Base64.encodeToString(
                    (userName + ":" + password).getBytes(),
                    Base64.NO_WRAP);


            httpPost.setHeader("Authorization", base64EncodedCredentials);

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("TenantId", TenantId));
            nameValuePairs.add(new BasicNameValuePair("JsonData", json));

            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            httpPost.setURI(new URI(UrlPost));

            response = httpclient.execute(httpPost);
            boolean responseBody = Boolean.parseBoolean(EntityUtils.toString(response.getEntity()));
            if ((response.getStatusLine().getStatusCode() == HttpsURLConnection.HTTP_OK) && responseBody == true) {
                EntityUtils.consumeQuietly(response.getEntity());

                //Toast.makeText(MainActivity.this, "Updated Data", Toast.LENGTH_SHORT).show();

                updateDisp.setVisibility(View.VISIBLE);
                updateDisp.setText("Updated...");

                try {

                    request.setURI(new URI("http://notifier.stech.com.pk:2337/sendNotifications/" + TenantId));
                    updateDisp.setVisibility(View.GONE);
                    response = httpclient.execute(request);
                    EntityUtils.toString(response.getEntity());
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                } catch (ClientProtocolException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                EntityUtils.consumeQuietly(response.getEntity());
            }


            System.out.println(responseBody + " values is");


        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }


        // enableStrictMode();


        // 11. return result
        return result;
    }


    public void enableStrictMode() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
    }


    //for the time schedular functions

    private Job createJob() {
        int jobType = 3;
        boolean isPeriodic = true;
        Long miliSecondsInterval = Long.parseLong(Interval) * 1000;
        intervalInMillisEditText = Long.toString(miliSecondsInterval);

        String intervalInMillisString = intervalInMillisEditText;
        if (TextUtils.isEmpty(intervalInMillisString)) {
            return null;
        }

        Long intervalInMillis = Long.parseLong(intervalInMillisString);
        Job.Builder builder = new Job.Builder(JOB_ID, this, jobType, JOB_PERIODIC_TASK_TAG)

                .setIntervalMillis(intervalInMillis);

        if (isPeriodic) {
            builder.setPeriodic(intervalInMillis);
        }

        return builder.build();
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

    @Override
    public void onJobScheduled(Context context, final Job job) {
        if (job != null) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //                    System.out.println("Shahbaz is running");
                    //                    Toast.makeText(MainActivity.this, "Job: " + job.getJobId() + " scheduled!", Toast.LENGTH_SHORT).show();
                    CallApi();
                    getTableData();
                }
            });
            Log.d(TAG, "Job: " + job.getJobId() + " scheduled!");


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

}
