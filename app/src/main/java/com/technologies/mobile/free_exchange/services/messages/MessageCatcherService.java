package com.technologies.mobile.free_exchange.services.messages;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by diviator on 27.09.2016.
 */
public class MessageCatcherService extends Service {

    private static final String LOG_TAG = "mCservice";

    AlarmManager alarmManager;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(LOG_TAG,"service");

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent receive = new Intent(this,CatchMessageReceiver.class);
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
