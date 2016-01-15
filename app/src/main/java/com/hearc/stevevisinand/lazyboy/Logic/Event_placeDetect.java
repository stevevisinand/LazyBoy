package com.hearc.stevevisinand.lazyboy.Logic;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by stevevisinand on 29.10.15.
 */
public class Event_placeDetect extends Event
{
    private String place;
    private double longitude;
    private double latitude;
    private int rayon; //meters

    private Double lastLatitude;
    private Double lastLongitude;

    private static int EARTHSIZE = 40075000; //meters
    private static double RATIO_CONVERT = 0.000009009; //src : wikipedia

    public Event_placeDetect(String name, String place, double latitude, double longitude, int rayon)
    {
        super(name);
        if(!place.equals("")){
            this.name = place;
        }

        this.place = place;
        this.latitude = latitude;
        this.longitude = longitude;
        this.rayon = rayon;

        this.lastLatitude = null;
        this.lastLongitude = null;
    }

    public String getDescription()
    {
        return "Lat/Long(" + String.format ("%.3f", this.latitude) + ", " + String.format ("%.3f", this.longitude) + "), Précision : " + this.rayon +"m";
    }

    public boolean check(Context context, Intent intent, Double longitude, Double latitude)
    {
        if(longitude == null || latitude == null){

            if(this.lastLatitude == null || this.lastLongitude ==null) {
                return false;
            }
            else{
                longitude = this.lastLongitude;
                latitude = this.lastLongitude;
            }
        }

        //preced check
        /*
        double Atan_RE = Math.atan((double) rayon / (double) EARTHSIZE); //little CPU optimization

        Log.i("Event_placeDetect", "rayon : "+ rayon +", Atan_RE : " + Atan_RE);
        double latitudeMax = Atan_RE + this.latitude;
        double latitudeMin = this.latitude - Atan_RE;

        double longitudeMax = Atan_RE + this.longitude;
        double longitudeMin = this.longitude - Atan_RE;
        */

        double error = RATIO_CONVERT * (double) rayon;

        double latitudeMax = this.latitude + error;
        double latitudeMin = this.latitude - error;

        double longitudeMax = this.longitude + error;
        double longitudeMin = this.longitude - error;

        Log.i("Event_placeDetect", "rayon : "+ rayon +", error : " + error +", RATIO_CONVERT : " + RATIO_CONVERT);

        Log.i("Event_placeDetect", "position : " + longitude + " , latitude : " + latitude);
        Log.i("Event_placeDetect", "longitude OK : " + this.longitude + " , latitude : " + this.latitude);
        Log.i("Event_placeDetect", "longitude OK max : " + longitudeMax + " , latitude max : " + latitudeMax);
        Log.i("Event_placeDetect", "longitude OK min : " + longitudeMin + " , latitude min : " + latitudeMax);

        if((latitude <= latitudeMax && latitude >= latitudeMin) && (longitude <= longitudeMax && longitude >= longitudeMin))
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}
