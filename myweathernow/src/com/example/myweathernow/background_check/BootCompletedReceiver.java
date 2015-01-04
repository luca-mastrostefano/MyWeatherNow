package com.example.myweathernow.background_check;

/**
 * Created by ele on 03/01/15.
 * Riceve la notifica di boot completo all'accensione del telefono
 * e setta un alarm manager che ogni tot minuti controlla se la
 * location dell'utente è cambiata
 */

import android.app.*;
import android.content.*;
import android.util.*;

public class BootCompletedReceiver extends BroadcastReceiver {
    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;

    @Override
    public void onReceive(Context context, Intent bootIntent) {
        if (bootIntent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            // ricevo la notifica di boot completo e setto l'alarm per il futuro
            Log.i("boot receiver", "boot received");
            // Set the alarm here.
            alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(context, AlarmReceiver.class);
            alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

            // ripete l'operazione ogni 10 secondi (dopo allunghiamo i tempi)
            alarmMgr.setInexactRepeating(AlarmManager.ELAPSED_REALTIME,
                    10000,
                    10000, alarmIntent);
            Log.i("boot receiver", "settato alarm manager");
        }
    }
}
