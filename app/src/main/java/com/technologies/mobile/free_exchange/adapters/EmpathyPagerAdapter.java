package com.technologies.mobile.free_exchange.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.technologies.mobile.free_exchange.fragments.EmpathyFragment;

/**
 * Created by diviator on 26.12.2016.
 */

public class EmpathyPagerAdapter extends FragmentPagerAdapter {
    public EmpathyPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Bundle args = new Bundle();
        args.putInt(EmpathyFragment.POSITION,position+1);
        return EmpathyFragment.getInstance(args);
    }

    @Override
    public int getCount() {
        return 4;
    }
}
