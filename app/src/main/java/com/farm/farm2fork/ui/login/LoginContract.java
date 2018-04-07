package com.farm.farm2fork.ui.login;

import com.farm.farm2fork.BasePresentor;
import com.farm.farm2fork.BaseView;

import org.json.JSONObject;

/**
 * Created by master on 7/4/18.
 */

public interface LoginContract {

    interface LoginFragmentView extends BaseView {


        void onValidationError();

        void checkSMSpermission();

        void showProgressBar();

        void onOtpReqSuccess();

        void hideProgressBar();

        void onValidationSuccess(String number);

        void onOtpReqFail();
    }

    interface OtpFragmentView extends BaseView {

        void onOtpValidationError();

        void showProgressBar();

        void hideProgressBar();

        void onOtpVerificationSuccess();

        void onOtpVerificationFail();
    }

    interface LoginFragmentPresentor extends BasePresentor {

        void performLogin(String number);

        void setView(LoginFragmentView loginFragmentView);

        void sendOtpReqtoServer(String number);
    }

    interface OtpFragmentPresentor extends BasePresentor {

    }

    interface OtpReqListener {
        void onOtpReqSuccess();

        void onOtpReqFail();
    }

    interface OtpCheckListener {
        void onOtpIncorrect();

        void onOtpCorrect(JSONObject response);
    }
}
