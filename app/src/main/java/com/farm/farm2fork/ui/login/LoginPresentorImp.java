package com.farm.farm2fork.ui.login;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;

import com.farm.farm2fork.data.PermissionHelper;
import com.farm.farm2fork.data.UserDataManager;

import org.json.JSONObject;

/**
 * Created by master on 1/4/18.
 */

public class LoginPresentorImp implements LoginPresentor, LoginReqManager.OnOtpNetworkReq, PermissionHelper.OnSMSPermissionCheck {

    private final LoginView loginView;
    private final UserDataManager userSessionDataManager;
    private final LoginReqManager loginDataManager;
    private final Context context;
    private final PermissionHelper permissionHelper;
    private String number;

    public LoginPresentorImp(LoginView loginView, Context context) {
        this.loginView = loginView;
        userSessionDataManager = new UserDataManager(context);
        loginDataManager = new LoginReqManager(this);
        permissionHelper = new PermissionHelper(context, this);
        this.context = context;
    }

    @Override
    public void performLogin(String number) {
        if (TextUtils.isEmpty(number) || number.length() != 10) {
            loginView.onValidationError();
        } else {
            this.number = number;
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                permissionHelper.checkSMSpermission();
            } else {
                sendOtpReqtoServer(number);
            }
        }
    }

    @Override
    public void sendOtpReqtoServer(String number) {
        loginView.showProgressBar();
        loginDataManager.sendOtpReqtoServer(number, context);
    }

    @Override
    public void performValidationOfOtp(String otp, String number) {
        if (TextUtils.isEmpty(otp)) {
            loginView.onOtpValidationError();
        } else {
            loginView.showProgressBar();
            loginDataManager.sendOtpCheckReq(otp, number);
        }
    }

    @Override
    public void onOtpReqSuccess() {
        loginView.hideProgressBar();
        loginView.onOtpReqSuccess();
    }

    @Override
    public void onOtpReqFail() {
        loginView.hideProgressBar();
        loginView.onOtpReqFail();
    }

    @Override
    public void onOtpIncorrect() {
        loginView.hideProgressBar();
        loginView.onOtpIncorrect();
    }

    @Override
    public void onOtpCorrect(JSONObject response) {
        try {
            loginView.hideProgressBar();
            userSessionDataManager.saveUserDetails(response.getString("uid"), response.getString("authtoken"), number);
            loginView.onOtpCorrect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSMSPermissionGranted() {
        sendOtpReqtoServer(number);
        number = null;
    }

    @Override
    public void onSMSPermissionNotGranted() {
        sendOtpReqtoServer(number);
        number = null;
    }

    public UserDataManager getUserSessionDataManager() {
        return userSessionDataManager;
    }
}
