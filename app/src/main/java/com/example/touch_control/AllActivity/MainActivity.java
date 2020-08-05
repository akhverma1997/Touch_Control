package com.example.touch_control.AllActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import com.example.touch_control.DefaultClient;
import com.example.touch_control.R;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.content.Context;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import android.app.NotificationManager;
import android.support.v4.app.NotificationCompat;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DefaultClient defaulter;
    static TextView tview,tview1,tview2,txtview;
    private static Context mContext;
    Button butn,butn1,butn2;
    EditText edtxt,edtxt1;
    private static boolean read=false;
    File outputFile,outputFile1;
    DateTimeFormatter formatter;
    long diff;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }
        defaulter = (DefaultClient)getApplicationContext();
        setContentView(R.layout.activity_main);
  //      edtxt=(EditText) findViewById(R.id.txt21);
  //      edtxt1=(EditText) findViewById(R.id.txt22);
        tview=(TextView)findViewById(R.id.txt41);
   //     tview1=(TextView)findViewById(R.id.textView13);

        txtview=(TextView)findViewById(R.id.textView3);
        butn=(Button)findViewById(R.id.button09);
        butn1=(Button)findViewById(R.id.button);
  //      butn2=(Button)findViewById(R.id.button51);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mContext = this;
    /*    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab31);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });  */

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);

        try {

            formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            outputFile1 = new File("/sdcard/Android/data/com.example.touch_control/files");
            outputFile = new File("/sdcard/Android/data/com.example.touch_control/files/"+ LocalDate.now().format(formatter)+".txt");
            if (!outputFile1.exists())
            {
                outputFile1.mkdirs();

                if (!outputFile.exists())
                {
                    try
                    {
                        outputFile.createNewFile();
                    }
                    catch (IOException e2)
                    {
                        // TODO Auto-generated catch block
                        e2.printStackTrace();
                    }
                }

            }
            else
            {
                if (!outputFile.exists())
                {
                    try
                    {
                        outputFile.createNewFile();
                    }
                    catch (IOException e2)
                    {
                        // TODO Auto-generated catch block
                        e2.printStackTrace();
                    }
                }
            }

            DeleteFiles(outputFile1);
            Runtime.getRuntime().exec("logcat -c");
            Runtime.getRuntime().exec("logcat -v time -f " + outputFile.getAbsolutePath());
       //     outputFile=new File();
      //      Log.d("Message1",""+String.valueOf(outputFile.getAbsolutePath()));
       //     Runtime.getRuntime().exec("logcat -f" + outputFile.getAbsolutePath());
        } catch (IOException e3) {
            e3.printStackTrace();
        }
    }

    private void DeleteFiles(File f) {
        if (f.isDirectory()) {
            System.out.println("Date Modified : " + f.lastModified());
            for (File f1 : f.listFiles()) {
                Log.d("Message2","Directory doesn't exists : "+f1);
                DeleteFiles(f1);
            }
        } else {
            diff = new Date().getTime() - f.lastModified();

            if (diff > 10 * 24 * 60 * 60 * 1000) {
                f.delete();
            }
          //  f.delete();
          //  Log.d("Message2","Directory doesn't exists : "+outputFile1.getAbsolutePath());
        }
    }


    public void second(View view) {
        startActivity(new Intent(MainActivity.this, Settings.class));
    }

    public void fourth(View view) {
        try {
            read=false;
             defaulter.getClient().connectWithAsyncTask();
        }catch (Exception c1)
        {
            Log.d("MainActivity",""+c1);
        }
    }

    public void fifth(View view) {
        try {
         //   startActivity(new Intent(MainActivity.this, Slideshow.class));
            read=true;
            defaulter.getClient().connectWithAsyncTask();
        }catch (Exception c2)
        {
            Log.d("MainActivity",""+c2);
        }
    }


    public static void mine(boolean d)
    {
        if(read==false) {
            if (d == true) {
                tview.setVisibility(TextView.INVISIBLE);
                Intent login = new Intent(mContext, Touchpad.class);
                mContext.startActivity(login);
            } else {
                tview.setVisibility(TextView.VISIBLE);
                Log.d("Message",""+"Not Connected");
            }

        }
        else
        {
            if (d == true) {
                tview.setVisibility(TextView.INVISIBLE);
                Intent login1 = new Intent(mContext, Slideshow.class);
                mContext.startActivity(login1);
            } else
                tview.setVisibility(TextView.VISIBLE);
        }
    }

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
                startActivity(new Intent(MainActivity.this, Settings.class));
            return true;
        }
        if (id == R.id.about) {
            startActivity(new Intent(MainActivity.this, About.class));
            return true;
        }
        if (id == R.id.help) {
            startActivity(new Intent(MainActivity.this, Help.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        Log.d("Debug","Working........");
       int id = item.getItemId();

    //    int id=  R.id.nav_gallery;
        if (id == R.id.nav_camera) {
            // Handle the camera action
            startActivity(new Intent(MainActivity.this, MotionControl.class));
            Log.d("Debug","Working........");
        } else if (id == R.id.nav_gallery) {
            Log.d("Debug","Working1........");
            butn.setVisibility(View.INVISIBLE);
            butn1.setVisibility(View.VISIBLE);
            tview.setVisibility(TextView.INVISIBLE);
            txtview.setText("In Hand Touchpad");
        //    startActivity(new Intent(MainActivity.this, MainActivity.class));
        } else if (id == R.id.nav_slideshow) {
            Log.d("Debug","Working2........");
       //     startActivity(new Intent(MainActivity.this, MainActivity.class));
            butn.setVisibility(View.VISIBLE);
            butn1.setVisibility(View.INVISIBLE);
            tview.setVisibility(TextView.INVISIBLE);
            txtview.setText("Slideshow");
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        Log.d("Debug","Working3........");
        return true;
    }

}
