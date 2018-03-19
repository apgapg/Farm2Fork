package com.farm.farm2fork.Interface;

import zh.wang.android.yweathergetter4a.WeatherInfo;

/**
 * Created by master on 13/3/18.
 */

public interface Weatherlistener {
    public void onWeatherDataReceived(WeatherInfo weatherInfo);
}
