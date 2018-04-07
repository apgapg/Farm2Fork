package com.farm.farm2fork.Utils;

import android.content.Context;
import android.provider.Settings;

/**
 * Created by master on 7/4/18.
 */

public class AppUtils {
    public static String getDeviceId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

}
