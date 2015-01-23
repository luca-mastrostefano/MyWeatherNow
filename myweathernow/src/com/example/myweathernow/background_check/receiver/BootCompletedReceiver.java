package com.example.myweathernow.background_check.receiver;

/**
 * Created by ele on 03/01/15.
 * Riceve la notifica di boot completo all'accensione del telefono
 * e setta un alarm manager
 */

import android.content.*;
import android.util.*;
import com.example.myweathernow.background_check.service.*;

public class BootCompletedReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent bootIntent) {
        if (bootIntent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            // ricevo la notifica di boot completo e setto l'alarm per il futuro
            Log.i("boot receiver", "boot received");
            Intent intent = new Intent(context, LocationService.class);
            context.startService(intent);
        }
    }
}
