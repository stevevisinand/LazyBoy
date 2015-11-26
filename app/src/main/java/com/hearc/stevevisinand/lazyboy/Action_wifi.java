package com.hearc.stevevisinand.lazyboy;

import android.util.Log;

/**
 * Created by stevevisinand on 29.10.15.
 */
public class Action_wifi extends Action{

    public Action_wifi(String name)
    {
        super(name);
    }

    public void apply()
    {
        Log.i("Event_nfc", "check()");
    }
}