package com.farm.farm2fork.ui.scheme;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.farm.farm2fork.ApplicationClass;
import com.farm.farm2fork.CustomViews.ItemOffsetDecoration;
import com.farm.farm2fork.Models.SchemeModel;
import com.farm.farm2fork.R;
import com.farm.farm2fork.Utils.ActivityUtils;
import com.farm.farm2fork.ui.farmscreen.FarmActivity;

import java.util.List;

/**
 * Created by master on 10/3/18.
 */

public class SchemeFragment extends Fragment implements SchemeContract.View {
    private Activity mContext;
    private View view;
    private RecyclerView recyclerView;
    private SchemeAdapter schemeAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((FarmActivity) mContext).setToolbarTitle(mContext.getResources().getString(R.string.nav_govt_schemes));
        if (view != null) {
            if (view.getParent() != null)
                ((ViewGroup) view.getParent()).removeView(view);
            return view;
        }
        view = inflater.inflate(R.layout.fragment_scheme, container, false);

        SchemeFragmentPresentor schemeFragmentPresentor = new SchemeFragmentPresentor(((ApplicationClass) getActivity().getApplication()).getmAppDataManager());
        schemeFragmentPresentor.setView(this);

        setUpRecyclerView();

        schemeFragmentPresentor.sendSchemeDataReq();
        return view;
    }

    private void setUpRecyclerView() {
        recyclerView = view.findViewById(R.id.recycelrview);
        schemeAdapter = new SchemeAdapter(mContext);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(mContext, R.dimen.item_offset);
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(schemeAdapter);
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

    @Override
    public void onSchemeListFetchSuccess(final List<SchemeModel> list) {
        if (isAdded()) {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    ActivityUtils.checkNotNull(schemeAdapter, "Farm Adapter is null").add(list);
                }
            });

        }
    }

    @Override
    public void onSchemeListFetchFail() {

    }
}
