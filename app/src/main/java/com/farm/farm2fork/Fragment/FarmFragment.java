package com.farm.farm2fork.Fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.farm.data.UserDataManager;
import com.farm.farm2fork.CustomViews.ItemOffsetDecoration;
import com.farm.farm2fork.FarmAdapter.FarmAdapter;
import com.farm.farm2fork.Interface.NetRetryListener;
import com.farm.farm2fork.Models.FarmModel;
import com.farm.farm2fork.R;
import com.farm.farm2fork.activity.MainNavScreen;

import java.util.List;

import static com.farm.farm2fork.Fragment.AddFarmFragment.BASE_URL;

/**
 * Created by master on 10/3/18.
 */

public class FarmFragment extends Fragment {
    private static final String TAG = FarmFragment.class.getName();
    private RecyclerView recyclerView;
    private Activity mContext;
    private FarmAdapter farmAdapter;
    private View progressBar;
    private UserDataManager userDataManager;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ((MainNavScreen) mContext).setToolbarTitle("Welcome");

        if (view != null) {
            if (view.getParent() != null)
                ((ViewGroup) view.getParent()).removeView(view);
            return view;
        }

        view = inflater.inflate(R.layout.fragment_farm, container, false);

        userDataManager = new UserDataManager(mContext);

        progressBar = view.findViewById(R.id.progressbar);
        view.findViewById(R.id.community).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  ((MainNavScreen) mContext).onCommunityButtonClick();
            }
        });
        recyclerView = view.findViewById(R.id.farmrecycelrview);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 2);
        farmAdapter = new FarmAdapter(getContext());
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(mContext, R.dimen.item_offset);
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(farmAdapter);

        makefetchfarmReqtoserver();

        ((MainNavScreen) mContext).setnetworkReqRetryListner(new NetRetryListener() {
            @Override
            public void onNetReqRetry(String name) {
                if (name.equals(FarmFragment.class.getName()))
                    makefetchfarmReqtoserver();
            }
        });

        return view;
    }

    private void makefetchfarmReqtoserver() {
        progressBar.setVisibility(View.VISIBLE);
        AndroidNetworking.post(BASE_URL + "fetchfarms.php")
                .addBodyParameter("authtoken", userDataManager.getAuthToken())
                .addBodyParameter("uid", userDataManager.getUid())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsObjectList(FarmModel.class, new ParsedRequestListener<List<FarmModel>>() {
                    @Override
                    public void onResponse(final List<FarmModel> response) {
                        progressBar.setVisibility(View.INVISIBLE);

                        new Handler().post(new Runnable() {
                            @Override
                            public void run() {
                                farmAdapter.add(response);


                            }
                        });
                        if (response.size() == 0)
                            ((MainNavScreen) mContext).showSnackBar();
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressBar.setVisibility(View.INVISIBLE);
                        ((MainNavScreen) mContext).showSnackBarNetError(FarmFragment.class.getName());

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
