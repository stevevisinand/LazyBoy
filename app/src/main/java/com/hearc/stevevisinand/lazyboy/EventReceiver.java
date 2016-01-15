package com.hearc.stevevisinand.lazyboy;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.Toast;

import com.hearc.stevevisinand.lazyboy.Logic.Configuration;
import com.hearc.stevevisinand.lazyboy.Utilities.PersistanceUtils;

import java.util.ArrayList;

/**
 * Created by stevevisinand on 26.11.15.
 */
public class EventReceiver extends BroadcastReceiver
{
    private WifiInfo wifiConnected;
    private ArrayList<Configuration> configList;
    private LocationManager locationManager;

    private Double curentLongitude;
    private Double curentLatitude;
    private Context currentContext;
    private Intent currentIntent;

    private boolean firstCheck;

    public EventReceiver(ArrayList configList)
    {
        super();
        this.configList = configList;
        this.curentLatitude = null;
        this.curentLongitude = null;
        this.currentIntent = null;
        this.firstCheck = true;
    }

    public void positionChanged(double latitude, double longitude)
    {
        this.curentLatitude = latitude;
        this.curentLongitude = longitude;

        this.checkconfigs();
    }


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

        //final ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        //checkWifi(connMgr, context);



        this.currentContext = context;
        this.currentIntent = intent;

        this.checkconfigs();
    }


    private void checkconfigs()
    {
        //Context is neccessary to apply settings
        if(this.currentContext != null)
        {
            if(firstCheck){
                firstCheck = false;
                //firstCheck response always with false ...
            }
            else {
                try {
                    this.configList = PersistanceUtils.loadConfigs(this.currentContext);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                for (Configuration config : configList) {
                    if (config.isEnable()) //check only if config is activated !
                    {
                        config.check(this.currentContext, this.currentIntent, this.curentLongitude, this.curentLatitude);
                    }
                }
            }
        }

    }


    private void checkWifi(ConnectivityManager connMgr, Context context)
    {
        final NetworkInfo networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        final WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        final WifiInfo connectionInfo = wifiManager.getConnectionInfo();

        Log.i("EVENT-RECEIVER", "NETWORK INFO : " + networkInfo.toString());

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
