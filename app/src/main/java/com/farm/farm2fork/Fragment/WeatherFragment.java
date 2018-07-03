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

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.farm.farm2fork.ApplicationClass;
import com.farm.farm2fork.CustomViews.ItemOffsetDecoration;
import com.farm.farm2fork.FarmAdapter.WeatherAdapter;
import com.farm.farm2fork.R;
import com.farm.farm2fork.models.WeatherModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Created by master on 10/3/18.
 */

public class WeatherFragment extends Fragment {
    private static final String TAG = WeatherFragment.class.getName();
    SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM (EEEE)");
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

        getWeatherData(getArguments().getString("loc_key"));

        return view;
    }

    private void getWeatherData(String loc_key) {
        AndroidNetworking.get("http://dataservice.accuweather.com/forecasts/v1/daily/5day/" + loc_key + "?apikey=wPPGSAmAyTuYLJV3MJ8ZVnAxGQOFAwdE&details=true&metric=true&language=" + ApplicationClass.localeCode)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "onResponse: " + response);
                        if (isAdded())

                            try {
                                JSONArray jsonArray = response.getJSONArray("DailyForecasts");
                                List<WeatherModel> list = new ArrayList<>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    WeatherModel weatherModel = new WeatherModel();
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);


                                    weatherModel.setDate(sdf.format(new Date(jsonObject.getLong("EpochDate") * 1000)));

                                    JSONObject jsonObjecttemp = jsonObject.getJSONObject("Temperature");
                                    JSONObject jsonObjectmax = jsonObjecttemp.getJSONObject("Maximum");
                                    weatherModel.setTemp_high(String.valueOf(jsonObjectmax.get("Value")));
                                    JSONObject jsonObjectmin = jsonObjecttemp.getJSONObject("Minimum");
                                    weatherModel.setTemp_low(String.valueOf(jsonObjectmin.get("Value")));

                                    JSONObject jsonObjectday = jsonObject.getJSONObject("Day");
                                    weatherModel.setDay_icon(jsonObjectday.getString("Icon"));
                                    weatherModel.setDay_text(jsonObjectday.getString("IconPhrase"));
                                    weatherModel.setDay_rain_probability(String.valueOf(jsonObjectday.get("RainProbability")));

                                    JSONObject jsonObjectnight = jsonObject.getJSONObject("Night");
                                    weatherModel.setNight_icon(jsonObjectnight.getString("Icon"));
                                    weatherModel.setNight_text(jsonObjectnight.getString("IconPhrase"));
                                    weatherModel.setNight_rain_probability(String.valueOf(jsonObjectnight.get("RainProbability")));

                                    list.add(weatherModel);
                                }
                                weatherAdapter.add(list);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
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
