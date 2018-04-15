package com.farm.farm2fork.ui.feeds;

public interface FeedsAdapterContract {

    interface LikeStatusListener {

        void onSuccess(String response);

        void onFail();
    }
}
