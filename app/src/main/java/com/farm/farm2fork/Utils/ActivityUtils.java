package com.farm.farm2fork.Utils;

import android.support.annotation.NonNull;

/**
 * Created by master on 4/4/18.
 */

public class ActivityUtils {
    public static @NonNull
    <T> T checkNotNull(final T reference, final Object errorMessage) {
        if (reference == null) {
            throw new NullPointerException(String.valueOf(errorMessage));
        }
        return reference;
    }
}
