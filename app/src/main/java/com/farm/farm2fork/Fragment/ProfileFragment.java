package com.farm.farm2fork.Fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.farm.farm2fork.R;
import com.farm.farm2fork.ui.farmscreen.FarmActivity;

import static com.farm.farm2fork.Utils.Constants.BASE_URL;


/**
 * Created by master on 10/3/18.
 */

public class ProfileFragment extends Fragment {
    private static final String TAG = ProfileFragment.class.getName();
    private Activity mContext;
    private View btn_save;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ((FarmActivity) mContext).setToolbarTitle("Profile");

        btn_save = view.findViewById(R.id.btn_save);

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeProfileUpdateReqtoserver();
            }
        });
        return view;
    }

    private void makeProfileUpdateReqtoserver() {
        AndroidNetworking.post(BASE_URL + "updateprofile.php")
                .addBodyParameter("name", "Aaloo")
                .addBodyParameter("number", "7054392300")
                .addBodyParameter("location", "try")
                .addBodyParameter("gcm_token", "try")
                .addBodyParameter("deviceid", "try")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "onResponse: " + response);

                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d(TAG, "onError: " + anError);
                    }
                });
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
