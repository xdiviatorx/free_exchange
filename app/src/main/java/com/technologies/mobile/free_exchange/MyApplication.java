package com.technologies.mobile.free_exchange;

import android.app.Application;
import android.preference.PreferenceManager;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKAccessTokenTracker;
import com.vk.sdk.VKSdk;
import io.fabric.sdk.android.Fabric;

/**
 * Created by diviator on 17.08.2016.
 */
public class MyApplication extends Application {

    public static final String MAIN_CATEGORY = "Главный экран";
    public static final String ADDING_CATEGORY = "Добавление обмена";
    public static final String SEARCH_CATEGORY = "Поиск";
    public static final String MESSAGING_CATEGORY = "Сообщения";

    public static final String LAUNCHED_ACTION = "Запущено";
    public static final String SEARCHING_ACTION = "Ищет";
    public static final String CONVERSATION_ACTION = "Общается";
    public static final String ADDING_ACTION = "Добавляет обмен";

    private Tracker mTracker;


    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());

        //vkAccessTokenTracker.startTracking();
        VKSdk.initialize(this);
        //int appId = getResources().getInteger(R.integer.com_vk_sdk_AppId);
        //String apiVersion = getResources().getString(R.string.api_version);
        //VKSdk.customInitialize(this,appId,apiVersion);
    }

    synchronized public Tracker getDefaultTracker() {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            // Чтобы включить ведение журнала отладки, используйте adb shell setprop log.tag.GAv4 DEBUG
            mTracker = analytics.newTracker(R.xml.global_tracker);
        }
        return mTracker;
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
