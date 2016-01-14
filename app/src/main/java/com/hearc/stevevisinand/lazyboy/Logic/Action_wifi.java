package com.hearc.stevevisinand.lazyboy.Logic;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.util.Log;

/**
 * Created by stevevisinand on 29.10.15.
 */
public class Action_wifi extends Action{

    private boolean activ;

    public Action_wifi(String name, boolean activ)
    {
        super(name);
        this.activ = activ;
    }

    public void apply(Context ctx)
    {
        WifiManager wifi = (WifiManager) ctx.getSystemService(Context.WIFI_SERVICE);

        //enable wifi only if it isn't already. Same to unenable
        if(wifi.isWifiEnabled() != this.activ)
        {
            wifi.setWifiEnabled(this.activ);
        }

        Log.i("Action", "Action_wifi " + this.activ);
    }

    public boolean isActiv(Context ctx)
    {
        WifiManager wifi = (WifiManager) ctx.getSystemService(Context.WIFI_SERVICE);
        return wifi.isWifiEnabled() == this.activ;
    }

}