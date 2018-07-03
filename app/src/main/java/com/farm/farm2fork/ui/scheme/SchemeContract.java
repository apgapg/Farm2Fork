package com.farm.farm2fork.ui.scheme;

import com.farm.farm2fork.models.SchemeModel;

import java.util.List;

/**
 * Created by master on 7/4/18.
 */

public class SchemeContract {
    public interface View {
        void onSchemeListFetchSuccess(List<SchemeModel> list);

        void onSchemeListFetchFail();
    }

    public interface SchemeFetchListener {

        void onSchemeFetchReqSuccess(List<SchemeModel> response);

        void onSchemeFetchReqFail();
    }
}
