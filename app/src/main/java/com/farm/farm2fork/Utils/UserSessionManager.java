package com.farm.farm2fork.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.List;


/**
 * Created by Reweyou on 12/17/2015.
 */
public class UserSessionManager {

    public static final String KEY_NAME = "username";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PIC = "pic";

    public static final String KEY_LOCATION = "location";
    public static final String KEY_MOBILE_NUMBER = "mobilenumber";
    public static final String KEY_LOGIN_LOCATION = "loginlocation";
    public static final String KEY_CUSTOM_LOCATION = "customlocation";
    public static final String KEY_CATEGORY = "category";
    public static final String KEY_LOGIN_FULLNAME = "fullname";
    // Sharedpref file name
    private static final String PREFER_NAME = "ReweyouPref";
    // All Shared Preferences Keys
    private static final String IS_USER_LOGIN = "IsUserLoggedIn";
    private static final String KEY_AUTH_TOKEN = "authtoken";
    private static final String KEY_DEVICE_ID = "deviceid";
    private static final String TAG = UserSessionManager.class.getName();
    private static final String KEY_REALNAME = "realname";
    private static final String KEY_UID = "uid";
    private static final String KEY_SHORT_INFO = "info";
    private static final String KEY_FCM_ID = "fcmid";
    private static final String KEY_BADGE = "badge";
    // Shared Preferences reference
    SharedPreferences pref;
    // Editor reference for Shared preferences
    SharedPreferences.Editor editor;
    // Context
    Context _context;
    // Shared pref mode
    int PRIVATE_MODE = 0;
    private Gson gson = new Gson();
    private List<String> likesList;
    private String FIRST_LOAD_TUT = "firsttimeload";
    private boolean mobileNumber;
    private String deviceid;
    private String shortinfo;


    // Constructor
    public UserSessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREFER_NAME, PRIVATE_MODE);
        editor = pref.edit();

    }


    public String getUsername() {
        return pref.getString(KEY_NAME, "");
    }

    public void setUsername(String fullname) {
        editor.putString(KEY_NAME, fullname);
        editor.commit();
    }

    public void createUserRegisterSession(String uid, String authtoken, String number) {

        editor.putBoolean(IS_USER_LOGIN, true);

        editor.putString(KEY_UID, uid);
        editor.putString(KEY_AUTH_TOKEN, authtoken);
        editor.putString(KEY_MOBILE_NUMBER, number);

        // commit changes
        editor.commit();
    }

    public String getUID() {
        return pref.getString(KEY_UID, "");
    }

    public String getAuthToken() {
        return pref.getString(KEY_AUTH_TOKEN, "");
    }

    public String getMobileNumber() {
        return pref.getString(KEY_MOBILE_NUMBER, "");
    }

    public boolean isUserLoggedIn() {
        return pref.getBoolean(IS_USER_LOGIN, false);
    }



   /* public void logoutUser() {
        Toast.makeText(_context, "invalid session! please login again", Toast.LENGTH_SHORT).show();
        //logoutUser1();
    }*/

    /*public void logoutUser1() {

        // Clearing all user data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Login Activity
        Intent i = new Intent(_context, LoginActivity.class);

        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        _context.startActivity(i);
    }*/

    // Check for login


    public void setfcmid(String token) {
        editor.putString(KEY_FCM_ID, token);
        editor.commit();
    }

    public String getfcmid() {
        return pref.getString(KEY_FCM_ID, "");
    }

    public void putinsharedpref(String key, int value) {

        editor.putInt(key, value);
        editor.commit();
    }

    public void putinsharedprefString(String key, String value) {

        editor.putString(key, value);
        editor.commit();
    }

    public void putinsharedprefBoolean(String key, boolean value) {

        editor.putBoolean(key, value);
        editor.commit();
    }


    public int getvaluefromsharedpref(String key) {
        return pref.getInt(key, -1);
    }

    public String getvaluefromsharedprefString(String key) {
        return pref.getString(key, "0");
    }

    public Boolean getvaluefromsharedprefBoolean(String key) {
        return pref.getBoolean(key, false);
    }
}