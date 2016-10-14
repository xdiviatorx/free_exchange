package com.technologies.mobile.free_exchange.services.subscribes;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.util.Log;

import com.technologies.mobile.free_exchange.activities.LoginActivity;
import com.technologies.mobile.free_exchange.notification.Notificator;
import com.technologies.mobile.free_exchange.notification.SubscribeNotificator;
import com.technologies.mobile.free_exchange.rest.ExchangeClient;
import com.technologies.mobile.free_exchange.rest.RetrofitService;
import com.technologies.mobile.free_exchange.rest.model.NewOffersByUidResponse;
import com.technologies.mobile.free_exchange.rest.model.SearchExtraditionItem;
import com.technologies.mobile.free_exchange.rest.model.SubscribeOffers;
import com.technologies.mobile.free_exchange.services.messages.CatchMessageReceiver;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by diviator on 11.10.2016.
 */

public class CatchExchangeReceiver extends BroadcastReceiver{

    private static final String LOG_TAG = "ecService";

    private static final int DELAY = 10000;
    private static final int ERROR_DELAY = 30000;

    private AlarmManager alarmManager;
    private Context mContext;

    @Override
    public void onReceive(Context context, Intent intent) {
        mContext = context;
        alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        catchExchange();
    }

    private void catchExchange(){
        Log.e(LOG_TAG,"CATCH EXCHANGE");
        final String uid = PreferenceManager.getDefaultSharedPreferences(mContext).getString(LoginActivity.ID,null);
        if( uid == null ) {
            nextCatchRegister(ERROR_DELAY);
            Log.e(LOG_TAG," UID is NULL");
            return;
        }
        Log.e(LOG_TAG," UID " + uid);

        ExchangeClient client = RetrofitService.createService(ExchangeClient.class);
        client.getNewOffersByUid(uid,ExchangeClient.apiKey).enqueue(new Callback<NewOffersByUidResponse>() {
            @Override
            public void onResponse(Call<NewOffersByUidResponse> call, Response<NewOffersByUidResponse> response) {
                if( response.body().getResponse().getCount() != 0 ){
                    for(SubscribeOffers subscribeOffers : response.body().getResponse().getLists()){
                        if( subscribeOffers.getCount() != 0 ){
                            SubscribeNotificator notificator = new SubscribeNotificator(mContext);
                            notificator.notificatePreparing(subscribeOffers);
                        }
                    }
                }
                nextCatchRegister(DELAY);
            }

            @Override
            public void onFailure(Call<NewOffersByUidResponse> call, Throwable t) {
                nextCatchRegister(ERROR_DELAY);
                Log.e(LOG_TAG,"ERROR " + t.toString());
            }
        });

    }

    private void nextCatchRegister(long delay){
        Intent receive = new Intent(mContext,CatchExchangeReceiver.class);
        PendingIntent pIntent = PendingIntent.getBroadcast(mContext,0,receive,PendingIntent.FLAG_CANCEL_CURRENT);
        alarmManager.set(AlarmManager.RTC_WAKEUP,System.currentTimeMillis()+delay,pIntent);
    }

}
