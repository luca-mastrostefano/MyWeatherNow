package com.example.myweathernow.util;

/**
 * Created by lucamastrostefano on 25/01/15.
 */
public class WeatherInfo {

    public static enum When{
        MORNING,
        AFTERNOON,
        EVENING,
        NIGHT;
    }

    private When when;
    private String timestamp;
    private double rainProbability;
    private double rainIntensity;

    private WeatherInfo(When when, String timestamp, double rainProbability, double rainIntensity){
        this.when = when;
        this.timestamp = timestamp;
        this.rainProbability = rainProbability;
        this.rainIntensity = rainIntensity;
    }

    public WeatherInfo(When when, double rainProbability, double rainIntensity){
        this(when, null, rainProbability, rainIntensity);
    }

    public WeatherInfo(String timestamp, double rainProbability, double rainIntensity){
        this(null, timestamp, rainProbability, rainIntensity);
    }

    public When getWhen() {
        return when;
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

