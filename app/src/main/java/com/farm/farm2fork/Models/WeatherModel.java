package com.farm.farm2fork.Models;

/**
 * Created by master on 24/2/17.
 */

public class WeatherModel {

    private String date;
    private String temp_high;
    private String temp_low;
    private String day_text;
    private String night_text;
    private String day_rain_probability;
    private String night_rain_probability;
    private String day_icon;
    private String night_icon;


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDay_icon() {
        return day_icon;
    }

    public void setDay_icon(String day_icon) {
        this.day_icon = day_icon;
    }

    public String getNight_icon() {
        return night_icon;
    }

    public void setNight_icon(String night_icon) {
        this.night_icon = night_icon;
    }

    public String getTemp_high() {
        return temp_high;
    }

    public void setTemp_high(String temp_high) {
        this.temp_high = temp_high;
    }

    public String getTemp_low() {
        return temp_low;
    }

    public void setTemp_low(String temp_low) {
        this.temp_low = temp_low;
    }

    public String getDay_rain_probability() {
        return day_rain_probability;
    }

    public void setDay_rain_probability(String day_rain_probability) {
        this.day_rain_probability = day_rain_probability;
    }

    public String getDay_text() {
        return day_text;
    }

    public void setDay_text(String day_text) {
        this.day_text = day_text;
    }

    public String getNight_rain_probability() {
        return night_rain_probability;
    }

    public void setNight_rain_probability(String night_rain_probability) {
        this.night_rain_probability = night_rain_probability;
    }

    public String getNight_text() {
        return night_text;
    }

    public void setNight_text(String night_text) {
        this.night_text = night_text;
    }
}
