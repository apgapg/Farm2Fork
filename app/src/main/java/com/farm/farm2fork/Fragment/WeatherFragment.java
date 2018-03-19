package com.farm.farm2fork.Fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.farm.farm2fork.CustomViews.ItemOffsetDecoration;
import com.farm.farm2fork.FarmAdapter.WeatherAdapter;
import com.farm.farm2fork.R;

import java.util.List;

import zh.wang.android.yweathergetter4a.WeatherInfo;

/**
 * Created by master on 10/3/18.
 */

public class WeatherFragment extends Fragment {
    private RecyclerView recyclerView;
    private Activity mContext;
    private WeatherAdapter weatherAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_weather, container, false);

        recyclerView = view.findViewById(R.id.farmrecycelrview);

         weatherAdapter = new WeatherAdapter(mContext);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(mContext, R.dimen.item_offset);
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(weatherAdapter);

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

    public void onWeatherDataReceived(List<WeatherInfo.ForecastInfo> forecastInfoList) {

        weatherAdapter.add(forecastInfoList);

    }
}
