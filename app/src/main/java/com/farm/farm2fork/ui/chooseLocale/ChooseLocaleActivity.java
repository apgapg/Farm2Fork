package com.farm.farm2fork.ui.chooseLocale;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

import com.farm.farm2fork.ApplicationClass;
import com.farm.farm2fork.R;
import com.farm.farm2fork.ui.farmscreen.FarmActivity;
import com.farm.farm2fork.ui.login.LoginActivity;

public class ChooseLocaleActivity extends AppCompatActivity {

    private static final String TAG = ChooseLocaleActivity.class.getName();
    private ChooseLocalePresentor mChooseLocalePresentor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (((ApplicationClass) getApplication()).getmAppDataManager().getLoggedInMode()) {
            startMainActivity();
        }
        setContentView(R.layout.fragment_choose_locale);

        mChooseLocalePresentor = new ChooseLocalePresentor(((ApplicationClass) getApplication()).getmAppDataManager());

        final RadioGroup rg_locale = findViewById(R.id.rg_locale);


        Button btn_next = findViewById(R.id.btn_next);

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedid = rg_locale.getCheckedRadioButtonId();
                Log.d(TAG, "onClick: " + selectedid);

                switch (selectedid) {
                    case R.id.rb_eng:
                        updateLocale("en");
                        break;
                    case R.id.rb_hi:
                        updateLocale("hi");
                        break;
                    case R.id.rb_mr:
                        updateLocale("mr");
                        break;
                    default:
                        updateLocale("en");
                        break;
                }
                startActivity(new Intent(ChooseLocaleActivity.this, LoginActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

    }

    public ChooseLocalePresentor getmChooseLocalePresentor() {
        return mChooseLocalePresentor;
    }

    private void updateLocale(String localeCode) {
        ApplicationClass.localeCode = localeCode;
        Log.d(TAG, "updateLocale: " + localeCode + "    " + ApplicationClass.localeCode);
        getmChooseLocalePresentor().updateLocale(localeCode);
    }

    public void startMainActivity() {
        startActivity(new Intent(ChooseLocaleActivity.this, FarmActivity.class));
        ActivityCompat.finishAffinity(this);
    }

}
