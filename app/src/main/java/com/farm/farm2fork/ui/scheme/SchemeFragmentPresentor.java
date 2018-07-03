package com.farm.farm2fork.ui.scheme;

import com.farm.farm2fork.data.AppDataManager;
import com.farm.farm2fork.models.SchemeModel;

import java.util.List;

/**
 * Created by master on 7/4/18.
 */

public class SchemeFragmentPresentor {

    private final AppDataManager mAppDataManager;
    private SchemeContract.View view;

    public SchemeFragmentPresentor(AppDataManager appDataManager) {
        mAppDataManager = appDataManager;
    }

    public AppDataManager getmAppDataManager() {
        return mAppDataManager;
    }

    public void setView(SchemeContract.View view) {
        this.view = view;

    }

    public void sendSchemeDataReq() {
        getmAppDataManager().sendSchemesReq(new SchemeContract.SchemeFetchListener() {
            @Override
            public void onSchemeFetchReqSuccess(List<SchemeModel> list) {
                view.onSchemeListFetchSuccess(list);
            }

            @Override
            public void onSchemeFetchReqFail() {
                view.onSchemeListFetchFail();
            }
        });
    }
}
