package com.farm.farm2fork.FarmAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.farm.farm2fork.Models.WeatherModel;
import com.farm.farm2fork.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by master on 1/5/17.
 */

public class WeatherAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = WeatherAdapter.class.getName();
    private final Context context;

    List<WeatherModel> messagelist = new ArrayList<>();

    public WeatherAdapter(Context context) {
        this.context = context;


    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new WeatherViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_weather1, parent, false));

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((WeatherViewHolder) holder).txtcurrentdate.setText(messagelist.get(position).getDate());
        ((WeatherViewHolder) holder).txtmax.setText(messagelist.get(position).getTemp_high() + "°c");
        ((WeatherViewHolder) holder).txtmin.setText(messagelist.get(position).getTemp_low() + "°c");
        ((WeatherViewHolder) holder).txtrainforecastday.setText(messagelist.get(position).getDay_rain_probability() + "%");
        ((WeatherViewHolder) holder).txtrainforecastnight.setText(messagelist.get(position).getNight_rain_probability() + "%");
        ((WeatherViewHolder) holder).txtforecastdaytext.setText(messagelist.get(position).getDay_text());
        ((WeatherViewHolder) holder).txtforecastnighttext.setText(messagelist.get(position).getNight_text());
        Glide.with(context).load("https://developer.accuweather.com/sites/default/files/" + String.format("%02d", Integer.valueOf(messagelist.get(position).getDay_icon())) + "-s.png").into(((WeatherViewHolder) holder).dayicon);
        Glide.with(context).load("https://developer.accuweather.com/sites/default/files/" + String.format("%02d", Integer.valueOf(messagelist.get(position).getNight_icon())) + "-s.png").into(((WeatherViewHolder) holder).nighticon);

    }

    @Override
    public int getItemCount() {
        return messagelist.size();
    }

    public void add(List<WeatherModel> list) {
        messagelist.clear();
        messagelist.addAll(list);
        Log.d(TAG, "add: list size: " + list.size());
        notifyDataSetChanged();
    }


    private class WeatherViewHolder extends RecyclerView.ViewHolder {


        private TextView txtcurrentdate, txtmax, txtmin, txtrainforecastday, txtrainforecastnight, txtforecastdaytext, txtforecastnighttext;
        private ImageView dayicon, nighticon;

        public WeatherViewHolder(View inflate) {
            super(inflate);

            txtcurrentdate = inflate.findViewById(R.id.currentdate);
            txtmax = inflate.findViewById(R.id.txtmax);
            txtmin = inflate.findViewById(R.id.txtmin);
            txtrainforecastday = inflate.findViewById(R.id.dayrain);
            txtrainforecastnight = inflate.findViewById(R.id.nightrain);
            txtforecastdaytext = inflate.findViewById(R.id.daytext);
            txtforecastnighttext = inflate.findViewById(R.id.nighttext);
            dayicon = inflate.findViewById(R.id.dayicon);
            nighticon = inflate.findViewById(R.id.nighticon);
        }
    }
}
