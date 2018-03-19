package com.farm.farm2fork;

import android.app.Application;

import com.androidnetworking.AndroidNetworking;
import com.farm.farm2fork.Utils.UserSessionManager;

/**
 * Created by master on 10/3/18.
 */

public class ApplicationClass extends Application {

    @Override
    public void onCreate() {

        super.onCreate();
        AndroidNetworking.initialize(getApplicationContext());


    }


}
