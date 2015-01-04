package com.example.myweathernow;

/**
 * Created by ele on 03/01/15.
 * Riceve la notifica di boot completo all'accensione del telefono
 * e setta un alarm manager che ogni tot minuti controlla se la
 * location dell'utente è cambiata
 */

import android.app.*;
import android.content.*;
import android.util.*;
import com.example.myweathernow.background_check.AlarmReceiver;

public class BootCompletedReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent bootIntent) {
        if (bootIntent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            // ricevo la notifica di boot completo e setto l'alarm per il futuro
            Log.i("boot receiver", "boot received");
            AlarmReceiver.register(context);
        }
    }
}
