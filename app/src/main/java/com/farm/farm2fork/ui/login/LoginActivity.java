package com.farm.farm2fork.ui.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.farm.farm2fork.Fragment.LoginFragment;
import com.farm.farm2fork.Fragment.OtpFragment;
import com.farm.farm2fork.R;
import com.farm.farm2fork.ui.mainfarmscreen.FarmScreen;

public class LoginActivity extends AppCompatActivity implements LoginView {

    private static final String TAG = LoginActivity.class.getName();
    private String number;
    private LoginPresentorImp loginPresentorImp;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        loginPresentorImp = new LoginPresentorImp(this, this);
        if (loginPresentorImp.getUserSessionDataManager().getLoggedInMode()) {
            startMainActivity();
        } else
            showFragment(new LoginFragment());

    }

    private void showFragment(Fragment fragment) {

        try {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.fl, fragment).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startMainActivity() {
        startActivity(new Intent(LoginActivity.this, FarmScreen.class));

        ActivityCompat.finishAffinity(this);
    }


    public void performLogin(String number) {
        this.number = number;
        loginPresentorImp.performLogin(number);
    }

    @Override
    public void onValidationError() {
        Toast.makeText(getApplicationContext(), "Please enter valid number!", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onOtpReqFail() {
        Toast.makeText(getApplicationContext(), "Something went wrong! Please try again", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onOtpReqSuccess() {
        showFragment(new OtpFragment());
    }

    @Override
    public void showProgressBar() {
        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("Please Wait!");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    public void hideProgressBar() {
        if (progressDialog != null) {
            progressDialog.cancel();
            progressDialog = null;
        }


    }

    @Override
    public void onOtpValidationError() {
        Toast.makeText(getApplicationContext(), "OTP cant be empty!", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onOtpCorrect() {
        Toast.makeText(getApplicationContext(), "OTP verification successful", Toast.LENGTH_SHORT).show();
        startMainActivity();


    }

    @Override
    public void onOtpIncorrect() {
        Toast.makeText(getApplicationContext(), "Something went wrong! Please try again", Toast.LENGTH_SHORT).show();

    }

    public void performValidationOfOtp(String otp) {
        loginPresentorImp.performValidationOfOtp(otp, number);
    }

    public void onOtpReceived(String otp) {
        loginPresentorImp.performValidationOfOtp(otp, number);
    }
}
