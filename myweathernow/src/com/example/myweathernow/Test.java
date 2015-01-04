package com.example.myweathernow;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by lucamastrostefano on 03/01/15.
 */
public class Test extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Alarm alarm = new Alarm();
        alarm.SetAlarm(Test.this);
        Log.v("test", "QUA");
    }
}