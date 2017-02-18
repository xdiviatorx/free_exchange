package com.technologies.mobile.free_exchange.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.technologies.mobile.free_exchange.AppSingle;
import com.technologies.mobile.free_exchange.MyApplication;
import com.technologies.mobile.free_exchange.R;
import com.technologies.mobile.free_exchange.adapters.NavigationRVAdapter;
import com.technologies.mobile.free_exchange.fragments.CreateSubscribeFragment;
import com.technologies.mobile.free_exchange.fragments.DialogFragment;
import com.technologies.mobile.free_exchange.fragments.FragmentAdapter;
import com.technologies.mobile.free_exchange.fragments.SubscribeExchangesFragment;
import com.technologies.mobile.free_exchange.listeners.RecyclerViewOnItemClickListener;
import com.technologies.mobile.free_exchange.logic.BitmapUtil;
import com.technologies.mobile.free_exchange.services.messages.MessageCatcherService;
import com.technologies.mobile.free_exchange.services.subscribes.SubscribeExchangeCatcherService;
import com.vk.sdk.VKSdk;

public class MainActivity extends AppCompatActivity {

    public static String LOG_TAG = "instance";

    private static final String STORED_FRAGMENT_INDEX = "STORED_FRAGMENT_INDEX";

    public static int LOGIN_REQUEST = 100;

    private int[] icons = {R.drawable.home, R.drawable.search_dark, R.drawable.plus_dark,
            R.drawable.message_dark, R.drawable.subscribe, R.drawable.exit};

    private Toolbar toolbar;

    private String photo = "";
    private String name = "";

    private DrawerLayout drawerLayout;
    private RecyclerView recyclerView;
    private NavigationRVAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private ActionBarDrawerToggle drawerToggle;

    private FragmentAdapter fragmentAdapter;

    private Tracker mTracker;

    Menu menu;
    Target target;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTracker = ((MyApplication) getApplication()).getDefaultTracker();
        mTracker.setScreenName(MyApplication.MAIN_CATEGORY);
        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory(MyApplication.MAIN_CATEGORY)
                .setAction(MyApplication.LAUNCHED_ACTION).build());

        fragmentAdapter = new FragmentAdapter(this);

        login();

        initToolbar();

        initNavigation();

        fragmentAdapter.initDefaultFragment();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        fragmentAdapter.initFragment(savedInstanceState.getInt(STORED_FRAGMENT_INDEX, 0));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.e(LOG_TAG, "SAVING");
        outState.putInt(STORED_FRAGMENT_INDEX, fragmentAdapter.getCurrentFragmentIndex());
        super.onSaveInstanceState(outState);
    }

    private void login() {
        if (!VKSdk.isLoggedIn()) {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivityForResult(intent, LOGIN_REQUEST);
        } else {
            Log.e(LOG_TAG, "service started");

            Intent messageCatcherServiceStarter = new Intent(this, MessageCatcherService.class);
            stopService(messageCatcherServiceStarter);
            startService(messageCatcherServiceStarter);

            Intent subscribeExchangeCatcherService = new Intent(this, SubscribeExchangeCatcherService.class);
            stopService(subscribeExchangeCatcherService);
            startService(subscribeExchangeCatcherService);
        }
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setActionBar(toolbar);
        setSupportActionBar(toolbar);
    }

    private void initNavigation() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        recyclerView = (RecyclerView) findViewById(R.id.left_drawer);
        recyclerView.setHasFixedSize(true);

        final String[] categories = getResources().getStringArray(R.array.navigations);

        adapter = new NavigationRVAdapter(this, categories, icons, photo, name);
        recyclerView.setAdapter(adapter);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        String name = PreferenceManager.getDefaultSharedPreferences(this).getString(LoginActivity.NAME, null);
        String photoUrl = PreferenceManager.getDefaultSharedPreferences(this).getString(LoginActivity.PHOTO, null);
        Log.e(LOG_TAG, "BEGIN PHOTO AND NAME");
        if (name != null && photoUrl != null) {
            Log.e(LOG_TAG, "PHOTO AND NAME " + name + " " + photoUrl);
            adapter.setPersonalData(name, photoUrl);
            adapter.notifyDataSetChanged();
        }

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        drawerToggle.setDrawerIndicatorEnabled(false);
        drawerToggle.syncState();

        /*VKRequest vkRequest = VKApi.users().get(VKParameters.from(VKApiConst.FIELDS,"photo_200"));
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
        });*/

        recyclerView.addOnItemTouchListener(new RecyclerViewOnItemClickListener(this, new RecyclerViewOnItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (position == categories.length) {
                    VKSdk.logout();
                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().remove(LoginActivity.ID).apply();
                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().remove(LoginActivity.NAME).apply();
                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().remove(LoginActivity.PHOTO).apply();
                    login();
                    drawerLayout.closeDrawers();
                } else if (position != 0) {
                    fragmentAdapter.initFragment(position - 1);
                    drawerLayout.closeDrawers();
                }
            }
        }));
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOGIN_REQUEST) {
            if (resultCode == RESULT_OK) {
                Log.e(LOG_TAG, "service started");

                initNavigation();
                initMenuItemAvatar();

                Intent messageCatcherServiceStarter = new Intent(this, MessageCatcherService.class);
                stopService(messageCatcherServiceStarter);
                startService(messageCatcherServiceStarter);

                Intent subscribeExchangeCatcherService = new Intent(this, SubscribeExchangeCatcherService.class);
                stopService(subscribeExchangeCatcherService);
                startService(subscribeExchangeCatcherService);
            }
        }
    }

    protected void initMenuItemAvatar() {
        if (menu == null) {
            return;
        }

        target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                Bitmap nBitmap = BitmapUtil.getRoundedCornerBitmap(bitmap);
                BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), nBitmap);

                menu.findItem(R.id.action_avatar).setIcon(bitmapDrawable);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
            }
        };
        String photoUrl = PreferenceManager.getDefaultSharedPreferences(this).getString(LoginActivity.PHOTO, null);
        Picasso.with(this).load(photoUrl).into(target);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        this.menu = menu;

        initMenuItemAvatar();

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_search: {
                fragmentAdapter.initFragment(1);
                break;
            }
            /*case R.id.action_add: {
                fragmentAdapter.initFragment(2);
                break;
            }
            case R.id.action_messages: {
                fragmentAdapter.initFragment(3);
                break;
            }*/
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean doubleBackPressed = false;

    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        int currFragment = AppSingle.getInstance().getCurrFragmentIndex();
        if (currFragment == FragmentAdapter.HOME
                || currFragment == FragmentAdapter.ADD
                || currFragment == FragmentAdapter.MESSAGE
                || currFragment == FragmentAdapter.SUBSCRIBES) {
            if (doubleBackPressed) {
                AppSingle.getInstance().setCurrFragmentIndex(-1,false);
                super.onBackPressed();
            } else {
                doubleBackPressed = true;
                Toast.makeText(this, R.string.press_back_twice_for_exit, Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        doubleBackPressed = false;
                    }
                }, 2000);
            }

        } else {
            CreateSubscribeFragment createSubscribeFragment = (CreateSubscribeFragment) fm.findFragmentByTag(CreateSubscribeFragment.TAG);
            if (createSubscribeFragment != null && createSubscribeFragment.isVisible()) {
                createSubscribeFragment.setLastTitle();
            }

            SubscribeExchangesFragment subscribeExchangesFragment =
                    (SubscribeExchangesFragment) fm.findFragmentByTag(SubscribeExchangesFragment.TAG);
            if (subscribeExchangesFragment != null && subscribeExchangesFragment.isVisible()) {
                subscribeExchangesFragment.setLastTitle();
            }

            DialogFragment dialogFragment = (DialogFragment) fm.findFragmentByTag(DialogFragment.TAG);
            if (dialogFragment != null && dialogFragment.isVisible()) {
                dialogFragment.setLastTitle();
            }

            AppSingle.getInstance().popBackStack();

            super.onBackPressed();
        }
    }

    public FragmentAdapter getFragmentAdapter() {
        return fragmentAdapter;
    }
}
