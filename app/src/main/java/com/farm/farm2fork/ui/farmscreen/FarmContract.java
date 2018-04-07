package com.farm.farm2fork.ui.farmscreen;

import com.farm.farm2fork.BaseView;
import com.farm.farm2fork.Models.FarmModel;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by master on 3/4/18.
 */

public interface FarmContract {

    interface FarmFragmentView extends BaseView {

        void showProgressBar();

        void hideProgressBar();

        void onFetchFarmReqFail();

        void onFarmFetchReqSuccess(List<FarmModel> farmModelList);
    }

    interface AddFarmFragmentView extends BaseView {


        void OnCropListFetch(List<String> cropList);

        void onValidationError();

        void showProgressBar();

        void onAddFarmReqFail();

        void onAddFarmReqSuccess();

        void hideProgressBar();
    }


    interface CropListReqListener {
        void onCropListFetch(JSONObject response);

    }

    interface FarmFetchListener {
        void onFarmFetchReqSuccess(List<FarmModel> response);

        void onFarmFetchReqFail();
    }

    interface CropListLoadListener {
        void onCropListFetch(List<String> cropList);
    }

    interface AddFarmReqListener {

        void onAddFarmReqSuccess(String response);

        void onAddFarmReqFail();

    }
}
