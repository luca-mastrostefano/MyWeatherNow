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

    public Object detect(IntentService intentService){
        final Location home = new Location(LocationManager.NETWORK_PROVIDER);

        // Acquire a reference to the system Location Manager
        LocationManager locationManager = (LocationManager) intentService.getSystemService(Context.LOCATION_SERVICE);

        // Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                home.setLatitude(location.getLatitude());
                home.setLatitude(location.getLatitude());
                Log.i("location service", "Latitude: " + home.getLatitude());
                Log.i("location service", "Logitude: " + home.getLongitude());

            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };
        // Register the listener with the Location Manager to receive location updates
        locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, locationListener, null);
        return null;
    }
}
