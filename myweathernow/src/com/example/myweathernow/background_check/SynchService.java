package com.example.myweathernow.background_check;

import android.app.*;
import android.content.*;
import android.location.*;
import android.os.*;
import android.preference.PreferenceManager;
import android.util.*;
import com.example.myweathernow.persistency.UserLocation;
import com.example.myweathernow.persistency.WeatherInfo;

/**
 * Created by ele on 03/01/15.
 * Servizio che si occupa di controllare quando la location cambia.
 */

public class SynchService extends IntentService implements LocationListener {

    private LocationsDetector locDetector;
    private NotificationHandler notificationHand;
    private Context context;

    public SynchService() {
        super("SynchService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        this.locDetector = new LocationsDetector(this);
        this.notificationHand = new NotificationHandler(this);
        Log.i("location check service", "inizio le op di update");
        // Acquire a reference to the system Location Manager
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        // Register the listener with the Location Manager to receive location updates
        locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, this, null);

    }

    @Override
    public void onLocationChanged(Location currentLocation) {
        Log.i("location service", "Latitude: " + currentLocation.getLatitude());
        Log.i("location service", "Logitude: " + currentLocation.getLongitude());
        try {
            WeatherInfo weatherInfo = new APIManager().getWeatherInfo(this, currentLocation);
        }catch(Exception e){

        }
        boolean showNotification = this.locDetector.addLocationToHistory(currentLocation);
        if(showNotification){
            notificationHand.show("Notifica Creata/Aggiornata");
        } else{
            notificationHand.hide();
        }

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
