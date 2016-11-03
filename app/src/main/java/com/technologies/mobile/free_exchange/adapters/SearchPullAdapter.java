package com.technologies.mobile.free_exchange.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
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
import com.technologies.mobile.free_exchange.logic.TextFormatter;
import com.technologies.mobile.free_exchange.rest.ExchangeClient;
import com.technologies.mobile.free_exchange.rest.RetrofitService;
import com.technologies.mobile.free_exchange.rest.model.Photo;
import com.technologies.mobile.free_exchange.rest.model.Search;
import com.technologies.mobile.free_exchange.rest.model.SearchExtraditionItem;
import com.technologies.mobile.free_exchange.rest.model.SearchResponse;

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
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        String imageUrl = (String) data.get(position).get(IMAGE);

        if (!imageUrl.isEmpty()) {
            Glide.with(context)
                    .load(imageUrl)
                    .into(viewHolder.roundedImageView);
        } else {
            viewHolder.roundedImageView.setImageResource(R.drawable.no_photo);
        }

        viewHolder.roundedImageView.setOnClickListener(new OnImageClickListener(position));

        //SPANNING

        TextFormatter formatter = new TextFormatter(context);

        HashMap<String, Object> item = data.get(position);

        String gives = formatter.highlight(item.get(GIVE).toString(), itemsGive);
        String gets = formatter.highlight(item.get(GET).toString(), itemsGet);
        //String place = formatter.highlight(item.get(GIVE).toString(),itemsGive);
        //String contacts = formatter.highlight(item.get(GIVE).toString(),itemsGive);

        if (android.os.Build.VERSION.SDK_INT < 24) {
            viewHolder.tvGives.setText(Html.fromHtml(gives));
            viewHolder.tvGets.setText(Html.fromHtml(gets));
        } else {
            viewHolder.tvGives.setText(Html.fromHtml(gives, Html.FROM_HTML_MODE_LEGACY));
            viewHolder.tvGets.setText(Html.fromHtml(gets, Html.FROM_HTML_MODE_LEGACY));
        }

        viewHolder.tvDate.setText(item.get(DATE).toString());
        viewHolder.tvPlace.setText(item.get(PLACE).toString());
        viewHolder.tvContacts.setText(item.get(CONTACTS).toString());

        if (CommentsCountManager.getCommentCount(getItem(position).getId()) != -1) {
            getItem(position).setCommentsCount(CommentsCountManager.getCommentCount(getItem(position).getId()));
            Log.e(LOG_TAG,"create new count");
        }

        if (getItem(position).getCommentsCount() != 0) {
            viewHolder.tvCommentsCount.setText(String.valueOf(getItem(position).getCommentsCount()));
        } else {
            viewHolder.tvCommentsCount.setText("");
        }

        viewHolder.ivComments.setOnClickListener(new OnImageClickListener(position));

        return convertView;
    }

    public ArrayList<HashMap<String, Object>> getData() {
        return data;
    }

    private class OnImageClickListener implements View.OnClickListener {

        int position;

        public OnImageClickListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.image: {
                    Intent intent = new Intent(context, ImagePreviewActivity.class);
                    String[] images = (String[]) data.get(position).get(IMAGES);
                    intent.putExtra(IMAGES, images);
                    context.startActivity(intent);
                    break;
                }
                case R.id.ivComments: {
                    Intent intent = new Intent(context, ExchangeMoreActivity.class);
                    intent.putExtra(ExchangeMoreActivity.EXCHANGE, getItem(position));
                    context.startActivity(intent);
                    break;
                }
            }
        }
    }

    public void initialUploading() {
        Loader.showProgressBar(context);
        uploading = true;
        performListQuery(0, UPLOAD_LENGTH);
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

    protected void performListQuery(final int offset, final int count) {
        ExchangeClient client = RetrofitService.createService(ExchangeClient.class);

        Call<Search> searchCall = client.findExchanges(itemsGive, itemsGet, offset, count, category, ExchangeClient.apiKey);

        searchCall.enqueue(new Callback<Search>() {
            @Override
            public void onResponse(Call<Search> call, Response<Search> response) {
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
                    if (searchExtraditionItem.getPhotosList() != null &&
                            searchExtraditionItem.getPhotosList().length != 0 &&
                            searchExtraditionItem.getPhotosList()[0].getPhoto807() != null) {
                        item.put(IMAGE, searchExtraditionItem.getPhotosList()[0].getPhoto807());
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
                notifyDataSetChanged();
                uploading = false;
                Loader.hideProgressBar(context);
            }

            @Override
            public void onFailure(Call<Search> call, Throwable t) {
                Log.e(LOG_TAG, "ERROR MAIN LIST " + t.toString());

                /*AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setNeutralButton("Закрыть",null);
                builder.setMessage(t.toString());
                builder.create().show();*/

                t.printStackTrace();
                uploading = false;
                Loader.hideProgressBar(context);
            }
        });
    }

}
