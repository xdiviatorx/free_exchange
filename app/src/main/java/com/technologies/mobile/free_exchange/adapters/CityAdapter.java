package com.technologies.mobile.free_exchange.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;

import com.technologies.mobile.free_exchange.R;
import com.technologies.mobile.free_exchange.rest.model.City;

import java.util.List;

/**
 * Created by diviator on 19.02.2017.
 */

public class CityAdapter extends BaseAdapter {

    private static final String TAG = "CityManager";

    Context mContext;
    List<City> mData;
    City mCurrentCity;

    public CityAdapter(Context context, List<City> data, City currentCity) {
        mContext = context;
        mData = data;
        mCurrentCity = currentCity;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public City getItem(int i) {
        return mData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return getItem(i).getId();
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(mContext).inflate(android.R.layout.simple_list_item_single_choice, viewGroup, false);
        CheckedTextView ctvText = (CheckedTextView) view.findViewById(android.R.id.text1);
        ctvText.setText(getItem(i).getName());
        if( getItemId(i) == mCurrentCity.getId() ){
            ctvText.setChecked(true);
        }
        ctvText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentCity = getItem(i);
                Log.e(TAG, "onClick: mCurCity = " + mCurrentCity.getName() );
                notifyDataSetChanged();
            }
        });
        return view;
    }

    public City getCurrentCity(){
        return mCurrentCity;
    }
}
