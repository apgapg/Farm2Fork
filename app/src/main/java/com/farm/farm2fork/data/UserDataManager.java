package com.farm.farm2fork.data;

import android.content.Context;

import com.farm.farm2fork.Models.CropNameModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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

    public boolean saveCropList(JSONObject response) {
        try {

            List<CropNameModel> croplist = new ArrayList<>();
            for (int i = 0; i < response.length(); i++) {
                croplist.add(new CropNameModel(response.getString("" + i)));
            }
            CropNameModel.saveInTx(croplist);
            return true;

        } catch (JSONException e) {
            e.printStackTrace();
            return false;

        }

    }
}
