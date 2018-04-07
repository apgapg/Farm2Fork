package com.farm.farm2fork.ui.login;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.androidnetworking.interfaces.StringRequestListener;
import com.farm.farm2fork.BuildConfig;
import com.farm.farm2fork.Models.FarmModel;
import com.farm.farm2fork.Models.LocationInfoModel;
import com.farm.farm2fork.Utils.NetworkUtils;
import com.farm.farm2fork.ui.farmscreen.FarmContract;

import org.json.JSONObject;

import java.util.List;

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

    public void sendCropNameListFetchReq(final FarmContract.CropListReqListener cropListReqListener) {
        AndroidNetworking.get(BASE_URL + "data_crop_list.php")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        NetworkUtils.parseResponse(TAG, response);
                        cropListReqListener.onCropListFetch(response);
                    }

                    @Override
                    public void onError(ANError anError) {
                        NetworkUtils.parseError(TAG, anError);
                    }
                });
    }

    public void makeFarmFetchReq(String uid, String authToken, final FarmContract.FarmFetchListener farmFetchListener) {
        AndroidNetworking.post(BASE_URL + "fetchfarms.php")
                .addBodyParameter("authtoken", authToken)
                .addBodyParameter("uid", uid)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsObjectList(FarmModel.class, new ParsedRequestListener<List<FarmModel>>() {
                    @Override
                    public void onResponse(final List<FarmModel> response) {
                        NetworkUtils.parseResponse(TAG, response);
                        farmFetchListener.onFarmFetchReqSuccess(response);
                    }

                    @Override
                    public void onError(ANError anError) {
                        NetworkUtils.parseError(TAG, anError);
                        farmFetchListener.onFarmFetchReqFail();
                    }
                });


    }

    public void sendAddFarmReq(String crop, String size, String farmSizeAcre, String farmSizeUnit, String uid, String authToken, String imageencoded, LocationInfoModel mlocationInfoModel, final FarmContract.AddFarmReqListener addFarmReqListener) {
        AndroidNetworking.post(BASE_URL + "addfarm_temp.php")
                .addBodyParameter("crop", crop)
                .addBodyParameter("farmsize", size + " " + farmSizeUnit)
                .addBodyParameter("farmsize_acre", farmSizeAcre)
                .addBodyParameter("image", imageencoded)
                .addBodyParameter("loc_address", mlocationInfoModel.getAddress())
                .addBodyParameter("loc_lat", mlocationInfoModel.getLatitude())
                .addBodyParameter("loc_long", mlocationInfoModel.getLongitude())
                .addBodyParameter("postalCode", mlocationInfoModel.getPostalzipcode())
                .addBodyParameter("loc_city", mlocationInfoModel.getCity())
                .addBodyParameter("uid", uid)
                .addBodyParameter("authtoken", authToken)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        NetworkUtils.parseResponse(TAG, response);
                        addFarmReqListener.onAddFarmReqSuccess(response);
                    }

                    @Override
                    public void onError(ANError anError) {
                        NetworkUtils.parseError(TAG, anError);
                        addFarmReqListener.onAddFarmReqFail();
                    }
                });
    }




     /*   */


}
