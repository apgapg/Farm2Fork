package com.farm.farm2fork.ui.login;

import android.content.Context;
import android.provider.Settings;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.StringRequestListener;
import com.farm.farm2fork.BuildConfig;
import com.farm.farm2fork.Utils.NetworkUtils;

import org.json.JSONObject;

import static com.farm.farm2fork.Utils.Constants.BASE_URL;

/**
 * Created by master on 1/4/18.
 */

public class LoginReqManager {

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
                        NetworkUtils.parseResponse(TAG, response);
                        if (response.contains("success"))
                            onOtpNetworkReq.onOtpReqSuccess();

                    }

                    @Override
                    public void onError(ANError anError) {
                        NetworkUtils.parseError(TAG, anError);
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
                        NetworkUtils.parseResponse(TAG, response);
                        onOtpNetworkReq.onOtpCorrect(response);
                    }

                    @Override
                    public void onError(ANError anError) {
                        NetworkUtils.parseError(TAG, anError);
                        onOtpNetworkReq.onOtpIncorrect();
                    }
                });
    }

    public interface OnOtpNetworkReq {
        void onOtpReqSuccess();

        void onOtpReqFail();

        void onOtpIncorrect();

        void onOtpCorrect(JSONObject response);
    }


}
