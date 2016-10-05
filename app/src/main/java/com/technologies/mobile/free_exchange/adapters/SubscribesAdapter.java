package com.technologies.mobile.free_exchange.adapters;

import android.content.Context;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.technologies.mobile.free_exchange.Loader;
import com.technologies.mobile.free_exchange.R;
import com.technologies.mobile.free_exchange.activities.LoginActivity;
import com.technologies.mobile.free_exchange.rest.ExchangeClient;
import com.technologies.mobile.free_exchange.rest.RetrofitService;
import com.technologies.mobile.free_exchange.rest.model.GetSubscribeListsResponse;
import com.technologies.mobile.free_exchange.rest.model.SubscribeList;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by diviator on 04.10.2016.
 */

public class SubscribesAdapter extends SimpleAdapter {

    public static final String LOG_TAG = "subscribesAdapter";

    public static final String LIST_ID = "LIST_ID";
    public static final String ITEMS_GET = "ITEMS_GET";
    public static final String ITEMS_GIVE = "ITEMS_GIVE";
    public static final String CATEGORIES = "CATEGORIES";
    public static final String NOTIFICATION = "NOTIFICATION";

    private int UPLOAD_LENGTH = 20;
    private boolean uploading = false;

    private ActivityActions mActivityActions;

    Context context;
    ArrayList<HashMap<String, Object>> data;

    public SubscribesAdapter(Context context, ArrayList<HashMap<String, Object>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
        this.context = context;
        this.data = data;
    }

    public void initialDownload() {
        Loader.showProgressBar(context);
        uploading = true;
        download(0, UPLOAD_LENGTH);
    }

    public void additionalDownload(int start) {
        if (!uploading) {
            uploading = true;
            download(start, UPLOAD_LENGTH);
        }
    }

    private void download(int offset, int count) {
        ExchangeClient client = RetrofitService.createService(ExchangeClient.class);
        String uid = PreferenceManager.getDefaultSharedPreferences(context).getString(LoginActivity.ID, null);
        if (uid == null) {
            return;
        }
        client.getSubscribeLists(uid, offset, count, ExchangeClient.apiKey).enqueue(new Callback<GetSubscribeListsResponse>() {
            @Override
            public void onResponse(Call<GetSubscribeListsResponse> call, Response<GetSubscribeListsResponse> response) {
                for (SubscribeList subscribeList : response.body().getResponse().getSubscribeLists()) {
                    HashMap<String, Object> item = new HashMap<>();
                    item.put(LIST_ID, subscribeList.getListId());
                    item.put(ITEMS_GET, subscribeList.getItemsGet());
                    item.put(ITEMS_GIVE, subscribeList.getItemsGive());
                    item.put(CATEGORIES, subscribeList.getCategories());
                    item.put(NOTIFICATION, subscribeList.getNotification());
                    data.add(item);
                }
                notifyDataSetChanged();
                uploading = false;
                Loader.hideProgressBar(context);
            }

            @Override
            public void onFailure(Call<GetSubscribeListsResponse> call, Throwable t) {
                t.printStackTrace();
                Log.e(LOG_TAG, "DOWNLOAD ERROR = " + t.toString());

                uploading = false;

                Loader.hideProgressBar(context);
            }
        });
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);

        ImageButton more = (ImageButton) view.findViewById(R.id.ibMore);
        more.setOnClickListener(new ItemClickListener(position, (String) data.get(position).get(LIST_ID)));

        TextView tvGives = (TextView) view.findViewById(R.id.gives);
        String gives = "";
        JSONArray jsonGives = (JSONArray) data.get(position).get(ITEMS_GIVE);
        for( int i = 0; i < jsonGives.length(); i++ ){
            if( i > 0 ){
                gives+=", ";
            }
            try {
                gives+=jsonGives.getString(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        tvGives.setText(gives);

        TextView tvGets = (TextView) view.findViewById(R.id.gets);
        String gets = "";
        JSONArray jsonGets = (JSONArray) data.get(position).get(ITEMS_GET);
        for( int i = 0; i < jsonGets.length(); i++ ){
            if( i > 0 ){
                gets+=", ";
            }
            try {
                gets+=jsonGets.getString(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        tvGets.setText(gets);

        TextView tvNotification = (TextView) view.findViewById(R.id.tvNotification);
        switch ((int)data.get(position).get(NOTIFICATION)){
            case 0:{
                tvNotification.setText(R.string.no);
                break;
            }
            case 1:{
                tvNotification.setText(R.string.yes);
                break;
            }
        }

        return view;
    }

    private class ItemClickListener implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {

        String listId;
        int position;

        ItemClickListener(int position, String listId) {
            this.listId = listId;
            this.position = position;
        }

        @Override
        public void onClick(View view) {
            PopupMenu popupMenu = new PopupMenu(context, view);
            popupMenu.inflate(R.menu.subs_item_menu);

            popupMenu.setOnMenuItemClickListener(this);
            popupMenu.show();
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.miEdit: {
                    if (mActivityActions != null) {
                        mActivityActions.editSubscribe(position, listId);
                    }
                    break;
                }
                case R.id.miDelete: {
                    if (mActivityActions != null) {
                        mActivityActions.deleteSubscribe(position, listId);
                    }
                    break;
                }
            }
            return true;
        }
    }

    public void setActivityAction(ActivityActions activityActionListener) {
        mActivityActions = activityActionListener;
    }

    public ArrayList<HashMap<String, Object>> getData() {
        return data;
    }

}
