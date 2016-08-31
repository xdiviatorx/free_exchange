package com.technologies.mobile.free_exchange.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.technologies.mobile.free_exchange.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by diviator on 30.08.2016.
 */
public class RecyclerAddedImagesAdapter extends RecyclerView.Adapter<RecyclerAddedImagesAdapter.MyViewHolder> {

    public static final String URI = "URI";
    public static final String BUTTON_VISIBILITY = "BUTTON_VISIBILITY";

    List<Map<String, Object>> mData;

    Context mContext;


    public RecyclerAddedImagesAdapter(Context context) {
        mData = new ArrayList<>();
        mContext = context;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView ivPhoto;
        Button bDelete;

        public MyViewHolder(View itemView) {
            super(itemView);
            ivPhoto = (ImageView) itemView.findViewById(R.id.ivPhoto);
            bDelete = (Button) itemView.findViewById(R.id.bDelete);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.child_exchange_photo, parent, false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Glide.with(mContext)
                .load((Uri) mData.get(position).get(URI))
                .into(holder.ivPhoto);
        holder.ivPhoto.setOnClickListener(new OnImageClickListener(position));
        holder.bDelete.setOnClickListener(new OnDeleteButtonClickListener(position));

        int visibility = (int) mData.get(position).get(BUTTON_VISIBILITY);
        if (visibility == View.GONE) {
            holder.bDelete.setVisibility(View.GONE);
        } else {
            holder.bDelete.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void addPhoto(Uri uri) {
        Map<String, Object> item = new HashMap<>();
        item.put(URI, uri);
        item.put(BUTTON_VISIBILITY, View.GONE);
        mData.add(item);
        notifyDataSetChanged();
    }

    public void deletePhoto(int position) {
        mData.remove(position);
        notifyItemChanged(position);
    }

    public void showDeleteButton(int position) {
        hideAllDeleteButtons();
        Map<String,Object> item = mData.get(position);
        item.put(BUTTON_VISIBILITY,View.VISIBLE);
        notifyItemChanged(position);
    }

    public void hideAllDeleteButtons() {
        for (Map<String,Object> item : mData) {
            item.put(BUTTON_VISIBILITY,View.GONE);
        }
        notifyDataSetChanged();
    }

    private class OnImageClickListener implements View.OnClickListener {

        int mPosition;

        public OnImageClickListener(int position) {
            mPosition = position;
        }

        @Override
        public void onClick(View view) {
            showDeleteButton(mPosition);
        }
    }

    private class OnDeleteButtonClickListener implements View.OnClickListener {

        int mPosition;

        public OnDeleteButtonClickListener(int position) {
            mPosition = position;
        }

        @Override
        public void onClick(View view) {
            deletePhoto(mPosition);
        }
    }


}
