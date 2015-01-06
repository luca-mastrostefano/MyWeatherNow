package com.example.myweathernow.persistency;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.preference.PreferenceManager;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by lucamastrostefano on 04/01/15.
 */
public class WeatherInfo {

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

    public WeatherInfo() {
    }

    public void store(Context context) {
        try {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor preferencesEditor = sharedPreferences.edit();
            JSONObject weather_description = new JSONObject();
            weather_description.put("humidity", Integer.toString(this.humidity));
            weather_description.put("windSpeed", Double.toString(this.windSpeed));
            weather_description.put("windDirection", Double.toString(this.windDirection));
            weather_description.put("windCardinalDirection", this.windCardinalDirection);
            weather_description.put("cloudiness", Double.toString(this.cloudiness));
            weather_description.put("sentence", this.sentence);
            weather_description.put("date", this.date.getTime());
            preferencesEditor.putString(WeatherInfo.weatherKey, weather_description.toString());
            preferencesEditor.commit();
        } catch (Exception e) {
        }
    }

    public static WeatherInfo getLast(Context context) throws JSONException {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String weatherStr = sharedPreferences.getString(WeatherInfo.weatherKey, null);
        if(weatherStr != null) {
            JSONObject weather_description = new JSONObject(weatherStr);
            WeatherInfo weatherInfo = new WeatherInfo();
            weatherInfo.humidity = weather_description.getInt("humidity");
            weatherInfo.windSpeed = weather_description.getDouble("windSpeed");
            weatherInfo.windDirection = weather_description.getDouble("windDirection");
            weatherInfo.windCardinalDirection = weather_description.getString("windCardinalDirection");
            weatherInfo.cloudiness = weather_description.getDouble("cloudiness");
            weatherInfo.sentence = weather_description.getString("sentence");
            weatherInfo.date = new Date(weather_description.getLong("date"));
            return weatherInfo;
        }
        return null;
    }

    public static WeatherInfo creteWeatherInfoFromJson(Context context, JSONObject json) throws JSONException {
        WeatherInfo weatherInfo = new WeatherInfo();
        JSONObject data = json.getJSONObject("data");
        JSONObject forecast = json.getJSONObject("forecast");
        weatherInfo.humidity = forecast.getInt("humidity");
        weatherInfo.temperature = forecast.getDouble("temperature");
        weatherInfo.windSpeed = forecast.getDouble("wind_speed");
        weatherInfo.windDirection = forecast.getDouble("wind_direction");
        weatherInfo.windCardinalDirection = forecast.getString("wind_cardinal_direction");
        weatherInfo.cloudiness = forecast.getDouble("cloudiness");
        weatherInfo.sentence = data.getString("sentence");
        weatherInfo.date = new Date();
        weatherInfo.store(context);
        return weatherInfo;
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
}
