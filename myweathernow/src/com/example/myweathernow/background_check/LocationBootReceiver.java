package com.example.myweathernow.background_check;

/**
 * Created by ele on 03/01/15.
 * Riceve la notifica di boot completo all'accensione del telefono
 * e setta un alarm manager che ogni tot minuti controlla se la
 * location dell'utente è cambiata
 */

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class LocationBootReceiver extends BroadcastReceiver {
    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;

    @Override
    public void onReceive(Context context, Intent bootIntent) {
        // ricevo la notifica di boot completo e inizio il mio service
        if (bootIntent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            System.out.println("boot receiver");
            // Set the alarm here.
            alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(context, LocationCheckerService.class);
            alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

            // ripete l'operazione ogni 10 secondi (dopo allunghiamo i tempi)
            alarmMgr.setInexactRepeating(AlarmManager.ELAPSED_REALTIME,
                    10000,
                    10000, alarmIntent);

        }
    }
}
