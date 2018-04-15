package com.farm.farm2fork.ui.feeds;

import com.farm.farm2fork.data.AppDataManager;

public class FeedsAdapterPresentor {
    private final AppDataManager mAppDataManager;

    public FeedsAdapterPresentor(AppDataManager appDataManager) {
        mAppDataManager = appDataManager;
    }

    public AppDataManager getmAppDataManager() {
        return mAppDataManager;
    }

    public void likeReq(String postid, final FeedsAdapterContract.LikeStatusListener likeStatusListener) {
        getmAppDataManager().makeLikeReq(postid, new FeedsAdapterContract.LikeStatusListener() {
            @Override
            public void onSuccess(String response) {
                if (response.equals("1")) {
                    likeStatusListener.onSuccess("true");
                } else if (response.equals("0")) {
                    likeStatusListener.onSuccess("false");
                }
            }

            @Override
            public void onFail() {
                likeStatusListener.onFail();
            }
        });
    }
}
