package com.farm.farm2fork.ui.login;

/**
 * Created by master on 1/4/18.
 */

public interface LoginPresentor {
    void performLogin(String number);

    void sendOtpReqtoServer(String number);

    void performValidationOfOtp(String s, String otp);

}
