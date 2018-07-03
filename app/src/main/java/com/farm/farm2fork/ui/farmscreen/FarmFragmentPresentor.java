package com.farm.farm2fork.ui.farmscreen;

import android.os.Handler;

import com.farm.farm2fork.data.AppDataManager;
import com.farm.farm2fork.models.FarmModel;

import java.util.List;

/**
 * Created by master on 7/4/18.
 */

public class FarmFragmentPresentor implements FarmContract.FarmFetchListener {
    private final AppDataManager mAppDataManager;
    private FarmContract.FarmFragmentView view;

    public FarmFragmentPresentor(AppDataManager appDataManager) {
        mAppDataManager = appDataManager;
    }

    public AppDataManager getmAppDataManager() {
        return mAppDataManager;
    }

    public void makeFetchFarmReq() {
        view.showProgressBar();
        getmAppDataManager().makeFetchFarmReq(this);
    }

    public void setView(FarmContract.FarmFragmentView view) {
        this.view = view;
    }

    @Override
    public void onFarmFetchReqSuccess(final List<FarmModel> response) {
        view.hideProgressBar();
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                view.onFarmFetchReqSuccess(response);
            }
        });
    }

    @Override
    public void onFarmFetchReqFail() {
        view.hideProgressBar();
        view.onFetchFarmReqFail();

    }
}
