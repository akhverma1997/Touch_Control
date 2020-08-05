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
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.view.inputmethod.InputMethodManager;
import com.example.touch_control.Commands;
import com.example.touch_control.R;
import com.example.touch_control.DefaultClient;

public class Touchpad extends AppCompatActivity implements View.OnTouchListener{

    View viewTouchPad;
    Button btnLeft;
    Button btnRight;
    Button btnKeyBoard;
 //   PCKeyboardView pcKeyboardView;
    View touchPadButtonPanel;
    String TAG = "MyTest";
    static float xInit = (float) 0.0;
    static float yInit = (float) 0.0;
    static float yInitScroll = (float) 0.0;
    long downTime = 0;
    long upTime = 0;
    boolean check=false;
    boolean btnLeftIsLongPressed = false;

//    private boolean keyBoardVisible = false;

    DefaultClient defaulter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        defaulter = (DefaultClient) getApplicationContext();
        setContentView(R.layout.activity_touchpad);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab21);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(check==false)
                {
                    check=true;
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);

                }
                else
                {
                        check = false;
                        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);

                }
            }
        });



        //  pcKeyboardView = (PCKeyboardView) findViewById(R.id.pcKeyboardView);
     //   pcKeyboardView.setParentActivity(this);
 //       touchPadButtonPanel = findViewById(R.id.txt31);
        viewTouchPad = this.findViewById(R.id.txt31);
        btnLeft = (Button) this.findViewById(R.id.button51);
        btnRight = (Button) this.findViewById(R.id.button61);
  //      btnKeyBoard = (Button) this.findViewById(R.id.btnKeyBoard);
        viewTouchPad.setOnTouchListener(this);
        btnLeft.setOnTouchListener(this);
        btnRight.setOnTouchListener(this);
  //      btnKeyBoard.setOnClickListener(this);
 //       findViewById(R.id.viewScrollPad).setOnTouchListener(this);

        final WifiManager wifiManager = (WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);
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
        }else{
            defaulter.getClient().connectWithAsyncTask();
        }


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void first(View view) {

    }



    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case android.R.id.home:
 //               startActivity(new Intent(Touchpad.this, MainActivity.class));
                DefaultClient defaulter1 = (DefaultClient)getApplicationContext();
                if(defaulter1.getClient() != null){
                    defaulter1.getClient().close();
                }
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void onDestroy(View view) {
   //     startActivity(new Intent(Touchpad.this, MainActivity.class));
        DefaultClient defaulter1 = (DefaultClient)getApplicationContext();
        if(defaulter1.getClient() != null){
            defaulter1.getClient().close();
        }
        finish();
  //     super.onDestroy();
    }

    public void setBtnLongPress(boolean press){
        if(press){
            btnLeftIsLongPressed = true;
            defaulter.getClient().sendCommandKeyAsync(Commands.COMMAND_TYPE_MOUSE_PRESSED, Commands.MOUSE_KEY_LEFT);
        }else{
            btnLeftIsLongPressed = false;
            btnLeft.setPressed(false);
            defaulter.getClient().sendCommandKeyAsync(Commands.COMMAND_TYPE_MOUSE_RElEASED, Commands.MOUSE_KEY_LEFT);
        }
    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.d("Touchpad",""+keyCode+"------");
        if (keyCode>=0 && keyCode != KeyEvent.KEYCODE_BACK) {
            Log.d("Touchpad", "" + keyCode + "------");
            ((DefaultClient) this.getApplicationContext()).getClient().sendCommandKeyAsync(Commands.COMMAND_TYPE_KEY_PRESSED_RELEASED, keyCode);
        }
        else if(keyCode == KeyEvent.KEYCODE_BACK)
        {

            Log.d("Touchpad", "Test2------"+check);
            if(check !=false)
            {
            //    check=false;
  //              startActivity(new Intent(Touchpad.this, MainActivity.class));
                Log.d("Touchpad", "Test1------");
                DefaultClient defaulter2 = (DefaultClient)getApplicationContext();
                if(defaulter2.getClient() != null){

                    defaulter2.getClient().close();
                }
                finish();
            }
             //   myView.performClick();
            else
            {
 //               startActivity(new Intent(Touchpad.this, MainActivity.class));
                Log.d("Touchpad", "Test1------");
                DefaultClient defaulter2 = (DefaultClient)getApplicationContext();
                if(defaulter2.getClient() != null){

                    defaulter2.getClient().close();
                }
                finish();
            }
        }
        else if(keyCode == KeyEvent.KEYCODE_1)
        {

        }
        return super.onKeyDown(keyCode, event); }



    public boolean onTouch(View view, MotionEvent me) {

        switch (view.getId()) {
            case R.id.txt31:
                float x = me.getX();
                float y = me.getY();
                if (me.getAction() == MotionEvent.ACTION_DOWN) {
                    Touchpad.xInit = x;
                    Touchpad.yInit = y;
                    downTime = me.getEventTime();
                } else if (me.getAction() == MotionEvent.ACTION_UP) {
                    upTime = me.getEventTime();
                    long diff = upTime - downTime;
                    Log.d(TAG, "Diff time = " + diff);

                    if (diff < 100) {
                        if(btnLeftIsLongPressed){
                            setBtnLongPress(false);
                        }else{
                            defaulter.getClient().sendCommandKeyAsync(Commands.COMMAND_TYPE_MOUSE_PRESSED,Commands.MOUSE_KEY_LEFT);
                            defaulter.getClient().sendCommandKeyAsync(Commands.COMMAND_TYPE_MOUSE_RElEASED , Commands.MOUSE_KEY_LEFT);
                        }
                    }
                } else if (me.getAction() == MotionEvent.ACTION_MOVE) {
                    int diffX = (int) (x - xInit);
                    int diffY = (int) (y - yInit);
                    if (diffX != 0 || diffY != 0) {
                        String commandStr = Commands.COMMAND_TYPE_MOUSE_MOVE + "," + diffX + "," + diffY;
                        Log.d(TAG, commandStr);
                        defaulter.getClient().sendRequestAsync(commandStr);
                    }

                    Touchpad.xInit = x;
                    Touchpad.yInit = y;
                    return true;
                }
                break;
/*            case R.id.viewScrollPad:
                float yScroll = me.getY();
                if (me.getAction() == MotionEvent.ACTION_DOWN) {
                    TouchPadActivity.yInitScroll = yScroll;
                }else if (me.getAction() == MotionEvent.ACTION_MOVE) {
                    int diffYScroll = (int) (yScroll - yInitScroll);
                    if (diffYScroll != 0) {
                        String commandStr = Commands.COMMAND_TYPE_MOUSE_WHEEL + "," + diffYScroll;
                        Log.d(TAG, commandStr);
                        cAndroidApplication.getClient().sendRequestAsync(commandStr);
                    }
                    TouchPadActivity.yInitScroll = yScroll;
                    return true;
                }
                break;   */
            case R.id.button51:
                String commandStr;
                if (me.getAction() == MotionEvent.ACTION_DOWN) {
                    btnLeft.setPressed(true);
                } else if (me.getAction() == MotionEvent.ACTION_UP) {
                    long diff = me.getEventTime()- me.getDownTime();
                    Log.d(TAG, "Dif == " + diff);
                    if(diff > 900){
                        if(!btnLeftIsLongPressed){
                            setBtnLongPress(true);
                        }
                    }else{
                        if(btnLeftIsLongPressed){
                            setBtnLongPress(false);
                        }else{
                            btnLeft.setPressed(false);
                            commandStr = Commands.COMMAND_TYPE_MOUSE_PRESSED + "," + Commands.MOUSE_KEY_LEFT;
                            Log.d(TAG, commandStr);
                            defaulter.getClient().sendRequestAsync(commandStr);
                            commandStr = Commands.COMMAND_TYPE_MOUSE_RElEASED + "," + Commands.MOUSE_KEY_LEFT;
                            Log.d(TAG, commandStr);
                            defaulter.getClient().sendRequestAsync(commandStr);
                        }
                    }
                }
                break;
            case R.id.button61:
                setBtnLongPress(false);
                if (me.getAction() == MotionEvent.ACTION_DOWN) {
                    btnRight.setPressed(true);
                } else if (me.getAction() == MotionEvent.ACTION_UP) {
                    commandStr = Commands.COMMAND_TYPE_MOUSE_PRESSED + "," + Commands.MOUSE_KEY_RIGHT;
                    Log.d(TAG, commandStr);
                    defaulter.getClient().sendRequestAsync(commandStr);
                    btnRight.setPressed(false);
                    commandStr = Commands.COMMAND_TYPE_MOUSE_RElEASED + "," + Commands.MOUSE_KEY_RIGHT;
                    Log.d(TAG, commandStr);
                    defaulter.getClient().sendRequestAsync(commandStr);
                }
                break;
            default:
                Log.e(TAG, "There is not onTouch action for Id = = " + view.getId());
                break;
        }
        return true;
    }

}
