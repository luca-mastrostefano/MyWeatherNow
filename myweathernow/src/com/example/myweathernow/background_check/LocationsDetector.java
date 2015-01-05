package com.example.myweathernow.background_check;

import android.app.IntentService;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by lucamastrostefano on 04/01/15.
 */
public class LocationsDetector {

    public void addLocationToHistory(Location currentLocation) {
    }

    public boolean isHome(Location currentLocation){
        return true;
    }

    public boolean isResting(Location currentLocation){
        return false;
    }
}
