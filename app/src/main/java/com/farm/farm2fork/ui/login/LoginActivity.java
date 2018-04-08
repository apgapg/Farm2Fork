package com.farm.farm2fork.ui.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

import com.farm.farm2fork.BaseActivity;
import com.farm.farm2fork.R;
import com.farm.farm2fork.ui.farmscreen.FarmActivity;

public class LoginActivity extends BaseActivity {

    private static final String TAG = LoginActivity.class.getName();
    private String number;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        showFragment(new LoginFragment(), false);

    }

    private void showFragment(Fragment fragment, boolean b) {

        try {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            if (b)
                fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
            fragmentTransaction.replace(R.id.fl, fragment).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startMainActivity() {
        startActivity(new Intent(LoginActivity.this, FarmActivity.class));
        ActivityCompat.finishAffinity(this);
    }


    public void onOtpReqSuccess() {
        showFragment(new OtpFragment(), true);
    }

    public void showProgressBar() {
        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("Please Wait!");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public void hideProgressBar() {
        if (progressDialog != null) {
            progressDialog.cancel();
            progressDialog = null;
        }
    }

    public void onOtpCorrect() {
        Toast.makeText(getApplicationContext(), "OTP verification successful", Toast.LENGTH_SHORT).show();
        startMainActivity();
    }

    public void setnumber(String number) {
        this.number = number;
    }

    public String getNumber() {
        return number;
    }

    public void showLoginFragment() {
        showFragment(new LoginFragment(), true);
    }
}
