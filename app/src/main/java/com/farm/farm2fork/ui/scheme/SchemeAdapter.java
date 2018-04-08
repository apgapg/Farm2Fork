package com.farm.farm2fork.ui.scheme;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.farm.farm2fork.Models.SchemeModel;
import com.farm.farm2fork.R;
import com.farm.farm2fork.ui.farmscreen.FarmActivity;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by master on 1/5/17.
 */

public class SchemeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = SchemeAdapter.class.getName();
    private final Context context;

    List<SchemeModel> messagelist = new ArrayList<>();

    public SchemeAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SchemeViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_scheme, parent, false));

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((SchemeViewHolder) holder).name.setText(messagelist.get(position).getName().trim());
    }

    @Override
    public int getItemCount() {
        return messagelist.size();
    }

    public void add(List<SchemeModel> list) {
        messagelist.clear();
        messagelist.addAll(list);
        Log.d(TAG, "add: list size: " + list.size());
        notifyDataSetChanged();
    }


    private class SchemeViewHolder extends RecyclerView.ViewHolder {


        private View rootview;
        private TextView name;

        public SchemeViewHolder(View inflate) {
            super(inflate);

            rootview = inflate.findViewById(R.id.rootview);
            name = inflate.findViewById(R.id.name);
            rootview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // ((FarmActivity) context).onAddFarmButtonClick();
                    ((FarmActivity) context).showDetailSchemeFragment(messagelist.get(getAdapterPosition()));

                }
            });
        }
    }
}
