package com.example.myweathernow.background_check.receiver;

/**
 * Created by ele on 03/01/15.
 * Riceve i tik dall'alarm manager e setta l'alarm la prima volta
 */

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.*;
import android.util.*;
import com.example.myweathernow.background_check.*;

public class AlarmReceiver extends BroadcastReceiver {

    //public final static int DELAY_ms = 1000 * 60 * 10;
    public final static int DELAY_ms = 1000 * 10;

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
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        // ripete l'operazione ogni 10 secondi (dopo allunghiamo i tempi)
        alarmMgr.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                DELAY_ms, DELAY_ms, alarmIntent);
        Log.i("boot receiver", "settato alarm manager");
    }
}
