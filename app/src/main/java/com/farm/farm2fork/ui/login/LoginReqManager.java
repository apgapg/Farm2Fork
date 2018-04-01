package com.farm.farm2fork.ui.login;

import android.content.Context;
import android.provider.Settings;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.StringRequestListener;
import com.farm.farm2fork.BuildConfig;

import org.json.JSONObject;

/**
 * Created by master on 1/4/18.
 */

public class LoginReqManager {

    public static final String BASE_URL = "https://www.reweyou.in/fasalapp/";
    private static final String TAG = LoginReqManager.class.getName();
    private final OnOtpNetworkReq onOtpNetworkReq;


    public LoginReqManager(OnOtpNetworkReq onOtpNetworkReq) {
        this.onOtpNetworkReq = onOtpNetworkReq;
    }

    public void sendOtpReqtoServer(String number, Context context) {
        AndroidNetworking.post(BASE_URL + "sendotp_1.php")
                .addBodyParameter("number", number)
                .addBodyParameter("gcm_token", "try")
                .addBodyParameter("deviceid", Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID))
                .addBodyParameter("device", android.os.Build.DEVICE)
                .addBodyParameter("model", android.os.Build.MODEL)
                .addBodyParameter("app_version", BuildConfig.VERSION_NAME)
                .addBodyParameter("os_version", android.os.Build.VERSION.SDK)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "onResponse: " + response);


                        if (response.contains("success"))
                            onOtpNetworkReq.onOtpReqSuccess();
                        else
                            onOtpNetworkReq.onOtpReqFail();


                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d(TAG, "onError: " + anError);
                        onOtpNetworkReq.onOtpReqFail();

                    }
                });
    }

    public void sendOtpCheckReq(String otp, String number) {
        AndroidNetworking.post(BASE_URL + "verifyotp.php")
                .addBodyParameter("number", number)
                .addBodyParameter("otp", otp)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        onOtpNetworkReq.onOtpCorrect(response);
                    }

                    @Override
                    public void onError(ANError anError) {

                        Log.d(TAG, "onError: " + anError);
                        onOtpNetworkReq.onOtpIncorrect();

                    }
                });
    }

    public interface OnOtpNetworkReq {
        void onOtpReqSuccess();

        void onOtpReqFail();

        void showProgressBar();

        void onOtpIncorrect();

        void onOtpCorrect(JSONObject response);
    }

}
