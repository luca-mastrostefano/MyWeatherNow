package com.example.myweathernow;

import android.app.*;
import android.content.*;
import android.os.*;
import android.util.*;
import com.example.myweathernow.background_check.*;

public class MWNhome extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Log.i("MWN", "sono nella main activity");

        //Set the alarm here.
        AlarmManager alarmMgr = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

        // ripete l'operazione ogni 10 secondi (dopo allunghiamo i tempi)
        alarmMgr.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                10000,
                10000, alarmIntent);

        Log.i("MWN", "settato alarm manager");
    }
}

