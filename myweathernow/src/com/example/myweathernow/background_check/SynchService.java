package com.example.myweathernow.background_check;

import android.app.*;
import android.content.*;
import android.location.Location;
import android.util.*;
import com.example.myweathernow.MeteoInfo;

/**
 * Created by ele on 03/01/15.
 * Servizio che si occupa di controllare quando la location cambia.
 */

public class SynchService extends IntentService {

    public SynchService() {
        super("SynchService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i("location check service", "inizio le op di update");


        Location currentLocation = null;
        try {
            MeteoInfo meteo = new APIManager(this.getApplicationContext()).getMeteoInfo(currentLocation);
        }catch(Exception e){

        }
        //Meteo meteo = new APIManager(this.getApplicationContext()).getMeteoInfo();
        //
        //if(location == da mostrare){
        //      aggiorna e attiva notifica
        // }else{
        //      disattiva notifica
        // }

        // solo per testare
        new LocationsDetector().detect(this);
        NotificationHandler handler = new NotificationHandler(this.getApplicationContext());
        handler.show("Notifica creata");
        handler.update("Notifica aggiornata");
    }
}
