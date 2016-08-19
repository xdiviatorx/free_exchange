package com.technologies.mobile.free_exchange.activities;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.technologies.mobile.free_exchange.R;
import com.technologies.mobile.free_exchange.adapters.SearchPullAdapter;
import com.technologies.mobile.free_exchange.fragments.ImageFragment;

public class ImagePreviewActivity extends AppCompatActivity {

    public static String LOG_TAG = "logs";

    ViewPager viewPager;
    PagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_preview);

        Intent data = getIntent();
        String[] imageUrls = data.getStringArrayExtra(SearchPullAdapter.IMAGES);
        int count = imageUrls.length;
        count = Math.max(1,count);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        pagerAdapter = new ImagePagerAdapter(getSupportFragmentManager(),count,imageUrls);
        viewPager.setAdapter(pagerAdapter);
    }

    private class ImagePagerAdapter extends FragmentPagerAdapter {

        private int pageCount;
        private String[] imageUrls;

        public ImagePagerAdapter(FragmentManager fm, int pageCount, String[] imageUrls) {
            super(fm);
            this.pageCount = pageCount;
            this.imageUrls = imageUrls;
            Log.e(LOG_TAG,"=====");
            Log.e(LOG_TAG,pageCount+"");
            Log.e(LOG_TAG,imageUrls.length+"");
        }

        @Override
        public Fragment getItem(int position) {
            String imageUrl = "";
            if( imageUrls.length > position ) {
                imageUrl = imageUrls[position];
            }
            return ImageFragment.getInstance(imageUrl);
        }

        @Override
        public int getCount() {
            return pageCount;
        }

    }
}
