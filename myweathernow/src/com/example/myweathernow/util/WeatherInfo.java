package com.example.myweathernow.util;

import org.json.JSONObject;

/**
 * Created by lucamastrostefano on 25/01/15.
 */
public class WeatherInfo {

    public static enum Period {
        MORNING,
        AFTERNOON,
        EVENING,
        NIGHT;
    }

    private Period period;
    private String timestamp;
    private double rainProbability;
    private double rainIntensity;

    public WeatherInfo(Period period, String timestamp, double rainProbability, double rainIntensity){
        this.period = period;
        this.timestamp = timestamp;
        this.rainProbability = rainProbability;
        this.rainIntensity = rainIntensity;
    }

    public WeatherInfo(Period period, double rainProbability, double rainIntensity){
        this(period, null, rainProbability, rainIntensity);
    }

    public WeatherInfo(String timestamp, double rainProbability, double rainIntensity){
        this(null, timestamp, rainProbability, rainIntensity);
    }

    public static WeatherInfo creteWeatherInfoFromJson(JSONObject json) throws Exception {
        WeatherInfo weatherInfo;
        String timestamp;
        Period period;
        JSONObject info;
        if(json.has("timestamp")){
            period = Period.valueOf(json.getString("period").toUpperCase());
            timestamp = json.getString("timestamp");
            info = json;
        }else{
            timestamp = null;
            String period_str = (String) json.keys().next();
            period = Period.valueOf(period_str.toUpperCase());
            info = json.getJSONObject(period_str);
        }
        return new WeatherInfo(period, timestamp, info.getDouble("rainProb"), info.getDouble("rainIntensity"));
    }

    public Period getPeriod() {
        return period;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public double getRainProbability() {
        return rainProbability;
    }

    public double getRainIntensity() {
        return rainIntensity;
    }
}

