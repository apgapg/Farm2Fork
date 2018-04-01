package com.farm.farm2fork;

import com.androidnetworking.AndroidNetworking;
import com.orm.SugarApp;

/**
 * Created by master on 10/3/18.
 */

public class ApplicationClass extends SugarApp {
    public static final String PACKAGE_NAME = "com.farm.farm2fork";

    @Override
    public void onCreate() {

        super.onCreate();
        AndroidNetworking.initialize(getApplicationContext());


    }


}
