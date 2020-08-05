package com.example.touch_control.AllActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.touch_control.DefaultClient;
import com.example.touch_control.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Settings extends AppCompatActivity implements View.OnClickListener {

    final String TAG = Settings.class.getName();
    private static final String IPADDRESS_PATTERN =
            "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
    private static final String PORT_PATTERN = "([0-9]{1,5})";




    Pattern patternIP = Pattern.compile(IPADDRESS_PATTERN);
    Pattern patternPort = Pattern.compile(PORT_PATTERN);
    EditText ip1;
    EditText port1;
    DefaultClient defaulter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        defaulter = (DefaultClient)getApplicationContext();


        setContentView(R.layout.activity_settings);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        findViewById(R.id.button51).setOnClickListener(this);
        ip1= (EditText) findViewById(R.id.txt21);
        port1= (EditText) findViewById(R.id.txt22);
        ip1.setText(defaulter.getIP());
        port1.setText(String.valueOf(defaulter.getPort()));
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }catch (NullPointerException npe){
            Log.i("test", npe.getMessage());
        }



    /*    FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        }); */
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button51:
                                StringBuffer strBuf = new StringBuffer("");
                                boolean dontMatch = false;
                                Matcher matcherIP = patternIP.matcher(ip1.getText());
                                Matcher matcherPort = patternPort.matcher(port1.getText());
                                Resources res = getResources();
                                if (!matcherIP.matches()) {
                                    // strBuf.append(res.getString(R.string.ip));
                                    dontMatch = true;
                                }


                                if (!matcherPort.matches()) {
                                    if (dontMatch) strBuf.append("\n");
                                    // strBuf.append(res.getString(R.string.port));
                                    dontMatch = true;
                                }

                                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                                builder.setTitle(res.getString(R.string.hold));
                                builder.setInverseBackgroundForced(true);
                                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                                if (dontMatch) {
                                    builder.setMessage(strBuf.toString());
                                    AlertDialog alert = builder.create();
                                    alert.show();
                                } else {
                                    Log.d("Settings",""+defaulter.getIP());
                                    Log.d("Settings",""+ip1.getText().toString());
                                  Log.d("Settings",""+String.valueOf(defaulter.getIP().equals(ip1.getText().toString())));
                                    if(!((defaulter.getIP().equals(ip1.getText().toString())) && (defaulter.getPort()==Integer.valueOf(port1.getText().toString()))) )
                                    {
                                        defaulter.setIp(ip1.getText().toString());
                                        defaulter.setPort(Integer.valueOf(port1.getText().toString()));
                                        boolean result = true;
                                        if (result) {

                                            builder.setMessage(res.getString(R.string.save));

                                            defaulter.getClient().close();
                                            Log.d("Settings","Check");


                                        } else {
                                            builder.setMessage(res.getString(R.string.error));
                                        }
                                        AlertDialog alert = builder.create();
                                        alert.show();
                                    }
                                    else{
                                        builder.setMessage("Duplicate String !");
                                        AlertDialog alert = builder.create();
                                        alert.show();
                                    }
                                }
                                break;

            default:
                Log.d(TAG, "This boutton dont have  onClick Listener. id = " + v.getId());
                break;
        }
    }

}
