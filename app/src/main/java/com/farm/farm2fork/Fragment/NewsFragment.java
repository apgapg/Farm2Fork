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
import com.farm.farm2fork.FarmAdapter.NewsAdapter;
import com.farm.farm2fork.Models.NewsModel;
import com.farm.farm2fork.R;
import com.farm.farm2fork.Utils.UserSessionManager;

import java.util.List;

import static com.farm.farm2fork.Fragment.AddFarmFragment.BASE_URL;

/**
 * Created by master on 10/3/18.
 */

public class NewsFragment extends Fragment {
    private static final String TAG = NewsFragment.class.getName();
    private RecyclerView recyclerView;
    private Activity mContext;
    private View progressbar;
    private UserSessionManager userSessionManager;
    private NewsAdapter newsAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_news, container, false);
        progressbar = view.findViewById(R.id.progressbar);

        userSessionManager = new UserSessionManager(mContext);

        recyclerView = view.findViewById(R.id.farmrecycelrview);

        newsAdapter = new NewsAdapter(mContext);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(mContext, R.dimen.item_offset);
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(newsAdapter);

        makefetchnewsReqtoserver();


        return view;
    }

    private void makefetchnewsReqtoserver() {
        progressbar.setVisibility(View.VISIBLE);
        AndroidNetworking.post(BASE_URL + "fetchnews.php")
                .addBodyParameter("authtoken", userSessionManager.getAuthToken())
                .addBodyParameter("uid", userSessionManager.getUID())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsObjectList(NewsModel.class, new ParsedRequestListener<List<NewsModel>>() {
                    @Override
                    public void onResponse(final List<NewsModel> response) {

                        progressbar.setVisibility(View.INVISIBLE);

                        new Handler().post(new Runnable() {
                            @Override
                            public void run() {
                                newsAdapter.add(response);


                            }
                        });

                    }

                    @Override
                    public void onError(ANError anError) {
                        progressbar.setVisibility(View.INVISIBLE);

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
