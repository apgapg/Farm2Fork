package com.farm.farm2fork.ui.login;

/**
 * Created by master on 1/4/18.
 */

public interface LoginView {
    void onValidationError();

    void onOtpReqFail();

    void onOtpReqSuccess();

    void showProgressBar();

    void hideProgressBar();

    void onOtpValidationError();

    void onOtpCorrect();

    void onOtpIncorrect();
}
