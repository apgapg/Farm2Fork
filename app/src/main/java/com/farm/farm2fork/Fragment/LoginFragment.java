package com.farm.farm2fork.Fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.farm.farm2fork.R;
import com.farm.farm2fork.ui.login.LoginActivity;

/**
 * Created by master on 10/3/18.
 */

public class LoginFragment extends Fragment {
    private static final String TAG = LoginFragment.class.getName();
    private Activity mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_login, container, false);

        Button btn_next = view.findViewById(R.id.btn_next);
        final EditText numberfield = view.findViewById(R.id.numberfield);

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ((LoginActivity) mContext).performLogin(numberfield.getText().toString().trim());

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
}
