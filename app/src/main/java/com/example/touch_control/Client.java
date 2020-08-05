package com.example.touch_control;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.*;
import java.net.UnknownHostException;
import java.sql.Connection;
import com.example.touch_control.R;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import com.example.touch_control.AllActivity.MainActivity;

public class Client{
    private static final String TAG = Client.class.getName();
    private static SocketAddress address;
    private Socket socket = null;
    private PrintWriter outPrintWriter = null;
    private BufferedReader inReader = null;
    private Context context;
    private int timeout = 2000;

    public Client(Context context) {
        this.context = context;
    }



    synchronized private boolean connect() {
        if (socket == null || !socket.isConnected()) {

            Log.d(TAG, "connect in");
            try {
                Log.d(TAG, "connect in1");
                address = new InetSocketAddress(((DefaultClient)context).getIP(), ((DefaultClient)context).getPort());
                Log.d(TAG, "connect in3");
                socket = new Socket();
                Log.d(TAG, "connect in4");
                socket.connect(address,timeout);
                Log.d(TAG, "connect in2");
                socket.setSoTimeout(2000);

                outPrintWriter = new PrintWriter(socket.getOutputStream(), true);

                inReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                Log.d(TAG, "connect in5");
                MainActivity.mine(true);
                return true;
            } catch (UnknownHostException ex) {
                Log.e(TAG, "Error: connect, UnknownHostException, exeption = " + ex.getMessage());
            } catch (IOException ex) {
                Log.e(TAG, "Error: connect, IOException, exeption = " + ex.getMessage());
            } catch (Exception ex)
            {
                Log.e(TAG, "Error: connect, exeption = " + ex.getMessage());
            }
            Log.d(TAG, "connect out");
            return false;


        } else {
            return true;
        }
    }

// synchronized
    synchronized public void connectWithAsyncTask() {
        Log.d(TAG, "connect start");
        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected void onPostExecute(Boolean aBoolean) {
                Log.d("Check", "connect start2");
                super.onPostExecute(aBoolean);
                Log.d("Check", "connect start3");
                if (!aBoolean) {
                    Toast.makeText(context, context.getResources().getString(R.string.connected_dont_msg), Toast.LENGTH_SHORT).show();
                    MainActivity.mine(false);
                } else {
                    Toast.makeText(context, context.getResources().getString(R.string.connected_msg), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            protected Boolean doInBackground(Void... voids) {

                return connect();
            }
        }.execute();
    }



    synchronized private String sendRequest(String command) {
        String strFromServer = null;
        try {
            if (outPrintWriter != null) {
                outPrintWriter.println(command);
            } else {
                if (connect()) {
                    outPrintWriter.println(command);
                }
            }

            if (inReader != null && (strFromServer = inReader.readLine()) != null) {
                Log.d(TAG, "from server is working = " + strFromServer);
            } else {
                Log.d(TAG, "from server is not working = " + strFromServer);
                close();
            }
        } catch (IOException ioe) {
            Log.e(TAG, "Error: sendRequest, IOException, exeption = " + ioe.getMessage());
        }
        return strFromServer;
    }

    synchronized public void sendCommandKeyAsync(int commandType, int key) {
        new AsyncTask<Integer, Void, String>() {
            @Override
            protected String doInBackground(Integer... params) {
                String commandStr = params[0] + "," + params[1];
                Log.d(TAG, commandStr);
                return sendRequest(commandStr);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

            }
        }.execute(commandType, key);

    }

    synchronized public void sendRequestAsync(String command) {
        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... params) {
                return sendRequest(params[0]);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

            }
        }.execute(command);

    }

    public boolean close() {

        try {
            if (outPrintWriter != null) {
                outPrintWriter.close();
                outPrintWriter = null;
            }
            if (inReader != null) {
                inReader.close();
                inReader = null;
            }
            if (socket != null) {
                socket.close();
                socket = null;
            }
            Log.d(TAG, "clint closed");
            return true;
        } catch (IOException ex) {
             ex.printStackTrace();
            return false;
        }
    }
}

