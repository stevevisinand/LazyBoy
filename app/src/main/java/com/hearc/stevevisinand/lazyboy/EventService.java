package com.hearc.stevevisinand.lazyboy;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.hearc.stevevisinand.lazyboy.Logic.Configuration;
import com.hearc.stevevisinand.lazyboy.Utilities.PersistanceUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by stevevisinand on 03.12.15.
 */
public class EventService extends IntentService {

    public static String CONFIGS_KEY = "CONFIG";

    private LocationManager locationMgr = null;
    private ArrayList<Configuration> configurationList;

    private EventReceiver eventReceiver;

    public EventService()
    {
        super("LazyBoyService");
    }


    private LocationListener onLocationChange = new LocationListener() {
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onProviderDisabled(String provider) {
        }

        @Override
        public void onLocationChanged(Location location) {

            Double latitude = location.getLatitude();
            Double longitude = location.getLongitude();

            /*Toast.makeText(getBaseContext(),
                    "Voici les coordonnées de votre téléphone : " + latitude + " " + longitude,
                    Toast.LENGTH_LONG).show();
                    */

            if(eventReceiver != null){
                eventReceiver.positionChanged(latitude, longitude);
            }
        }
    };

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }

    private void loadConfigList(){
        this.configurationList = new ArrayList<Configuration>();
        //load saved configurations or keep a created new list if empty
        try {
            this.configurationList = PersistanceUtils.loadConfigs(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate() {

        loadConfigList();

        boolean gpgNeeded = false;
        for(Configuration config : configurationList)
        {
            if(config.needGPS()){
                gpgNeeded = true;
            }
        }

        //start location manager only if needed
        if(gpgNeeded)
        {
            long timeUpdate = 1000 * 60 * 2; //every 2 minutes

            locationMgr = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationMgr.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, timeUpdate, 0, onLocationChange);
            locationMgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, timeUpdate, 0, onLocationChange);
        }

        super.onCreate();

        /*Toast.makeText(getBaseContext(),
                "EventService Started",
                Toast.LENGTH_LONG).show();*/

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Log.i("LocalService", "Received start id " + startId + ": " + intent);

        loadConfigList();

        //start EventReceiver
        eventReceiver = new EventReceiver(this.configurationList);

        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        //filter.addAction("android.intent.action.ACTION_POWER_CONNECTED");
        //filter.addAction("android.intent.action.ACTION_POWER_DISCONNECTED");
        filter.addAction(Intent.ACTION_BATTERY_CHANGED);

        this.registerReceiver(eventReceiver, filter);

        // We want this service to continue running until it is explicitly
        // stopped, so return sticky.
        return START_STICKY;
    }



    @Override
    public void onDestroy()
    {
        /*Toast.makeText(getBaseContext(),
                "EventService onDestroy()",
                Toast.LENGTH_LONG).show();*/

        this.unregisterReceiver(eventReceiver);

        /*
        ArrayList<Configuration> oldConfigs = new ArrayList<Configuration>();
        //load saved configurations or keep a created new list if empty
        try {
            oldConfigs = PersistanceUtils.loadConfigs(this);
        } catch (Exception e) {
            e.printStackTrace();
        }


        for (Configuration currentConfig: this.configurationList)
        {
            if(currentConfig.isProduceOne()) {
                for (Configuration oldConfig : oldConfigs) {
                    if (currentConfig.getId() == oldConfig.getId()) {

                        oldConfig.setProduceOne(currentConfig.isProduceOne());
                    }
                }
            }
        }

        PersistanceUtils.saveConfigs(oldConfigs, this);
        */

        super.onDestroy();
        if(locationMgr != null) {
            locationMgr.removeUpdates(onLocationChange);
        }
    }
}
