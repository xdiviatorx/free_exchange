package com.technologies.mobile.free_exchange.services.subscribes;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.technologies.mobile.free_exchange.services.messages.CatchMessageReceiver;

/**
 * Created by diviator on 11.10.2016.
 */

public class SubscribeExchangeCatcherService extends Service {

    AlarmManager alarmManager;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent receive = new Intent(this,CatchExchangeReceiver.class);
        PendingIntent pIntent = PendingIntent.getBroadcast(this,0,receive,PendingIntent.FLAG_CANCEL_CURRENT);
        alarmManager.set(AlarmManager.RTC_WAKEUP,System.currentTimeMillis(),pIntent);

        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
