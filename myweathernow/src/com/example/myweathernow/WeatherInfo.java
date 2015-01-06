package com.example.myweathernow;

/**
 * Created by lucamastrostefano on 04/01/15.
 */
public class WeatherInfo {

    private int humidity;
    private double wind;
    private double wind_direction;
    private double rain_probability;
    private String sentence;
    private double temperature;

    public WeatherInfo(){
    }

    public String getSentence() {
        return sentence;
    }

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

    public double getWind() {
        return wind;
    }

    public void setWind(double wind) {
        this.wind = wind;
    }

    public double getWind_direction() {
        return wind_direction;
    }

    public void setWind_direction(double wind_direction) {
        this.wind_direction = wind_direction;
    }

    public double getRain_probability() {
        return rain_probability;
    }

    public void setRain_probability(double rain_probability) {
        this.rain_probability = rain_probability;
    }

}
