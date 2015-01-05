package com.example.myweathernow.background_check;

import android.app.*;
import android.content.*;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.*;
import com.example.myweathernow.MeteoInfo;

/**
 * Created by ele on 03/01/15.
 * Servizio che si occupa di controllare quando la location cambia.
 */

public class SynchService extends IntentService implements LocationListener {

    public SynchService() {
        super("SynchService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i("location check service", "inizio le op di update");
        // Acquire a reference to the system Location Manager
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        // Register the listener with the Location Manager to receive location updates
        locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, this, null);

    }

    @Override
    public void onLocationChanged(Location currentLocation) {
        currentLocation.setLatitude(currentLocation.getLatitude());
        currentLocation.setLatitude(currentLocation.getLatitude());
        Log.i("location service", "Latitude: " + currentLocation.getLatitude());
        Log.i("location service", "Logitude: " + currentLocation.getLongitude());
        try {
            MeteoInfo meteo = new APIManager().getMeteoInfo(this.getApplicationContext(), currentLocation);
        }catch(Exception e){

        }
        //
        //if(location == da mostrare){
        //      aggiorna e attiva notifica
        // }else{
        //      disattiva notifica
        // }

        // solo per testare
        new LocationsDetector().addLocationToHistory(currentLocation);
        NotificationHandler handler = new NotificationHandler(this.getApplicationContext(), 1);
        handler.show("Notifica creata");
        handler.update("Notifica aggiornata");
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
}
