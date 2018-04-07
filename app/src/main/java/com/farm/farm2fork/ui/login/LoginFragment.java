package com.farm.farm2fork.ui.login;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.farm.farm2fork.ApplicationClass;
import com.farm.farm2fork.R;
import com.farm.farm2fork.data.PermissionHelper;

/**
 * Created by master on 10/3/18.
 */

public class LoginFragment extends Fragment implements LoginContract.LoginFragmentView, PermissionHelper.OnSMSPermissionCheck {
    private static final String TAG = LoginFragment.class.getName();
    private Activity mContext;
    private LoginFragmentPresentor mLoginFragmentPresentor;
    private String number;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_login, container, false);

        Button btn_next = view.findViewById(R.id.btn_next);
        final EditText numberfield = view.findViewById(R.id.numberfield);

        mLoginFragmentPresentor = new LoginFragmentPresentor(((ApplicationClass) mContext.getApplication()).getmAppDataManager());
        mLoginFragmentPresentor.setView(this);

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getmLoginFragmentPresentor().performLogin(numberfield.getText().toString().trim());

            }
        });

        return view;
    }

    public LoginFragmentPresentor getmLoginFragmentPresentor() {
        return mLoginFragmentPresentor;
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


    @Override
    public void onValidationError() {
        Toast.makeText(mContext.getApplicationContext(), "Please enter valid number!", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void checkSMSpermission() {
        PermissionHelper permissionHelper = new PermissionHelper(mContext, this);
        permissionHelper.checkSMSpermission();
    }

    @Override
    public void showProgressBar() {
        ((LoginActivity) mContext).showProgressBar();
    }

    @Override
    public void onOtpReqSuccess() {
        ((LoginActivity) mContext).onOtpReqSuccess();
    }

    @Override
    public void hideProgressBar() {
        ((LoginActivity) mContext).hideProgressBar();
    }

    @Override
    public void onValidationSuccess(String number) {
        ((LoginActivity) mContext).setnumber(number);
    }

    @Override
    public void onOtpReqFail() {
        Toast.makeText(mContext.getApplicationContext(), "Something went wrong! Please try again", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onSMSPermissionGranted() {
        getmLoginFragmentPresentor().sendOtpReqtoServer(((LoginActivity) mContext).getNumber());
    }

    @Override
    public void onSMSPermissionNotGranted() {
        getmLoginFragmentPresentor().sendOtpReqtoServer(number);
    }
}
