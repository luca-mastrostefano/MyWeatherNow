package com.example.myweathernow.background_check;

/**
 * Created by ele on 03/01/15.
 * Riceve la notifica di boot completo all'accensione del telefono
 * e setta un alarm manager che ogni tot minuti controlla se la
 * location dell'utente è cambiata
 */

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.*;
import android.util.*;

public class AlarmReceiver extends BroadcastReceiver {

    public final static int DELAY_ms = 10000;

    @Override
    public void onReceive(Context context, Intent bootIntent) {
        // ho ricevuto il tik dall'alarmmanager e quindi chiamo il service
        Log.i("alarm receiver", "alarm tik received");
        Intent checkLocation = new Intent(context, SynchService.class);
        context.startService(checkLocation);
    }

    public static void register(Context context){
        AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        // ripete l'operazione ogni 10 secondi (dopo allunghiamo i tempi)
        alarmMgr.setInexactRepeating(AlarmManager.ELAPSED_REALTIME,
                System.currentTimeMillis(), DELAY_ms, alarmIntent);
        Log.i("boot receiver", "settato alarm manager");
    }
}
