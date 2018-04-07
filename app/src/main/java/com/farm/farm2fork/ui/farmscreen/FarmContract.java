package com.farm.farm2fork.ui.farmscreen;

import com.farm.farm2fork.BasePresentor;
import com.farm.farm2fork.BaseView;
import com.farm.farm2fork.Models.FarmModel;

import java.util.List;

/**
 * Created by master on 3/4/18.
 */

public interface FarmContract {

    interface FarmView extends BaseView<Presentor> {

        void showProgressBar();

        void hideProgressBar();

        void onFetchFarmReqFail();

        void onFarmFetchReqSuccess(List<FarmModel> farmModelList);
    }

    interface AddFarmView extends BaseView<Presentor> {


        void OnCropListFetch(List<String> cropList);

    }

    interface Presentor extends BasePresentor {
        void makeFetchFarmReq();

        void onUnsubscribe();

        void getCropList();

        void fetchCropNameList();
    }

}
