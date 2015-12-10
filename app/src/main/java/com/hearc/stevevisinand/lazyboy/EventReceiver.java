package com.hearc.stevevisinand.lazyboy;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by stevevisinand on 26.11.15.
 */
public class EventReceiver extends BroadcastReceiver
{
    private WifiInfo wifiConnected;


    @Override
    public void onReceive(Context context, Intent intent) {

        //test
        /*Bundle extras = intent.getExtras();
        if (extras != null) {
            for (String key: extras.keySet()) {
                Log.v("WIFI-EVENT", "key [" + key + "]: " +
                        extras.get(key));
            }
        }
        else {
            Log.v("WIFI-EVENT", "no extras");
        }
        */

        final ConnectivityManager connMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        final NetworkInfo networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        final WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        final WifiInfo connectionInfo = wifiManager.getConnectionInfo();

        Log.i("EVENT-RECEIVER", "NETWORK INFO : "+networkInfo.toString());

        if(networkInfo.getState() == NetworkInfo.State.CONNECTED) {
            this.wifiConnected = connectionInfo;
            Log.i("EVENT-RECEIVER", "WIFI - Connected in SSID : " +this.wifiConnected.getSSID());


            Toast toast = Toast.makeText(context, "STALKER MODE ON - Connected in SSID : " +this.wifiConnected.getSSID(), Toast.LENGTH_LONG);
            toast.show();

        }
        else if(networkInfo.getState() == NetworkInfo.State.DISCONNECTED && this.wifiConnected!=null){
            Log.i("EVENT-RECEIVER", "WIFI - Disconnected in SSID : " +this.wifiConnected.getSSID());


            Toast toast = Toast.makeText(context, "STALKER MODE ON - Disconnected in SSID : " +this.wifiConnected.getSSID(), Toast.LENGTH_LONG);
            toast.show();

            this.wifiConnected = null;
        }



        //Log.i("WIFI-EVENT", context.toString() + "  " + intent.toString());
    }
}
