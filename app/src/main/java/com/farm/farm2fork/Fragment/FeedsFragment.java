package com.farm.farm2fork.Fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.farm.farm2fork.CustomViews.ItemOffsetDecoration;
import com.farm.farm2fork.FarmAdapter.FeedsAdapter;
import com.farm.farm2fork.Interface.NetRetryListener;
import com.farm.farm2fork.Models.FeedsModel;
import com.farm.farm2fork.R;
import com.farm.farm2fork.data.UserDataManager;
import com.farm.farm2fork.ui.mainfarmscreen.FarmScreen;

import java.util.List;

import static com.farm.farm2fork.Fragment.AddFarmFragment.BASE_URL;

/**
 * Created by master on 10/3/18.
 */

public class FeedsFragment extends Fragment {
    private static final String TAG = FeedsFragment.class.getName();
    private RecyclerView recyclerView;
    private Activity mContext;
    private UserDataManager userDataManager;
    private View progressbar;
    private FeedsAdapter feedsAdapter;
    private String crop, city;
    private View nopost;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_feeds, container, false);
        progressbar = view.findViewById(R.id.progressbar);
        crop = getArguments().getString("crop");
        city = getArguments().getString("city");

        nopost = view.findViewById(R.id.nopost);
        nopost.setVisibility(View.INVISIBLE);
        Log.d(TAG, "onCreateView: " + crop);
       /* view.findViewById(R.id.addpost).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((FarmScreen) mContext).showAddFeedFragment(crop,city);
            }
        });*/

        recyclerView = view.findViewById(R.id.farmrecycelrview);
        userDataManager = new UserDataManager(mContext);
        feedsAdapter = new FeedsAdapter(mContext);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(mContext, R.dimen.item_offset);
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(feedsAdapter);

        makefetchfarmReqtoserver();
        ((FarmScreen) mContext).setnetworkReqRetryListner(new NetRetryListener() {
            @Override
            public void onNetReqRetry(String name) {
                if (name.equals(FeedsFragment.class.getName())) {
                    makefetchfarmReqtoserver();
                }
            }
        });

        return view;
    }

    private void makefetchfarmReqtoserver() {
        nopost.setVisibility(View.INVISIBLE);
        progressbar.setVisibility(View.VISIBLE);
        AndroidNetworking.post(BASE_URL + "fetchfeeds.php")
                .addBodyParameter("crop", crop)
                .addBodyParameter("authtoken", userDataManager.getAuthToken())
                .addBodyParameter("uid", userDataManager.getUid())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsObjectList(FeedsModel.class, new ParsedRequestListener<List<FeedsModel>>() {
                    @Override
                    public void onResponse(final List<FeedsModel> response) {

                        progressbar.setVisibility(View.INVISIBLE);

                        new Handler().post(new Runnable() {
                            @Override
                            public void run() {
                                feedsAdapter.add(response);


                            }
                        });
                        if (response.size() == 0)
                            nopost.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressbar.setVisibility(View.INVISIBLE);
                        ((FarmScreen) mContext).showSnackBarNetError(FeedsFragment.class.getName());

                        Log.d(TAG, "onError: " + anError);
                    }
                });

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
