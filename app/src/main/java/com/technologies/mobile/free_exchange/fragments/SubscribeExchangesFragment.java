package com.technologies.mobile.free_exchange.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.technologies.mobile.free_exchange.R;
import com.technologies.mobile.free_exchange.activities.ExchangeMoreActivity;
import com.technologies.mobile.free_exchange.adapters.SearchPullAdapter;
import com.technologies.mobile.free_exchange.adapters.SubscribePullAdapter;
import com.technologies.mobile.free_exchange.listeners.OnIconClickListener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by diviator on 07.10.2016.
 */

public class SubscribeExchangesFragment extends Fragment implements AdapterView.OnItemClickListener, AbsListView.OnScrollListener, OnIconClickListener{

    public static final String TAG = "subscribeExchangeTag";
    public static final String LIST_ID = "LIST_ID";

    ListView lvExchanges;
    SubscribePullAdapter spAdapter;

    String lastTitle;

    String listId;

    public SubscribeExchangesFragment(){

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listId = getArguments().getString(LIST_ID);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_subscribe_exchanges,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setTitle();
        initViews(view);
    }

    private void initViews(View view){
        lvExchanges = (ListView) view.findViewById(R.id.lv);

        String[] from = {SearchPullAdapter.GIVE,SearchPullAdapter.GET,SearchPullAdapter.PLACE,SearchPullAdapter.CONTACTS,SearchPullAdapter.DATE};
        int[] to = {R.id.gives,R.id.gets,R.id.place,R.id.contacts,R.id.date};
        ArrayList<HashMap<String,Object>> data = new ArrayList<>();

        spAdapter = new SubscribePullAdapter(getContext(),data,R.layout.exchange_item,from,to);

        lvExchanges.setAdapter(spAdapter);

        lvExchanges.setOnScrollListener(this); // NO FLOATING
        lvExchanges.setOnItemClickListener(this);

        spAdapter.setListId(listId);
        spAdapter.initialUploading();
        spAdapter.setOnIconClickListener(this);
    }

    private void setTitle() {
        lastTitle = getActivity().getTitle().toString();
        String newTitle = getString(R.string.subs_exchanges);
        getActivity().setTitle(newTitle);
    }

    public void setLastTitle() {
        if (lastTitle != null) {
            getActivity().setTitle(lastTitle);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(getContext(), ExchangeMoreActivity.class);
        intent.putExtra(ExchangeMoreActivity.EXCHANGE, spAdapter.getItem(i));
        startActivity(intent);
    }

    @Override
    public void onIconClick(View view, int i) {
        Fragment dialogFragment = new DialogFragment();
        Bundle args = new Bundle();

        int uid = (int) spAdapter.getData().get(i).get(SearchPullAdapter.UID);
        String authorName = (String) spAdapter.getData().get(i).get(SearchPullAdapter.AUTHOR_NAME);
        String authorVkId = (String) spAdapter.getData().get(i).get(SearchPullAdapter.VK_ID);

        args.putString(DialogFragment.INTERLOCUTOR_ID,String.valueOf(uid));
        args.putString(DialogFragment.INTERLOCUTOR_NAME,authorName);
        args.putString(DialogFragment.INTERLOCUTOR_VK_ID,authorVkId);
        dialogFragment.setArguments(args);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.content,dialogFragment,DialogFragment.TAG).addToBackStack(null).commit();
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {

    }

    @Override
    public void onScroll(AbsListView absListView, int i, int i1, int i2) {
        if( i >= Math.max(i2-10,10) ) {
            spAdapter.additionalUploading(i2);
        }
    }
}
