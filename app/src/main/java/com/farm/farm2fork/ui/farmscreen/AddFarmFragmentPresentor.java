package com.farm.farm2fork.ui.farmscreen;

import android.os.Handler;

import com.farm.farm2fork.Models.LocationInfoModel;
import com.farm.farm2fork.data.AppDataManager;

import java.util.List;

/**
 * Created by master on 7/4/18.
 */

public class AddFarmFragmentPresentor implements FarmContract.CropListLoadListener, FarmContract.AddFarmReqListener {
    private final AppDataManager mAppDataManager;
    private FarmContract.AddFarmFragmentView view;

    public AddFarmFragmentPresentor(AppDataManager appDataManager) {
        mAppDataManager = appDataManager;
    }

    public AppDataManager getmAppDataManager() {
        return mAppDataManager;
    }

    public void loadCropList() {
        getmAppDataManager().loadCropList(this);
    }

    @Override
    public void onCropListFetch(List<String> cropList) {
        view.OnCropListFetch(cropList);
    }

    public void setView(FarmContract.AddFarmFragmentView view) {
        this.view = view;
    }

    public void addFarm(boolean locationtaken, String crop, String size, String farmSizeUnit, String imageencoded, LocationInfoModel mlocationInfoModel) {
        if (locationtaken && crop.length() != 0 && size.length() != 0) {
            view.showProgressBar();
            String farmSizeAcre = getAcreSize(size, farmSizeUnit);
            getmAppDataManager().sendAddFarmReq(crop, size, farmSizeAcre, farmSizeUnit, imageencoded, mlocationInfoModel, this);
        } else view.onValidationError();


    }

    private String getAcreSize(String size, String farmSizeUnit) {
        String farmSizeAcre = "NA";
        if (farmSizeUnit.equals("bigha")) {
            farmSizeAcre = String.valueOf(Float.valueOf(size) * 0.625);

        } else if (farmSizeUnit.equals("acre")) {
            farmSizeAcre = size;


        } else if (farmSizeUnit.equals("hectare")) {
            farmSizeAcre = String.valueOf(Float.valueOf(size) * 2.471);

        } else if (farmSizeUnit.equals("guntha")) {
            farmSizeAcre = String.valueOf(Float.valueOf(size) * 0.025);

        }
        return farmSizeAcre;
    }


    @Override
    public void onAddFarmReqSuccess(String response) {
        view.hideProgressBar();
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                view.onAddFarmReqSuccess();
            }
        });
    }

    @Override
    public void onAddFarmReqFail() {
        view.hideProgressBar();
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                view.onAddFarmReqFail();
            }
        });

    }
}
