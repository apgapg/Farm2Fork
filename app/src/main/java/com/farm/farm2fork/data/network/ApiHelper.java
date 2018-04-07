package com.farm.farm2fork.ui.login;

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

public class ApiHelper {

    private static final String TAG = ApiHelper.class.getName();

    public ApiHelper() {
    }

    public void sendOtpReqtoServer(String number, String deviceId, final LoginContract.OtpReqListener otpReqListener) {
        AndroidNetworking.post(BASE_URL + "sendotp_1.php")
                .addBodyParameter("number", number)
                .addBodyParameter("gcm_token", "try")
                .addBodyParameter("deviceid", deviceId)
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
                            otpReqListener.onOtpReqSuccess();

                    }

                    @Override
                    public void onError(ANError anError) {
                        NetworkUtils.parseError(TAG, anError);
                        otpReqListener.onOtpReqFail();
                    }
                });
    }

    public void sendOtpCheckReq(String otp, String number, final LoginContract.OtpCheckListener otpCheckListener) {
        AndroidNetworking.post(BASE_URL + "verifyotp.php")
                .addBodyParameter("number", number)
                .addBodyParameter("otp", otp)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        NetworkUtils.parseResponse(TAG, response);
                        otpCheckListener.onOtpCorrect(response);
                    }

                    @Override
                    public void onError(ANError anError) {
                        NetworkUtils.parseError(TAG, anError);
                        otpCheckListener.onOtpIncorrect();
                    }
                });
    }


     /*   */


}
