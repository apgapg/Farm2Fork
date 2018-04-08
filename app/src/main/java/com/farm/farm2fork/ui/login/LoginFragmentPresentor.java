package com.farm.farm2fork.ui.login;

import android.os.Build;
import android.os.Handler;
import android.text.TextUtils;

import com.farm.farm2fork.data.AppDataManager;

/**
 * Created by master on 7/4/18.
 */

public class LoginFragmentPresentor implements LoginContract.LoginFragmentPresentor, LoginContract.OtpReqListener {

    private final AppDataManager mAppDataManager;
    private LoginContract.LoginFragmentView view;

    public LoginFragmentPresentor(AppDataManager appDataManager) {
        mAppDataManager = appDataManager;
    }

    public AppDataManager getmAppDataManager() {
        return mAppDataManager;
    }

    @Override
    public void performLogin(String number) {
        if (TextUtils.isEmpty(number) || number.length() != 10) {
            view.onValidationError();
        } else {
            view.onValidationSuccess(number);
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                view.checkSMSpermission();
            } else {
                sendOtpReqtoServer(number);
            }
        }
    }

    @Override
    public void setView(LoginContract.LoginFragmentView loginFragmentView) {
        this.view = loginFragmentView;
    }

    @Override
    public void sendOtpReqtoServer(String number) {
        view.showProgressBar();
        getmAppDataManager().sendOtpReqtoServer(number, this);

    }

    @Override
    public void onOtpReqSuccess() {
        view.hideProgressBar();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                view.onOtpReqSuccess();

            }
        }, 500);
    }

    @Override
    public void onOtpReqFail() {
        view.onOtpReqFail();
    }


}
