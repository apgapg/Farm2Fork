package com.farm.farm2fork.ui.chooseLocale;

import com.farm.farm2fork.data.AppDataManager;

/**
 * Created by master on 8/4/18.
 */

public class ChooseLocalePresentor {

    private final AppDataManager mAppDataManager;

    public ChooseLocalePresentor(AppDataManager appDataManager) {
        mAppDataManager = appDataManager;
    }

    public AppDataManager getmAppDataManager() {
        return mAppDataManager;
    }

    public void updateLocale(String localeCode) {
        getmAppDataManager().updateLocale(localeCode);
    }
}
