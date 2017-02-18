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
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.technologies.mobile.free_exchange.AppSingle;
import com.technologies.mobile.free_exchange.R;
import com.technologies.mobile.free_exchange.adapters.DialogListAdapter;
import com.technologies.mobile.free_exchange.rest.ExchangeClient;
import com.technologies.mobile.free_exchange.rest.RetrofitService;
import com.technologies.mobile.free_exchange.rest.model.Dialog;
import com.technologies.mobile.free_exchange.rest.model.DialogMessagesResponse;
import com.technologies.mobile.free_exchange.rest.model.ListDialogsResponse;
import com.vk.sdk.VKAccessToken;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by diviator on 24.08.2016.
 */
public class MessageFragment extends Fragment implements AbsListView.OnScrollListener, AdapterView.OnItemClickListener{

    public static final String LOG_TAG = "myLogsMessageFragment";

    private ListView lvDialogs;

    @Nullable
    private DialogListAdapter dialogListAdapter = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_message,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
    }

    private void initViews(View view){
        lvDialogs = (ListView) view.findViewById(R.id.lvDialogs);

        String[] from = {DialogListAdapter.INTERLOCUTOR_NAME,DialogListAdapter.LAST_MESSAGE};
        int[] to = {R.id.tvName,R.id.tvLastMessage};
        dialogListAdapter = new DialogListAdapter(getContext(),new ArrayList<HashMap<String, Object>>(),R.layout.item_dialog,from,to);

        lvDialogs.setAdapter(dialogListAdapter);

        lvDialogs.setOnScrollListener(this);
        lvDialogs.setOnItemClickListener(this);

        dialogListAdapter.initialUploading();
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {

    }

    @Override
    public void onScroll(AbsListView absListView, int i, int i1, int i2) {
        if( i >= Math.max(i2-10,10) ) {
            dialogListAdapter.additionalUploading(i2);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        String interlocutorId = (String) dialogListAdapter.getData().get(position).get(DialogListAdapter.INTERLOCUTOR_ID);
        String dialogId = String.valueOf(dialogListAdapter.getData().get(position).get(DialogListAdapter.DIALOG_ID));
        String interlocutorName = (String) dialogListAdapter.getData().get(position).get(DialogListAdapter.INTERLOCUTOR_NAME);
        String interlocutorVkId = (String) dialogListAdapter.getData().get(position).get(DialogListAdapter.INTERLOCUTOR_VK_ID);

        if( interlocutorName == null ){
            interlocutorName=getString(R.string.dialog);
            if( dialogId != null ){
                interlocutorName+= " №" + dialogId;
            }
        }

        DialogFragment dialogFragment = new DialogFragment();
        Bundle args = new Bundle();
        args.putString(DialogFragment.INTERLOCUTOR_NAME,interlocutorName);
        args.putString(DialogFragment.INTERLOCUTOR_ID,interlocutorId);
        args.putString(DialogFragment.DIALOG_ID,dialogId);
        args.putString(DialogFragment.INTERLOCUTOR_VK_ID,interlocutorVkId);
        dialogFragment.setArguments(args);

        AppSingle.getInstance().setCurrFragmentIndex(FragmentAdapter.DIALOG,true);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.content,dialogFragment,DialogFragment.TAG).addToBackStack(null).commit();
    }
}
