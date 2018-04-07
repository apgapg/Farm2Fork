package com.farm.farm2fork;

import com.androidnetworking.AndroidNetworking;
import com.farm.farm2fork.Utils.AppLogger;
import com.farm.farm2fork.data.AppDataManager;
import com.farm.farm2fork.data.prefs.AppPrefsHelper;
import com.orm.SugarApp;

/**
 * Created by master on 10/3/18.
 */

public class ApplicationClass extends SugarApp {
    public static final String PACKAGE_NAME = "com.farm.farm2fork";
    private AppDataManager mAppDataManager;

    @Override
    public void onCreate() {

        super.onCreate();
        AppLogger.init();
        AndroidNetworking.initialize(getApplicationContext());
        AppPrefsHelper appPrefsHelper = new AppPrefsHelper(getApplicationContext());
        mAppDataManager = new AppDataManager(appPrefsHelper);
    }

    public AppDataManager getmAppDataManager() {
        return mAppDataManager;
    }
}
