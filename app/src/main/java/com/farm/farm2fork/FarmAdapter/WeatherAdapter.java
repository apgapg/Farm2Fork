package com.farm.farm2fork.FarmAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.farm.farm2fork.Models.FarmModel;
import com.farm.farm2fork.R;

import java.util.ArrayList;
import java.util.List;

import zh.wang.android.yweathergetter4a.WeatherInfo;


/**
 * Created by master on 1/5/17.
 */

public class WeatherAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = WeatherAdapter.class.getName();
    private final Context context;

    List<WeatherInfo.ForecastInfo> messagelist = new ArrayList<>();

    public WeatherAdapter(Context context) {
        this.context = context;


    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new WeatherViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_weather, parent, false));

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((WeatherViewHolder) holder).txtcurrentdate.setText(messagelist.get(position).getForecastDate().replace(" 2018",""));
        ((WeatherViewHolder) holder).txtmax.setText("Max: "+messagelist.get(position).getForecastTempHigh()+"°c");
        ((WeatherViewHolder) holder).txtmin.setText("Min: "+messagelist.get(position).getForecastTempLow()+"°c");
        ((WeatherViewHolder) holder).txtforecast.setText(messagelist.get(position).getForecastText());
    }

    @Override
    public int getItemCount() {
        return messagelist.size();
    }

    public void add(List<WeatherInfo.ForecastInfo> list) {
        messagelist.clear();
        messagelist.addAll(list);
        Log.d(TAG, "add: list size: " + list.size());
        notifyDataSetChanged();
    }


    private class WeatherViewHolder extends RecyclerView.ViewHolder {


        private TextView txtcurrentdate,txtmax,txtmin,txtforecast;

        public WeatherViewHolder(View inflate) {
            super(inflate);

            txtcurrentdate = inflate.findViewById(R.id.currentdate);
            txtmax = inflate.findViewById(R.id.maxtemp);
            txtmin = inflate.findViewById(R.id.mintemp);
            txtforecast = inflate.findViewById(R.id.forecast);

        }
    }
}
