package com.farm.farm2fork.ui.feeds;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.farm.farm2fork.ApplicationClass;
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
    private final FeedsAdapterPresentor mPresentor;

    List<FeedsModel> messagelist = new ArrayList<>();

    public FeedsAdapter(Context context) {
        this.context = context;
        mPresentor = new FeedsAdapterPresentor(((ApplicationClass) ((Activity) context).getApplication()).getmAppDataManager());

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
        ((FeedsTextViewHolder) holder).like_text.setText("(" + messagelist.get(position).getLike() + ")");

        if (messagelist.get(position).getLike_status().equals("true")) {
            ((FeedsTextViewHolder) holder).like_text.setTypeface(Typeface.DEFAULT_BOLD);
            ((FeedsTextViewHolder) holder).like_text.setTextColor(Color.parseColor("#f44336"));
            ((FeedsTextViewHolder) holder).like_image.setImageResource(R.drawable.ic_thumbs_up_hand_symbol);

        } else {
            ((FeedsTextViewHolder) holder).like_text.setTypeface(Typeface.DEFAULT);
            ((FeedsTextViewHolder) holder).like_text.setTextColor(Color.parseColor("#bdbdbd"));
            ((FeedsTextViewHolder) holder).like_image.setImageResource(R.drawable.ic_thumbs_up);

        }

        ((FeedsTextViewHolder) holder).comment.setText("COMMENT (" + messagelist.get(position).getComment() + ")");
        if (holder instanceof FeedsImageViewHolder)
            Glide.with(context).load(messagelist.get(position).getImage()).diskCacheStrategy(DiskCacheStrategy.RESULT).into(((FeedsImageViewHolder) holder).image);
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, @NonNull List<Object> payloads) {

        if (payloads.contains("like")) {
            messagelist.get(position).setLike_status("true");
            ((FeedsTextViewHolder) holder).like_text.setTypeface(Typeface.DEFAULT_BOLD);
            ((FeedsTextViewHolder) holder).like_text.setTextColor(Color.parseColor("#f44336"));
            ((FeedsTextViewHolder) holder).like_image.setImageResource(R.drawable.ic_thumbs_up_hand_symbol);
            ((FeedsTextViewHolder) holder).like_text.setText("(" + String.valueOf(Integer.parseInt(messagelist.get(position).getLike()) + 1) + ")");
            messagelist.get(position).setLike(String.valueOf(Integer.parseInt(messagelist.get(position).getLike()) + 1));


        } else if (payloads.contains("unlike")) {
            messagelist.get(position).setLike_status("false");
            ((FeedsTextViewHolder) holder).like_text.setTypeface(Typeface.DEFAULT);
            ((FeedsTextViewHolder) holder).like_text.setTextColor(Color.parseColor("#bdbdbd"));
            ((FeedsTextViewHolder) holder).like_image.setImageResource(R.drawable.ic_thumbs_up);
            if (Integer.parseInt(messagelist.get(position).getLike()) != 0) {
                ((FeedsTextViewHolder) holder).like_text.setText("(" + String.valueOf(Integer.parseInt(messagelist.get(position).getLike()) - 1) + ")");
                messagelist.get(position).setLike(String.valueOf(Integer.parseInt(messagelist.get(position).getLike()) - 1));

            }
        } else
            super.onBindViewHolder(holder, position, payloads);
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


        private View rootview, like_cont;
        private TextView name, city, date, description, like_text, comment;
        private ImageView like_image;

        public FeedsTextViewHolder(View inflate) {
            super(inflate);

            rootview = inflate.findViewById(R.id.rootview);
            like_cont = inflate.findViewById(R.id.like_cont);
            name = inflate.findViewById(R.id.name);
            city = inflate.findViewById(R.id.city);
            date = inflate.findViewById(R.id.date);
            description = inflate.findViewById(R.id.description);
            like_text = inflate.findViewById(R.id.like_text);
            like_image = inflate.findViewById(R.id.like_image);
            comment = inflate.findViewById(R.id.comment);

            like_cont.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (messagelist.get(getAdapterPosition()).getLike_status().equals("false"))
                        notifyItemChanged(getAdapterPosition(), "like");
                    else notifyItemChanged(getAdapterPosition(), "unlike");

                    final int position = getAdapterPosition();
                    mPresentor.likeReq(messagelist.get(position).getPostid(), new FeedsAdapterContract.LikeStatusListener() {
                        @Override
                        public void onSuccess(String response) {
                            //messagelist.get(position).setLike_status(response);


                        }

                        @Override
                        public void onFail() {
                            if (messagelist.get(getAdapterPosition()).getLike_status().equals("false"))
                                notifyItemChanged(getAdapterPosition(), "like");
                            else notifyItemChanged(getAdapterPosition(), "unlike");
                        }
                    });
                }
            });

        }
    }
}
