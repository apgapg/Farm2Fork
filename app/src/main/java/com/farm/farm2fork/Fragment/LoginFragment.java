package com.farm.farm2fork.Fragment;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.farm.farm2fork.BuildConfig;
import com.farm.farm2fork.R;
import com.farm.farm2fork.SplashScreen;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import static com.farm.farm2fork.Fragment.AddFarmFragment.BASE_URL;

/**
 * Created by master on 10/3/18.
 */

public class LoginFragment extends Fragment {
    private static final String TAG = LoginFragment.class.getName();
    private Activity mContext;
    private ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_login, container, false);

        Button btn_next = view.findViewById(R.id.btn_next);
        final EditText numberfield = view.findViewById(R.id.numberfield);

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


               /* */


                if (numberfield.getText().toString().trim().length() != 10) {
                    Toast.makeText(mContext, "Please enter valid number", Toast.LENGTH_SHORT).show();
                    // proceedtoOtpscreen();
                } else {
                    Dexter.withActivity(mContext)
                            .withPermission(Manifest.permission.READ_SMS)
                            .withListener(new PermissionListener() {
                                @Override
                                public void onPermissionGranted(PermissionGrantedResponse response) {
                                    progressDialog = new ProgressDialog(mContext);
                                    progressDialog.setMessage("Please Wait!");
                                    progressDialog.setCancelable(false);
                                    progressDialog.show();
                                    ((SplashScreen) mContext).setnumber(numberfield.getText().toString().trim());
                                    makeOtpReqtoserver(numberfield.getText().toString().trim());
                                }

                                @Override
                                public void onPermissionDenied(PermissionDeniedResponse response) {/* ... */
                                    progressDialog = new ProgressDialog(mContext);
                                    progressDialog.setMessage("Please Wait!");
                                    progressDialog.setCancelable(false);
                                    progressDialog.show();
                                    ((SplashScreen) mContext).setnumber(numberfield.getText().toString().trim());
                                    makeOtpReqtoserver(numberfield.getText().toString().trim());
                                }

                                @Override
                                public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {/* ... */}
                            }).check();


                }

            }
        });

        return view;
    }

    private void makeOtpReqtoserver(String number) {
        Log.d(TAG, "makeOtpReqtoserver: " + number);
        AndroidNetworking.post(BASE_URL + "sendotp_1.php")
                .addBodyParameter("number", number)
                .addBodyParameter("gcm_token", "try")
                .addBodyParameter("deviceid", Settings.Secure.getString(mContext.getContentResolver(),
                        Settings.Secure.ANDROID_ID))
                .addBodyParameter("device", android.os.Build.DEVICE)
                .addBodyParameter("model", android.os.Build.MODEL)
                .addBodyParameter("app_version", BuildConfig.VERSION_NAME)
                .addBodyParameter("os_version", android.os.Build.VERSION.SDK)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "onResponse: " + response);
                        if (progressDialog != null) {
                            progressDialog.cancel();
                            progressDialog = null;
                        }


                        if (response.contains("success"))
                            proceedtoOtpscreen();
                        else
                            Toast.makeText(mContext, "Something went wrong! Please try again", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d(TAG, "onError: " + anError);
                        Toast.makeText(mContext, "Something went wrong! Please try again", Toast.LENGTH_SHORT).show();

                    }
                });
    }

    private void proceedtoOtpscreen() {
        Fragment fragment = null;
        Class fragmentClass = null;
        fragmentClass = OtpFragment.class;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        FragmentManager fragmentManager = ((SplashScreen) mContext).getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fl, fragment).commit();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity)
            mContext = (Activity) context;
        else throw new IllegalArgumentException("Context should be an instance of Activity");
    }

    @Override
    public void onDestroy() {
        mContext = null;
        super.onDestroy();

    }
}
