package com.farm.farm2fork.ui.farmscreen;

import android.content.Context;

import com.farm.farm2fork.Models.FarmModel;

import java.util.List;

/**
 * Created by master on 3/4/18.
 */

public class FarmPresentor implements FarmContract.Presentor, FarmDataManager.FarmFetchListener {


    private final FarmDataManager mFarmDataManager;
    private FarmContract.FarmView mFarmView;
    private FarmContract.AddFarmView mAddFarmView;

    public FarmPresentor(Context mContext) {

        mFarmDataManager = new FarmDataManager(mContext);

    }


    @Override
    public void makeFetchFarmReq() {
        mFarmView.showProgressBar();
        getmFarmDataManager().makeFetchFarmReq(this);
    }

    @Override
    public void onUnsubscribe() {
        getmFarmDataManager().onUnsubscribe();
    }

    @Override
    public void getCropList() {
        mFarmDataManager.getCropList(new FarmDataManager.CropListFetchListener() {
            @Override
            public void onCropListFetch(List<String> cropList) {
                mAddFarmView.OnCropListFetch(cropList);
            }
        });
    }

    @Override
    public void fetchCropNameList() {
        getmFarmDataManager().fetchCropNameList();
    }


    public void setmFarmView(FarmContract.FarmView mFarmView) {
        this.mFarmView = mFarmView;
    }

    public void setmAddFarmView(FarmContract.AddFarmView mAddFarmView) {
        this.mAddFarmView = mAddFarmView;
    }

    public FarmDataManager getmFarmDataManager() {
        return mFarmDataManager;
    }

    @Override
    public void onFarmFetchReqSuccess(List<FarmModel> response) {
        mFarmView.hideProgressBar();
        mFarmView.onFarmFetchReqSuccess(response);
    }

    @Override
    public void onFarmFetchReqFail() {
        mFarmView.hideProgressBar();
        mFarmView.onFetchFarmReqFail();

    }
}
