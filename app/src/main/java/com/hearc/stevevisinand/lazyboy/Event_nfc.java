package com.hearc.stevevisinand.lazyboy;

import android.util.Log;

/**
 * Created by stevevisinand on 29.10.15.
 */
public class Event_nfc extends Event
{
    public Event_nfc(String name)
    {
        super(name);
    }

    public void check()
    {
        Log.i("Event_nfc", "check()");
    }
}
