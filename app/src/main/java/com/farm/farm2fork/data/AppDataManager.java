package com.farm.farm2fork.data;

import com.farm.farm2fork.Models.CropNameModel;
import com.farm.farm2fork.Models.CurrentWeatherModel;
import com.farm.farm2fork.Models.FarmModel;
import com.farm.farm2fork.Models.LocationInfoModel;
import com.farm.farm2fork.Models.SchemeModel;
import com.farm.farm2fork.data.prefs.AppPrefsHelper;
import com.farm.farm2fork.ui.community.CommunityContract;
import com.farm.farm2fork.ui.farmscreen.FarmContract;
import com.farm.farm2fork.ui.feeds.FeedsAdapterContract;
import com.farm.farm2fork.ui.login.ApiHelper;
import com.farm.farm2fork.ui.login.LoginContract;
import com.farm.farm2fork.ui.scheme.SchemeContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by master on 7/4/18.
 */

public class AppDataManager implements DataManager, LoginContract.OtpReqListener, LoginContract.OtpCheckListener, FarmContract.CropListReqListener, FarmContract.FarmFetchListener {

    private static final String FETCH_CROP_LIST_BOOLEAN = "croplistfetch";

    private final AppPrefsHelper mAppPrefsHelper;
    private final ApiHelper mApiHelper;
    private LoginContract.OtpReqListener mOtpReqListener;
    private LoginContract.OtpCheckListener mOtpCheckListener;
    private FarmContract.FarmFetchListener mFarmFetchListener;

    public AppDataManager(AppPrefsHelper appPrefsHelper) {
        mAppPrefsHelper = appPrefsHelper;
        mApiHelper = new ApiHelper();

    }

    public AppPrefsHelper getmAppPrefsHelper() {
        return mAppPrefsHelper;
    }

    public ApiHelper getmApiHelper() {
        return mApiHelper;
    }


    public void saveUserDetails(String uid, String authtoken, String number) {
        mAppPrefsHelper.saveUserDetails(uid, authtoken, number);

    }

    public void clear() {
        mAppPrefsHelper.clear();
    }


    public boolean getLoggedInMode() {
        return mAppPrefsHelper.getLoggedInMode();
    }

    public String getUid() {
        return mAppPrefsHelper.getUid();
    }

    public String getAuthToken() {
        return mAppPrefsHelper.getAuthToken();
    }

    public String getNumber() {
        return mAppPrefsHelper.getNumber();
    }

    public void putinsharedprefBoolean(String key, boolean value) {

        mAppPrefsHelper.putinsharedprefBoolean(key, value);
    }

    public Boolean getvaluefromsharedprefBoolean(String key) {
        return mAppPrefsHelper.getvaluefromsharedprefBoolean(key);
    }

    public boolean saveCropList(JSONObject response) {
        try {

            List<CropNameModel> croplist = new ArrayList<>();
            for (int i = 0; i < response.length(); i++) {
                croplist.add(new CropNameModel(response.getString("" + i)));
            }
            CropNameModel.saveInTx(croplist);
            return true;

        } catch (JSONException e) {
            e.printStackTrace();
            return false;

        }

    }

    public void sendOtpReqtoServer(String number, LoginContract.OtpReqListener otpReqListener) {
        mOtpReqListener = otpReqListener;
        getmApiHelper().sendOtpReqtoServer(number, getmAppPrefsHelper().getDeviceId(), this);
    }

    @Override
    public void onOtpReqSuccess() {
        mOtpReqListener.onOtpReqSuccess();
    }

    @Override
    public void onOtpReqFail() {
        mOtpReqListener.onOtpReqFail();

    }

    public void sendOtpCheckReq(String otp, String number, LoginContract.OtpCheckListener otpCheckListener) {
        this.mOtpCheckListener = otpCheckListener;
        getmApiHelper().sendOtpCheckReq(otp, number, this);
    }

    @Override
    public void onOtpIncorrect() {
        mOtpCheckListener.onOtpIncorrect();
    }

    @Override
    public void onOtpCorrect(JSONObject response) {
        mOtpCheckListener.onOtpCorrect(response);
    }

    public void fetchCropNameList() {
        if (!getvaluefromsharedprefBoolean(FETCH_CROP_LIST_BOOLEAN)) {
            getmApiHelper().sendCropNameListFetchReq(this);
        }
    }

    @Override
    public void onCropListFetch(JSONObject response) {
        if (saveCropList(response)) {
            putinsharedprefBoolean(FETCH_CROP_LIST_BOOLEAN, true);
        }
    }

    public void makeFetchFarmReq(FarmContract.FarmFetchListener farmFetchListener) {
        mFarmFetchListener = farmFetchListener;
        getmApiHelper().makeFarmFetchReq(getUid(), getAuthToken(), this);
    }

    @Override
    public void onFarmFetchReqSuccess(List<FarmModel> response) {
        mFarmFetchListener.onFarmFetchReqSuccess(response);
    }

    @Override
    public void onFarmFetchReqFail() {
        mFarmFetchListener.onFarmFetchReqFail();

    }

   /* public void loadCropList(final FarmContract.CropListLoadListener cropListLoadListener) {
        getmObservableHelper().getCropList(new ObservableHelper.CropListFetchListener() {

            @Override
            public void onCropListFetch(List<String> cropList) {
                cropListLoadListener.onCropListFetch(cropList);
            }
        });
    }*/

    public void sendAddFarmReq(String crop, String size, String farmSizeAcre, String farmSizeUnit, String imageencoded, LocationInfoModel mlocationInfoModel, final FarmContract.AddFarmReqListener addFarmReqListener) {
        getmApiHelper().sendAddFarmReq(crop, size, farmSizeAcre, farmSizeUnit, getUid(), getAuthToken(), imageencoded, mlocationInfoModel, new FarmContract.AddFarmReqListener() {
            @Override
            public void onAddFarmReqSuccess(String response) {
                if (response.contains("Successfully Uploaded")) {
                    addFarmReqListener.onAddFarmReqSuccess(response);
                } else
                    addFarmReqListener.onAddFarmReqFail();
            }

            @Override
            public void onAddFarmReqFail() {
                addFarmReqListener.onAddFarmReqFail();

            }
        });

    }

    public void sendSchemesReq(final SchemeContract.SchemeFetchListener schemeFetchListener) {
        getmApiHelper().sendSchemeReq(getUid(), getAuthToken(), new SchemeContract.SchemeFetchListener() {
            @Override
            public void onSchemeFetchReqSuccess(List<SchemeModel> list) {
                schemeFetchListener.onSchemeFetchReqSuccess(list);
            }

            @Override
            public void onSchemeFetchReqFail() {
                schemeFetchListener.onSchemeFetchReqFail();
            }
        });
    }

    public void updateLocale(String localeCode) {
        getmAppPrefsHelper().updateLocale(localeCode);
    }

    public void getCurrentWeather(String loc_key, final CommunityContract.WeatherListener weatherListener) {
        getmApiHelper().getCurrentWeather(loc_key, new CommunityContract.WeatherFetchListener() {
            @Override
            public void onWeatherFetchSuccess(JSONArray response) {
                try {

                    JSONObject jsonObject = response.getJSONObject(0);
                    String forecast = jsonObject.getString("WeatherText");
                    JSONObject jsonObjectTemp = jsonObject.getJSONObject("Temperature");
                    JSONObject jsonObjecttempMetric = jsonObjectTemp.getJSONObject("Metric");
                    String currentTemp = String.valueOf(jsonObjecttempMetric.get("Value")) + "°c";
                    String humidity = jsonObject.getString("RelativeHumidity") + "%";
                    JSONObject jsonObjectwind = jsonObject.getJSONObject("Wind");
                    JSONObject jsonObjectspeed = jsonObjectwind.getJSONObject("Speed");
                    JSONObject jsonObjectwindmetric = jsonObjectspeed.getJSONObject("Metric");
                    String wind = String.valueOf(jsonObjectwindmetric.get("Value"));

                    CurrentWeatherModel currentWeatherModel = new CurrentWeatherModel(forecast, currentTemp, humidity, wind);
                    weatherListener.onWeatherFetchSuccess(currentWeatherModel);

                } catch (Exception e) {
                    e.printStackTrace();
                    weatherListener.onWeatherFetchFail();

                }
            }

            @Override
            public void onWeatherFetchFail() {
                weatherListener.onWeatherFetchFail();
            }
        });
    }


    public void makeLikeReq(String postid, final FeedsAdapterContract.LikeStatusListener LikeStatusListener) {
        getmApiHelper().makeLikeReq(getUid(), getAuthToken(), postid, new FeedsAdapterContract.LikeStatusListener() {
            @Override
            public void onSuccess(String response) {
                LikeStatusListener.onSuccess(response);
            }

            @Override
            public void onFail() {
                LikeStatusListener.onFail();
            }
        });
    }
}
