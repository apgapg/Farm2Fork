package com.farm.farm2fork.Models;

/**
 * Created by master on 9/4/18.
 */

public class CurrentWeatherModel {
    private final String forecast;
    private final String currentTemp;
    private final String humidity;
    private final String wind;

    public CurrentWeatherModel(String forecast, String currentTemp, String humidity, String wind) {
        this.forecast = forecast;
        this.currentTemp = currentTemp;
        this.humidity = humidity;
        this.wind = wind;
    }

    public String getCurrentTemp() {
        return currentTemp;
    }

    public String getForecast() {
        return forecast;
    }

    public String getHumidity() {
        return humidity;
    }

    public String getWind() {
        return wind;
    }
}
