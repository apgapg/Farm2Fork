package com.farm.farm2fork.Fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.farm.farm2fork.Interface.LocationSetListener;
import com.farm.farm2fork.MainNavScreen;
import com.farm.farm2fork.Models.LocationInfoModel;
import com.farm.farm2fork.R;
import com.farm.farm2fork.Utils.UserSessionManager;
import com.schibstedspain.leku.LocationPickerActivity;

/**
 * Created by master on 10/3/18.
 */

public class AddFarmFragment extends Fragment {
    public static final String BASE_URL = "https://www.reweyou.in/fasalapp/";
    private static final String TAG = AddFarmFragment.class.getName();
    private Activity mContext;
    private Button btn_add;
    private TextView txt_location;
    private UserSessionManager userSessionManager;
    private LocationInfoModel mlocationInfoModel;
    private EditText ed_size, ed_crop;
    private boolean locationtaken=false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_farm, container, false);
        userSessionManager = new UserSessionManager(mContext);
        btn_add = view.findViewById(R.id.add);
        txt_location = view.findViewById(R.id.txt_location);

        ed_size = view.findViewById(R.id.ed_size);
        ed_crop = view.findViewById(R.id.ed_crop);

        ((MainNavScreen) mContext).setonLocationSetByUser(new LocationSetListener() {

            @Override
            public void onLocationSetByUser(LocationInfoModel locationInfoModel) {
                txt_location.setText(locationInfoModel.getAddress());
                mlocationInfoModel = locationInfoModel;
                locationtaken=true;


            }
        });

        txt_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new LocationPickerActivity.Builder()
                        .withGooglePlacesEnabled()
                        .withGeolocApiKey("AIzaSyBtuQ0bOdBshWBziK31gyUY2wKFnQnrEyc")
                        .withSearchZone("es_IN")
                        .shouldReturnOkOnBackPressed()
                        .withSatelliteViewHidden()
                        .build(((MainNavScreen) mContext));

                ((MainNavScreen) mContext).startActivityForResult(intent, 1);
            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (locationtaken && ed_crop.getText().toString().trim().length() != 0 && ed_size.getText().toString().trim().length() != 0)
                    makeServerReq();
                else
                    Toast.makeText(mContext, "Please fill up all details", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    private void makeServerReq() {
        final ProgressDialog progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage("Adding Farm! Please Wait");
        progressDialog.show();
        Log.d(TAG, "makeServerReq: " + ed_size.getText().toString().trim());
        Log.d(TAG, "makeServerReq: " + ed_crop.getText().toString());
        Log.d(TAG, "makeServerReq: " + mlocationInfoModel.getCity());
        AndroidNetworking.post(BASE_URL + "addfarm.php")
                .addBodyParameter("crop", ed_crop.getText().toString().trim())
                .addBodyParameter("farmsize", ed_size.getText().toString().trim())
                .addBodyParameter("image", "dummy")
                .addBodyParameter("loc_address", mlocationInfoModel.getAddress())
                .addBodyParameter("loc_lat", mlocationInfoModel.getLatitude())
                .addBodyParameter("loc_long", mlocationInfoModel.getLongitude())
                .addBodyParameter("postalCode", mlocationInfoModel.getPostalzipcode())
                .addBodyParameter("loc_city", mlocationInfoModel.getCity())
                .addBodyParameter("uid", userSessionManager.getUID())
                .addBodyParameter("authtoken", userSessionManager.getAuthToken())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.cancel();
                        Log.d(TAG, "onResponse: " + response);
                        if (response.contains("Successfully Uploaded")) {
                            ((MainNavScreen) mContext).showMainScreen();
                        } else
                            Toast.makeText(mContext, "Something went wrong! Please try again", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d(TAG, "onError: " + anError);
                        progressDialog.cancel();
                        Toast.makeText(mContext, "Something went wrong! Please try again", Toast.LENGTH_SHORT).show();
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
