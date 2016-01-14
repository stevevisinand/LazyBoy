package com.hearc.stevevisinand.lazyboy.Logic;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.util.Log;

/**
 * Created by stevevisinand on 29.10.15.
 */
public class Event_ACConnect extends Event
{

    public Event_ACConnect(String name)
    {
        super(name);
    }

    public String getDescription()
    {
        return "Connecté au secteur";
    }

    public boolean check(Context context, Intent intent, Double longitude, Double latitude)
    {
        Log.i("Event_ACConnect", "check()");

        if(intent != null)
        {
            int chargePlug = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
            boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
            boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;

            return (usbCharge || acCharge);
        }

        return false;

    }
}
