package com.technologies.mobile.free_exchange.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.technologies.mobile.free_exchange.R;
import com.technologies.mobile.free_exchange.adapters.SearchPullAdapter;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by diviator on 24.08.2016.
 */
public class HomeFragment extends Fragment implements AbsListView.OnScrollListener{

    public static String LOG_TAG = "logs";

    private ListView lv;
    private SearchPullAdapter lvAdapter;
    private ArrayList<HashMap<String,Object>> data;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,container,false);
        initViews(view);
        return view;
    }

    private void initViews(View view){
        lv = (ListView) view.findViewById(R.id.lv);

        String[] from = {SearchPullAdapter.GIVE,SearchPullAdapter.GET,SearchPullAdapter.PLACE,SearchPullAdapter.CONTACTS,SearchPullAdapter.DATE};
        int[] to = {R.id.gives,R.id.gets,R.id.place,R.id.contacts,R.id.date};
        ArrayList<HashMap<String,Object>> data = new ArrayList<>();

        lvAdapter = new SearchPullAdapter(getContext(),data,R.layout.exchange_item,from,to);

        lv.setAdapter(lvAdapter);

        lv.setOnScrollListener(this);

        lvAdapter.initialUploading();

    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {

    }

    @Override
    public void onScroll(AbsListView absListView, int i, int i1, int i2) {

        if( i >= Math.max(i2-10,10) ) {
            lvAdapter.additionalUploading(i2);
        }

    }
}
