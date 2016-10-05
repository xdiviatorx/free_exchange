package com.technologies.mobile.free_exchange.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.technologies.mobile.free_exchange.R;
import com.technologies.mobile.free_exchange.adapters.ActivityActions;
import com.technologies.mobile.free_exchange.adapters.SubscribesAdapter;
import com.technologies.mobile.free_exchange.rest.ExchangeClient;
import com.technologies.mobile.free_exchange.rest.RetrofitService;
import com.technologies.mobile.free_exchange.rest.model.DeleteSubscribeListResponse;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by diviator on 04.10.2016.
 */

public class SubscribesFragment extends Fragment implements AbsListView.OnScrollListener, ActivityActions{

    public static final String LOG_TAG = "subscribesFragment";

    ListView lvSubscribers;
    SubscribesAdapter subscribesAdapter;

    ImageButton floatingAdd;

    public SubscribesFragment(){

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_subscribes,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        initFloating(view);
    }

    public void initViews(View view){
        lvSubscribers = (ListView) view.findViewById(R.id.lvSubscribes);

        ArrayList<HashMap<String,Object>> data = new ArrayList<>();
        String[] from = {SubscribesAdapter.ITEMS_GET,SubscribesAdapter.ITEMS_GIVE};
        int[] to = {R.id.gets,R.id.gives};
        subscribesAdapter = new SubscribesAdapter(getContext(),data,R.layout.subscribe_item,from,to);

        lvSubscribers.setAdapter(subscribesAdapter);
        subscribesAdapter.initialDownload();
        lvSubscribers.setOnScrollListener(this);

        subscribesAdapter.setActivityAction(this);
    }

    private void initFloating(View view){
        floatingAdd = (ImageButton) view.findViewById(R.id.floating_add);
        floatingAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment createSubscribeFragment = new CreateSubscribeFragment();

                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction
                        .add(R.id.content,createSubscribeFragment,CreateSubscribeFragment.TAG)
                        .addToBackStack(null).commit();
            }
        });
    }
    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {

    }

    @Override
    public void onScroll(AbsListView absListView, int i, int i1, int i2) {

        if( i >= Math.max(i2-10,10) ) {
            subscribesAdapter.additionalDownload(i2);
        }

    }

    @Override
    public void deleteSubscribe(int position, String listId) {
        subscribesAdapter.getData().remove(position);
        subscribesAdapter.notifyDataSetChanged();

        ExchangeClient client = RetrofitService.createService(ExchangeClient.class);
        client.deleteSubscribeList(listId,ExchangeClient.apiKey).enqueue(new Callback<DeleteSubscribeListResponse>() {
            @Override
            public void onResponse(Call<DeleteSubscribeListResponse> call, Response<DeleteSubscribeListResponse> response) {

            }

            @Override
            public void onFailure(Call<DeleteSubscribeListResponse> call, Throwable t) {
                Log.e(LOG_TAG,"DELETE ERROR " + t.toString());
                subscribesAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void editSubscribe(int position, String listId) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

        Fragment createSubscribeFragment = new CreateSubscribeFragment();
        Bundle args = new Bundle();

        args.putString(CreateSubscribeFragment.LIST_ID,listId);
        args.putInt(CreateSubscribeFragment.NOTIFICATION,
                (int)subscribesAdapter.getData().get(position).get(SubscribesAdapter.NOTIFICATION));

        JSONArray itemsGetJson = ((JSONArray)subscribesAdapter.getData().get(position).get(SubscribesAdapter.ITEMS_GET));
        ArrayList<String> itemsGet = new ArrayList<>();
        for( int i = 0; i < itemsGetJson.length(); i++ ){
            try {
                itemsGet.add(itemsGetJson.getString(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        args.putStringArrayList(CreateSubscribeFragment.ITEMS_GET,itemsGet);

        JSONArray itemsGiveJson = ((JSONArray)subscribesAdapter.getData().get(position).get(SubscribesAdapter.ITEMS_GIVE));
        ArrayList<String> itemsGive = new ArrayList<>();
        for( int i = 0; i < itemsGiveJson.length(); i++ ){
            try {
                itemsGive.add(itemsGiveJson.getString(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        args.putStringArrayList(CreateSubscribeFragment.ITEMS_GIVE,itemsGive);

        createSubscribeFragment.setArguments(args);

        fragmentManager.beginTransaction().add(R.id.content,createSubscribeFragment,CreateSubscribeFragment.TAG)
                .addToBackStack(null).commit();
    }
}