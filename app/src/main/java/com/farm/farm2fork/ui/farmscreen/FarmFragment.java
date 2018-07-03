package com.farm.farm2fork.ui.farmscreen;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.farm.farm2fork.ApplicationClass;
import com.farm.farm2fork.FarmAdapter.FarmAdapter;
import com.farm.farm2fork.R;
import com.farm.farm2fork.models.FarmModel;

import java.util.List;

/**
 * Created by master on 10/3/18.
 */

public class FarmFragment extends Fragment implements FarmContract.FarmFragmentView {
    private static final String TAG = FarmFragment.class.getName();
    private Activity mContext;
    private FarmAdapter farmAdapter;
    private View mProgressBar;
    private View view;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ((FarmActivity) mContext).setToolbarTitle(mContext.getResources().getString(R.string.welcome));

        if (view != null) {
            if (view.getParent() != null)
                ((ViewGroup) view.getParent()).removeView(view);
            return view;
        }

        view = inflater.inflate(R.layout.fragment_farm, container, false);

        view.findViewById(R.id.text_profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((FarmActivity) mContext).showProfileFragment();
            }
        });
        mProgressBar = view.findViewById(R.id.progressbar);
        view.findViewById(R.id.bottom_bar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((FarmActivity) mContext).showAddFarmFragment();
            }
        });

        setUpRecyclerView();
        if (((ApplicationClass) mContext.getApplication()).getmAppDataManager().isProfileComplete() == 1) {
            view.findViewById(R.id.text_profile).setVisibility(View.GONE);
        }
        FarmFragmentPresentor farmFragmentPresentor = new FarmFragmentPresentor(((ApplicationClass) mContext.getApplication()).getmAppDataManager());
        farmFragmentPresentor.setView(this);

        farmFragmentPresentor.makeFetchFarmReq();

        return view;
    }


    private void setUpRecyclerView() {
        RecyclerView recyclerView = view.findViewById(R.id.farmrecycelrview);
        //GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 2);
        farmAdapter = new FarmAdapter(getContext());
       /* ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(mContext, R.dimen.item_offset);
        recyclerView.addItemDecoration(itemDecoration);*/
        recyclerView.setLayoutManager(new GridLayoutManager(mContext, 2, GridLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(farmAdapter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG, "onAttach: ");
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
    public void showProgressBar() {
        if (isAdded()) mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {

        if (isAdded()) mProgressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onFetchFarmReqFail() {
        Toast.makeText(mContext.getApplicationContext(), "couldn't fetch data!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFarmFetchReqSuccess(final List<FarmModel> farmModelList) {

        farmAdapter.add(farmModelList);

    }


}
