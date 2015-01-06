package com.example.myweathernow.persistency;

import android.content.*;
import android.location.*;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * Created by ele on 05/01/15.
 */
public class UserLocation {
    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor preferencesEditor;
    private static final String preferencesName = "myweathernow_preferences";
    private static final String locationKey = "last_location";
    private static final String locationPing = "number_of_ping";

    public UserLocation(Context context) {
        Log.v("UserLocation", context.toString());
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        this.preferencesEditor = this.sharedPreferences.edit();
    }

    public void setLocation(Location currentLocation){
        String currentLoc = "" + String.valueOf(currentLocation.getLatitude()) + "-"
                + String.valueOf(currentLocation.getLongitude());
        preferencesEditor.putString(UserLocation.locationKey, currentLoc);
        preferencesEditor.putInt(UserLocation.locationPing, 1);
        preferencesEditor.commit();
    }

    public Location getLastLocation(){
        String loc = this.sharedPreferences.getString(UserLocation.locationKey, "");
        String[] coordinates = loc.split("-");
        Long lat = Long.parseLong(coordinates[0]);
        Long lon = Long.parseLong(coordinates[1]);
        Location oldLocation = new Location(LocationManager.NETWORK_PROVIDER);
        oldLocation.setLatitude(lat);
        oldLocation.setLongitude(lon);
        return oldLocation;

    }

    public int getLastPing(){
        return this.sharedPreferences.getInt(UserLocation.locationPing, 1);
    }

    public int updateLocation(){
        int oldNumberPing = this.sharedPreferences.getInt(UserLocation.locationPing, 1);
        int newNumberPing = oldNumberPing++;
        preferencesEditor.putInt(UserLocation.locationPing, newNumberPing);
        preferencesEditor.commit();
        return newNumberPing;
    }

    public boolean hasLocation(){
        return this.sharedPreferences.contains(UserLocation.locationKey);
    }
}
