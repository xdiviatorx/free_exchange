package com.technologies.mobile.free_exchange.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.technologies.mobile.free_exchange.AppSingle;
import com.technologies.mobile.free_exchange.R;
import com.technologies.mobile.free_exchange.activities.SearchActivity;

/**
 * Created by diviator on 25.08.2016.
 */
public class FragmentAdapter {

    public static final int HOME = 0;
    public static final int ADD = 2;
    public static final int MESSAGE = 3;
    public static final int SUBSCRIBES = 4;
    public static final int DIALOG = 5;
    public static final int CREATE_EDIT_SUBSCRIBE = 6; // in SubscribesFragment
    public static final int OFFERS_IN_SUBSCRIBE = 7; // in SubscribesFragment

    private Activity activity;
    private FragmentManager fragmentManager;

    private Fragment home;
    private Fragment add;
    private Fragment message;
    private Fragment subscribes;
    private Fragment dialog;

    public FragmentAdapter(Activity activity) {
        this.activity = activity;
        this.fragmentManager = ((AppCompatActivity) activity).getSupportFragmentManager();

        home = new HomeFragment();
        add = new AddFragment();
        message = new MessageFragment();
        subscribes = new SubscribesFragment();
        dialog = new DialogFragment();
    }

    public void initDefaultFragment() {
        initFragment(HOME);
    }

    public void initFragment(int index) {
        Fragment fragment;
        boolean backStack = false;
        switch (index) {
            case HOME: {
                fragment = home;
                setCurrentFragmentIndex(HOME, backStack);
                break;
            }
            case 1: {
                //fragment = new SearchFragment();
                Intent intent = new Intent(activity, SearchActivity.class);
                activity.startActivity(intent);
                return;
            }
            case ADD: {
                fragment = add;
                setCurrentFragmentIndex(ADD, backStack);
                break;
            }
            case MESSAGE: {
                fragment = message;
                setCurrentFragmentIndex(MESSAGE, backStack);
                break;
            }
            case SUBSCRIBES: {
                fragment = subscribes;
                setCurrentFragmentIndex(SUBSCRIBES, backStack);
                break;
            }
            case DIALOG: {
                fragment = dialog;
                backStack = true;
                setCurrentFragmentIndex(DIALOG, backStack);
                break;
            }
            default: {
                fragment = home;
                setCurrentFragmentIndex(HOME, backStack);
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

    public void setCurrentFragmentIndex(int index, boolean backStack){
        AppSingle.getInstance().setCurrFragmentIndex(index, backStack);
    }

    public int getCurrentFragmentIndex() {
        return AppSingle.getInstance().getCurrFragmentIndex();
    }
}
