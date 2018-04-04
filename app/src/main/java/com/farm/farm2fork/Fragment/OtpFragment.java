package com.farm.farm2fork.Fragment;

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

import com.farm.farm2fork.R;
import com.farm.farm2fork.ui.login.LoginActivity;

import swarajsaaj.smscodereader.interfaces.OTPListener;
import swarajsaaj.smscodereader.receivers.OtpReader;

/**
 * Created by master on 10/3/18.
 */

public class OtpFragment extends Fragment implements OTPListener {
    private static final String TAG = OtpFragment.class.getName();
    private Activity mContext;
    private EditText ed_otp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_otp, container, false);

        Button btn_next = view.findViewById(R.id.btn_next);
        ed_otp = view.findViewById(R.id.ed_otp);

        OtpReader.bind(this, "REWEYU");

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ((LoginActivity) mContext).performValidationOfOtp(ed_otp.getText().toString().trim());

            }
        });

        return view;
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
        Log.d(TAG, "otpReceived: " + messageText);

        ((LoginActivity) mContext).onOtpReceived(messageText.replace("Your Reweyou authentication OTP: ", ""));

    }
}
