package com.example.myweathernow.background_check;

import android.app.*;
import android.content.*;
import android.location.*;
import android.os.*;
import android.util.*;
import com.example.myweathernow.Meteo;

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

        Meteo meteo = new APIManager(this.getApplicationContext()).getMeteoInfo();
        //store meteo
        //
        //if(location == da mostrare){
        //      aggiorna e attiva notifica
        // }else{
        //      disattiva notifica
        // }
        new LocationsDetector().detect(this);

    }
}
