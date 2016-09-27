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
public class FragmentAdapter{

    public static final int MESSAGE = 3;
    public static final int DIALOG = 4;

    private Activity activity;
    private FragmentManager fragmentManager;

    public FragmentAdapter(Activity activity){
        this.activity = activity;
        this.fragmentManager = ((AppCompatActivity)activity).getSupportFragmentManager();
    }

    public void initDefaultFragment(){
        initFragment(0);
    }

    public void initFragment(int index){
        Fragment fragment;
        boolean backStack = false;
        switch ( index ){
            case 0:{
                fragment = new HomeFragment();
                break;
            }
            case 1:{
                //fragment = new SearchFragment();
                Intent intent = new Intent(activity, SearchActivity.class);
                activity.startActivity(intent);
                return;
            }
            case 2:{
                fragment = new AddFragment();
                break;
            }
            case MESSAGE:{
                fragment = new MessageFragment();
                break;
            }
            case DIALOG:{
                fragment = new DialogFragment();
                backStack = true;
                break;
            }
            default:
                fragment = new HomeFragment();
        }

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if( !backStack) {
            fragmentManager.popBackStack();
            transaction.replace(R.id.content,fragment).commit();
        }else{
            transaction.add(R.id.content,fragment).addToBackStack(null).commit();
        }
        String[] titles = activity.getResources().getStringArray(R.array.fragments);
        activity.setTitle(titles[index]);
    }
}
