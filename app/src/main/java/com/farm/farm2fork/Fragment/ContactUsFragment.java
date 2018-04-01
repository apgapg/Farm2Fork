package com.farm.farm2fork.Fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.farm.farm2fork.R;
import com.farm.farm2fork.activity.MainNavScreen;

/**
 * Created by master on 10/3/18.
 */

public class ContactUsFragment extends Fragment {
    private Activity mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_contact_us, container,false);
        ((MainNavScreen) mContext).setToolbarTitle("Contact Us");



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
}
