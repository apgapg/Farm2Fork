package com.farm.farm2fork.FarmAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.farm.farm2fork.Models.NewsModel;
import com.farm.farm2fork.R;
import com.farm.farm2fork.ui.farmscreen.FarmScreen;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by master on 1/5/17.
 */

public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = NewsAdapter.class.getName();
    private final Context context;

    List<Object> messagelist = new ArrayList<>();

    public NewsAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            return new NewsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news, parent, false));

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return messagelist.size();
    }

    public void add(List<NewsModel> list) {
        messagelist.clear();
        messagelist.addAll(list);
        Log.d(TAG, "add: list size: " + list.size());
        notifyDataSetChanged();
    }



    private class NewsViewHolder extends RecyclerView.ViewHolder {


        private View rootview;

        public NewsViewHolder(View inflate) {
            super(inflate);

            rootview = inflate.findViewById(R.id.rootview);
            rootview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((FarmScreen) context).onAddFarmButtonClick();
                }
            });
        }
    }
}
