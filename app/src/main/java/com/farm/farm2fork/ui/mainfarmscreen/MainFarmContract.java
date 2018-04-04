package com.farm.farm2fork.ui.mainfarmscreen;

import com.farm.farm2fork.BasePresentor;
import com.farm.farm2fork.BaseView;
import com.farm.farm2fork.Models.FarmModel;

import java.util.List;

/**
 * Created by master on 3/4/18.
 */

public interface MainFarmContract {

    interface View extends BaseView<Presentor> {

        void showProgressBar();

        void hideProgressBar();

        void onFetchFarmReqFail();

        void onFarmFetchReqSuccess(List<FarmModel> farmModelList);
    }

    interface Presentor extends BasePresentor {
        void makeFetchFarmReq();
    }

}
