package com.hearc.stevevisinand.lazyboy.Logic;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by stevevisinand on 29.10.15.
 */
public class Event_NFC extends Event
{
    private String wifiSSIDManaged;

    public Event_NFC(String name, String SSIDWifi)
    {
        super(name);
        this.wifiSSIDManaged = SSIDWifi;
    }

    public boolean check(Context context, Intent intent, Double longitude, Double latitude)
    {
        Log.i("Event_NFC", "check()");

        //TODO : all NFC check
        return false;
    }

    public String getDescription()
    {
        //TODO : Be serious...
        return "Hello";
    }
}
