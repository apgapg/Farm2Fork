package com.farm.farm2fork.ui.login;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.farm.farm2fork.ApplicationClass;
import com.farm.farm2fork.R;

import swarajsaaj.smscodereader.interfaces.OTPListener;
import swarajsaaj.smscodereader.receivers.OtpReader;

/**
 * Created by master on 10/3/18.
 */

public class OtpFragment extends Fragment implements LoginContract.OtpFragmentView, OTPListener {
    private static final String TAG = OtpFragment.class.getName();
    private Activity mContext;
    private EditText ed_otp;
    private LoginContract.OtpFragmentPresentor mPresentor;
    private OtpFragmentPresentor mOtpFragmentPresentor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_otp, container, false);

        Button btn_next = view.findViewById(R.id.btn_next);
        ed_otp = view.findViewById(R.id.ed_otp);

        OtpReader.bind(this, "REWEYU");

        mOtpFragmentPresentor = new OtpFragmentPresentor(((ApplicationClass) mContext.getApplication()).getmAppDataManager());
        mOtpFragmentPresentor.setView(this);

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getmOtpFragmentPresentor().performValidationOfOtp(ed_otp.getText().toString().trim(), ((LoginActivity) mContext).getNumber());

            }
        });

        return view;
    }

    public OtpFragmentPresentor getmOtpFragmentPresentor() {
        return mOtpFragmentPresentor;
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
    public void otpReceived(String messageText) {
        Log.d(TAG, "OTP Received: " + messageText);
        String otp = messageText.replace("Your Reweyou authentication OTP: ", "");
        getmOtpFragmentPresentor().performValidationOfOtp(otp, ((LoginActivity) mContext).getNumber());

    }


    @Override
    public void onOtpValidationError() {
        Toast.makeText(mContext.getApplicationContext(), "OTP cant be empty!", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void showProgressBar() {
        ((LoginActivity) mContext).showProgressBar();

    }

    @Override
    public void hideProgressBar() {
        ((LoginActivity) mContext).hideProgressBar();

    }

    @Override
    public void onOtpVerificationSuccess() {
        ((LoginActivity) mContext).onOtpCorrect();

    }

    @Override
    public void onOtpVerificationFail() {
        Toast.makeText(mContext.getApplicationContext(), "Something went wrong! Please try again", Toast.LENGTH_SHORT).show();

    }
}
