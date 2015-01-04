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

public class SynchService extends IntentService {

    public SynchService() {
        super("SynchService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i("location check service", "inizio le op di update");

        //TODO qui avviene la chiamata al server/database per aggiornamenti meteo - task di Luca

        //TODO qui avviene il controllo della location - task di Ele
        LocationsDetector locationsDetector = new LocationsDetector();
        locationsDetector.detect(this);


        //TODO qui viene creata la notifica - task di Ele
        // Spostare in una classe apparte, cambiare la creazione della nofitica con meteodo tradizionale,
        // e fare in modo di aggiornarla

    }
}
