package com.farm.farm2fork.ui.community;

import com.farm.farm2fork.Models.CurrentWeatherModel;
import com.farm.farm2fork.data.AppDataManager;

/**
 * Created by master on 9/4/18.
 */

public class CommunityFragmentPresentor {
    private final AppDataManager mAppDataManager;
    private CommunityContract.CommunityFragmentView view;

    public CommunityFragmentPresentor(AppDataManager appDataManager) {
        mAppDataManager = appDataManager;
    }

    public AppDataManager getmAppDataManager() {
        return mAppDataManager;
    }

    public void setView(CommunityContract.CommunityFragmentView view) {
        this.view = view;
    }

    public void getCurrentWeather(String loc_key) {
        getmAppDataManager().getCurrentWeather(loc_key, new CommunityContract.WeatherListener() {
            @Override
            public void onWeatherFetchSuccess(CurrentWeatherModel currentWeatherModel) {
                view.onWeatherFetchSuccess(currentWeatherModel);
            }

            @Override
            public void onWeatherFetchFail() {
                view.onWeatherFetchFail();
            }
        });
    }
}
