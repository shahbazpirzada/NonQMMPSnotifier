package com.example.stech.printercloudapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.stech.printercloudapp.ipintervalmodel.IpAddressInverval;

import java.util.List;

public class SecondActivity extends AppCompatActivity {

    EditText editipaddress;
    EditText editintervalVal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        //Prepopulate the data of TextField...

        String Ip="";
        String Interval="";

        List<IpAddressInverval> listIp = IpAddressInverval.listAll(IpAddressInverval.class);
        for (IpAddressInverval ips:listIp) {
            Ip= ips.getIpAddress();
            Interval = ips.getInterval();
            System.out.println("Ip"+Ip);
            System.out.println("Interval is"+Interval);
        }
        editipaddress = (EditText)findViewById(R.id.editIP);
        editintervalVal = (EditText)findViewById(R.id.editInterval);

        editipaddress.setText(Ip);
        editintervalVal.setText(Interval);

    }

    public void NewFunction(View view){



        long Count = IpAddressInverval.count(IpAddressInverval.class);



        if(editipaddress.getText().toString().matches("")) {

            Toast.makeText(this, "Please Fill the Parametters", Toast.LENGTH_SHORT).show();
        }

        if(editintervalVal.getText().toString().matches("")){

            Toast.makeText(this, "Please Fill the Parametters", Toast.LENGTH_SHORT).show();

        }

        else
        {

            // IpAddressInverval.deleteAll(IpAddressInverval.class);


            if(Count == 0){
                // for finders using raw query.
                List<IpAddressInverval> iplist = IpAddressInverval.findWithQuery(IpAddressInverval.class, "Select * from IP_ADDRESS_INVERVAL");

                double id= 0;

                for (IpAddressInverval ip1:iplist) {
                    id= ip1.getId();

                }

                System.out.println("fetching the id"+id);

                if(id==0) {

                    IpAddressInverval ipAddressInverval = new IpAddressInverval();
                    ipAddressInverval.setIpAddress(editipaddress.getText().toString());
                    ipAddressInverval.setInterval(editintervalVal.getText().toString());
                    ipAddressInverval.save();

                    Toast.makeText(this, "Data Saved", Toast.LENGTH_SHORT).show();
                }
            }

            else
            {
                List<IpAddressInverval> iplist = IpAddressInverval.findWithQuery(IpAddressInverval.class, "Select * from IP_ADDRESS_INVERVAL");

                double id= 0;

                for (IpAddressInverval ip1:iplist) {
                    id= ip1.getId();

                }

                IpAddressInverval ipini = IpAddressInverval.findById(IpAddressInverval.class, (int) id);
                ipini.setIpAddress(editipaddress.getText().toString());
                ipini.setInterval(editintervalVal.getText().toString());
                ipini.save(); // updates the previous entry with new values.
                Toast.makeText(this, "Updated Successfully", Toast.LENGTH_SHORT).show();
            }




        }


    }

    public void fillRequiredParametter(View view)
    {

            MainActivity.emptyDatabase();
            MainActivity.fillDatabase();

       // Toast.makeText(this, "Hy in button", Toast.LENGTH_SHORT).show();
    }
}
