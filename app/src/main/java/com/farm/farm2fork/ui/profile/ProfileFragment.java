package com.farm.farm2fork.ui.profile;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.farm.farm2fork.ApplicationClass;
import com.farm.farm2fork.R;
import com.farm.farm2fork.Utils.KeyboardUtils;
import com.farm.farm2fork.Utils.NetworkUtils;
import com.farm.farm2fork.ui.farmscreen.FarmActivity;

import static com.farm.farm2fork.Utils.Constants.BASE_URL;


/**
 * Created by master on 10/3/18.
 */

public class ProfileFragment extends Fragment {
    private static final String TAG = ProfileFragment.class.getName();
    private Activity mContext;
    private View btn_save;
    private EditText fieldName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ((FarmActivity) mContext).setToolbarTitle("Profile");

        fieldName = view.findViewById(R.id.field_name);
        if (((ApplicationClass) mContext.getApplication()).getmAppDataManager().getName() != null) {
            fieldName.setText(((ApplicationClass) mContext.getApplication()).getmAppDataManager().getName());
        }

        btn_save = view.findViewById(R.id.btn_save);

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(fieldName.getText().toString().trim()))
                    makeProfileUpdateReqtoserver();
                else Toast.makeText(mContext, "Name cannot be empty!", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    private void makeProfileUpdateReqtoserver() {
        AndroidNetworking.post(BASE_URL + "updateprofile.php")
                .addBodyParameter("name", fieldName.getText().toString())
                .addBodyParameter("number", ((ApplicationClass) getActivity().getApplication()).getmAppDataManager().getNumber())
                .addBodyParameter("uid", ((ApplicationClass) getActivity().getApplication()).getmAppDataManager().getUid())
                .addBodyParameter("authtoken", ((ApplicationClass) getActivity().getApplication()).getmAppDataManager().getAuthToken())
                .addBodyParameter("location", "try")
                .addBodyParameter("gcm_token", "try")
                .addBodyParameter("deviceid", "try")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsObject(BaseResponseModel.class, new ParsedRequestListener<BaseResponseModel>() {


                    @Override
                    public void onResponse(BaseResponseModel response) {
                        Log.d(TAG, "onResponse: " + response.getMessage());
                        NetworkUtils.parseResponse(TAG, response);
                        Toast.makeText(mContext, "Profile updated successfully!", Toast.LENGTH_SHORT).show();
                        KeyboardUtils.hideKeyboard(mContext);

                        mContext.onBackPressed();
                    }

                    @Override
                    public void onError(ANError anError) {
                        NetworkUtils.parseError(TAG, anError);
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
