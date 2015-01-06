package com.example.myweathernow.persistency;

import android.content.*;
import android.location.*;

/**
 * Created by ele on 05/01/15.
 */
public class TemporanyLocation {
    private final SharedPreferences sharedPreferences;
    final SharedPreferences.Editor preferencesEditor;
    private static final String preferencesName = "myweathernow_preferences";
    private static final String locationKey = "temp_location";
    private static final String locationPing = "temp_ping";

    public TemporanyLocation(Context c) {
        this.sharedPreferences = c.getSharedPreferences(TemporanyLocation.preferencesName, 0);
        this.preferencesEditor = this.sharedPreferences.edit();
    }

    public void addLocation(Location current){
        String currentLoc = "" + String.valueOf(current.getLatitude()) + "-"
                + String.valueOf(current.getLongitude());
        preferencesEditor.putString(TemporanyLocation.locationKey, currentLoc);
        preferencesEditor.putInt(TemporanyLocation.locationPing, 1);
        preferencesEditor.commit();
    }

    public Location getOldLocation(){
        String loc = this.sharedPreferences.getString(TemporanyLocation.locationKey, "");
        String[] coordinates = loc.split("-");
        Long lat = Long.parseLong(coordinates[0]);
        Long lon = Long.parseLong(coordinates[1]);
        Location oldLocation = new Location(LocationManager.NETWORK_PROVIDER);
        oldLocation.setLatitude(lat);
        oldLocation.setLongitude(lon);
        return oldLocation;

    }

    public int getOldPing(){
        return this.sharedPreferences.getInt(TemporanyLocation.locationPing, 1);
    }

    public int updateLocation(){
        int oldPing = this.sharedPreferences.getInt(TemporanyLocation.locationPing, 1);
        int newPing = oldPing++;
        preferencesEditor.putInt(TemporanyLocation.locationPing, newPing);
        preferencesEditor.commit();
        return newPing;
    }

    public boolean hasLocation(){
        return this.sharedPreferences.contains(TemporanyLocation.locationKey);
    }
}
