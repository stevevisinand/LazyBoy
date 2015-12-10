package com.hearc.stevevisinand.lazyboy;

import android.Manifest;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by stevevisinand on 03.12.15.
 */
public class EventService extends IntentService {

    private LocationManager locationMgr = null;
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

            Toast.makeText(getBaseContext(),
                    "Voici les coordonnées de votre téléphone : " + latitude + " " + longitude,
                    Toast.LENGTH_LONG).show();
        }
    };

    public EventService() {
        super("My Service");
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }

    @Override
    public void onCreate() {
        locationMgr = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationMgr.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 0, onLocationChange);
        locationMgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, onLocationChange);
        super.onCreate();

        Toast.makeText(getBaseContext(),
                "coucou",
                Toast.LENGTH_LONG).show();


        //start EventReceiver
        EventReceiver e = new EventReceiver();
        this.registerReceiver(e,
                new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("LocalService", "Received start id " + startId + ": " + intent);
        // We want this service to continue running until it is explicitly
        // stopped, so return sticky.
        return START_STICKY;
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        locationMgr.removeUpdates(onLocationChange);
    }
}
