package com.farm.farm2fork;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.farm.farm2fork.Utils.ActivityUtils;

/**
 * Created by master on 8/4/18.
 */

public class BaseActivity extends AppCompatActivity {
    @Override
    protected void attachBaseContext(Context newBase) {
        String lang_code = ApplicationClass.localeCode; //load it from SharedPref
        Log.d("de", "attachBaseContext: " + lang_code);
        Context context = ActivityUtils.changeLang(newBase, lang_code);
        super.attachBaseContext(context);
    }
}
