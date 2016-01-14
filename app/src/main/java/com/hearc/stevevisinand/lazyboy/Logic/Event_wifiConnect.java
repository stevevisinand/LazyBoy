package com.hearc.stevevisinand.lazyboy.Logic;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

/**
 * Created by stevevisinand on 29.10.15.
 */
public class Event_wifiConnect extends Event
{
    private String wifiSSIDManaged;

    public Event_wifiConnect(String name, String SSIDWifi)
    {
        super(name);
        this.wifiSSIDManaged = SSIDWifi;

        this.name = "Wifi : " + SSIDWifi.substring(1, SSIDWifi.length()-1);
    }

    public String getDescription()
    {
        return "SSID: "+ wifiSSIDManaged.substring(1, wifiSSIDManaged.length()-1);
    }

    public boolean check(Context context, Intent intent, Double longitude, Double latitude)
    {
        Log.i("Event_wifiConnect", "check()");

        final ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        final NetworkInfo networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        final WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        final WifiInfo connectionInfo = wifiManager.getConnectionInfo();

        if (networkInfo.getState() == NetworkInfo.State.CONNECTED) {
            Log.i("Event_wifiConnect", "WIFI - Connected in SSID :" + connectionInfo.getSSID().toString());
            Log.i("Event_wifiConnect", "wifiSSIDManaged:" + wifiSSIDManaged);

            return connectionInfo.getSSID().toString().equals(wifiSSIDManaged);
        }
        return false;

    }
}
