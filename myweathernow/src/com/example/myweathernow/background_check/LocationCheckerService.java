package com.example.myweathernow.background_check;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

/**
 * Created by ele on 03/01/15.
 * Servizio che si occupa di controllare quando la location cambia.
 */

public class LocationCheckerService extends IntentService {

    public LocationCheckerService() {
        super("LocationCheckerService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i("service","location service");
        // Acquire a reference to the system Location Manager
//        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
//
//        Location last = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//        System.out.println(last.getLatitude());
//        System.out.println(last.getLongitude());
//
//        // Define a listener that responds to location updates
//        LocationListener locationListener = new MyLocationListener(this.getApplication());
//
//        // Register the listener with the Location Manager to receive location updates
//        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

    }
}
