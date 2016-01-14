package com.hearc.stevevisinand.lazyboy.Utilities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.hearc.stevevisinand.lazyboy.EventService;

/**
 * Created by stevevisinand on 05.01.16.
 */
public class Autostart extends BroadcastReceiver
{
    /**
     * Start EventService on Boot
     * @param arg0
     * @param arg1
     */
    public void onReceive(Context arg0, Intent arg1)
    {
        Intent intent = new Intent(arg0, EventService.class);
        arg0.startService(intent);
    }
}
