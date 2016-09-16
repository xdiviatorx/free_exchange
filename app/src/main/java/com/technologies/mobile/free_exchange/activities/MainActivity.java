package com.technologies.mobile.free_exchange.activities;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import com.technologies.mobile.free_exchange.Loader;
import com.technologies.mobile.free_exchange.R;
import com.technologies.mobile.free_exchange.adapters.NavigationRVAdapter;
import com.technologies.mobile.free_exchange.adapters.SearchPullAdapter;
import com.technologies.mobile.free_exchange.fragments.AddFragment;
import com.technologies.mobile.free_exchange.fragments.FragmentAdapter;
import com.technologies.mobile.free_exchange.fragments.HomeFragment;
import com.technologies.mobile.free_exchange.fragments.MessageFragment;
import com.technologies.mobile.free_exchange.fragments.SearchFragment;
import com.technologies.mobile.free_exchange.listeners.RecyclerViewOnItemClickListener;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiPhoto;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity  {

    public static String LOG_TAG = "logs";

    public static int LOGIN_REQUEST = 100;

    private int[] icons = {R.drawable.home,R.drawable.search_dark,
            R.drawable.plus_dark,R.drawable.message_dark,R.drawable.exit};

    private Toolbar toolbar;

    private String photo = "";
    private String name = "";

    private DrawerLayout drawerLayout;
    private RecyclerView recyclerView;
    private NavigationRVAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private ActionBarDrawerToggle drawerToggle;

    private FragmentAdapter fragmentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentAdapter = new FragmentAdapter(this);

        login();

        initToolbar();

        initNavigation();

        fragmentAdapter.initDefaultFragment();
    }



    private void login(){
        if( !VKSdk.isLoggedIn() ) {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivityForResult(intent, LOGIN_REQUEST);
        }
    }

    private void initToolbar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void initNavigation(){
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        recyclerView = (RecyclerView) findViewById(R.id.left_drawer);
        recyclerView.setHasFixedSize(true);

        String[] categories = getResources().getStringArray(R.array.navigations);

        adapter = new NavigationRVAdapter(this, categories,icons,photo,name);
        recyclerView.setAdapter(adapter);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        drawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open_drawer,R.string.close_drawer){
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        drawerToggle.syncState();

        VKRequest vkRequest = VKApi.users().get(VKParameters.from(VKApiConst.FIELDS,"photo_200"));
        vkRequest.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                //Log.e(LOG_TAG,response.json.toString());
                try {
                    JSONArray jsonArray = response.json.getJSONArray("response");
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    String name = jsonObject.getString("first_name");
                    name+= " " + jsonObject.getString("last_name");
                    String photoUrl = jsonObject.getString("photo_200");
                    //Log.e(LOG_TAG, "NAME = " + name);
                    //Log.e(LOG_TAG, "PHOTO = " + photoUrl);
                    adapter.setPersonalData(name,photoUrl);
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        recyclerView.addOnItemTouchListener(new RecyclerViewOnItemClickListener(this, new RecyclerViewOnItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if( position == 5 ){
                        VKSdk.logout();
                        login();
                        drawerLayout.closeDrawers();
                }else if( position != 0 ){
                    fragmentAdapter.initFragment(position-1);
                    drawerLayout.closeDrawers();
                }

            }
        }));
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if( requestCode == LOGIN_REQUEST ){
            if( resultCode == RESULT_OK ){
                //
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch ( item.getItemId() ){
            case R.id.action_search:{
                fragmentAdapter.initFragment(1);
                break;
            }
            case R.id.action_add:{
                fragmentAdapter.initFragment(2);
                break;
            }
            case R.id.action_messages:{
                fragmentAdapter.initFragment(3);
                break;
            }
        }

        return super.onOptionsItemSelected(item);
    }
}
