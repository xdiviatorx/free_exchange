package com.technologies.mobile.free_exchange.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.technologies.mobile.free_exchange.R;
import com.technologies.mobile.free_exchange.rest.model.City;

/**
 * Created by diviator on 19.02.2017.
 */

public class CityManager {

    private static final String TAG = "CityManager";

    public static final String CITY_NAME = "CITY_NAME";
    public static final String CITY_ID = "CITY_ID";

    private CityManager(){}

    private static City mCurCity = null;

    private static City getGlobalCity(Context context) {
        City city = new City();
        city.setName(PreferenceManager.getDefaultSharedPreferences(context).getString(CITY_NAME, getDefaultName(context)));
        city.setId(PreferenceManager.getDefaultSharedPreferences(context).getInt(CITY_ID, getDefaultId(context)));
        return city;
    }

    public static void build(Context context){
        mCurCity = getGlobalCity(context);
    }

    public static City getGlobalCity(){
        return mCurCity;
    }

    public static void setGlobalCity(Context context, City city) {
        Log.e(TAG, "setGlobalCity: City.Name = " + city.getName());
        Log.e(TAG, "setGlobalCity: City.Id = " + city.getId());
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString(CITY_NAME, city.getName());
        editor.putInt(CITY_ID, city.getId());
        editor.apply();
        mCurCity = city;
    }

    private static String getDefaultName(Context context) {
        return context.getString(R.string.default_city);
    }

    private static int getDefaultId(Context context) {
        return context.getResources().getInteger(R.integer.default_city);
    }
}
