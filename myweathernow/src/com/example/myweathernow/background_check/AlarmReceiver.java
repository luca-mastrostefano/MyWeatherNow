package com.example.myweathernow.background_check;

/**
 * Created by ele on 03/01/15.
 * Riceve la notifica di boot completo all'accensione del telefono
 * e setta un alarm manager che ogni tot minuti controlla se la
 * location dell'utente è cambiata
 */

import android.content.*;
import android.util.*;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent bootIntent) {
        // ho ricevuto il tik dall'alarmmanager e quindi chiamo il service
        Log.i("alarm receiver", "alarm tik received");
        Intent checkLocation = new Intent(context, LocationCheckerService.class);
        context.startService(checkLocation);
    }
}
