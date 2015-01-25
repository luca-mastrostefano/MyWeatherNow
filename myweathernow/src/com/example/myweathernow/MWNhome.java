package com.example.myweathernow;

import android.app.*;
import android.content.*;
import android.os.*;
import android.preference.*;
import android.util.*;
import android.widget.*;
import com.example.myweathernow.background_check.service.*;
import com.example.myweathernow.persistency.*;
import org.json.*;

import java.text.*;

public class MWNhome extends Activity {

    private static final DateFormat dateFormatter = new SimpleDateFormat("EEE, dd MMM");

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Log.i("MWNhome", "Entrato nella home");
        Log.i("Thread Home", ""  + Thread.currentThread().getId());
        if(this.isFirstStart()){
            Log.e("MWNhome","Service not started, let's start it now!");
            Intent intent = new Intent(this, LocationService.class);
            startService(intent);
        }else{
            Intent intent = new Intent(this, LocationService.class);
            startService(intent);
        }
        try {
            WeatherManager weatherManager = WeatherManager.getLast(this.getApplicationContext());
            if(weatherManager != null) {
                this.refreshUI(weatherManager);
            }
        } catch (JSONException e) {
            //Cant get weatherInfo
        }
        RefreshWeatherInfo refreshWeatherInfo = new RefreshWeatherInfo(this);
        refreshWeatherInfo.execute();
    }

    public void refreshUI(WeatherManager weatherManager){
        Log.i("MWNhome", weatherManager.toString());
        /*
        ((TextView) this.findViewById(R.id.city)).setText("Roma");
        ((TextView) this.findViewById(R.id.date)).setText(dateFormatter.format(weatherManager.getDate()));
        ((TextView) this.findViewById(R.id.value_humidity)).setText(weatherManager.getHumidity() + "%");
        ((TextView) this.findViewById(R.id.value_temperature)).setText(Double.toString(Math.ceil(weatherManager.getTemperature() - 273.15)) + "°");
        ((TextView) this.findViewById(R.id.value_wind)).setText(Double.toString(Math.round(weatherManager.getWindSpeed()*10)/10) + "[m/s] " + weatherManager.getWindCardinalDirection());
        ((TextView) this.findViewById(R.id.value_cloud)).setText(Double.toString(weatherManager.getCloudiness()) + "%");
        ((TextView) this.findViewById(R.id.weather_suggestion)).setText(weatherManager.getSentence());*/
    }

    private boolean isFirstStart(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String firstStart = "first_start";
        boolean isFirstStart = sharedPreferences.getBoolean(firstStart, true);
//        Log.e("MWNhome","SharedPreferences firstStart disabled!");
//        if(isFirstStart && false){
        if(isFirstStart){
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

