package com.hearc.stevevisinand.lazyboy.Logic;

/**
 * Created by stevevisinand on 12.11.15.
 */
public class EventFactory {

    public static String EVENT_WIFI_CONNECT= "Connexion wifi";
    public static String EVENT_PLACE_DETECTER= "Emplacement";
    public static String EVENT_NFC_DISCOVERED= "Puce NFC";
    public static String EVENT_AC_CONNECTED= "Connexion au secteur";

    public static Event createWifiEvent(String SSIDWifi)
    {
        return new Event_wifiConnect(EVENT_WIFI_CONNECT, SSIDWifi);
    }

    public static Event createPlaceEvent(String place, double latitude, double longitude, int rayon)
    {
        return new Event_placeDetect(EVENT_PLACE_DETECTER, place, latitude, longitude, rayon);
    }

    public static Event createACConnectEvent()
    {
        return new Event_ACConnect(EVENT_AC_CONNECTED);
    }

    public static Event createNFCEvent(String nfcTag)
    {
        return new Event_NFC(EVENT_NFC_DISCOVERED, nfcTag);
    }

    public static String[] getEventsCatalog()
    {
        String[] tabStr = {EVENT_WIFI_CONNECT, EVENT_PLACE_DETECTER, EVENT_AC_CONNECTED, EVENT_NFC_DISCOVERED};
        return tabStr;
    }
}