package com.farm.data;

import android.Manifest;
import android.app.Activity;
import android.content.Context;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

/**
 * Created by master on 1/4/18.
 */

public class PermissionHelper {

    private final Context context;
    private final OnSMSPermissionCheck onSMSPermissionCheck;

    public PermissionHelper(Context context, OnSMSPermissionCheck onSMSPermissionCheck) {
        this.context = context;
        this.onSMSPermissionCheck = onSMSPermissionCheck;
    }

    public void checkSMSpermission() {

        Dexter.withActivity((Activity) context)
                .withPermission(Manifest.permission.READ_SMS)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        onSMSPermissionCheck.onSMSPermissionGranted();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        onSMSPermissionCheck.onSMSPermissionNotGranted();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    public interface OnSMSPermissionCheck {
        void onSMSPermissionGranted();

        void onSMSPermissionNotGranted();
    }
}
