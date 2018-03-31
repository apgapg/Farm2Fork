package com.farm.farm2fork.FarmAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.farm.farm2fork.Models.PriceModel;
import com.farm.farm2fork.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by master on 1/5/17.
 */

public class PriceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = PriceAdapter.class.getName();
    private final Context context;

    List<Object> messagelist = new ArrayList<>();

    public PriceAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 1)
            return new PriceStateViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_price_state_header, parent, false));
        else
            return new PriceCommodityViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_price_commodity, parent, false));

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == 1) {
            ((PriceStateViewHolder) holder).txtstate.setText((String) messagelist.get(position));

        } else {
            ((PriceCommodityViewHolder) holder).txtmandi.setText(((PriceModel) messagelist.get(position)).getMarket());
            ((PriceCommodityViewHolder) holder).txtmax.setText(((PriceModel) messagelist.get(position)).getMaxprice());
            ((PriceCommodityViewHolder) holder).txtmin.setText(((PriceModel) messagelist.get(position)).getMinprice());
            ((PriceCommodityViewHolder) holder).txtmodal.setText(((PriceModel) messagelist.get(position)).getModalprice());
        }
    }

    @Override
    public int getItemCount() {
        return messagelist.size();
    }

    public void add(List<Object> list) {
        messagelist.clear();
        messagelist.addAll(list);
        Log.d(TAG, "add: list size: " + list.size());
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (messagelist.get(position) instanceof String) {
            return 1;
        } else
            return 2;
    }

    private class PriceCommodityViewHolder extends RecyclerView.ViewHolder {
        private TextView txtmandi, txtmax, txtmin, txtmodal;


        public PriceCommodityViewHolder(View inflate) {
            super(inflate);
            txtmandi = inflate.findViewById(R.id.mandi);
            txtmax = inflate.findViewById(R.id.max);
            txtmin = inflate.findViewById(R.id.min);
            txtmodal = inflate.findViewById(R.id.modal);

        }
    }

    private class PriceStateViewHolder extends RecyclerView.ViewHolder {
        private TextView txtstate;

        public PriceStateViewHolder(View inflate) {
            super(inflate);
            txtstate = inflate.findViewById(R.id.state);


        }
    }
}
