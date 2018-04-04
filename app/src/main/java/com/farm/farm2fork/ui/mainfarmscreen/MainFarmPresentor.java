package com.farm.farm2fork.ui.mainfarmscreen;

import android.content.Context;

import com.farm.farm2fork.Models.FarmModel;
import com.farm.farm2fork.data.UserDataManager;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by master on 3/4/18.
 */

public class MainFarmPresentor implements MainFarmContract.Presentor, MainFarmReqManager.OnCropListReqListener, MainFarmReqManager.Presentor {

    private static final String FETCH_CROP_LIST_BOOLEAN = "croplistfetch";
    private final UserDataManager mUserDataManager;
    private final MainFarmReqManager mMainFarmReqManager;
    private final MainFarmContract.View mAddFarmView;

    public MainFarmPresentor(Context mContext, MainFarmContract.View mAddFarmView) {
        mUserDataManager = new UserDataManager(mContext);
        mMainFarmReqManager = new MainFarmReqManager(this);
        this.mAddFarmView = mAddFarmView;
        mAddFarmView.setPresentor(this);

    }

    public UserDataManager getmUserDataManager() {
        return mUserDataManager;
    }

    public MainFarmReqManager getmMainFarmReqManager() {
        return mMainFarmReqManager;
    }


    public void fetchCropNameList() {
        if (!getmUserDataManager().getvaluefromsharedprefBoolean(FETCH_CROP_LIST_BOOLEAN)) {
            getmMainFarmReqManager().sendCropNameListFetchReq(this);
        }

    }

    @Override
    public void onCropListFetch(JSONObject response) {
        if (getmUserDataManager().saveCropList(response)) {
            getmUserDataManager().putinsharedprefBoolean(FETCH_CROP_LIST_BOOLEAN, true);
        }
    }

    @Override
    public void makeFetchFarmReq() {
        mAddFarmView.showProgressBar();
        getmMainFarmReqManager().makeFarmFetchReq(getmUserDataManager());
    }

    @Override
    public void onFarmFetchReqSuccess(List<FarmModel> farmModelList) {
        mAddFarmView.hideProgressBar();
        mAddFarmView.onFarmFetchReqSuccess(farmModelList);
    }

    @Override
    public void onFarmFetchReqFail() {
        mAddFarmView.hideProgressBar();
        mAddFarmView.onFetchFarmReqFail();

    }
}
