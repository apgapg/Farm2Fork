package com.farm.farm2fork;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.farm.farm2fork.Fragment.LoginFragment;
import com.farm.farm2fork.Utils.UserSessionManager;

import org.json.JSONObject;

import static com.farm.farm2fork.Fragment.AddFarmFragment.BASE_URL;

public class SplashScreen extends AppCompatActivity {

    private static final String TAG = SplashScreen.class.getName();
    private String number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        UserSessionManager userSessionManager = new UserSessionManager(this);
        if (userSessionManager.isUserLoggedIn()) {
            startMainActivity();
        } else
            populateLoginFragment();
    }

    private void populateLoginFragment() {
        Fragment fragment = null;
        Class fragmentClass = null;
        fragmentClass = LoginFragment.class;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fl, fragment).commit();

    }

    public void startMainActivity() {
        startActivity(new Intent(SplashScreen.this, MainNavScreen.class));
        finish();
    }

    public void validateOtpReq(String messageText) {
        AndroidNetworking.post(BASE_URL + "verifyotp.php")
                .addBodyParameter("number", number)
                .addBodyParameter("otp", messageText)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            UserSessionManager userSessionManager = new UserSessionManager(SplashScreen.this);
                            userSessionManager.createUserRegisterSession(response.getString("uid"), response.getString("authtoken"), number);
                            Toast.makeText(SplashScreen.this, "OTP verification successful", Toast.LENGTH_SHORT).show();

                            startMainActivity();
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(SplashScreen.this, "Something went wrong! Please try again", Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                        Log.d(TAG, "onError: " + anError);
                        Toast.makeText(SplashScreen.this, "Something went wrong! Please try again", Toast.LENGTH_SHORT).show();


                    }
                });
    }

    public void setnumber(String number) {
        this.number = number;
    }
}
