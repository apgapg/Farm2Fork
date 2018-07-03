package com.farm.farm2fork.ui.login;

import android.text.TextUtils;

import com.farm.farm2fork.data.AppDataManager;

import org.json.JSONObject;

/**
 * Created by master on 7/4/18.
 */

public class OtpFragmentPresentor implements LoginContract.OtpCheckListener {

    private final AppDataManager mAppDataManager;
    private LoginContract.OtpFragmentView view;

    public OtpFragmentPresentor(AppDataManager appDataManager) {
        mAppDataManager = appDataManager;
    }

    public AppDataManager getmAppDataManager() {
        return mAppDataManager;
    }

    public void setView(LoginContract.OtpFragmentView otpFragmentView) {
        this.view = otpFragmentView;
    }


    public void performValidationOfOtp(String otp, String number) {
        if (TextUtils.isEmpty(otp)) {
            view.onOtpValidationError();
        } else {
            view.showProgressBar();
            getmAppDataManager().sendOtpCheckReq(otp, number, this);
        }
    }

    @Override
    public void onOtpIncorrect() {
        view.hideProgressBar();
        view.onOtpVerificationFail();
    }

    @Override
    public void onOtpCorrect(JSONObject response) {
        try {
            getmAppDataManager().saveUserDetails(response.getString("uid"), response.getString("authtoken"), response.getString("number"), response.getInt("profile_complete"), response.getString("name"));
            view.hideProgressBar();
            view.onOtpVerificationSuccess();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
