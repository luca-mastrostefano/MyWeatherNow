package com.example.myweathernow.persistency;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by lucamastrostefano on 04/01/15.
 */
public class WeatherInfo {

    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor preferencesEditor;
    private static final String preferencesName = "myweathernow_preferences";
    private static final String weatherKey = "last_weather";

    private int humidity;
    private double windSpeed;
    private double windDirection;
    private double rainProbability;
    private double temperature;
    private String sentence;

    public WeatherInfo(Context context) {
        this.sharedPreferences = context.getSharedPreferences(WeatherInfo.preferencesName, 0);
        this.preferencesEditor = this.sharedPreferences.edit();
    }

    public void store(){
        try {
            JSONObject weather_description = new JSONObject();
            weather_description.put("humidity", Integer.toString(this.humidity));
            weather_description.put("windSpeed", Double.toString(this.windSpeed));
            weather_description.put("windDirection", Double.toString(this.windDirection));
            weather_description.put("rainProbability", Double.toString(this.rainProbability));
            weather_description.put("sentence", this.sentence);
            preferencesEditor.putString(WeatherInfo.weatherKey, weather_description.toString());
            preferencesEditor.commit();
        }catch(Exception e){}
    }

    public void fetchLast() throws JSONException {
        String weatherStr = this.sharedPreferences.getString(WeatherInfo.weatherKey, "");
        JSONObject weather_description = new JSONObject(weatherStr);
        this.humidity = weather_description.getInt("humidity");
        this.windSpeed = weather_description.getDouble("windSpeed");
        this.windDirection = weather_description.getDouble("windDirection");
        this.rainProbability = weather_description.getDouble("rainProbability");
        this.sentence = weather_description.getString("sentence");
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

    public double getRainProbability() {
        return rainProbability;
    }

    public void setRainProbability(double rainProbability) {
        this.rainProbability = rainProbability;
    }

}
