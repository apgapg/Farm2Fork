package com.farm.farm2fork.ui.farmscreen;

import android.content.Context;

import com.farm.farm2fork.Models.FarmModel;
import com.farm.farm2fork.data.UserDataManager;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by master on 7/4/18.
 */

public class FarmDataManager implements FarmReqManager.OnCropListReqListener, FarmReqManager.FarmFetchListener {
    private static final String FETCH_CROP_LIST_BOOLEAN = "croplistfetch";

    private final ObservableHelper mObservableHelper;
    private final UserDataManager mUserDataManager;
    private final FarmReqManager mFarmReqManager;
    private Object cropList;
    private CropListFetchListener mCropListFetchListener;
    private FarmFetchListener mFarmFetchListener;

    public FarmDataManager(Context mContext) {
        mObservableHelper = new ObservableHelper();
        mUserDataManager = new UserDataManager(mContext);
        mFarmReqManager = new FarmReqManager(this);
    }

    public void getCropList(CropListFetchListener cropListFetchListener) {
        this.mCropListFetchListener = cropListFetchListener;
        mObservableHelper.getCropList(new ObservableHelper.CropListFetchListener() {

            @Override
            public void onCropListFetch(List<String> cropList) {
                mCropListFetchListener.onCropListFetch(cropList);
            }
        });
    }

    public void onUnsubscribe() {
        mObservableHelper.onUnsubscribe();
    }


    @Override
    public void onFarmFetchReqSuccess(List<FarmModel> farmModelList) {
        mFarmFetchListener.onFarmFetchReqSuccess(farmModelList);

    }

    @Override
    public void onFarmFetchReqFail() {
        mFarmFetchListener.onFarmFetchReqFail();

    }

    public UserDataManager getmUserDataManager() {
        return mUserDataManager;
    }

    public FarmReqManager getmFarmReqManager() {
        return mFarmReqManager;
    }

    public void fetchCropNameList() {
        if (!getmUserDataManager().getvaluefromsharedprefBoolean(FETCH_CROP_LIST_BOOLEAN)) {
            getmFarmReqManager().sendCropNameListFetchReq(this);
        }

    }

    @Override
    public void onCropListFetch(JSONObject response) {
        if (getmUserDataManager().saveCropList(response)) {
            getmUserDataManager().putinsharedprefBoolean(FETCH_CROP_LIST_BOOLEAN, true);
        }
    }

    public void makeFetchFarmReq(FarmDataManager.FarmFetchListener farmFetchListener) {
        this.mFarmFetchListener = farmFetchListener;
        getmFarmReqManager().makeFarmFetchReq(getmUserDataManager());
    }


    public interface CropListFetchListener {
        void onCropListFetch(List<String> cropList);
    }

    public interface FarmFetchListener {
        void onFarmFetchReqSuccess(List<FarmModel> response);

        void onFarmFetchReqFail();
    }
}
