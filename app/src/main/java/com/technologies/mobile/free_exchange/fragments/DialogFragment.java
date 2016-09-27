package com.technologies.mobile.free_exchange.fragments;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.technologies.mobile.free_exchange.R;
import com.technologies.mobile.free_exchange.activities.LoginActivity;
import com.technologies.mobile.free_exchange.adapters.ChatMessage;
import com.technologies.mobile.free_exchange.adapters.MessageListAdapter;
import com.technologies.mobile.free_exchange.logic.TextFormatter;
import com.technologies.mobile.free_exchange.rest.ExchangeClient;
import com.technologies.mobile.free_exchange.rest.RetrofitService;
import com.technologies.mobile.free_exchange.rest.model.AddMessageResponse;
import com.technologies.mobile.free_exchange.rest.model.DialogMessage;
import com.technologies.mobile.free_exchange.rest.model.DialogMessagesResponse;
import com.technologies.mobile.free_exchange.rest.model.ListDialogsResponse;

import java.security.spec.ECField;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by diviator on 22.09.2016.
 */
public class DialogFragment extends Fragment implements View.OnClickListener, AbsListView.OnScrollListener{

    public static final String LOG_TAG = "DialogFragment";

    public static final String INTERLOCUTOR_ID = "INTERLOCUTOR_UID";
    public static final String DIALOG_ID = "DIALOG_ID";

    public static final String TAG = "dialogTag";

    private String lastTitle = null;

    private ImageButton bSendMessage;
    private EditText etMessage;

    private ListView lvMessages;
    private MessageListAdapter messageListAdapter;


    @Nullable private String interlocutorId;
    @Nullable private String dialogId;

    public DialogFragment(){

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        interlocutorId = getArguments().getString(INTERLOCUTOR_ID,null);
        dialogId = getArguments().getString(DIALOG_ID,null);

        if( dialogId == null ){
            getDialogId();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dialog,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setTitle();
        initViews(view);
    }

    private void initViews(View view){
        etMessage = (EditText) view.findViewById(R.id.etMessage);

        bSendMessage = (ImageButton) view.findViewById(R.id.bSendMessage);
        bSendMessage.setOnClickListener(this);

        lvMessages = (ListView) view.findViewById(R.id.lvMessages);
        lvMessages.setTranscriptMode(ListView.TRANSCRIPT_MODE_NORMAL);
        lvMessages.setStackFromBottom(true);

        if( dialogId != null ) {
            initMessageList();
        }else{
            initDefaultMessageList();
        }
    }

    private void initMessageList(){
        messageListAdapter = new MessageListAdapter(getContext(),R.layout.item_left_message,dialogId, lvMessages);

        lvMessages.setAdapter(messageListAdapter);

        messageListAdapter.initialUploading();

        lvMessages.setOnScrollListener(this);
    }

    private void initDefaultMessageList(){
        messageListAdapter = new MessageListAdapter(getContext(),R.layout.item_left_message, lvMessages);

        lvMessages.setAdapter(messageListAdapter);
    }

    @Override
    public void onClick(View view) {
        String message = etMessage.getText().toString();
        if( !message.isEmpty() ){
            String uid = PreferenceManager.getDefaultSharedPreferences(getContext()).getString(LoginActivity.ID,null);
            if( uid == null ){
                return;
            }

            ChatMessage chatMessage = new ChatMessage(message);
            messageListAdapter.addMessageToList(chatMessage);

            addMessage();
        }
    }

    public void addMessage(){
        ExchangeClient client = RetrofitService.createService(ExchangeClient.class);

        String uid = PreferenceManager.getDefaultSharedPreferences(getContext()).getString(LoginActivity.ID,null);
        if( uid == null || interlocutorId == null){
            return;
        }

        Call<AddMessageResponse> addMessageResponseCall = client.addMessage(interlocutorId,uid,etMessage.getText().toString(),ExchangeClient.apiKey);
        addMessageResponseCall.enqueue(new Callback<AddMessageResponse>() {
            @Override
            public void onResponse(Call<AddMessageResponse> call, Response<AddMessageResponse> response) {
                if( dialogId == null ){
                    dialogId = response.body().getDialogId();
                    initMessageList();
                }
            }

            @Override
            public void onFailure(Call<AddMessageResponse> call, Throwable t) {

            }
        });
    }

    private void setTitle(){
        lastTitle = getActivity().getTitle().toString();
        if( interlocutorId != null ) {
            getActivity().setTitle(interlocutorId);
        }
    }

    public void setLastTitle(){
        if( lastTitle != null ){
            getActivity().setTitle(lastTitle);
        }
    }

    public void getDialogId(){
        ExchangeClient client = RetrofitService.createService(ExchangeClient.class);

        String uid = PreferenceManager.getDefaultSharedPreferences(getContext()).getString(LoginActivity.ID,null);

        if( uid != null ) {
            Call<ListDialogsResponse> listDialogsResponseCall = client.getDialogs(uid, interlocutorId, 0, 30, ExchangeClient.apiKey);
            listDialogsResponseCall.enqueue(new Callback<ListDialogsResponse>() {
                @Override
                public void onResponse(Call<ListDialogsResponse> call, Response<ListDialogsResponse> response) {
                    if( response.body().getListDialogs().getDialogs().length != 0 ) {
                        dialogId = String.valueOf(response.body().getListDialogs().getDialogs()[0].getDialog_id());
                        initMessageList();
                    }
                }

                @Override
                public void onFailure(Call<ListDialogsResponse> call, Throwable t) {

                }
            });
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {

    }

    @Override
    public void onScroll(AbsListView absListView, int i, int i1, int i2) {
        int fixPosition = i2-i-1;
        if( i != 0 && fixPosition >= i2-5 ){
            messageListAdapter.additionalUploading(i2);
        }
    }
}
