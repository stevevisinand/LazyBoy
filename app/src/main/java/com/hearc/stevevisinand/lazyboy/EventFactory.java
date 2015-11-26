package com.hearc.stevevisinand.lazyboy;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by stevevisinand on 12.11.15.
 */
public class EventFactory {

    public static String EVENT_NFC = "NFC";

    public static Event getEvent(String eventType)
    {
        if(eventType == null){
            return null;
        }
        else if(eventType.equalsIgnoreCase(EVENT_NFC)){
            return new Event_nfc(EVENT_NFC);
        }

        return null;
    }

    public static String[] getEventsCatalog()
    {
        String[] tabStr = {EVENT_NFC};
        return tabStr;
    }
}