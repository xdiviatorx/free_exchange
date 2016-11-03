package com.technologies.mobile.free_exchange.activities;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.technologies.mobile.free_exchange.R;
import com.technologies.mobile.free_exchange.fragments.SubscribeExchangesFragment;

public class SubscribeExchangeActivity extends AppCompatActivity {

    public static final String LIST_ID = "LIST_ID";

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscribe_exchange);

        initToolbar();

        String listId = getIntent().getStringExtra(LIST_ID);
        initFragment(listId);
    }

    private void initToolbar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_arrow);
    }

    public void initFragment(String listId){
        Fragment fragment = new SubscribeExchangesFragment();
        Bundle args = new Bundle();
        args.putString(SubscribeExchangesFragment.LIST_ID,listId);
        fragment.setArguments(args);

        getSupportFragmentManager().beginTransaction().replace(R.id.content,fragment).commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }

}
