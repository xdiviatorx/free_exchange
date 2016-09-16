package com.technologies.mobile.free_exchange;

import android.app.Application;

import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKAccessTokenTracker;
import com.vk.sdk.VKSdk;

/**
 * Created by diviator on 17.08.2016.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //vkAccessTokenTracker.startTracking();
        VKSdk.initialize(this);
        //int appId = getResources().getInteger(R.integer.com_vk_sdk_AppId);
        //String apiVersion = getResources().getString(R.string.api_version);
        //VKSdk.customInitialize(this,appId,apiVersion);
    }

    VKAccessTokenTracker vkAccessTokenTracker = new VKAccessTokenTracker() {
        @Override
        public void onVKAccessTokenChanged(VKAccessToken oldToken, VKAccessToken newToken) {
            if (newToken == null) {
                // VKAccessToken is invalid
                newToken = oldToken;
            }
        }
    };

}
