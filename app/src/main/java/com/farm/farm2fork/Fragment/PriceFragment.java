package com.farm.farm2fork.Fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.farm.farm2fork.FarmAdapter.PriceAdapter;
import com.farm.farm2fork.Models.PriceModel;
import com.farm.farm2fork.R;
import com.farm.farm2fork.Utils.UserSessionManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.farm.farm2fork.Fragment.AddFarmFragment.BASE_URL;

/**
 * Created by master on 10/3/18.
 */

public class PriceFragment extends Fragment {
    private static final String TAG = PriceFragment.class.getName();
    private Activity mContext;
    private UserSessionManager userSessionManager;
    private String crop;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/YYYY");
    private RecyclerView recyclerView;
    private PriceAdapter priceAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        crop = getArguments().getString("crop");

        View view = inflater.inflate(R.layout.fragment_prices_tab, container, false);
        ((TextView) view.findViewById(R.id.crop)).setText("Crop: " + crop);
        userSessionManager = new UserSessionManager(mContext);

        recyclerView = view.findViewById(R.id.farmrecycelrview);
        userSessionManager = new UserSessionManager(mContext);
        priceAdapter = new PriceAdapter(mContext);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(priceAdapter);

        makereqtoserver();

        return view;
    }

    private void makereqtoserver() {
        AndroidNetworking.post(BASE_URL + "htmlparser.php")
                .addBodyParameter("crop", crop)
                .addBodyParameter("date", sdf.format(new Date()))

                .addBodyParameter("uid", userSessionManager.getUID())
                .addBodyParameter("authtoken", userSessionManager.getAuthToken())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsObjectList(PriceModel.class, new ParsedRequestListener<List<PriceModel>>() {

                    @Override
                    public void onResponse(List<PriceModel> list) {
                        List<Object> mainlist = new ArrayList<>();
                        mainlist.add(list.get(0).getState());
                        String currentState = list.get(0).getState();
                        for (int i = 0; i < list.size(); i++) {
                            if (list.get(i).getState().equals(currentState)) {
                                mainlist.add(list.get(i));
                            } else {
                                mainlist.add(list.get(i).getState());
                                currentState = list.get(i).getState();
                                mainlist.add(list.get(i));

                            }
                        }
                        priceAdapter.add(mainlist);
                    }


                    @Override
                    public void onError(ANError anError) {
                        Log.d(TAG, "onError: " + anError.getResponse());

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
