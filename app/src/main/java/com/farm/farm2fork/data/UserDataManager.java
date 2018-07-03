package com.farm.farm2fork.data;

import android.content.Context;

import com.farm.farm2fork.data.prefs.AppPrefsHelper;
import com.farm.farm2fork.models.CropNameModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by master on 1/4/18.
 */

public class UserDataManager {

    private final AppPrefsHelper appPrefsHelper;

    public UserDataManager(Context context) {
        appPrefsHelper = new AppPrefsHelper(context);

    }




    public void clear() {
        appPrefsHelper.clear();
    }


    public boolean getLoggedInMode() {
        return appPrefsHelper.getLoggedInMode();
    }

    public String getUid() {
        return appPrefsHelper.getUid();
    }

    public String getAuthToken() {
        return appPrefsHelper.getAuthToken();
    }

    public String getNumber() {
        return appPrefsHelper.getNumber();
    }

    public void putinsharedprefBoolean(String key, boolean value) {

        appPrefsHelper.putinsharedprefBoolean(key, value);
    }

    public Boolean getvaluefromsharedprefBoolean(String key) {
        return appPrefsHelper.getvaluefromsharedprefBoolean(key);
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
