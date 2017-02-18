package com.technologies.mobile.free_exchange.activities;

import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.technologies.mobile.free_exchange.R;
import com.technologies.mobile.free_exchange.adapters.EmpathyPagerAdapter;

import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;

public class EmpathyActivity extends AppCompatActivity{

    public static final int EMPATHY_INTERVAL = 3000; // milliseconds

    AutoScrollViewPager asvpPager;
    PagerAdapter pAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empathy);
        initViews();
    }

    protected void initViews(){
        asvpPager = (AutoScrollViewPager) findViewById(R.id.asvpPager);
        pAdapter = new EmpathyPagerAdapter(getSupportFragmentManager());
        asvpPager.setAdapter(pAdapter);

        asvpPager.setInterval(EMPATHY_INTERVAL);
        asvpPager.startAutoScroll(EMPATHY_INTERVAL);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        moveTaskToBack(true);
    }
}
