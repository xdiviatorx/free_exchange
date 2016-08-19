package com.technologies.mobile.free_exchange.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.SimpleAdapter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.technologies.mobile.free_exchange.R;
import com.technologies.mobile.free_exchange.adapters.SearchPullAdapter;
import com.technologies.mobile.free_exchange.rest.ExchangeClient;
import com.technologies.mobile.free_exchange.rest.RetrofitService;
import com.technologies.mobile.free_exchange.rest.model.Search;
import com.technologies.mobile.free_exchange.rest.model.SearchExtraditionItem;
import com.technologies.mobile.free_exchange.rest.model.SearchResponse;
import com.technologies.mobile.free_exchange.rest.model.VkGroupIdResponse;

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

public class MainActivity extends AppCompatActivity implements AbsListView.OnScrollListener{

    public static String LOG_TAG = "logs";

    public static int LOGIN_REQUEST = 100;

    private int UPLOAD_LENGTH = 20;

    private ListView lv;
    private SearchPullAdapter lvAdapter;
    private ArrayList<HashMap<String,Object>> data;

    private String GIVE = "GIVE";
    private String GET = "GET";
    private String CATEGORY = "CATEGORY";
    private String TEXT = "TEXT";
    private String DATE = "DATE";
    private String IMAGE = "IMAGE";

    private boolean uploading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        login();
    }

    private void login(){
        Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
        startActivityForResult(intent,LOGIN_REQUEST);
    }

    private void initViews(){
        lv = (ListView) findViewById(R.id.lv);

        String[] from = {GIVE,GET,CATEGORY,TEXT,DATE};
        int[] to = {R.id.gives,R.id.gets,R.id.category,R.id.text,R.id.date};
        data = new ArrayList<>();
        lvAdapter = new SearchPullAdapter(this,data,R.layout.exchange_item,from,to);

        lv.setAdapter(lvAdapter);

        lv.setOnScrollListener(this);
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
                    if( searchExtraditionItem.getPhotosList().size() != 0  ) {
                        item.put(IMAGE, searchExtraditionItem.getPhotosList().get(0));
                    }else{
                        item.put(IMAGE, "");
                    }

                    long timestamp = searchExtraditionItem.getCreated();
                    timestamp*=1000;
                    DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm");
                    String date = df.format(new Date(timestamp));

                    item.put(DATE,date);

                    data.add(item);
                }
                lvAdapter.notifyDataSetChanged();
                uploading=false;
            }

            @Override
            public void onFailure(Call<Search> call, Throwable t) {
                Log.e(LOG_TAG,"ERROR");
                t.printStackTrace();
            }
        });
    }

    private void vkGroupIdQuery(){
        ExchangeClient client = RetrofitService.createService(ExchangeClient.class);

        Call<VkGroupIdResponse> vkGroupIdCall = client.getVkGroupId(ExchangeClient.apiKey);

        vkGroupIdCall.enqueue(new Callback<VkGroupIdResponse>() {
            @Override
            public void onResponse(Call<VkGroupIdResponse> call, Response<VkGroupIdResponse> response) {
                Log.e(LOG_TAG,"SUCCESS");
                VkGroupIdResponse vkGroupIdResponse = response.body();
                if( vkGroupIdResponse.getVkGroupId() != null ){
                    Log.e(LOG_TAG,"VK GROUP ID = " + vkGroupIdResponse.getVkGroupId());
                }else{
                    if( vkGroupIdResponse.getError() != null ) {
                        Log.e(LOG_TAG, vkGroupIdResponse.getError().getResult());
                    }
                }
            }

            @Override
            public void onFailure(Call<VkGroupIdResponse> call, Throwable t) {
                Log.e(LOG_TAG,"FAILED");
                t.printStackTrace();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if( requestCode == LOGIN_REQUEST ){
            if( resultCode == RESULT_OK ){
                initViews();
                performListQuery(0,UPLOAD_LENGTH+10);
            }
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {

    }

    @Override
    public void onScroll(AbsListView absListView, int i, int i1, int i2) {

        if( i >= Math.max(i2-10,10) && !uploading ) {
            uploading=true;
            performListQuery(i2,UPLOAD_LENGTH);
        }

    }
}
