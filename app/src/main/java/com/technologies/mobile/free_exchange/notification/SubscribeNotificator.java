package com.technologies.mobile.free_exchange.notification;

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

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.technologies.mobile.free_exchange.R;
import com.technologies.mobile.free_exchange.activities.DialogActivity;
import com.technologies.mobile.free_exchange.activities.SubscribeExchangeActivity;
import com.technologies.mobile.free_exchange.fragments.DialogFragment;
import com.technologies.mobile.free_exchange.rest.ExchangeClient;
import com.technologies.mobile.free_exchange.rest.RetrofitService;
import com.technologies.mobile.free_exchange.rest.model.NewMessage;
import com.technologies.mobile.free_exchange.rest.model.SearchExtraditionItem;
import com.technologies.mobile.free_exchange.rest.model.SetNotifiedResponse;
import com.technologies.mobile.free_exchange.rest.model.SubscribeOffers;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by diviator on 11.10.2016.
 */

public class SubscribeNotificator {

    private int NOTIFICATION_ID = 197;

    Context mContext;
    NotificationManager mNotificationManager;

    String listId;

    public SubscribeNotificator(Context context){
        mContext = context;
        mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public void notificatePreparing(SubscribeOffers subscribeOffers){
        if( subscribeOffers.getCount() != 0 ){
            listId = subscribeOffers.getListId();
            notificate(subscribeOffers.getOffers()[0]);
        }
    }

    private void notificate(SearchExtraditionItem exItem){
        prepareNotificationLarge(exItem);
    }

    private void prepareNotificationLarge(final SearchExtraditionItem exItem){
        final Bitmap large = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_launcher);

        Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                makeNotification(exItem, bitmap);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                makeNotification(exItem, large);
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };

        if( exItem.getPhotosList() != null && exItem.getPhotosList().length != 0 ) {
            Picasso.with(mContext)
                    .load(exItem.getPhotosList()[0])
                    .into(target);
        }else{
            makeNotification(exItem, large);
        }
    }

    private void makeNotification(SearchExtraditionItem exItem, Bitmap large){
        ExchangeClient client = RetrofitService.createService(ExchangeClient.class);
        client.setNotified(listId,ExchangeClient.apiKey).enqueue(new Callback<SetNotifiedResponse>() {
            @Override
            public void onResponse(Call<SetNotifiedResponse> call, Response<SetNotifiedResponse> response) {

            }

            @Override
            public void onFailure(Call<SetNotifiedResponse> call, Throwable t) {

            }
        });

        Intent intent = new Intent(mContext,SubscribeExchangeActivity.class);
        intent.putExtra(SubscribeExchangeActivity.LIST_ID,listId);
        PendingIntent pIntent = PendingIntent.getActivity(mContext, NOTIFICATION_ID, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Notification.Builder builder = new Notification.Builder(mContext);
        builder.setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(large)
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setTicker(exItem.getGive())
                .setContentTitle(mContext.getString(R.string.new_exchange))
                .setContentText(mContext.getString(R.string.give)+":"+exItem.getGive()+"\n"+mContext.getString(R.string.get)+":"+exItem.getGet())
                .setContentIntent(pIntent)
                .setSound(alarmSound);

        Notification notification = builder.build();

        mNotificationManager.notify(NOTIFICATION_ID, notification);
    }

}
