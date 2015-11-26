package com.hearc.stevevisinand.lazyboy;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by stevevisinand on 12.11.15.
 */
public class ActionFactory {

    public static String ACTION_WIFI = "WiFi";

    public static Action getAction(String actionType)
    {
        if(actionType == null){
            return null;
        }
        else if(actionType.equalsIgnoreCase(ACTION_WIFI)){
            return new Action_wifi(ACTION_WIFI);
        }

        return null;
    }

    public static String[] getActionsCatalog()
    {
        String[] tabStr = {ACTION_WIFI};
        return tabStr;
    }
}
