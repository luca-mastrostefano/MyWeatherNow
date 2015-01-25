package com.example.myweathernow.persistency;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import com.example.myweathernow.util.WeatherInfo;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

/**
 * Created by lucamastrostefano on 04/01/15.
 */
public class WeatherManager {

    private static final String preferencesName = "myweathernow_preferences";
    private static final String weatherKey = "last_weather";

    Map<WeatherInfo.Period, WeatherInfo> today_overview;
    List<WeatherInfo> today_details;
    Map<WeatherInfo.Period, WeatherInfo> tomorrow_overview;
    List<WeatherInfo> tomorrow_details;

    public WeatherManager() {
        this.today_details = new ArrayList<WeatherInfo>(50);
        this.today_overview = new HashMap<WeatherInfo.Period, WeatherInfo>(6);
        this.tomorrow_details = new ArrayList<WeatherInfo>(50);
        this.tomorrow_overview = new HashMap<WeatherInfo.Period, WeatherInfo>(6);
    }

    public static WeatherManager getLast(Context context) throws JSONException {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String weatherStr = sharedPreferences.getString(WeatherManager.weatherKey, null);
        if(weatherStr != null) {
            JSONObject weather_description = new JSONObject(weatherStr);
            return creteWeatherManagerFromJson(context, weather_description);
        }
        return null;
    }

    public static WeatherManager creteWeatherManagerFromJson(Context context, JSONObject json) throws JSONException {
        WeatherManager weatherManager = new WeatherManager();
        Log.d("WeatherManager", json.toString());
        String days[] = new String[]{"today", "tomorrow"};
        List<Map<WeatherInfo.Period, WeatherInfo>> overviews = new ArrayList<Map<WeatherInfo.Period, WeatherInfo>>(2);
        overviews.add(weatherManager.today_overview);
        overviews.add(weatherManager.tomorrow_overview);
        List<List<WeatherInfo>> details = new ArrayList<List<WeatherInfo>>(2);
        details.add(weatherManager.today_details);
        details.add(weatherManager.tomorrow_details);

        for(int i = 0; i < days.length; i++) {
            JSONObject info = json.getJSONObject(days[i]);
            Iterator keys_iterator = info.keys();
            while(keys_iterator.hasNext()){
                Object key = keys_iterator.next();
                if (key.equals("details")) {
                    JSONArray weather_details = json.getJSONArray("details");
                    for(int index = 0; index < weather_details.length(); index++) {
                        JSONObject weather_obj = weather_details.getJSONObject(index);
                        WeatherInfo.Period period = WeatherInfo.Period.valueOf(weather_obj.getString("period").toUpperCase());
                        WeatherInfo weatherInfo = new WeatherInfo(period, weather_obj.getString("timestamp"),
                                weather_obj.getDouble("rainProb"), weather_obj.getDouble("rainIntensity"));
                        details.get(i).add( weatherInfo);
                    }
                } else {
                    String period_str = (String) key;
                    WeatherInfo.Period period = WeatherInfo.Period.valueOf(period_str.toUpperCase());
                    JSONObject more_info = json.getJSONObject(period_str);
                    WeatherInfo weatherInfo =
                            new WeatherInfo(period, more_info.getDouble("rainProb"), more_info.getDouble("rainIntensity"));
                    overviews.get(i).put(period, weatherInfo);
                }

            }
        }
        weatherManager.store(context, json);
        return weatherManager;
    }

    private void store(Context context, JSONObject json) {
        try {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor preferencesEditor = sharedPreferences.edit();
            preferencesEditor.putString(WeatherManager.weatherKey, json.toString());
            preferencesEditor.commit();
        } catch (Exception e) {
        }
    }

    public static String getPreferencesName() {
        return preferencesName;
    }

    @Override
    public String toString() {
        return null;
        /*
        return "WeatherManager{" +
                "humidity=" + humidity +
                ", windSpeed=" + windSpeed +
                ", windDirection=" + windDirection +
                ", windCardinalDirection='" + windCardinalDirection + '\'' +
                ", cloudiness=" + cloudiness +
                ", temperature=" + temperature +
                ", sentence='" + sentence + '\'' +
                ", date=" + date +
                '}';*/
    }
}
