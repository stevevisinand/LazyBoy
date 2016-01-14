package com.hearc.stevevisinand.lazyboy.Logic;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

/**
 * Created by stevevisinand on 29.10.15.
 */
public class Action_planeMode_on extends Action{

    public Action_planeMode_on(String name)
    {
        super(name);
    }

    public void apply(Context ctx)
    {
        //airplane mode can't be implemented since 4.2 :,(

        //http://stackoverflow.com/questions/5533881/toggle-airplane-mode-in-android
        /**
        boolean isEnabled = isAirplaneModeOn(ctx);


        // toggle airplane mode
        if(!isEnabled){
            Log.i("Action", "Action_planeMode_on : airplane mode is not on");

            setAirplaneMode(ctx, 0);
        }

        **/


        // Post an intent to reload
        //Intent intent = new Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED);
        //intent.putExtra("state", !isEnabled);
        //sendBroadcast(intent);

        Log.i("Action", "Action_planeMode_on");
    }

    public static boolean isAirplaneModeOn(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return Settings.System.getInt(context.getContentResolver(),
                    Settings.System.AIRPLANE_MODE_ON, 0) != 0;
        } else {
            return Settings.Global.getInt(context.getContentResolver(),
                    Settings.Global.AIRPLANE_MODE_ON, 0) != 0;
        }
    }
    public static void setAirplaneMode(Context context, int value) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Settings.System.putInt(
                    context.getContentResolver(),
                    Settings.System.AIRPLANE_MODE_ON, value);
        } else {
            Settings.Global.putInt(
                    context.getContentResolver(),
                    Settings.Global.AIRPLANE_MODE_ON, value);
        }
    }

    public boolean isActiv(Context ctx)
    {
        return false;
    }


}