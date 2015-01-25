package com.example.myweathernow.background_check.service;

import android.content.*;
import android.location.*;
import android.net.*;
import android.os.*;
import android.util.*;
import com.example.myweathernow.background_check.*;
import com.example.myweathernow.persistency.*;

/**
 * Created by ele on 18/01/15.
 */
public class AsyncLocationTask extends AsyncTask <Location, Void, Void>{
    private LocationsDetector locDetector;
    private NotificationHandler notificationHand;
    private Context context;

    public AsyncLocationTask(Context c) {
        this.locDetector = new LocationsDetector(c);
        this.notificationHand = new NotificationHandler(c);
        this.context = c;
    }

    private boolean isNetworkAvailable(){
        ConnectivityManager cm =
                (ConnectivityManager) this.context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }


    @Override
    protected Void doInBackground(Location... currentLocations) {
        Location currentLocation = currentLocations[0];
        Log.i("**************************************", "Location update");
        Log.i("location", "" + currentLocation.getLatitude());
        Log.i("location", "" + currentLocation.getLongitude());
        Log.i("Thread Service 2", ""  + Thread.currentThread().getId());
        boolean showNotification = locDetector.addLocationToHistory(currentLocation);
        try {
            if(isNetworkAvailable()){
                APIManager apiManager = new APIManager(APIManager.InformationType.OVERVIEW, APIManager.When.TODAY);
                WeatherManager weatherManager = apiManager.getWeatherInfo(context, currentLocation);
                if(showNotification){
                    Log.i("Notification", "mostrata notifica");
                    notificationHand.show(weatherManager);
                } else{
                    Log.i("Notification", "tolta notifica");
                    notificationHand.hide();
                }
            }

        }catch(Exception e){
            Log.w("AsyncLocationTask", e.toString());
        }
        return null;
    }
}
