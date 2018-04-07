package com.farm.farm2fork.ui.farmscreen;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
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
import com.farm.farm2fork.CustomViews.ItemOffsetDecoration;
import com.farm.farm2fork.FarmAdapter.FarmAdapter;
import com.farm.farm2fork.Models.FarmModel;
import com.farm.farm2fork.R;
import com.farm.farm2fork.Utils.ActivityUtils;

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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        if (view != null) {
            if (view.getParent() != null)
                ((ViewGroup) view.getParent()).removeView(view);
            return view;
        }

        view = inflater.inflate(R.layout.fragment_farm, container, false);

        mProgressBar = view.findViewById(R.id.progressbar);


        setUpRecyclerView();

        FarmFragmentPresentor farmFragmentPresentor = new FarmFragmentPresentor(((ApplicationClass) mContext.getApplication()).getmAppDataManager());
        farmFragmentPresentor.setView(this);

        farmFragmentPresentor.makeFetchFarmReq();

        return view;
    }


    private void setUpRecyclerView() {
        RecyclerView recyclerView = view.findViewById(R.id.farmrecycelrview);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 2);
        farmAdapter = new FarmAdapter(getContext());
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(mContext, R.dimen.item_offset);
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setLayoutManager(gridLayoutManager);
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
        if (isAdded()) {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    ActivityUtils.checkNotNull(farmAdapter, "Farm Adapter is null").add(farmModelList);
                }
            });
            if (farmModelList.size() == 0)
                ((FarmActivity) mContext).showSnackBar();
        }
    }


}
