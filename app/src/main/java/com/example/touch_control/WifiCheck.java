package com.example.touch_control;
import com.example.touch_control.R;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

public class WifiCheck extends BroadcastReceiver {
    final static String TAG = WifiCheck.class.getName();

    @Override
    public void onReceive(final Context context, final Intent intent) {
        DefaultClient cont =(DefaultClient)context.getApplicationContext();
        Resources res = context.getResources();

        if(intent.getAction().equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {
            NetworkInfo networkInfo = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
            if(networkInfo.isConnected()) {
                // Wifi is connected
                Log.d(TAG, "Wifi is connected: " + String.valueOf(networkInfo));
                if(cont.getClient() != null){
                    cont.getClient().connectWithAsyncTask();
                }
            }
        } else if(intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            NetworkInfo networkInfo = intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
            if(networkInfo.getType() == ConnectivityManager.TYPE_WIFI && !networkInfo.isConnected()) {
                // Wifi is disconnected
                Log.d(TAG, "Wifi is disconnected: " + String.valueOf(networkInfo));
                if(cont.getClient() != null){
                    cont.getClient().close();
                }
            }
        }
    }
}

