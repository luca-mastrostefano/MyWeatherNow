package com.example.myweathernow;

import android.app.*;
import android.content.*;
import android.graphics.*;
import android.os.*;
import android.preference.*;
import android.util.*;
import android.view.*;
import android.widget.*;
import com.example.myweathernow.background_check.*;
import com.example.myweathernow.background_check.service.*;
import com.example.myweathernow.persistency.*;
import com.example.myweathernow.util.*;
import org.json.*;

import java.text.*;
import java.util.*;

public class MWNhome extends Activity {

    private static final DateFormat dateFormatter = new SimpleDateFormat("EEE, dd MMM");
    private APIManager.Day day;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.prova);
        Log.i("MWNhome", "Entrato nella home");
        Log.i("Thread Home", ""  + Thread.currentThread().getId());
        this.day = APIManager.Day.TODAY;
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
        ((TextView) this.findViewById(R.id.city)).setText("Roma");
        ((TextView) this.findViewById(R.id.date)).setText(dateFormatter.format(weatherManager.getDate()));
        List<WeatherInfo> details = weatherManager.getDetails(this.day);
        //TODO bla bla bla with details
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

    public void showTodayInfo(View view) {
        Button today = (Button)view.findViewById(R.id.button_today);
        Button tomorrow = (Button)view.findViewById(R.id.button_tomorrow);
        tomorrow.setBackgroundColor(Color.parseColor("#80FFFFFF"));
        tomorrow.setTextColor(Color.parseColor("#ff0c2c46"));
        today.setBackgroundColor(Color.TRANSPARENT);
        today.setTextColor(Color.parseColor("#ffffff"));
        this.day = APIManager.Day.TODAY;
        try {
            WeatherManager weatherManager = WeatherManager.getLast(this.getApplicationContext());
            if(weatherManager != null) {
                this.refreshUI(weatherManager);
            }
        } catch (JSONException e) {
            //Cant get weatherInfo
        }
    }

    public void showTomorrowInfo(View view) {
        Button today = (Button)view.findViewById(R.id.button_today);
        Button tomorrow = (Button)view.findViewById(R.id.button_tomorrow);
        today.setBackgroundColor(Color.parseColor("#80FFFFFF"));
        today.setTextColor(Color.parseColor("#ff0c2c46"));
        tomorrow.setBackgroundColor(Color.TRANSPARENT);
        tomorrow.setTextColor(Color.parseColor("#ffffff"));
        this.day = APIManager.Day.TOMORROW;
        try {
            WeatherManager weatherManager = WeatherManager.getLast(this.getApplicationContext());
            if(weatherManager != null) {
                this.refreshUI(weatherManager);
            }
        } catch (JSONException e) {
            //Cant get weatherInfo
        }
    }
}

