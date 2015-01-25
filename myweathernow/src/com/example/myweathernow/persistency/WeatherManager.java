package com.example.myweathernow.persistency;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by lucamastrostefano on 04/01/15.
 */
public class WeatherManager {

    private static final String preferencesName = "myweathernow_preferences";
    private static final String weatherKey = "last_weather";

    private int humidity;
    private double windSpeed;
    private double windDirection;
    private String windCardinalDirection;
    private double cloudiness;
    private double temperature;
    private String sentence;
    private Date date;

    public WeatherManager() {
    }

    public void store(Context context) {
        try {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor preferencesEditor = sharedPreferences.edit();
            JSONObject weather_description = new JSONObject();
            weather_description.put("temperature", this.temperature);
            weather_description.put("humidity", this.humidity);
            weather_description.put("windSpeed", this.windSpeed);
            weather_description.put("windDirection", this.windDirection);
            weather_description.put("windCardinalDirection", this.windCardinalDirection);
            weather_description.put("cloudiness", this.cloudiness);
            weather_description.put("sentence", this.sentence);
            weather_description.put("date", this.date.getTime());
            preferencesEditor.putString(WeatherManager.weatherKey, weather_description.toString());
            preferencesEditor.commit();
        } catch (Exception e) {
        }
    }

    public static WeatherManager getLast(Context context) throws JSONException {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String weatherStr = sharedPreferences.getString(WeatherManager.weatherKey, null);
        if(weatherStr != null) {
            JSONObject weather_description = new JSONObject(weatherStr);
            WeatherManager weatherManager = new WeatherManager();
            weatherManager.temperature = weather_description.getDouble("temperature");
            weatherManager.humidity = weather_description.getInt("humidity");
            weatherManager.windSpeed = weather_description.getDouble("windSpeed");
            weatherManager.windDirection = weather_description.getDouble("windDirection");
            weatherManager.windCardinalDirection = weather_description.getString("windCardinalDirection");
            weatherManager.cloudiness = weather_description.getDouble("cloudiness");
            weatherManager.sentence = weather_description.getString("sentence");
            weatherManager.date = new Date(weather_description.getLong("date"));
            return weatherManager;
        }
        return null;
    }

    public static WeatherManager creteWeatherManagerFromJson(Context context, JSONObject json) throws Exception {
        WeatherManager weatherManager = new WeatherManager();
        Log.d("WeatherManager", json.toString());
        JSONObject data = json.getJSONObject("data");
        JSONObject forecast = data.getJSONObject("forecast");
        weatherManager.humidity = forecast.getInt("humidity");
        weatherManager.temperature = forecast.getDouble("temperature");
        weatherManager.windSpeed = forecast.getDouble("wind_speed");
        weatherManager.windDirection = forecast.getDouble("wind_direction");
        weatherManager.windCardinalDirection = new String(forecast.getString("wind_cardinal_direction").getBytes("UTF-8"));
        weatherManager.cloudiness = forecast.getDouble("cloudiness");
        weatherManager.sentence = data.getString("sentence");
        weatherManager.date = new Date();
        weatherManager.store(context);
        return weatherManager;
    }

    public static String getPreferencesName() {
        return preferencesName;
    }

    public int getHumidity() {
        return humidity;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public double getWindDirection() {
        return windDirection;
    }

    public String getWindCardinalDirection() {
        return windCardinalDirection;
    }

    public double getCloudiness() {
        return cloudiness;
    }

    public double getTemperature() {
        return temperature;
    }

    public String getSentence() {
        return sentence;
    }

    public Date getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "WeatherManager{" +
                "humidity=" + humidity +
                ", windSpeed=" + windSpeed +
                ", windDirection=" + windDirection +
                ", windCardinalDirection='" + windCardinalDirection + '\'' +
                ", cloudiness=" + cloudiness +
                ", temperature=" + temperature +
                ", sentence='" + sentence + '\'' +
                ", date=" + date +
                '}';
    }
}
