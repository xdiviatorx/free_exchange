package com.technologies.mobile.free_exchange.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.technologies.mobile.free_exchange.R;
import com.technologies.mobile.free_exchange.activities.ExchangeMoreActivity;
import com.technologies.mobile.free_exchange.activities.MainActivity;
import com.technologies.mobile.free_exchange.adapters.CategoriesPagerAdapter;
import com.technologies.mobile.free_exchange.adapters.SearchPullAdapter;
import com.technologies.mobile.free_exchange.listeners.OnIconClickListener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by diviator on 24.08.2016.
 */
public class HomeFragment extends Fragment implements AbsListView.OnScrollListener{

    public static String LOG_TAG = "logs";

    private ImageButton floatingAdd;
    boolean isFABVisible = false;

    private int lastFirstVisibleItem = 0;

    TabLayout tabLayout;
    ViewPager viewPager;
    PagerAdapter pagerAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initFloating(view);
        initViews(view);
    }

    private void initViews(View view){
        tabLayout = (TabLayout) view.findViewById(R.id.tabs);

        viewPager = (ViewPager) view.findViewById(R.id.vpLists);
        viewPager.setOffscreenPageLimit(getResources().getIntArray(R.array.categoriesIds).length);

        pagerAdapter = new CategoriesPagerAdapter(getChildFragmentManager(),getContext());

        viewPager.setAdapter(pagerAdapter);

        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {
        if( lastFirstVisibleItem < absListView.getFirstVisiblePosition() ){
            floatingHide();
        }else if( lastFirstVisibleItem > absListView.getFirstVisiblePosition() ){
            floatingShow();
        }
        lastFirstVisibleItem = absListView.getFirstVisiblePosition();
    }

    @Override
    public void onScroll(AbsListView absListView, int i, int i1, int i2) {

    }



    private void initFloating(View view){
        floatingAdd = (ImageButton) view.findViewById(R.id.floating_add);
        floatingAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).getFragmentAdapter().initFragment(2);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        floatingShow();
    }

    private void floatingShow(){
        Animation anim = AnimationUtils.loadAnimation(getContext(),R.anim.move_up);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                floatingAdd.setVisibility(View.VISIBLE);
                isFABVisible = true;
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        if( !isFABVisible ) {
            floatingAdd.startAnimation(anim);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        floatingHide();
    }

    private void floatingHide(){
        Animation anim = AnimationUtils.loadAnimation(getContext(),R.anim.move_down);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                floatingAdd.setVisibility(View.INVISIBLE);
                isFABVisible = false;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        if( isFABVisible ) {
            floatingAdd.startAnimation(anim);
        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        viewPager.destroyDrawingCache();
    }
}
