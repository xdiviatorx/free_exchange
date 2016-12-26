package com.technologies.mobile.free_exchange.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.technologies.mobile.free_exchange.R;
import com.technologies.mobile.free_exchange.activities.SearchActivity;

/**
 * Created by diviator on 25.08.2016.
 */
public class FragmentAdapter {

    public static final int MESSAGE = 3;
    public static final int SUBSCRIBES = 4;
    public static final int DIALOG = 5;

    private Activity activity;
    private FragmentManager fragmentManager;

    private int currentFragmentIndex = -1;

    private final Fragment home = new HomeFragment();
    private final Fragment add = new AddFragment();
    private final Fragment message = new MessageFragment();
    private final Fragment subscribes = new SubscribesFragment();
    private final Fragment dialog = new DialogFragment();

    public FragmentAdapter(Activity activity) {
        this.activity = activity;
        this.fragmentManager = ((AppCompatActivity) activity).getSupportFragmentManager();
    }

    public void initDefaultFragment() {
        initFragment(0);
    }

    public void initFragment(int index) {
        Fragment fragment;
        boolean backStack = false;
        switch (index) {
            case 0: {
                if (currentFragmentIndex == 0) {
                    return;
                }
                fragment = home;
                fragment.setRetainInstance(true);
                currentFragmentIndex = 0;
                break;
            }
            case 1: {
                //fragment = new SearchFragment();
                Intent intent = new Intent(activity, SearchActivity.class);
                activity.startActivity(intent);
                return;
            }
            case 2: {
                fragment = add;
                currentFragmentIndex = 2;
                break;
            }
            case MESSAGE: {
                fragment = message;
                currentFragmentIndex = MESSAGE;
                break;
            }
            case SUBSCRIBES: {
                fragment = subscribes;
                currentFragmentIndex = SUBSCRIBES;
                break;
            }
            case DIALOG: {
                fragment = dialog;
                backStack = true;
                currentFragmentIndex = DIALOG;
                break;
            }
            default: {
                fragment = home;
                currentFragmentIndex = 0;
            }
        }

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (!backStack) {
            fragmentManager.popBackStack();
            fragmentManager.popBackStack();
            fragmentManager.popBackStack();
            fragmentManager.popBackStack();
            fragmentManager.popBackStack();
            transaction.replace(R.id.content, fragment).commit();
        } else {
            transaction.add(R.id.content, fragment).addToBackStack(null).commit();
        }
        String[] titles = activity.getResources().getStringArray(R.array.fragments);
        activity.setTitle(titles[index]);
    }

    public int getCurrentFragmentIndex() {
        return currentFragmentIndex;
    }
}
