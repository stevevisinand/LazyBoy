package com.hearc.stevevisinand.lazyboy.Logic;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

/**
 * Created by stevevisinand on 29.10.15.
 */
public class Action_launchApp extends Action{

    private String appPackage;

    public Action_launchApp(String appName,String appPackage)
    {
        super(appName);

        this.appPackage = appPackage;
    }

    public void apply(Context ctx)
    {
        Intent i;
        try
        {
            i = ctx.getPackageManager().getLaunchIntentForPackage(this.appPackage);
            if (i == null)
                throw new PackageManager.NameNotFoundException();
            i.addCategory(Intent.CATEGORY_LAUNCHER);
            ctx.startActivity(i);
        }
        catch (PackageManager.NameNotFoundException e)
        {
            //app doesn't exists
        }

        Log.i("Action", "Action_launchApp ");
    }

    public boolean isActiv(Context ctx)
    {
        //TODO : how Check ?
        return false;
    }


}