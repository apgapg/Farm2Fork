package com.farm.farm2fork.ui.community;

import com.farm.farm2fork.Models.CurrentWeatherModel;

import org.json.JSONArray;

/**
 * Created by master on 9/4/18.
 */

public class CommunityContract {
    public interface CommunityFragmentView {
        void onWeatherFetchSuccess(CurrentWeatherModel currentWeatherModel);

        void onWeatherFetchFail();
    }

    public interface WeatherListener {
        void onWeatherFetchSuccess(CurrentWeatherModel currentWeatherModel);

        void onWeatherFetchFail();
    }

    public interface WeatherFetchListener {
        void onWeatherFetchSuccess(JSONArray response);

        void onWeatherFetchFail();
    }
}
