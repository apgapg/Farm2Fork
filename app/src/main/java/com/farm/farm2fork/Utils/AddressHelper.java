package com.farm.farm2fork.Utils;

import android.location.Address;

/**
 * Created by master on 6/6/18.
 */
public class AddressHelper {
    public static String getAddressInString(Address address) {
        String strAddress = "";

        StringBuilder strReturnedAddress = new StringBuilder();

        for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
            strReturnedAddress.append(address.getAddressLine(i)).append("\n");
        }
        strAddress = strReturnedAddress.toString();

        return strAddress.trim();
    }
}
