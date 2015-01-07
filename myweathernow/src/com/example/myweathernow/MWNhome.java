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

    private static final DateFormat dateFormatter = new SimpleDateFormat("EEE, dd MMM");

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Log.i("MWNhome", "Entrato nella home");
        if(this.isFirstStart()){
            Log.e("MWNhome","Alarm disabled!");
            //AlarmReceiver.register(this);
        }
        try {
            WeatherInfo weatherInfo = WeatherInfo.getLast(this.getApplicationContext());
            if(weatherInfo != null) {
                this.refreshUI(weatherInfo);
            }
        } catch (JSONException e) {
            //Cant get weatherInfo
        }
        RefreshWeatherInfo refreshWeatherInfo = new RefreshWeatherInfo(this);
        refreshWeatherInfo.execute();
    }

    public void refreshUI(WeatherInfo weatherInfo){
        Log.i("MWNhome", weatherInfo.toString());
        ((TextView) this.findViewById(R.id.city)).setText("Roma");
        ((TextView) this.findViewById(R.id.date)).setText(dateFormatter.format(weatherInfo.getDate()));
        ((TextView) this.findViewById(R.id.value_humidity)).setText(weatherInfo.getHumidity() + "%");
        ((TextView) this.findViewById(R.id.value_temperature)).setText(Double.toString(Math.ceil(weatherInfo.getTemperature() - 273.15)) + "°");
        ((TextView) this.findViewById(R.id.value_wind)).setText(Double.toString(Math.round(weatherInfo.getWindSpeed()*10)/10) + "[m/s] " + weatherInfo.getWindCardinalDirection());
        ((TextView) this.findViewById(R.id.value_cloud)).setText(Double.toString(weatherInfo.getCloudiness()) + "%");
        ((TextView) this.findViewById(R.id.weather_suggestion)).setText(weatherInfo.getSentence());
    }

    private boolean isFirstStart(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String firstStart = "first_start";
        boolean isFirstStart = sharedPreferences.getBoolean(firstStart, true);
        Log.e("MWNhome","SharedPreferences firstStart disabled!");
        if(isFirstStart && false){
            final SharedPreferences.Editor preferencesEditor = sharedPreferences.edit();
            preferencesEditor.putBoolean(firstStart, false);
            preferencesEditor.commit();
        }
        if(isFirstStart) {
            Log.i("MWNhome", "firstStart");
        }else{
            Log.i("MWNhome", "già partito");
        }
        return isFirstStart;
    }

}

