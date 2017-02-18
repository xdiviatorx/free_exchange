package com.technologies.mobile.free_exchange.adapters;

import android.content.Context;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.technologies.mobile.free_exchange.R;
import com.technologies.mobile.free_exchange.fragments.OffersListFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by diviator on 23.12.2016.
 */

public class CategoriesPagerAdapter extends FragmentPagerAdapter {
    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();

    private Context mContext;

    public CategoriesPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
        init();
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }

    private void init() {
        int[] ids = mContext.getResources().getIntArray(R.array.viewPagerCategoriesIds);
        String[] titles = mContext.getResources().getStringArray(R.array.viewPagerCategoriesNames);
        for (int i = 0; i < ids.length; i++) {
            addFragment(ids[i],titles[i]);
        }
    }


    private void addFragment(int category, String title) {
        Fragment fragment = OffersListFragment.getInstance(category);
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }

    private void addFragment(int category, @StringRes int title) {
        String tit = mContext.getString(title);
        addFragment(category, tit);
    }
}
