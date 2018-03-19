package com.farm.farm2fork.FarmAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.farm.farm2fork.MainNavScreen;
import com.farm.farm2fork.Models.FarmModel;
import com.farm.farm2fork.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by master on 1/5/17.
 */

public class FeedsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = FeedsAdapter.class.getName();
    private final Context context;

    List<Object> messagelist = new ArrayList<>();

    public FeedsAdapter(Context context) {
        this.context = context;


        //////////For trial
        messagelist.add(new FarmModel());
        messagelist.add("Add Farm");


    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            return new FeedsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_feeds, parent, false));

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

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



    private class FeedsViewHolder extends RecyclerView.ViewHolder {


        private View rootview;

        public FeedsViewHolder(View inflate) {
            super(inflate);

            rootview = inflate.findViewById(R.id.rootview);
            rootview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((MainNavScreen)context).onAddFarmButtonClick();
                }
            });
        }
    }
}
