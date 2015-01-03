package com.example.myweathernow.background_check;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;
import com.example.myweathernow.MWNhome;
import com.example.myweathernow.R;

/**
 * Created by ele on 03/01/15.
 */
public class MyLocationListener implements LocationListener{
    private Context c;
    private Location home;

    public MyLocationListener(Context c){
        this.c = c;
        this.home = null;
    }

    public void onLocationChanged(Location location) {
        NotificationCompat.Builder mBuilder = new Builder(this.c);
        mBuilder.setSmallIcon(R.drawable.weather_sample);
        mBuilder.setContentTitle("MyWeatherNow Notification");
        mBuilder.setContentText("Location Changed Latitude: " + location.getLatitude() + " Longitude: " + location.getLongitude());
        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this.c, MWNhome.class);

        // The stack builder object will contain an artificial back stack for the started Activity.
        // This ensures that navigating backward from the Activity leads out of your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this.c);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(MWNhome.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);

        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) c.getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        mNotificationManager.notify(1, mBuilder.build());



        // Called when a new location is found by the network location provider.
        System.out.println("NUOVA LOCATION");
        System.out.println(location.getLatitude());
        System.out.println(location.getLongitude());
    }

    public void onStatusChanged(String provider, int status, Bundle extras) {}

    public void onProviderEnabled(String provider) {}

    public void onProviderDisabled(String provider) {}

//    public checkLocationChange(Location location){
//        if(home == null){
//            // prima volta
//            home = location;
//        }else {
//            if(!home.equals(location)){
//
//            }
//        }
//    }
}
