package com.example.touch_control.AllActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.example.touch_control.DefaultClient;
import com.example.touch_control.R;
import com.example.touch_control.Commands;


enum SlideDirection {
    Right, Left
}

public class Slideshow extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {

    static final String TAG = Slideshow.class.getName();
    int PRIMARY_CODE_F5 = 135;
    int PRIMARY_CODE_ESC = 111;
    int PRIMARY_CODE_RIGHT = 22;
    int PRIMARY_CODE_LEFT = 21;

    float initX;
    long downTime;

    boolean started = false;
    Button power;

    DefaultClient defaulter4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slideshow);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        defaulter4 = (DefaultClient) getApplicationContext();

        power = (Button) findViewById(R.id.button42);
        power.setOnClickListener(this);
        power.setText(getResources().getString(R.string.powerpoint));
        started = false;

        findViewById(R.id.txt32).setOnTouchListener(this);


        final WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        int wifi = wifiManager.getWifiState();
        if (wifi != WifiManager.WIFI_STATE_ENABLED && wifi != WifiManager.WIFI_STATE_ENABLING) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(getResources().getString(R.string.hold));
            builder.setMessage(getResources().getString(R.string.wifienable));
            builder.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    wifiManager.setWifiEnabled(true);
                }
            });
            builder.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        }

        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (NullPointerException npe) {
            Log.i("test", npe.getMessage());
        }

   //     getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case android.R.id.home:

            /*  DefaultClient defaulter = (DefaultClient)getApplicationContext();
                if(defaulter.getClient() != null){
                    defaulter.getClient().close();

       //             startActivity(new Intent(Slideshow.this, MainActivity.class));
                }
                finish();  */
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed()
    {
        // code here to show dialog

    /*    DefaultClient defaulter2 = (DefaultClient)getApplicationContext();
        if(defaulter2.getClient() != null){
            defaulter2.getClient().close();

       //     startActivity(new Intent(Slideshow.this, MainActivity.class));
        } */
    //    finish();
    //   onDestroy();
        super.onBackPressed();  // optional depending on your needs
    }

   protected void onDestroy() {
        //     startActivity(new Intent(Touchpad.this, MainActivity.class));
        DefaultClient defaulter1 = (DefaultClient)getApplicationContext();
        if(defaulter1.getClient() != null){
            defaulter1.getClient().close();
        }
      //  finish();
             super.onDestroy();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button42:
                if (!started) {
                    started = true;
                    power.setText(getResources().getString(R.string.close));
                    Log.d(TAG, "This boutton dont have  onClick Listener. Minus");
                    defaulter4.getClient().sendCommandKeyAsync(
                            Commands.COMMAND_TYPE_KEY_PRESSED_RELEASED, PRIMARY_CODE_F5);
                } else {
                    started = false;
                    power.setText(getResources().getString(R.string.open));
                    Log.d(TAG, "This boutton dont have  onClick Listener. Plus");
                    defaulter4.getClient().sendCommandKeyAsync(
                            Commands.COMMAND_TYPE_KEY_PRESSED_RELEASED, PRIMARY_CODE_ESC);
                }
                break;

            default:
                Log.e(TAG, "This boutton dont have  onClick Listener. id = " + v.getId());
                break;
        }
    }

    public void createSlideEvent(SlideDirection slideDirection) {
        if (!started) {
            return;
        }
        Log.d(TAG, "Diff X = " + slideDirection);
        if (slideDirection == SlideDirection.Right) {
            defaulter4.getClient().sendCommandKeyAsync(
                    Commands.COMMAND_TYPE_KEY_PRESSED_RELEASED, PRIMARY_CODE_RIGHT);
        } else if (slideDirection == SlideDirection.Left) {
            defaulter4.getClient().sendCommandKeyAsync(
                    Commands.COMMAND_TYPE_KEY_PRESSED_RELEASED, PRIMARY_CODE_LEFT);
        } else {
            Log.e(TAG, "This SlideDirection is not defined = " + slideDirection);
        }

    }

    public boolean onTouch(View v, MotionEvent me) {
        switch (v.getId()) {
            case R.id.txt32:
                if (me.getAction() == MotionEvent.ACTION_DOWN) {
                    initX = me.getX();
                    downTime = me.getEventTime();
                } else if (me.getAction() == MotionEvent.ACTION_UP) {
                    float diffX = me.getX() - initX;
                    long diffTime = me.getEventTime() - downTime;
                    float speed = diffX / diffTime;
                    if (Math.abs(speed) > 1.0) {
                        if (speed > 0) {
                            createSlideEvent(SlideDirection.Right);
                        } else {
                            createSlideEvent(SlideDirection.Left);
                        }
                    }
                }
                break;
            default:
                Log.e(TAG, "There is not onTouch action for Id = = " + v.getId());
                break;
        }
        return true;

    }

    @Override
    protected void onStop() {
        defaulter4.getClient().sendCommandKeyAsync(
                Commands.COMMAND_TYPE_KEY_PRESSED_RELEASED, PRIMARY_CODE_ESC);
        super.onStop();
    }
}
