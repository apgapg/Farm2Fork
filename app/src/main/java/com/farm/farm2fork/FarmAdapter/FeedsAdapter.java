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
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.farm.farm2fork.Models.FeedsModel;
import com.farm.farm2fork.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by master on 1/5/17.
 */

public class FeedsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = FeedsAdapter.class.getName();
    private final Context context;

    List<FeedsModel> messagelist = new ArrayList<>();

    public FeedsAdapter(Context context) {
        this.context = context;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 2)
            return new FeedsImageViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_feeds_image, parent, false));
        else
            return new FeedsTextViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_feeds_text, parent, false));

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ((FeedsTextViewHolder) holder).name.setText(messagelist.get(position).getName());
        ((FeedsTextViewHolder) holder).city.setText(messagelist.get(position).getCity());
        ((FeedsTextViewHolder) holder).date.setText(messagelist.get(position).getDate());
        ((FeedsTextViewHolder) holder).description.setText(messagelist.get(position).getDescription());
        ((FeedsTextViewHolder) holder).like.setText("LIKE (" + messagelist.get(position).getLike() + ")");
        ((FeedsTextViewHolder) holder).comment.setText("COMMENT (" + messagelist.get(position).getComment() + ")");
        if (holder instanceof FeedsImageViewHolder)
            Glide.with(context).load(messagelist.get(position).getImage()).diskCacheStrategy(DiskCacheStrategy.RESULT).into(((FeedsImageViewHolder) holder).image);
    }

    @Override
    public int getItemCount() {
        return messagelist.size();
    }

    public void add(List<FeedsModel> list) {
        messagelist.clear();
        messagelist.addAll(list);
        Log.d(TAG, "add: list size: " + list.size());
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (!messagelist.get(position).getImage().isEmpty()) {
            return 2;
        } else
            return 1;
    }

    private class FeedsImageViewHolder extends FeedsTextViewHolder {


        private ImageView image;

        public FeedsImageViewHolder(View inflate) {
            super(inflate);


            image = inflate.findViewById(R.id.image);

        }
    }

    private class FeedsTextViewHolder extends RecyclerView.ViewHolder {


        private View rootview;
        private TextView name, city, date, description, like, comment;

        public FeedsTextViewHolder(View inflate) {
            super(inflate);

            rootview = inflate.findViewById(R.id.rootview);
            name = inflate.findViewById(R.id.name);
            city = inflate.findViewById(R.id.city);
            date = inflate.findViewById(R.id.date);
            description = inflate.findViewById(R.id.description);
            like = inflate.findViewById(R.id.like);
            comment = inflate.findViewById(R.id.comment);

        }
    }
}
