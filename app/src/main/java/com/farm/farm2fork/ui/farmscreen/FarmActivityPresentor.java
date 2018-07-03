package com.farm.farm2fork.ui.farmscreen;

import com.farm.farm2fork.data.AppDataManager;
import com.farm.farm2fork.models.FarmModel;

import java.util.List;

/**
 * Created by master on 3/4/18.
 */

public class FarmActivityPresentor implements FarmContract.FarmFetchListener {


    private final AppDataManager mAppDataManager;
    private FarmContract.FarmFragmentView mFarmFragmentView;
    private FarmContract.AddFarmFragmentView mAddFarmFragmentView;

    public FarmActivityPresentor(AppDataManager appDataManager) {
        mAppDataManager = appDataManager;
    }

    public AppDataManager getmAppDataManager() {
        return mAppDataManager;
    }


    public void fetchCropNameList() {
        getmAppDataManager().fetchCropNameList();
    }


    @Override
    public void onFarmFetchReqSuccess(List<FarmModel> response) {
        mFarmFragmentView.hideProgressBar();
        mFarmFragmentView.onFarmFetchReqSuccess(response);
    }

    @Override
    public void onFarmFetchReqFail() {
        mFarmFragmentView.hideProgressBar();
        mFarmFragmentView.onFetchFarmReqFail();

    }
}
