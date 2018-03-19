package com.farm.farm2fork.FarmAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.farm.farm2fork.MainNavScreen;
import com.farm.farm2fork.Models.FarmModel;
import com.farm.farm2fork.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by master on 1/5/17.
 */

public class FarmAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = FarmAdapter.class.getName();
    private final Context context;

    List<Object> messagelist = new ArrayList<>();

    public FarmAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 1)
            return new AddFarmViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_add_farm, parent, false));
        else if (viewType == 2)
            return new FarmViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_farm, parent, false));
        else
            return new FarmViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_farm, parent, false));

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == 2) {
            ((FarmViewHolder) holder).txtcity.setText(((FarmModel) messagelist.get(position)).getLoc_city());
            ((FarmViewHolder) holder).txtcrop.setText(((FarmModel) messagelist.get(position)).getCrop());
            ((FarmViewHolder) holder).txtfarmsize.setText(((FarmModel) messagelist.get(position)).getFarmsize()+" acre");

        }
    }

    @Override
    public int getItemCount() {
        return messagelist.size();
    }

    public void add(List<FarmModel> list) {
        messagelist.clear();
        messagelist.addAll(list);
        messagelist.add("Add Farm");
        Log.d(TAG, "add: list size: " + list.size());
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (messagelist.get(position) instanceof FarmModel)
            return 2;
        else if (messagelist.get(position) instanceof String)
            return 1;
        else return 0;
    }

    private class FarmViewHolder extends RecyclerView.ViewHolder {


        private  TextView txtcrop, txtcity,txtfarmsize;

        public FarmViewHolder(View inflate) {
            super(inflate);
            txtcrop=inflate.findViewById(R.id.crop);
            txtcity =inflate.findViewById(R.id.address);
            txtfarmsize =inflate.findViewById(R.id.size);

        }
    }

    private class AddFarmViewHolder extends RecyclerView.ViewHolder {


        private View rootview;

        public AddFarmViewHolder(View inflate) {
            super(inflate);

            rootview = inflate.findViewById(R.id.rootview);
            rootview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((MainNavScreen) context).onAddFarmButtonClick();
                }
            });
        }
    }
}
