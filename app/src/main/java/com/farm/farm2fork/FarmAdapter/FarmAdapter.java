package com.farm.farm2fork.FarmAdapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.farm.farm2fork.R;
import com.farm.farm2fork.databinding.ItemFarmBinding;
import com.farm.farm2fork.models.FarmModel;
import com.farm.farm2fork.ui.farmscreen.FarmActivity;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by master on 1/5/17.
 */

public class FarmAdapter extends RecyclerView.Adapter<FarmAdapter.FarmViewHolder> {

    private static final String TAG = FarmAdapter.class.getName();
    private final Context mContext;

    List<FarmModel> messagelist = new ArrayList<>();

    public FarmAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public FarmViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemFarmBinding itemFarmBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_farm, parent, false);

        return new FarmViewHolder(itemFarmBinding);
    }

    @Override
    public void onBindViewHolder(FarmViewHolder holder, int position) {
        holder.binding.setFarmModel(messagelist.get(position));
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return messagelist.size();
    }

    public void add(List<FarmModel> list) {
        messagelist.clear();
        messagelist.addAll(list);
        Log.d(TAG, "add: list size: " + list.size());
        notifyDataSetChanged();
    }


    class FarmViewHolder extends RecyclerView.ViewHolder {


        private ItemFarmBinding binding;

        public FarmViewHolder(ItemFarmBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.rootview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((FarmActivity) mContext).showCommunityFragment(messagelist.get(getAdapterPosition()));
                }
            });


        }
    }
}