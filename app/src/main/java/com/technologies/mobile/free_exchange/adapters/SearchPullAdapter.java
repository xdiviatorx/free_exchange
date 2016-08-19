package com.technologies.mobile.free_exchange.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;
import com.technologies.mobile.free_exchange.R;
import com.technologies.mobile.free_exchange.activities.ImagePreviewActivity;
import com.technologies.mobile.free_exchange.activities.MainActivity;
import com.technologies.mobile.free_exchange.rest.ExchangeClient;
import com.technologies.mobile.free_exchange.rest.RetrofitService;
import com.technologies.mobile.free_exchange.rest.model.Search;
import com.technologies.mobile.free_exchange.rest.model.SearchExtraditionItem;
import com.technologies.mobile.free_exchange.rest.model.SearchResponse;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
public class SearchPullAdapter extends SimpleAdapter{

    public static String LOG_TAG = "logs";

    public static String GIVE = "GIVE";
    public static String GET = "GET";
    public static String CATEGORY = "CATEGORY";
    public static String TEXT = "TEXT";
    public static String DATE = "DATE";
    public static String IMAGE = "IMAGE";
    public static String PLACE = "PLACE";
    public static String CONTACTS = "CONTACTS";
    public static String IMAGES = "IMAGES";

    private int UPLOAD_LENGTH = 20;

    private boolean uploading = false;

    Context context;

    ArrayList<HashMap<String,Object>> data;


    public SearchPullAdapter(Context context, ArrayList<HashMap<String,Object>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);

        this.context = context;
        this.data =data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view =  super.getView(position, convertView, parent);

        RoundedImageView roundedImageView = (RoundedImageView) view.findViewById(R.id.image);

        String imageUrl = (String) data.get(position).get(IMAGE);

        if( !imageUrl.isEmpty() ) {
            Picasso.with(context)
                    .load(imageUrl)
                    .into(roundedImageView);
        }else{
            roundedImageView.setImageResource(R.drawable.no_photo);
        }

        roundedImageView.setOnClickListener(new OnImageClickListener(position));

        return view;
    }

    private class OnImageClickListener implements View.OnClickListener{

        int position;

        public OnImageClickListener(int position){
            this.position = position;
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(context, ImagePreviewActivity.class);
            String[] images = (String[]) data.get(position).get(IMAGES);
            intent.putExtra(IMAGES,images);
            context.startActivity(intent);
        }
    }

    public void init(){
        uploading=true;
        performListQuery(0,UPLOAD_LENGTH);
    }

    public void uploading(int start){
        if( !uploading  ) {
            uploading = true;
            performListQuery(start, UPLOAD_LENGTH);
        }
    }

    private void performListQuery(final int offset,final int count){
        ExchangeClient client = RetrofitService.createService(ExchangeClient.class);

        String itemsGive = "";
        String itemsGet = "";
        int category = 0;


        Call<Search> searchCall = client.findExchanges(itemsGive,itemsGet,offset,count,category,ExchangeClient.apiKey);

        searchCall.enqueue(new Callback<Search>() {
            @Override
            public void onResponse(Call<Search> call, Response<Search> response) {
                Search search = response.body();
                SearchResponse searchResponse = search.getSearchResponse();
                SearchExtraditionItem[] searchExtraditionItems = searchResponse.getSearchExtraditionItems();

                for( int i = 0; i < searchExtraditionItems.length; i++ ){
                    SearchExtraditionItem searchExtraditionItem = searchExtraditionItems[i];

                    if( searchExtraditionItem == null ){
                        continue;
                    }

                    Log.e(LOG_TAG,"==ITEM#" + (i+1) + "==");
                    Log.e(LOG_TAG,"OFFSET = " + offset + " COUNT = " + count);
                    Log.e(LOG_TAG,searchExtraditionItem.getGet());
                    Log.e(LOG_TAG,searchExtraditionItem.getGive());
                    Log.e(LOG_TAG,searchExtraditionItem.getText());
                    //Log.e(LOG_TAG,searchExtraditionItem.getPhotos());
                    if( searchExtraditionItem.getPhotosList() != null ) {
                        for (String link : searchExtraditionItem.getPhotosList()){
                            Log.e(LOG_TAG,link);
                        }
                    }

                    HashMap<String,Object> item = new HashMap<>();
                    item.put(GET,searchExtraditionItem.getGet());
                    item.put(GIVE,searchExtraditionItem.getGive());
                    item.put(CATEGORY,searchExtraditionItem.getCategory());
                    item.put(TEXT,searchExtraditionItem.getText());
                    if( searchExtraditionItem.getPhotosList().length != 0  ) {
                        item.put(IMAGE, searchExtraditionItem.getPhotosList()[0]);
                    }else{
                        item.put(IMAGE, "");
                    }
                    item.put(IMAGES,searchExtraditionItem.getPhotosList());
                    item.put(PLACE, searchExtraditionItem.getPlace());
                    item.put(CONTACTS, searchExtraditionItem.getContacts());

                    long timestamp = searchExtraditionItem.getCreated();
                    timestamp*=1000;
                    DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm");
                    String date = df.format(new Date(timestamp));

                    item.put(DATE,date);

                    data.add(item);
                }
                notifyDataSetChanged();
                uploading=false;
            }

            @Override
            public void onFailure(Call<Search> call, Throwable t) {
                Log.e(LOG_TAG,"ERROR");
                t.printStackTrace();
            }
        });
    }

}
