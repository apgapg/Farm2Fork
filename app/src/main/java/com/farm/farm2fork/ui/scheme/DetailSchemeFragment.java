package com.farm.farm2fork.ui.scheme;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.farm.farm2fork.R;
import com.farm.farm2fork.models.SchemeModel;
import com.farm.farm2fork.ui.farmscreen.FarmActivity;
import com.google.gson.Gson;

/**
 * Created by master on 10/3/18.
 */

public class DetailSchemeFragment extends Fragment {
    private Activity mContext;
    private TextView name, about, eligibility, benefits, avail, otherdetail;
    private SchemeModel schemeModel;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((FarmActivity) mContext).setToolbarTitle(getResources().getString(R.string.details));

        if (view != null) {
            if (view.getParent() != null)
                ((ViewGroup) view.getParent()).removeView(view);
            return view;
        }
        view = inflater.inflate(R.layout.fragment_detail_scheme, container, false);

        schemeModel = new Gson().fromJson(getArguments().getString("scheme"), SchemeModel.class);

        name = view.findViewById(R.id.name);
        about = view.findViewById(R.id.about);
        eligibility = view.findViewById(R.id.eligibility);
        benefits = view.findViewById(R.id.benefits);
        avail = view.findViewById(R.id.avail);
        otherdetail = view.findViewById(R.id.otherdetail);

        name.setText(schemeModel.getName().trim());
        about.setText(schemeModel.getAbout().trim());
        eligibility.setText(schemeModel.getEligibility().trim());
        benefits.setText(schemeModel.getBenefits().trim());
        avail.setText(schemeModel.getAvail().trim());
        otherdetail.setText(schemeModel.getOtherdetails().trim());

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
