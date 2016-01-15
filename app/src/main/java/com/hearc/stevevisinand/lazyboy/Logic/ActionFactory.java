package com.hearc.stevevisinand.lazyboy.Logic;

/**
 * Created by stevevisinand on 12.11.15.
 */
public class ActionFactory {

    public static String ACTION_AUDIOVOLUME = "Volume audio";
    public static String ACTION_AUDIO_VIBRATEMODE = "Mode vibreur";
    public static String ACTION_AUDIO_SILENTMODE = "Mode silence";
    public static String ACTION_AUDIO_NORMALMODE = "Mode sonnerie normal";
    public static String ACTION_PLANEMODE_ON = "Activer mode avion";
    public static String ACTION_WIFI_ON = "Activer le WiFi";
    public static String ACTION_WIFI_OFF = "Désactiver le WiFi";
    public static String ACTION_BT_ON = "Activer le Bluetooth";
    public static String ACTION_BT_OFF = "Désactiver le Bluetooth";
    public static String ACTION_LAUNCHAPP = "Lancer une application";

    public static Action getAction(String actionType)
    {
        if(actionType == null){
            return null;
        }
        else if(actionType.equalsIgnoreCase(ACTION_AUDIO_VIBRATEMODE)){
            return new Action_ringerMode_vibrate(ACTION_AUDIO_VIBRATEMODE);
        }
        else if(actionType.equalsIgnoreCase(ACTION_AUDIO_SILENTMODE)){
            return new Action_ringerMode_silent(ACTION_AUDIO_SILENTMODE);
        }
        else if(actionType.equalsIgnoreCase(ACTION_AUDIO_NORMALMODE)){
            return new Action_ringerMode_normal(ACTION_AUDIO_NORMALMODE);
        }
        else if(actionType.equalsIgnoreCase(ACTION_PLANEMODE_ON)){
            return new Action_planeMode_on(ACTION_PLANEMODE_ON);
        }
        else if(actionType.equalsIgnoreCase(ACTION_WIFI_ON)){
            return new Action_wifi(ACTION_WIFI_ON, true);
        }
        else if(actionType.equalsIgnoreCase(ACTION_WIFI_OFF)){
            return new Action_wifi(ACTION_WIFI_OFF, false);
        }
        else if(actionType.equalsIgnoreCase(ACTION_BT_ON)){
            return new Action_bluetooth(ACTION_BT_ON, true);
        }
        else if(actionType.equalsIgnoreCase(ACTION_BT_OFF)){
            return new Action_bluetooth(ACTION_BT_OFF, false);
        }

        return null;
    }

    public static String[] getActionsCatalog()
    {
        String[] tabStr =  {ACTION_AUDIO_VIBRATEMODE, ACTION_AUDIO_SILENTMODE, ACTION_AUDIO_NORMALMODE,
                ACTION_WIFI_ON, ACTION_WIFI_OFF, ACTION_BT_ON, ACTION_BT_OFF, ACTION_LAUNCHAPP};
        return tabStr;
    }
}
