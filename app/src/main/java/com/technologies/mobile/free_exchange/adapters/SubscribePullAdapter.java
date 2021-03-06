package com.technologies.mobile.free_exchange.adapters;

import android.content.Context;
import android.util.Log;

import com.technologies.mobile.free_exchange.Loader;
import com.technologies.mobile.free_exchange.R;
import com.technologies.mobile.free_exchange.rest.ExchangeClient;
import com.technologies.mobile.free_exchange.rest.RetrofitService;
import com.technologies.mobile.free_exchange.rest.model.GetOffersByListResponse;
import com.technologies.mobile.free_exchange.rest.model.Search;
import com.technologies.mobile.free_exchange.rest.model.SearchExtraditionItem;
import com.technologies.mobile.free_exchange.rest.model.SearchResponse;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by diviator on 07.10.2016.
 */

public class SubscribePullAdapter extends SearchPullAdapter {

    public static String LOG_TAG = "subscribePullAdapter";

    private String listId;

    public SubscribePullAdapter(Context context, ArrayList<HashMap<String, Object>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
    }

    public void setListId(String listId) {
        this.listId = listId;
    }

    @Override
    protected void performListQuery(int offset, int count) {
        ExchangeClient client = RetrofitService.createService(ExchangeClient.class);

        Log.e(LOG_TAG,"LIST ID" + listId);

        client.getOffersByList(listId, offset, count, ExchangeClient.apiKey).enqueue(new Callback<GetOffersByListResponse>() {
            @Override
            public void onResponse(Call<GetOffersByListResponse> call, Response<GetOffersByListResponse> response) {
                metaData.addAll(Arrays.asList(response.body().getResponse().getOffers()));
                for (SearchExtraditionItem searchExtraditionItem : response.body().getResponse().getOffers()) {
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

                    item.put(DATE, searchExtraditionItem.getDate());

                    item.put(COMMENTS_COUNT, searchExtraditionItem.getCommentsCount());

                    data.add(item);
                }
                notifyDataSetChanged();
                uploading = false;
                if( onSearchPerformListener != null ){
                    onSearchPerformListener.onSearchPerformed(response.body().getResponse().getCount());
                }
                Loader.hideProgressBar(context);
            }

            @Override
            public void onFailure(Call<GetOffersByListResponse> call, Throwable t) {
                Log.e(LOG_TAG, "ERROR SUBSCRIBE OFFERS LIST " + t.toString());
                t.printStackTrace();
                uploading = false;
                if( onSearchPerformListener != null ){
                    onSearchPerformListener.onSearchPerformed(0);
                }
                Loader.hideProgressBar(context);
            }
        });
    }
}
