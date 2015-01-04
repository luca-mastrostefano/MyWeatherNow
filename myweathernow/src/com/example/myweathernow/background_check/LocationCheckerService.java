package com.example.myweathernow.background_check;

import android.app.*;
import android.content.*;
import android.location.*;
import android.os.*;
import android.util.*;

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
        Log.i("location check service", "inizio le op di update");

        //TODO qui avviene la chiamata al server/database per aggiornamenti meteo - task di Luca

        //TODO qui avviene il controllo della location - task di Ele
        // spostare il codice sottostante in una classe a parte
        /*
            inizio codice controllo location
         */
        final Location home = new Location(LocationManager.NETWORK_PROVIDER);

        // Acquire a reference to the system Location Manager
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

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
        /*
            fine codice controllo location
         */

        //TODO qui viene creata la notifica - task di Ele
        // Spostare in una classe apparte, cambiare la creazione della nofitica con meteodo tradizionale,
        // e fare in modo di aggiornarla

//        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
//        mBuilder.setSmallIcon(R.drawable.weather_sample);
//        mBuilder.setContentTitle("MyWeatherNow Notification");
//        mBuilder.setContentText("Location Changed Latitude: " + home.getLatitude() + " Longitude: " + home.getLongitude());
//        // Creates an explicit intent for an Activity in your app
//        Intent resultIntent = new Intent(this, MWNhome.class);
//
//        // The stack builder object will contain an artificial back stack for the started Activity.
//        // This ensures that navigating backward from the Activity leads out of your application to the Home screen.
//        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
//        // Adds the back stack for the Intent (but not the Intent itself)
//        stackBuilder.addParentStack(MWNhome.class);
//        // Adds the Intent that starts the Activity to the top of the stack
//        stackBuilder.addNextIntent(resultIntent);
//
//        PendingIntent resultPendingIntent =
//                stackBuilder.getPendingIntent(
//                        0,
//                        PendingIntent.FLAG_UPDATE_CURRENT
//                );
//
//        mBuilder.setContentIntent(resultPendingIntent);
//        NotificationManager mNotificationManager =
//                (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
//        // mId allows you to update the notification later on.
//        mNotificationManager.notify(1, mBuilder.build());


    }
}
