package com.farm.farm2fork.ui.mainfarmscreen;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.farm.farm2fork.Models.FarmModel;
import com.farm.farm2fork.Utils.NetworkUtils;
import com.farm.farm2fork.data.UserDataManager;

import org.json.JSONObject;

import java.util.List;

import static com.farm.farm2fork.Fragment.AddFarmFragment.BASE_URL;

/**
 * Created by master on 3/4/18.
 */

public class FarmReqManager {

    private static final String TAG = FarmReqManager.class.getName();
    private final Presentor mPresentor;

    public FarmReqManager(FarmReqManager.Presentor mPresentor) {
        this.mPresentor = mPresentor;
    }

    public void sendCropNameListFetchReq(final OnCropListReqListener onCropListReqListener) {

        AndroidNetworking.get(BASE_URL + "data_crop_list.php")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        NetworkUtils.parseResponse(TAG, response);
                        onCropListReqListener.onCropListFetch(response);
                    }

                    @Override
                    public void onError(ANError anError) {
                        NetworkUtils.parseError(TAG, anError);
                    }
                });
    }

    public void makeFarmFetchReq(UserDataManager userDataManager) {
        AndroidNetworking.post(BASE_URL + "fetchfarms.php")
                .addBodyParameter("authtoken", userDataManager.getAuthToken())
                .addBodyParameter("uid", userDataManager.getUid())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsObjectList(FarmModel.class, new ParsedRequestListener<List<FarmModel>>() {
                    @Override
                    public void onResponse(final List<FarmModel> response) {
                        NetworkUtils.parseResponse(TAG, response);
                        mPresentor.onFarmFetchReqSuccess(response);
                    }

                    @Override
                    public void onError(ANError anError) {
                        NetworkUtils.parseError(TAG, anError);

                        mPresentor.onFarmFetchReqFail();
                    }
                });

    }


    public interface OnCropListReqListener {
        void onCropListFetch(JSONObject response);

    }

    public interface Presentor {
        void onFarmFetchReqSuccess(List<FarmModel> response);

        void onFarmFetchReqFail();
    }
}
