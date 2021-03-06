package com.technologies.mobile.free_exchange.services.messages;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.util.Log;

import com.technologies.mobile.free_exchange.activities.LoginActivity;
import com.technologies.mobile.free_exchange.fragments.DialogFragment;
import com.technologies.mobile.free_exchange.notification.Notificator;
import com.technologies.mobile.free_exchange.rest.ExchangeClient;
import com.technologies.mobile.free_exchange.rest.RetrofitService;
import com.technologies.mobile.free_exchange.rest.model.NewMessage;
import com.technologies.mobile.free_exchange.rest.model.NewMessagesResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by diviator on 27.09.2016.
 */
public class CatchMessageReceiver extends BroadcastReceiver {

    private static final int NEXT_CATCH_DELAY = 3*60*1000;
    private static final int NEXT_CATCH_DELAY_IF_RECEIVED = 30*1000;
    private static final int NEXT_CATCH_DELAY_IF_ERROR = 10*60*1000;

    private static final String LOG_TAG = "mCservice";

    private AlarmManager alarmManager;
    private Context mContext;

    @Override
    public void onReceive(Context context, Intent intent) {
        mContext = context;
        alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        catchMessage();
    }

    private void catchMessage(){
        Log.e(LOG_TAG,"CATCH MESSAGE");
        final String uid = PreferenceManager.getDefaultSharedPreferences(mContext).getString(LoginActivity.ID,null);
        if( uid == null ) {
            nextCatchRegister(NEXT_CATCH_DELAY_IF_ERROR);
            Log.e(LOG_TAG," UID is NULL");
            return;
        }
        Log.e(LOG_TAG," UID " + uid);

        ExchangeClient client = RetrofitService.createService(ExchangeClient.class);
        Call<NewMessagesResponse> newMessagesResponseCall = client.getNewMessages(uid,ExchangeClient.apiKey);
        newMessagesResponseCall.enqueue(new Callback<NewMessagesResponse>() {
            @Override
            public void onResponse(Call<NewMessagesResponse> call, Response<NewMessagesResponse> response) {
                int i = 0;
                if( response.body() != null
                        && response.body().getResponse() != null
                        && response.body().getResponse().getMessages() != null
                        && response.body().getResponse().getMessages().length != 0 ){
                    for( NewMessage message : response.body().getResponse().getMessages() ){
                        Log.e(LOG_TAG,"\t***");
                        Log.e(LOG_TAG,"D " + i + " = " + message.getDialogId());
                        Log.e(LOG_TAG,"M " + i + " = " + message.getText());
                        Log.e(LOG_TAG,"V " + i + " = " + message.getViewed());
                        i++;
                    }
                    Intent action = new Intent(DialogFragment.NEW_MESSAGE_ACTION);
                    mContext.sendBroadcast(action);

                    Notificator notificator = new Notificator(mContext);
                    notificator.notificate(uid);

                    nextCatchRegister(NEXT_CATCH_DELAY_IF_RECEIVED);
                }else{
                    nextCatchRegister(NEXT_CATCH_DELAY);
                }
            }

            @Override
            public void onFailure(Call<NewMessagesResponse> call, Throwable t) {
                nextCatchRegister(NEXT_CATCH_DELAY_IF_ERROR);
                Log.e(LOG_TAG,"ERROR " + t.toString());
            }
        });
    }

    private void nextCatchRegister(long delay){
        Intent receive = new Intent(mContext,CatchMessageReceiver.class);
        PendingIntent pIntent = PendingIntent.getBroadcast(mContext,0,receive,PendingIntent.FLAG_CANCEL_CURRENT);
        alarmManager.set(AlarmManager.RTC_WAKEUP,System.currentTimeMillis()+delay,pIntent);
    }

}