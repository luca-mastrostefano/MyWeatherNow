package com.example.myweathernow.background_check.test;

import android.app.*;
import android.content.*;
import android.location.*;
import android.os.*;
import android.util.*;
import android.widget.*;

/**
 * Created by ele on 18/01/15.
 */
public class LocationService extends Service {
    public LocationManager locationManager;
    public MyLocationListener listener;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("Location service", "partito");
        Log.i("Thread Service 1", ""  + Thread.currentThread().getId());
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        listener = new MyLocationListener(this);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 0, listener);
        return START_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v("STOP_SERVICE", "DONE");
        locationManager.removeUpdates(listener);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    public class MyLocationListener implements LocationListener {
        private Context context;

        public MyLocationListener(Context c) {

            this.context = c;
        }

        public void onLocationChanged(final Location currentLocation) {
            AsyncLocationTask async = new AsyncLocationTask(this.context);
            async.execute(currentLocation);
        }

        public void onProviderDisabled(String provider)
        {
            Toast.makeText(getApplicationContext(), "Gps Disabled", Toast.LENGTH_SHORT).show();
        }


        public void onProviderEnabled(String provider)
        {
            Toast.makeText( getApplicationContext(), "Gps Enabled", Toast.LENGTH_SHORT).show();
        }


        public void onStatusChanged(String provider, int status, Bundle extras)
        {

        }

    }
}
