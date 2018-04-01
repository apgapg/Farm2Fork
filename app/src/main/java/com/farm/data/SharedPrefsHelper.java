package com.farm.data;

import android.content.Context;
import android.content.SharedPreferences;

import static com.farm.farm2fork.ApplicationClass.PACKAGE_NAME;

/**
 * Created by master on 1/4/18.
 */

public class SharedPrefsHelper {

    private static final String PREFS = PACKAGE_NAME + ".PREFS";
    private static final String IS_LOGGED_IN = "IS_LOGGED_IN";
    private static final String KEY_UID = "UID";
    private static final String KEY_AUTH_TOKEN = "AUTHTOKEN";
    private static final String KEY_MOBILE_NUMBER = "NUMBER";

    private final SharedPreferences sharedPreferences;

    public SharedPrefsHelper(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    public void clear() {
        sharedPreferences.edit().clear().apply();
    }


    public boolean getLoggedInMode() {
        return sharedPreferences.getBoolean(IS_LOGGED_IN, false);
    }

    private void setLoggedInMode(boolean loggedIn) {
        sharedPreferences.edit().putBoolean("IS_LOGGED_IN", loggedIn).apply();
    }

    public void saveUserDetails(String uid, String authtoken, String number) {
        sharedPreferences.edit().putString(KEY_UID, uid).commit();
        sharedPreferences.edit().putString(KEY_AUTH_TOKEN, authtoken).commit();
        sharedPreferences.edit().putString(KEY_MOBILE_NUMBER, number).commit();
        setLoggedInMode(true);
    }

    public String getUid() {
        return sharedPreferences.getString(KEY_UID, null);
    }

    public String getAuthToken() {
        return sharedPreferences.getString(KEY_AUTH_TOKEN, null);
    }

    public String getNumber() {
        return sharedPreferences.getString(KEY_MOBILE_NUMBER, null);
    }

    public void putinsharedprefBoolean(String key, boolean value) {

        sharedPreferences.edit().putBoolean(key, value).commit();
    }

    public Boolean getvaluefromsharedprefBoolean(String key) {
        return sharedPreferences.getBoolean(key, false);
    }

}
