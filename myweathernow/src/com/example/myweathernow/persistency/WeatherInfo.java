package com.example.myweathernow.persistency;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
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
    private double cloudiness;
    private double temperature;
    private String sentence;
    private Date date;

    public WeatherInfo() {
    }

    public void store(Context context){
        try {
            SharedPreferences sharedPreferences = context.getSharedPreferences(WeatherInfo.preferencesName, 0);
            SharedPreferences.Editor preferencesEditor = sharedPreferences.edit();
            JSONObject weather_description = new JSONObject();
            weather_description.put("humidity", Integer.toString(this.humidity));
            weather_description.put("windSpeed", Double.toString(this.windSpeed));
            weather_description.put("windDirection", Double.toString(this.windDirection));
            weather_description.put("cloudiness", Double.toString(this.cloudiness));
            weather_description.put("sentence", this.sentence);
            weather_description.put("date", this.date.getTime());
            preferencesEditor.putString(WeatherInfo.weatherKey, weather_description.toString());
            preferencesEditor.commit();
        } catch (Exception e){}
    }

    public static WeatherInfo getLast(Context context) throws JSONException {
        SharedPreferences sharedPreferences = context.getSharedPreferences(WeatherInfo.preferencesName, 0);
        String weatherStr = sharedPreferences.getString(WeatherInfo.weatherKey, "");
        JSONObject weather_description = new JSONObject(weatherStr);
        WeatherInfo weatherInfo = new WeatherInfo();
        weatherInfo.humidity = weather_description.getInt("humidity");
        weatherInfo.windSpeed = weather_description.getDouble("windSpeed");
        weatherInfo.windDirection = weather_description.getDouble("windDirection");
        weatherInfo.cloudiness = weather_description.getDouble("cloudiness");
        weatherInfo.sentence = weather_description.getString("sentence");
        weatherInfo.date = new Date(weather_description.getLong("date"));
        return weatherInfo;
    }

    public static WeatherInfo creteWeatherInfoFromJson(Context context ,JSONObject json) throws JSONException {
        WeatherInfo weatherInfo = new WeatherInfo();
        JSONObject data = json.getJSONObject("data");
        JSONObject forecast = json.getJSONObject("forecast");
        weatherInfo.setHumidity(forecast.getInt("humidity"));
        weatherInfo.setTemperature(forecast.getDouble("temperature"));
        weatherInfo.setWindSpeed(forecast.getDouble("wind_speed"));
        weatherInfo.setWindDirection(forecast.getDouble("wind_direction"));
        weatherInfo.setCloudiness(forecast.getDouble("cloudiness"));
        weatherInfo.setSentence(data.getString("sentence"));
        weatherInfo.setDate(new Date());
        weatherInfo.store(context);
        return weatherInfo;
    }

    public String getSentence() {return sentence;}

    public void setSentence(String sentence) {
        this.sentence = sentence;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public double getWindDirection() {
        return windDirection;
    }

    public void setWindDirection(double windDirection) {
        this.windDirection = windDirection;
    }

    public double getCloudiness() {return cloudiness;}

    public void setCloudiness(double cloudiness) {this.cloudiness = cloudiness;}

    public Date getDate() {return date;}

    public void setDate(Date date) {this.date = date;}

}
