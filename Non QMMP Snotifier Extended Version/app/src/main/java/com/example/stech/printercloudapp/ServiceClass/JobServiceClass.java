package com.example.stech.printercloudapp.ServiceClass;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.stech.printercloudapp.MainActivity;
import com.example.stech.printercloudapp.branchmodel.Dim_Branch;
import com.example.stech.printercloudapp.fact_dash_snapshotmodel.fact_dash_snapshot;
import com.example.stech.printercloudapp.ipintervalmodel.IpAddressInverval;
import com.example.stech.printercloudapp.stat_branch_categorymodel.Stat_Branch_Category;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.Base64;
import com.loopj.android.http.SyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.util.EntityUtils;
import io.hypertrack.smart_scheduler.Job;
import io.hypertrack.smart_scheduler.SmartScheduler;

public class JobServiceClass extends Service implements SmartScheduler.JobScheduledCallback  {



    public static final int JOB_ID = 1;
    public static final String TAG = MainActivity.class.getSimpleName();
    public static final String JOB_PERIODIC_TASK_TAG = "io.hypertrack.android_scheduler_demo.JobPeriodicTask";


    public String Ip = "";
    public String Interval = "";

    public String intervalInMillisEditText;

    public JSONArray jsonArray;

    public JSONArray GlobalJsonArray;

    public static final String UrlPost = "http://notifier.stech.com.pk:2337/save?format=json";
   // public final String TenantId = "NORTHWINDNS-72e6d769-023f-435f-9786-4032dbea8710";
    public String TenantId = "ANDROIDMONITOR-SQL-3038763d-9d6b-4f0e-b560-8d8aab0f2d5a";

    public String dataGet;

    public static final String my_intent_service1 = "com.exampledemo.parsaniahardik.scanbarcodeqrdemonuts1";
    public static final String my_intent_service2 = "com.exampledemo.parsaniahardik.scanbarcodeqrdemonuts2";

    public static final String Result ="";

    private int resultActivity = Activity.RESULT_CANCELED;




    public JobServiceClass() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        //get the interval values from Database
        LoadDatabaseData();
        GlobalJsonArray = new JSONArray();
        if (Ip.matches("") || Interval.matches("")) {

            Toast.makeText(this, "Please Fill the Parametters", Toast.LENGTH_SHORT).show();
        } else {

            SmartScheduler jobScheduler = SmartScheduler.getInstance(this);

            // Check if any periodic job is currently scheduled
            if (jobScheduler.contains(JOB_ID)) {
                removePeriodicJob();
                //return;
            }

            // Create a new job with specified params
            Job job = createJob();
            if (job == null) {
//                Toast.makeText(MainActivity.this, "Invalid paramteres specified. " +
//                        "Please try again with correct job params.", Toast.LENGTH_SHORT).show();

                System.out.println("Invalid paramteres specified. Please try again with correct job params.");


                // return;
            }

            // Schedule current created job
            if (jobScheduler.addJob(job)) {
                Toast.makeText(getApplicationContext(), "Job successfully added!", Toast.LENGTH_SHORT).show();
                System.out.println("Job successfully added!");

            }

        }


        return START_STICKY;
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

    //for the time schedular functions

    public Job createJob() {
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

    public void removePeriodicJob() {

        SmartScheduler jobScheduler = SmartScheduler.getInstance(this);
        if (!jobScheduler.contains(JOB_ID)) {
            //  Toast.makeText(MainActivity.this, "No job exists with JobID: " + JOB_ID, Toast.LENGTH_SHORT).show();

            System.out.println("No job exists with JobID: " + JOB_ID);
            return;
        }

        if (jobScheduler.removeJob(JOB_ID)) {
//            Toast.makeText(MainActivity.this, "Job successfully removed!", Toast.LENGTH_SHORT).show();
            System.out.println("Job successfully removed!");
        }
    }

    @Override
    public void onJobScheduled(Context context, final Job job) {
        if (job != null) {
            new MainActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {


                    CallApi();
                    getTableData();

                }
            });

        }
    }


    //call the api and get the data

    public void CallApi() {

        //String AddressIp = "http://"+Ip+":5700/api/values";
        String AddressIp = "http://" + Ip + "/login/login.action?username=InstallAdmin&password=ulan&page=/touchscreens/getDisplayInfo3.qsp?display=1";


        new SyncHttpClient().get(AddressIp, new TextHttpResponseHandler() {


            @Override
            public void onStart() {


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                System.out.println("Error message" + responseString);
                resultActivity  = Activity.RESULT_CANCELED;
                String msg = "Failed";
                printResultToMainAcitivty(my_intent_service1,resultActivity,msg);

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {


                String  str = responseString;

                resultActivity  = Activity.RESULT_OK;
                String msg = "Success";
                printResultToMainAcitivty(my_intent_service1,resultActivity,msg);

                //   Toast.makeText(MainActivity.this, "Data Received", Toast.LENGTH_SHORT).show();

                try {
                    SpliterStringMethod(str);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }


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


        POST(UrlPost);

    }


    public void POST(String url) {

        HttpResponse response = null;
        HttpClient httpclient = null;
        httpclient = new DefaultHttpClient();

        HttpGet request = new HttpGet();

        String json = "";
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

                resultActivity = Activity.RESULT_OK;
                String message = "Success";
                printResultToMainAcitivty(my_intent_service2,resultActivity,message);

                EntityUtils.consumeQuietly(response.getEntity());

                try {

                    request.setURI(new URI("http://notifier.stech.com.pk:2337/sendNotifications/" + TenantId));

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

            resultActivity = Activity.RESULT_CANCELED;
            String message = "Failed";
            printResultToMainAcitivty(my_intent_service2, resultActivity,message);

        }

    }

    private void printResultToMainAcitivty(String intentValue,int resultCode, String Msg) {

        Intent intent = new Intent(intentValue);
        intent.putExtra(Result,resultCode);
        intent.putExtra("Name",Msg);
        intent.putExtra("check",intentValue);

        sendBroadcast(intent);

    }




}
