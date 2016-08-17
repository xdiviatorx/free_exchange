package com.technologies.mobile.free_exchange.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.technologies.mobile.free_exchange.R;
import com.technologies.mobile.free_exchange.rest.ExchangeClient;
import com.technologies.mobile.free_exchange.rest.RetrofitService;
import com.technologies.mobile.free_exchange.rest.model.Resp;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    public static String LOG_TAG = "logs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        

        login();
    }

    private void login(){
        Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
        startActivity(intent);
    }

    private void query(){
        ExchangeClient client = RetrofitService.createService(ExchangeClient.class);

        Call<Resp> vkGroupIdCall = client.getVkGroupId(ExchangeClient.apiKey);

        vkGroupIdCall.enqueue(new Callback<Resp>() {
            @Override
            public void onResponse(Call<Resp> call, Response<Resp> response) {
                Log.e(LOG_TAG,"SUCCESS");
                Resp resp = response.body();
                if( resp.getVkGroupId() != null ){
                    Log.e(LOG_TAG,"VK GROUP ID = " + resp.getVkGroupId());
                }else{
                    if( resp.getError() != null ) {
                        Log.e(LOG_TAG, resp.getError().getResult());
                    }
                }
            }

            @Override
            public void onFailure(Call<Resp> call, Throwable t) {
                Log.e(LOG_TAG,"FAILED");
            }
        });
    }
}
