package com.farm.farm2fork.data;

import com.farm.farm2fork.Models.CropNameModel;
import com.farm.farm2fork.data.prefs.AppPrefsHelper;
import com.farm.farm2fork.ui.login.ApiHelper;
import com.farm.farm2fork.ui.login.LoginContract;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by master on 7/4/18.
 */

public class AppDataManager implements DataManager, LoginContract.OtpReqListener, LoginContract.OtpCheckListener {
    private final AppPrefsHelper mAppPrefsHelper;
    private final ApiHelper mApiHelper;
    private LoginContract.OtpReqListener mOtpReqListener;
    private LoginContract.OtpCheckListener mOtpCheckListener;

    public AppDataManager(AppPrefsHelper appPrefsHelper) {
        mAppPrefsHelper = appPrefsHelper;
        mApiHelper = new ApiHelper();
    }

    public AppPrefsHelper getmAppPrefsHelper() {
        return mAppPrefsHelper;
    }

    public ApiHelper getmApiHelper() {
        return mApiHelper;
    }

    public void saveUserDetails(String uid, String authtoken, String number) {
        mAppPrefsHelper.saveUserDetails(uid, authtoken, number);

    }

    public void clear() {
        mAppPrefsHelper.clear();
    }


    public boolean getLoggedInMode() {
        return mAppPrefsHelper.getLoggedInMode();
    }

    public String getUid() {
        return mAppPrefsHelper.getUid();
    }

    public String getAuthToken() {
        return mAppPrefsHelper.getAuthToken();
    }

    public String getNumber() {
        return mAppPrefsHelper.getNumber();
    }

    public void putinsharedprefBoolean(String key, boolean value) {

        mAppPrefsHelper.putinsharedprefBoolean(key, value);
    }

    public Boolean getvaluefromsharedprefBoolean(String key) {
        return mAppPrefsHelper.getvaluefromsharedprefBoolean(key);
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

    public void sendOtpReqtoServer(String number, LoginContract.OtpReqListener otpReqListener) {
        mOtpReqListener = otpReqListener;
        getmApiHelper().sendOtpReqtoServer(number, getmAppPrefsHelper().getDeviceId(), this);
    }

    @Override
    public void onOtpReqSuccess() {
        mOtpReqListener.onOtpReqSuccess();
    }

    @Override
    public void onOtpReqFail() {
        mOtpReqListener.onOtpReqFail();

    }

    public void sendOtpCheckReq(String otp, String number, LoginContract.OtpCheckListener otpCheckListener) {
        this.mOtpCheckListener = otpCheckListener;
        getmApiHelper().sendOtpCheckReq(otp, number, this);
    }

    @Override
    public void onOtpIncorrect() {
        mOtpCheckListener.onOtpIncorrect();
    }

    @Override
    public void onOtpCorrect(JSONObject response) {
        mOtpCheckListener.onOtpCorrect(response);
    }
}
