package com.farm.farm2fork.ui.login;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;

import com.farm.farm2fork.R;

import java.util.Locale;

/**
 * Created by master on 10/3/18.
 */

public class ChooseLocaleFragment extends Fragment {
    private static final String TAG = ChooseLocaleFragment.class.getName();
    private Activity mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_choose_locale, container, false);

        final RadioGroup rg_locale = view.findViewById(R.id.rg_locale);


        Button btn_next = view.findViewById(R.id.btn_next);

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedid = rg_locale.getCheckedRadioButtonId();
                Log.d(TAG, "onClick: " + selectedid);
                switch (selectedid) {
                    case R.id.rb_eng:
                        updateLocale("en");
                    case R.id.rb_hi:
                        updateLocale("hi");
                    case R.id.rb_mr:
                        updateLocale("mr");
                    default:
                        updateLocale("en");
                }
            }
        });

        return view;
    }


    private void updateLocale(String localeCode) {
        Locale locale = new Locale(localeCode, "IN");
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        mContext.getBaseContext().getResources().updateConfiguration(config,
                mContext.getBaseContext().getResources().getDisplayMetrics());

        ((LoginActivity) mContext).showLoginFragment();
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
