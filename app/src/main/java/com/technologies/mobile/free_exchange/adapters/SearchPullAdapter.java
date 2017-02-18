package com.technologies.mobile.free_exchange.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;
import com.technologies.mobile.free_exchange.CommentsCountManager;
import com.technologies.mobile.free_exchange.Loader;
import com.technologies.mobile.free_exchange.R;
import com.technologies.mobile.free_exchange.activities.ExchangeMoreActivity;
import com.technologies.mobile.free_exchange.activities.ImagePreviewActivity;
import com.technologies.mobile.free_exchange.activities.MainActivity;
import com.technologies.mobile.free_exchange.fragments.DialogFragment;
import com.technologies.mobile.free_exchange.listeners.OnIconClickListener;
import com.technologies.mobile.free_exchange.listeners.OnImageClickListener;
import com.technologies.mobile.free_exchange.listeners.OnSearchBeginListener;
import com.technologies.mobile.free_exchange.listeners.OnSearchPerformListener;
import com.technologies.mobile.free_exchange.logic.TextFormatter;
import com.technologies.mobile.free_exchange.rest.ExchangeClient;
import com.technologies.mobile.free_exchange.rest.RetrofitService;
import com.technologies.mobile.free_exchange.rest.model.Photo;
import com.technologies.mobile.free_exchange.rest.model.Search;
import com.technologies.mobile.free_exchange.rest.model.SearchExtraditionItem;
import com.technologies.mobile.free_exchange.rest.model.SearchResponse;
import com.technologies.mobile.free_exchange.views.AutomaticPhotoLayout;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by diviator on 18.08.2016.
 */
public class SearchPullAdapter extends SimpleAdapter {

    public static String LOG_TAG = "mySearchAdapter";

    public static String GIVE = "GIVE";
    public static String GET = "GET";
    public static String CATEGORY = "CATEGORY";
    public static String TEXT = "TEXT";
    public static String DATE = "DATE";
    public static String IMAGE = "IMAGE";
    public static String PLACE = "PLACE";
    public static String CONTACTS = "CONTACTS";
    public static String IMAGES = "IMAGES";
    public static String UID = "UID";
    public static String VK_ID = "VK_ID";
    public static String AUTHOR_NAME = "AUTHOR_NAME";
    public static String COMMENTS_COUNT = "COMMENTS_COUNT";

    protected int UPLOAD_LENGTH = 20;

    protected boolean uploading = false;

    protected OnSearchPerformListener onSearchPerformListener = null;

    protected OnIconClickListener onIconClickListener = null;

    protected OnSearchBeginListener onSearchBeginListener = null;

    Context context;

    ArrayList<HashMap<String, Object>> data;
    ArrayList<SearchExtraditionItem> metaData;

    int mResource;

    String itemsGive = "";
    String itemsGet = "";
    int category = 0;

    public SearchPullAdapter(Context context, ArrayList<HashMap<String, Object>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);

        this.context = context;
        this.data = data;
        mResource = resource;

        metaData = new ArrayList<>();
    }

    private static class ViewHolder {
        RoundedImageView roundedImageView;
        TextView tvGives;
        TextView tvGets;
        TextView tvContacts;
        TextView tvPlace;
        TextView tvDate;
        ImageView ivComments;
        TextView tvCommentsCount;
        TextView tvName;
        AutomaticPhotoLayout aplPhotos;
        ImageView ivMessage;
    }

    @Override
    public SearchExtraditionItem getItem(int position) {
        return metaData.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(mResource, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.roundedImageView = (RoundedImageView) convertView.findViewById(R.id.image);
            viewHolder.tvGives = (TextView) convertView.findViewById(R.id.gives);
            viewHolder.tvGets = (TextView) convertView.findViewById(R.id.gets);
            viewHolder.tvContacts = (TextView) convertView.findViewById(R.id.contacts);
            viewHolder.tvPlace = (TextView) convertView.findViewById(R.id.place);
            viewHolder.tvDate = (TextView) convertView.findViewById(R.id.date);
            viewHolder.ivComments = (ImageView) convertView.findViewById(R.id.ivComments);
            viewHolder.tvCommentsCount = (TextView) convertView.findViewById(R.id.tvCommentsCount);
            viewHolder.tvName = (TextView) convertView.findViewById(R.id.tvName);
            viewHolder.aplPhotos = (AutomaticPhotoLayout) convertView.findViewById(R.id.aplPhotos);
            viewHolder.ivMessage = (ImageView) convertView.findViewById(R.id.ivMessage);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        /*String imageUrl = (String) data.get(position).get(IMAGE);

        if (!imageUrl.isEmpty()) {
            Glide.with(context)
                    .load(imageUrl)
                    .into(viewHolder.roundedImageView);
        } else {
            viewHolder.roundedImageView.setImageResource(R.drawable.no_photo);
        }

        viewHolder.roundedImageView.setOnClickListener(new OnImageClickListener(position));*/

        viewHolder.aplPhotos.removeAll();
        if( getItem(position).getPhotosArray() != null ) {
            viewHolder.aplPhotos.addPhotos(Arrays.asList(getItem(position).getPhotosArray()));
        }
        viewHolder.aplPhotos.setOnImageClickListener(new OnImageClickListener(position));

        //SPANNING

        TextFormatter formatter = new TextFormatter(context);

        HashMap<String, Object> item = data.get(position);

        //String gives = formatter.highlight(item.get(GIVE).toString(), itemsGive);
        //String gets = formatter.highlight(item.get(GET).toString(), itemsGet);
        //String place = formatter.highlight(item.get(GIVE).toString(),itemsGive);
        //String contacts = formatter.highlight(item.get(GIVE).toString(),itemsGive);

        viewHolder.tvGets.setText(metaData.get(position).getFormattedGet());
        viewHolder.tvGives.setText(metaData.get(position).getFormattedGive());

        viewHolder.tvDate.setText(item.get(DATE).toString());
        viewHolder.tvPlace.setText(item.get(PLACE).toString());
        viewHolder.tvContacts.setText(item.get(CONTACTS).toString());

        if (CommentsCountManager.getCommentCount(getItem(position).getId()) != -1) {
            getItem(position).setCommentsCount(CommentsCountManager.getCommentCount(getItem(position).getId()));
            Log.e(LOG_TAG,"create new count");
        }

        viewHolder.tvCommentsCount.setText(String.valueOf(getItem(position).getCommentsCount()));

        //viewHolder.ivComments.setOnClickListener(new OnImageClickListener(position));

        viewHolder.tvName.setText(getItem(position).getUserData().getName());

        viewHolder.ivMessage.setOnClickListener(new OnImageClickListener(position));

        return convertView;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    public ArrayList<HashMap<String, Object>> getData() {
        return data;
    }

    private class OnImageClickListener implements View.OnClickListener, com.technologies.mobile.free_exchange.listeners.OnImageClickListener {

        int position;

        public OnImageClickListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.aplPhotos: {
                    break;
                }
                case R.id.ivComments: {
                    break;
                }
                case R.id.ivMessage:{
                    if( onIconClickListener != null ){
                        onIconClickListener.onIconClick(view, position);
                    }
                    break;
                }
            }
        }

        @Override
        public void onImageClicked(int imageIndex) {
            Intent intent = new Intent(context, ImagePreviewActivity.class);
            String[] images = (String[]) data.get(position).get(IMAGES);
            intent.putExtra(ImagePreviewActivity.IMAGES, images);
            intent.putExtra(ImagePreviewActivity.ITEM,imageIndex);
            context.startActivity(intent);
        }
    }

    private void init(){
        data = new ArrayList<>();
        metaData = new ArrayList<>();
        notifyDataSetChanged();

        if( onSearchBeginListener != null ){
            onSearchBeginListener.onSearchBegun();
        }
        uploading = true;
        performListQuery(0, UPLOAD_LENGTH);
    }

    public void initialUploading() {
        init();
        Loader.showProgressBar(context);
    }

    public void initialUploadingWithoutProgressBar() {
        init();
        Loader.alertIfNoInternet(context);
    }

    public void additionalUploading(int start) {
        if (!uploading) {
            uploading = true;
            performListQuery(start, UPLOAD_LENGTH);
        }
    }

    public void setUploadingParams(String itemsGive, String itemsGet, int category) {
        this.itemsGive = itemsGive;
        this.itemsGet = itemsGet;
        this.category = category;
    }

    public void setUploadingParams(int category) {
        this.category = category;
    }

    protected void performListQuery(final int offset, final int count) {
        ExchangeClient client = RetrofitService.createService(ExchangeClient.class);

        Call<Search> searchCall = client.findExchanges(itemsGive, itemsGet, offset, count, category, ExchangeClient.apiKey);

        searchCall.enqueue(new Callback<Search>() {
            @Override
            public void onResponse(Call<Search> call, Response<Search> response) {
                if( response.body() != null ) {
                    Log.e(LOG_TAG, "MAIN LIST SUCCESS = " + response.body().toString());
                    Log.e(LOG_TAG, "MAIN LIST SUCCESS COUNT = " + response.body().getSearchResponse().getCount());
                    Search search = response.body();
                    SearchResponse searchResponse = search.getSearchResponse();
                    SearchExtraditionItem[] searchExtraditionItems = searchResponse.getSearchExtraditionItems();

                    metaData.addAll(Arrays.asList(searchExtraditionItems));

                    for (int i = 0; i < searchExtraditionItems.length; i++) {
                        SearchExtraditionItem searchExtraditionItem = searchExtraditionItems[i];

                        if (searchExtraditionItem == null) {
                            continue;
                        }

                        HashMap<String, Object> item = new HashMap<>();
                        item.put(GET, searchExtraditionItem.getGet());
                        item.put(GIVE, searchExtraditionItem.getGive());
                        item.put(CATEGORY, searchExtraditionItem.getCategory());
                        item.put(TEXT, searchExtraditionItem.getText());
                        if (searchExtraditionItem.getPhotosArray() != null &&
                                searchExtraditionItem.getPhotosArray().length != 0 &&
                                !searchExtraditionItem.getPhotosArray()[0].isEmpty()) {
                            item.put(IMAGE, searchExtraditionItem.getPhotosArray()[0]);
                        } else {
                            item.put(IMAGE, "");
                        }
                        item.put(IMAGES, searchExtraditionItem.getPhotosArray());
                        item.put(PLACE, searchExtraditionItem.getPlace());
                        item.put(CONTACTS, searchExtraditionItem.getContacts());
                        item.put(UID, searchExtraditionItem.getUid());
                        item.put(VK_ID, searchExtraditionItem.getUserData().getVkId());

                        String authorName = searchExtraditionItem.getUserData().getName();
                        if (authorName == null) {
                            authorName = context.getString(R.string.dialog);
                        }
                        item.put(AUTHOR_NAME, authorName);
                        item.put(COMMENTS_COUNT, searchExtraditionItem.getCommentsCount());
                        item.put(DATE, searchExtraditionItem.getDate());

                        data.add(item);
                    }
                }
                notifyDataSetChanged();
                uploading = false;
                Loader.hideProgressBar(context);
                if( onSearchPerformListener != null ){
                    if( response.body() != null ) {
                        onSearchPerformListener.onSearchPerformed(response.body().getSearchResponse().getCount());
                    }else{
                        onSearchPerformListener.onSearchPerformed(0);
                    }
                }
            }

            @Override
            public void onFailure(Call<Search> call, Throwable t) {
                Log.e(LOG_TAG, "ERROR MAIN LIST " + t.toString());

                /*AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setNeutralButton("Закрыть",null);
                builder.setMessage(t.toString());
                builder.create().show();*/
                if( onSearchPerformListener != null ){
                    onSearchPerformListener.onSearchPerformed(0);
                }
                t.printStackTrace();
                uploading = false;
                Loader.hideProgressBar(context);
            }
        });
    }

    public void setOnSearchPerformListener(OnSearchPerformListener onSearchPerformListener) {
        this.onSearchPerformListener = onSearchPerformListener;
    }

    public void setOnIconClickListener(OnIconClickListener onIconClickListener) {
        this.onIconClickListener = onIconClickListener;
    }

    public void setOnSearchBeginListener(OnSearchBeginListener onSearchBeginListener) {
        this.onSearchBeginListener = onSearchBeginListener;
    }
}
