package com.example.myweathernow.persistency;

import android.content.*;
import android.location.*;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * Created by ele on 05/01/15.
 */
public class UserLocationHandler {
    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor preferencesEditor;
    private static final String preferencesName = "myweathernow_preferences";
    private static final String locationKey = "last_location";
    private static final String locationPing = "number_of_ping";
    public static final int PING_THREASHOLD = 3;

    public UserLocationHandler(Context context) {
        Log.v("UserLocation", context.toString());
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        this.preferencesEditor = this.sharedPreferences.edit();
    }

    public void setLocation(Location currentLocation){
        String currentLoc = "" + String.valueOf(currentLocation.getLatitude()) + "-"
                + String.valueOf(currentLocation.getLongitude());
        preferencesEditor.putString(UserLocationHandler.locationKey, currentLoc);
        preferencesEditor.putInt(UserLocationHandler.locationPing, 1);
        preferencesEditor.commit();
    }

    public Location getLastLocation(){
        String loc = this.sharedPreferences.getString(UserLocationHandler.locationKey, "");
        String[] coordinates = loc.split("-");
        Double lat = Double.parseDouble(coordinates[0]);
        Double lon = Double.parseDouble(coordinates[1]);
        Location oldLocation = new Location(LocationManager.NETWORK_PROVIDER);
        oldLocation.setLatitude(lat);
        oldLocation.setLongitude(lon);
        return oldLocation;

    }

    public boolean isKnownLocation(Location location){
        return false;
    }

    public void storeKnownLocation(Location location){

    }

    public int increasePing(){
        int oldNumberPing = this.sharedPreferences.getInt(UserLocationHandler.locationPing, 1);
        Log.i("old number of ping", ""+ oldNumberPing);
        int newNumberPing = oldNumberPing + 1;
        preferencesEditor.putInt(UserLocationHandler.locationPing, newNumberPing);
        preferencesEditor.commit();
        if(newNumberPing >= PING_THREASHOLD){
            this.storeKnownLocation(null);
        }
        return newNumberPing;
    }

    public boolean hasLocation(){
        return this.sharedPreferences.contains(UserLocationHandler.locationKey);
    }
}
