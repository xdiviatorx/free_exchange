package com.technologies.mobile.free_exchange.adapters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;
import com.technologies.mobile.free_exchange.Loader;
import com.technologies.mobile.free_exchange.R;
import com.technologies.mobile.free_exchange.rest.ExchangeClient;
import com.technologies.mobile.free_exchange.rest.RetrofitService;
import com.technologies.mobile.free_exchange.rest.model.Comment;
import com.technologies.mobile.free_exchange.rest.model.GetCommentsResponse;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by diviator on 19.10.2016.
 */

public class CommentAdapter extends BaseAdapter {

    public static String LOG_TAG = "commentAdapter";

    private static int DOWNLOAD_COUNT = 20;

    Context mContext;
    int mResource;

    ArrayList<Comment> mData;

    int offerId;

    boolean downloading = false;

    public CommentAdapter(Context context, int resource) {
        mContext = context;
        mResource = resource;
        mData = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Comment getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(mResource, viewGroup, false);
        }
        RoundedImageView rivAvatar = (RoundedImageView) view.findViewById(R.id.rivAvatar);
        TextView tvName = (TextView) view.findViewById(R.id.tvName);
        TextView tvComment = (TextView) view.findViewById(R.id.tvComment);

        if (getItem(position).getUserData().getPhoto() != null && !getItem(position).getUserData().getPhoto().isEmpty()) {
            Picasso.with(mContext).load(getItem(position).getUserData().getPhoto()).into(rivAvatar);
        } else {
            rivAvatar.setImageResource(R.drawable.no_photo);
        }

        tvName.setText(getItem(position).getUserData().getName());
        tvComment.setText(getItem(position).getText());

        return view;
    }

    public void setOfferId(int offerId) {
        this.offerId = offerId;
    }

    public void init() {
        Loader.showProgressBar(mContext);
        downloading = true;
        downloading(0);
    }

    public void additionalDownloading(int offset) {
        if (!downloading) {
            downloading = true;
            downloading(offset);
        }
    }

    private void downloading(int offset) {
        ExchangeClient client = RetrofitService.createService(ExchangeClient.class);
        client.getComments(offerId, offset, DOWNLOAD_COUNT, ExchangeClient.apiKey).enqueue(new Callback<GetCommentsResponse>() {
            @Override
            public void onResponse(Call<GetCommentsResponse> call, Response<GetCommentsResponse> response) {
                mData.addAll(Arrays.asList(response.body().getResponse().getComments()));
                notifyDataSetChanged();
                Loader.hideProgressBar(mContext);
            }

            @Override
            public void onFailure(Call<GetCommentsResponse> call, Throwable t) {
                Log.e(LOG_TAG, "COMMENT ADAPTER DOWNLOAD ERROR " + t.toString());
                Loader.hideProgressBar(mContext);
            }
        });
    }

    public void addComment(Comment comment){
        mData.add(0,comment);
        notifyDataSetChanged();
    }

}
