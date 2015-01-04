package com.example.myweathernow;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.example.myweathernow.background_check.LocationBootReceiver;

public class MWNhome extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Log.i("MWN", "sono nella main activity");

//        Set the alarm here.
        AlarmManager alarmMgr = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(this, LocationBootReceiver.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

        // ripete l'operazione ogni 10 secondi (dopo allunghiamo i tempi)
        alarmMgr.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                10000,
                10000, alarmIntent);
    }
}

