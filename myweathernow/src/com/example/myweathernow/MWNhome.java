package com.example.myweathernow;

import android.app.*;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.*;
import android.preference.PreferenceManager;
import android.util.*;
import android.widget.TextView;
import com.example.myweathernow.background_check.receiver.*;
import com.example.myweathernow.persistency.WeatherInfo;
import org.json.JSONException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class MWNhome extends Activity {

    private static final DateFormat dateFormatter = new SimpleDateFormat("EEE, d MMM");

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        if(this.isFirstStart()){
            AlarmReceiver.register(this.getApplicationContext());
        }
        try {
            WeatherInfo weatherInfo = WeatherInfo.getLast(this.getApplicationContext());
            ((TextView) this.findViewById(R.id.city)).setText("Roma");
            ((TextView) this.findViewById(R.id.value_humidity)).setText(weatherInfo.getHumidity());
            ((TextView) this.findViewById(R.id.date)).setText(dateFormatter.format(weatherInfo.getDate()));
            ((TextView) this.findViewById(R.id.value_humidity)).setText(weatherInfo.getHumidity());
            ((TextView) this.findViewById(R.id.value_rain)).setText(weatherInfo.getHumidity());
            ((TextView) this.findViewById(R.id.value_temperature)).setText(weatherInfo.getHumidity());
            ((TextView) this.findViewById(R.id.value_wind)).setText(weatherInfo.getHumidity());
            ((TextView) this.findViewById(R.id.value_temperature)).setText(weatherInfo.getHumidity());
        } catch (JSONException e) {
            //Cant get weatherInfo
        }

    }

    private boolean isFirstStart(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);;
        String firstStart = "first_start";
        boolean isFirstStart = sharedPreferences.getBoolean(firstStart, true);
        if(isFirstStart){
            final SharedPreferences.Editor preferencesEditor = sharedPreferences.edit();
            preferencesEditor.putBoolean(firstStart, false);
            preferencesEditor.commit();
        }
        return isFirstStart;
    }

}

