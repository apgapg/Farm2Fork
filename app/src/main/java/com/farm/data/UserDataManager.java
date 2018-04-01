package com.farm.data;

import android.content.Context;

/**
 * Created by master on 1/4/18.
 */

public class UserDataManager {

    private final SharedPrefsHelper sharedPrefsHelper;

    public UserDataManager(Context context) {
        sharedPrefsHelper = new SharedPrefsHelper(context);
    }


    public void saveUserDetails(String uid, String authtoken, String number) {
        sharedPrefsHelper.saveUserDetails(uid, authtoken, number);

    }

    public void clear() {
        sharedPrefsHelper.clear();
    }


    public boolean getLoggedInMode() {
        return sharedPrefsHelper.getLoggedInMode();
    }

    public String getUid() {
        return sharedPrefsHelper.getUid();
    }

    public String getAuthToken() {
        return sharedPrefsHelper.getAuthToken();
    }

    public String getNumber() {
        return sharedPrefsHelper.getNumber();
    }

    public void putinsharedprefBoolean(String key, boolean value) {

        sharedPrefsHelper.putinsharedprefBoolean(key, value);
    }

    public Boolean getvaluefromsharedprefBoolean(String key) {
        return sharedPrefsHelper.getvaluefromsharedprefBoolean(key);
    }
}
