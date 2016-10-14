package com.technologies.mobile.free_exchange.services.messages;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by diviator on 27.09.2016.
 */
public class ServiceStarterReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent messageCatcherServiceStarter = new Intent(context,MessageCatcherService.class);
        context.startService(messageCatcherServiceStarter);
    }

}