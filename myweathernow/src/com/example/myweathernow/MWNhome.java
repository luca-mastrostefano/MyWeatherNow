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
        AlarmReceiver.register(this.getApplicationContext());
    }
}

