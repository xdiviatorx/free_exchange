package com.technologies.mobile.free_exchange.notification;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.technologies.mobile.free_exchange.R;
import com.technologies.mobile.free_exchange.activities.DialogActivity;
import com.technologies.mobile.free_exchange.fragments.DialogFragment;
import com.technologies.mobile.free_exchange.rest.ExchangeClient;
import com.technologies.mobile.free_exchange.rest.RetrofitService;
import com.technologies.mobile.free_exchange.rest.model.Dialog;
import com.technologies.mobile.free_exchange.rest.model.NewMessage;
import com.technologies.mobile.free_exchange.rest.model.NewMessagesResponse;
import com.technologies.mobile.free_exchange.rest.model.SetViewedResponse;

import java.io.IOException;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by diviator on 30.09.2016.
 */
public class Notificator {

    private int NOTIFICATION_ID = 753;
    private int NOTIFICATION_POST_DELAY_MILLIS = 3000;

    Context mContext;
    NotificationManager mNotificationManager;

    public Notificator(Context context) {
        mContext = context;
        mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public void notificate(final String uid) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ExchangeClient client = RetrofitService.createService(ExchangeClient.class);
                client.getNewMessages(uid,ExchangeClient.apiKey).enqueue(new Callback<NewMessagesResponse>() {
                    @Override
                    public void onResponse(Call<NewMessagesResponse> call, Response<NewMessagesResponse> response) {
                        postDelayedNotification(response.body().getResponse().getMessages());
                    }

                    @Override
                    public void onFailure(Call<NewMessagesResponse> call, Throwable t) {

                    }
                });
            }
        },NOTIFICATION_POST_DELAY_MILLIS);
    }

    private void postDelayedNotification(NewMessage[] messages){
        HashMap<String, Boolean> done = new HashMap<>();
        for (NewMessage message : messages) {
            if (!done.containsKey(message.getDialogId())) {
                done.put(message.getDialogId(),true);
                prepareNotificationLarge(message);
            }
        }
    }

    private void makeNotification(NewMessage message, Bitmap large) {
        ExchangeClient client = RetrofitService.createService(ExchangeClient.class);
        client.setViewed(message.getDialogId(),message.getToId(),ExchangeClient.apiKey).enqueue(new Callback<SetViewedResponse>() {
            @Override
            public void onResponse(Call<SetViewedResponse> call, Response<SetViewedResponse> response) {

            }

            @Override
            public void onFailure(Call<SetViewedResponse> call, Throwable t) {

            }
        });

        Intent intent = new Intent(mContext, DialogActivity.class);

        intent.putExtra(DialogFragment.DIALOG_ID, message.getDialogId());
        intent.putExtra(DialogFragment.INTERLOCUTOR_ID, message.getFromId());
        intent.putExtra(DialogFragment.INTERLOCUTOR_NAME, message.getUserDataFrom().getName());
        intent.putExtra(DialogFragment.INTERLOCUTOR_VK_ID,message.getUserDataFrom().getVkId());

        PendingIntent pIntent = PendingIntent.getActivity(mContext, NOTIFICATION_ID, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Notification.Builder builder = new Notification.Builder(mContext);
        builder.setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(large)
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setTicker(message.getUserDataFrom().getName()+": "+message.getText())
                .setContentTitle(message.getUserDataFrom().getName())
                .setContentText(message.getText())
                .setContentIntent(pIntent)
                .setSound(alarmSound);

        Notification notification = builder.build();

        mNotificationManager.notify(NOTIFICATION_ID, notification);
    }

    public void prepareNotificationLarge(final NewMessage message){
        final Bitmap large = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_launcher);

        Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                makeNotification(message, bitmap);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                makeNotification(message, large);
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };

        if( message.getUserDataFrom().getPhoto() != null && !message.getUserDataFrom().getPhoto().isEmpty() ) {
            Picasso.with(mContext)
                    .load(message.getUserDataFrom().getPhoto())
                    .into(target);
        }else{
            makeNotification(message, large);
        }
    }

    public void cancelNotification() {
        mNotificationManager.cancel(NOTIFICATION_ID);
    }

    private boolean isDialogOpened(String dialogId) {
        return false;
        /*android.app.FragmentManager fm = ((Activity) mContext).getFragmentManager();
        DialogFragment dialogFragment = (DialogFragment) fm.findFragmentByTag(DialogFragment.TAG);
        if (dialogFragment != null && dialogFragment.isVisible()) {
            if (dialogFragment.getDlgId() != null && dialogFragment.getDlgId().equals(dialogId)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }*/
    }
}