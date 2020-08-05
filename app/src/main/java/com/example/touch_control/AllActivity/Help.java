package com.example.touch_control.AllActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.touch_control.R;

import java.io.File;

public class Help extends AppCompatActivity {

    EditText edit,edit1,edit2 ;
    Button startBtn;
    File outputFile2;
    Uri path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        edit = (EditText) findViewById(R.id.txt71);
        edit1 = (EditText) findViewById(R.id.txt72);
        edit2 = (EditText) findViewById(R.id.txt73);
        startBtn = (Button) findViewById(R.id.button52);
        outputFile2 = new File("/sdcard/Android/data/com.example.touch_control/files");
        startBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                sendEmail();
            }
        });

        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }catch (NullPointerException npe){
            Log.i("test", npe.getMessage());
        }

    }

    protected void sendEmail() {
        Log.i("Send email", "");
        String[] TO = {"akhverma1997@gmail.com"};
        String[] CC = {""};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        if (outputFile2.isDirectory()) {
            for (File f1 : outputFile2.listFiles()) {
                Log.d("Message2","Directory doesn't exists : "+f1);
                path = Uri.fromFile(f1);
                emailIntent .putExtra(Intent.EXTRA_STREAM, path);
            }
        }

        String mailtext= "Phone Number : "+edit1.getText().toString()+"\n"+"Query :  "+edit2.getText().toString();

        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Query from "+edit.getText().toString());
        emailIntent.putExtra(Intent.EXTRA_TEXT, mailtext);

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            finish();
            Log.i("Finished sending email.", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(Help.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
