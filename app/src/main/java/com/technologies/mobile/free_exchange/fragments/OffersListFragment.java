package com.technologies.mobile.free_exchange.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.technologies.mobile.free_exchange.AppSingle;
import com.technologies.mobile.free_exchange.R;
import com.technologies.mobile.free_exchange.activities.DialogActivity;
import com.technologies.mobile.free_exchange.activities.ExchangeMoreActivity;
import com.technologies.mobile.free_exchange.adapters.SearchPullAdapter;
import com.technologies.mobile.free_exchange.listeners.OnIconClickListener;
import com.technologies.mobile.free_exchange.listeners.OnSearchBeginListener;
import com.technologies.mobile.free_exchange.listeners.OnSearchPerformListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by diviator on 23.12.2016.
 */

public class OffersListFragment extends Fragment implements
        AdapterView.OnItemClickListener, OnIconClickListener, AbsListView.OnScrollListener,
        OnSearchPerformListener, SwipeRefreshLayout.OnRefreshListener, OnSearchBeginListener {

    public static final String CATEGORY = "CATEGORY";

    int mCategory = 0;

    private SwipeRefreshLayout srl;
    private ListView lv;
    private SearchPullAdapter lvAdapter;

    @Nullable
    private View view;

    public OffersListFragment() {
    }

    public static Fragment getInstance(int category) {
        Fragment fragment = new OffersListFragment();
        Bundle args = new Bundle();
        args.putInt(CATEGORY, category);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCategory = getArguments().getInt(CATEGORY, 0);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list_offers, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
    }

    private void initViews(View view) {
        this.view = view;

        lv = (ListView) view.findViewById(R.id.lv);

        String[] from = {SearchPullAdapter.GIVE, SearchPullAdapter.GET, SearchPullAdapter.PLACE, SearchPullAdapter.CONTACTS, SearchPullAdapter.DATE};
        int[] to = {R.id.gives, R.id.gets, R.id.place, R.id.contacts, R.id.date};
        ArrayList<HashMap<String, Object>> data = new ArrayList<>();

        lvAdapter = new SearchPullAdapter(getContext(), data, R.layout.exchange_item, from, to);
        lvAdapter.setOnSearchPerformListener(this);
        lvAdapter.setOnSearchBeginListener(this);

        lv.setAdapter(lvAdapter);

        lv.setOnItemClickListener(this);
        lv.setOnScrollListener(this);

        lvAdapter.setUploadingParams(mCategory);
        lvAdapter.initialUploading();
        lvAdapter.setOnIconClickListener(this);

        srl = (SwipeRefreshLayout) view.findViewById(R.id.srl);
        srl.setOnRefreshListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(getContext(), ExchangeMoreActivity.class);
        intent.putExtra(ExchangeMoreActivity.EXCHANGE, lvAdapter.getItem(i));
        startActivity(intent);
    }

    @Override
    public void onIconClick(View view, int i) {
        switch (view.getId()) {
            case R.id.ivMessage: {
                /*Fragment dialogFragment = new DialogFragment();
                Bundle args = new Bundle();

                int uid = (int) lvAdapter.getData().get(i).get(SearchPullAdapter.UID);
                String authorName = (String) lvAdapter.getData().get(i).get(SearchPullAdapter.AUTHOR_NAME);
                String authorVkId = (String) lvAdapter.getData().get(i).get(SearchPullAdapter.VK_ID);

                args.putString(DialogFragment.INTERLOCUTOR_ID,String.valueOf(uid));
                args.putString(DialogFragment.INTERLOCUTOR_NAME,authorName);
                args.putString(DialogFragment.INTERLOCUTOR_VK_ID,authorVkId);
                dialogFragment.setArguments(args);


                FragmentManager fragmentManager = getChildFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(dialogFragment,DialogFragment.TAG).addToBackStack(null).commit();*/
                int uid = (int) lvAdapter.getData().get(i).get(SearchPullAdapter.UID);
                String authorName = (String) lvAdapter.getData().get(i).get(SearchPullAdapter.AUTHOR_NAME);
                String authorVkId = (String) lvAdapter.getData().get(i).get(SearchPullAdapter.VK_ID);

                Intent intent = new Intent(getContext(), DialogActivity.class);
                intent.putExtra(DialogFragment.INTERLOCUTOR_ID, String.valueOf(uid));
                intent.putExtra(DialogFragment.INTERLOCUTOR_NAME, authorName);
                intent.putExtra(DialogFragment.INTERLOCUTOR_VK_ID, authorVkId);

                startActivity(intent);
                break;
            }
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {

    }

    @Override
    public void onScroll(AbsListView absListView, int i, int i1, int i2) {
        if (i >= Math.max(i2 - 10, 10)) {
            lvAdapter.additionalUploading(i2);
        }
    }

    @Override
    public void onSearchPerformed(int count) {
        if (view != null && view.findViewById(R.id.pbOfferList) != null) {
            view.findViewById(R.id.pbOfferList).setVisibility(View.GONE);
        }
        if (count == 0 && view != null && view.findViewById(R.id.itemTryAgain) != null) {
            view.findViewById(R.id.itemTryAgain).setVisibility(View.VISIBLE);
        }
        if (srl != null) {
            srl.setRefreshing(false);
        }
    }

    @Override
    public void onRefresh() {
        lvAdapter.initialUploading();
    }

    @Override
    public void onSearchBegun() {
        if (view != null && view.findViewById(R.id.itemTryAgain) != null) {
            view.findViewById(R.id.itemTryAgain).setVisibility(View.GONE);
        }
    }
}
