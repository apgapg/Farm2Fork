package com.farm.farm2fork.data;

import com.farm.farm2fork.Models.CropNameModel;
import com.farm.farm2fork.Models.FarmModel;
import com.farm.farm2fork.Models.LocationInfoModel;
import com.farm.farm2fork.data.prefs.AppPrefsHelper;
import com.farm.farm2fork.ui.farmscreen.FarmContract;
import com.farm.farm2fork.ui.farmscreen.ObservableHelper;
import com.farm.farm2fork.ui.login.ApiHelper;
import com.farm.farm2fork.ui.login.LoginContract;

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
    private final ObservableHelper mObservableHelper;
    private LoginContract.OtpReqListener mOtpReqListener;
    private LoginContract.OtpCheckListener mOtpCheckListener;
    private FarmContract.FarmFetchListener mFarmFetchListener;

    public AppDataManager(AppPrefsHelper appPrefsHelper) {
        mAppPrefsHelper = appPrefsHelper;
        mApiHelper = new ApiHelper();
        mObservableHelper = new ObservableHelper();

    }

    public AppPrefsHelper getmAppPrefsHelper() {
        return mAppPrefsHelper;
    }

    public ApiHelper getmApiHelper() {
        return mApiHelper;
    }

    public ObservableHelper getmObservableHelper() {
        return mObservableHelper;
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

    public void loadCropList(final FarmContract.CropListLoadListener cropListLoadListener) {
        getmObservableHelper().getCropList(new ObservableHelper.CropListFetchListener() {

            @Override
            public void onCropListFetch(List<String> cropList) {
                cropListLoadListener.onCropListFetch(cropList);
            }
        });
    }

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
}
