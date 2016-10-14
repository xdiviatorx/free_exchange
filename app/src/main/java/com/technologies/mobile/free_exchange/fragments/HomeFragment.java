package com.technologies.mobile.free_exchange.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.technologies.mobile.free_exchange.R;
import com.technologies.mobile.free_exchange.adapters.SearchPullAdapter;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by diviator on 24.08.2016.
 */
public class HomeFragment extends Fragment implements AbsListView.OnScrollListener, AdapterView.OnItemClickListener{

    public static String LOG_TAG = "logs";

    private ListView lv;
    private SearchPullAdapter lvAdapter;
    private ArrayList<HashMap<String,Object>> data;

    private ImageButton floatingAdd;

    boolean isFABVisible = false;

    private FragmentAdapter fragmentAdapter;

    private int lastFirstVisibleItem = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentAdapter = new FragmentAdapter(getActivity());

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,container,false);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initFloating(view);
        initViews(view);
    }

    private void initViews(View view){
        lv = (ListView) view.findViewById(R.id.lv);

        String[] from = {SearchPullAdapter.GIVE,SearchPullAdapter.GET,SearchPullAdapter.PLACE,SearchPullAdapter.CONTACTS,SearchPullAdapter.DATE};
        int[] to = {R.id.gives,R.id.gets,R.id.place,R.id.contacts,R.id.date};
        ArrayList<HashMap<String,Object>> data = new ArrayList<>();

        lvAdapter = new SearchPullAdapter(getContext(),data,R.layout.exchange_item,from,to);

        lv.setAdapter(lvAdapter);

        lv.setOnScrollListener(this);
        lv.setOnItemClickListener(this);

        lvAdapter.initialUploading();

    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {
        if( lastFirstVisibleItem < absListView.getFirstVisiblePosition() ){
            floatingHide();
        }else if( lastFirstVisibleItem > absListView.getFirstVisiblePosition() ){
            floatingShow();
        }
        lastFirstVisibleItem = absListView.getFirstVisiblePosition();
    }

    @Override
    public void onScroll(AbsListView absListView, int i, int i1, int i2) {

        if( i >= Math.max(i2-10,10) ) {
            lvAdapter.additionalUploading(i2);
        }

    }



    private void initFloating(View view){
        floatingAdd = (ImageButton) view.findViewById(R.id.floating_add);
        floatingAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentAdapter.initFragment(2);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        floatingShow();
    }

    private void floatingShow(){
        Animation anim = AnimationUtils.loadAnimation(getContext(),R.anim.move_up);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                floatingAdd.setVisibility(View.VISIBLE);
                isFABVisible = true;
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        if( !isFABVisible ) {
            floatingAdd.startAnimation(anim);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        floatingHide();
    }

    private void floatingHide(){
        Animation anim = AnimationUtils.loadAnimation(getContext(),R.anim.move_down);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                floatingAdd.setVisibility(View.INVISIBLE);
                isFABVisible = false;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        if( isFABVisible ) {
            floatingAdd.startAnimation(anim);
        }
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Fragment dialogFragment = new DialogFragment();
        Bundle args = new Bundle();

        int uid = (int) lvAdapter.getData().get(i).get(SearchPullAdapter.UID);
        String authorName = (String) lvAdapter.getData().get(i).get(SearchPullAdapter.AUTHOR_NAME);
        String authorVkId = (String) lvAdapter.getData().get(i).get(SearchPullAdapter.VK_ID);

        args.putString(DialogFragment.INTERLOCUTOR_ID,String.valueOf(uid));
        args.putString(DialogFragment.INTERLOCUTOR_NAME,authorName);
        args.putString(DialogFragment.INTERLOCUTOR_VK_ID,authorVkId);
        dialogFragment.setArguments(args);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.content,dialogFragment,DialogFragment.TAG).addToBackStack(null).commit();
    }
}
