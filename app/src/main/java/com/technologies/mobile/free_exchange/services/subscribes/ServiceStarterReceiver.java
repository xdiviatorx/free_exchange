package com.technologies.mobile.free_exchange.services.subscribes;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by diviator on 11.10.2016.
 */

public class ServiceStarterReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent subscribeExchangeCatcherServiceStarter = new Intent(context,SubscribeExchangeCatcherService.class);
        context.startService(subscribeExchangeCatcherServiceStarter);
    }

}
