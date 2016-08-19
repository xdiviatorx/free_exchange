package com.technologies.mobile.free_exchange.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.AbsListView;
import android.widget.ListView;

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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements AbsListView.OnScrollListener{

    public static String LOG_TAG = "logs";

    public static int LOGIN_REQUEST = 100;



    private ListView lv;
    private SearchPullAdapter lvAdapter;
    private ArrayList<HashMap<String,Object>> data;




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

        String[] from = {SearchPullAdapter.GIVE,SearchPullAdapter.GET,SearchPullAdapter.PLACE,SearchPullAdapter.CONTACTS,SearchPullAdapter.DATE};
        int[] to = {R.id.gives,R.id.gets,R.id.place,R.id.contacts,R.id.date};
        ArrayList<HashMap<String,Object>> data = new ArrayList<>();

        lvAdapter = new SearchPullAdapter(this,data,R.layout.exchange_item,from,to);

        lv.setAdapter(lvAdapter);

        lv.setOnScrollListener(this);

        lvAdapter.init();
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if( requestCode == LOGIN_REQUEST ){
            if( resultCode == RESULT_OK ){
                initViews();
            }
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {

    }

    @Override
    public void onScroll(AbsListView absListView, int i, int i1, int i2) {

        if( i >= Math.max(i2-10,10) ) {
            lvAdapter.uploading(i2);
        }

    }
}
