package com.technologies.mobile.free_exchange;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.technologies.mobile.free_exchange.rest.ExchangeClient;
import com.technologies.mobile.free_exchange.rest.RetrofitService;
import com.technologies.mobile.free_exchange.rest.model.Result;
import com.technologies.mobile.free_exchange.rest.model.VkGroupId;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    public static String LOG_TAG = "logs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ExchangeClient client = RetrofitService.createService(ExchangeClient.class);

        Call<Result> vkGroupIdCall = client.getVkGroupId(ExchangeClient.apiKey);

        vkGroupIdCall.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                Log.e(LOG_TAG,"SUCCESS");
                Log.e(LOG_TAG," " + response.body().getVkGroupId().getResult());
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                Log.e(LOG_TAG,"FAILED");
            }
        });
    }
}
