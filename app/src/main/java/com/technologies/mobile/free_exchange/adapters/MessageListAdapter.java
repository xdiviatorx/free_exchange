package com.technologies.mobile.free_exchange.adapters;

import android.content.Context;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.technologies.mobile.free_exchange.Loader;
import com.technologies.mobile.free_exchange.R;
import com.technologies.mobile.free_exchange.activities.LoginActivity;
import com.technologies.mobile.free_exchange.rest.ExchangeClient;
import com.technologies.mobile.free_exchange.rest.RetrofitService;
import com.technologies.mobile.free_exchange.rest.model.DialogMessage;
import com.technologies.mobile.free_exchange.rest.model.DialogMessagesResponse;
import com.technologies.mobile.free_exchange.rest.model.NewMessage;
import com.technologies.mobile.free_exchange.rest.model.NewMessagesResponse;
import com.technologies.mobile.free_exchange.rest.model.SetViewedResponse;

import java.util.LinkedList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by diviator on 26.09.2016.
 */
public class MessageListAdapter extends ArrayAdapter {

    public static final String LOG_TAG = "MessageListAdapter";

    public static final int INPUT_MESSAGE = 123;
    public static final int OUTPUT_MESSAGE = 234;

    public static final int INITIAL_UPLOAD_LENGTH = 40;
    public static final int UPLOAD_LENGTH = 20;

    private LinkedList<ChatMessage> mData;

    private Context mContext;

    private boolean uploading = false;

    private String mDialogId;

    private ListView mList;

    public MessageListAdapter(Context context, int resource, String dialogId, ListView lv) {
        super(context, resource);
        mContext = context;
        mDialogId = dialogId;
        mData = new LinkedList<>();
        mList = lv;
    }

    public MessageListAdapter(Context context, int resource, ListView lv) {
        super(context, resource);
        mContext = context;
        mDialogId = null;
        mData = new LinkedList<>();
        mList = lv;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public ChatMessage getItem(int position) {
        return mData.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //return super.getView(position, convertView, parent);
        View view;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ChatMessage chatMessage = getItem(position);
        if (chatMessage.getType() == INPUT_MESSAGE) {
            view = inflater.inflate(R.layout.item_left_message, parent, false);
            RoundedImageView rivAvatar = (RoundedImageView) view.findViewById(R.id.rivAvatar);
            if (chatMessage.getPhotoUrl() != null && !chatMessage.getPhotoUrl().isEmpty()) {
                Glide.with(mContext).load(chatMessage.getPhotoUrl()).into(rivAvatar);
            }else{
                rivAvatar.setImageResource(R.drawable.no_photo);
            }
        } else {
            view = inflater.inflate(R.layout.item_right_message, parent, false);
        }

        TextView tvMessageText = (TextView) view.findViewById(R.id.tvMessageText);
        tvMessageText.setText(chatMessage.getText());

        return view;
    }

    public void initialUploading() {
        Loader.showProgressBar(mContext);
        uploading = true;
        execGetMessages(0, INITIAL_UPLOAD_LENGTH);
        mData = new LinkedList<>();
    }

    public void additionalUploading(int start) {
        if (!uploading) {
            uploading = true;
            execGetMessages(start, UPLOAD_LENGTH);
        }
    }

    private void execGetMessages(int offset, int count) {
        ExchangeClient client = RetrofitService.createService(ExchangeClient.class);
        Call<DialogMessagesResponse> dialogMessagesResponseCall = client.getDialogMessages(mDialogId, offset, count, ExchangeClient.apiKey);
        dialogMessagesResponseCall.enqueue(new Callback<DialogMessagesResponse>() {
            @Override
            public void onResponse(Call<DialogMessagesResponse> call, Response<DialogMessagesResponse> response) {
                if (response.body().getDialogMessages().getDialogMessage().length != 0) {
                    setViewed();
                }
                Log.e(LOG_TAG, "DIALOG ID = " + response.body().getDialogMessages().getDialogId());
                String uid = PreferenceManager.getDefaultSharedPreferences(mContext).getString(LoginActivity.ID, null);
                for (DialogMessage message : response.body().getDialogMessages().getDialogMessage()) {
                    //TODO SHOW MESSAGE TEXT
                    //tvMessages.setText(tvMessages.getText().toString() + message.getFromId() + ": " + message.getText() + "\n");
                    ChatMessage chatMessage = new ChatMessage(message.getUserDataFrom(), message.getText(), uid);
                    mData.addFirst(chatMessage);
                }
                if (response.body().getDialogMessages().getDialogMessage().length != 0) {
                    int index = mList.getFirstVisiblePosition() + response.body().getDialogMessages().getDialogMessage().length;
                    View v = mList.getChildAt(mList.getHeaderViewsCount());
                    int top = (v == null) ? 0 : v.getTop();

                    notifyDataSetChanged();

                    mList.setSelectionFromTop(index, top);
                }
                uploading = false;
                Loader.hideProgressBar(mContext);
            }

            @Override
            public void onFailure(Call<DialogMessagesResponse> call, Throwable t) {
                Log.e(LOG_TAG, "DIALOG ERROR = " + t.toString());

                uploading = false;
                Loader.hideProgressBar(mContext);
            }
        });
    }

    public void addMessageToList(ChatMessage chatMessage) {
        mData.addLast(chatMessage);
        notifyDataSetChanged();
    }

    public void downloadNewMessages() {
        if (mDialogId == null) {
            return;
        }
        final String uid = PreferenceManager.getDefaultSharedPreferences(mContext).getString(LoginActivity.ID, null);
        if (uid == null) {
            return;
        }
        ExchangeClient client = RetrofitService.createService(ExchangeClient.class);
        Call<NewMessagesResponse> newMessagesResponseCall = client.getNewMessages(uid, ExchangeClient.apiKey);
        newMessagesResponseCall.enqueue(new Callback<NewMessagesResponse>() {
            @Override
            public void onResponse(Call<NewMessagesResponse> call, Response<NewMessagesResponse> response) {
                setViewed();
                NewMessage[] newMessages = response.body().getResponse().getMessages();
                for (int i = newMessages.length - 1; i >= 0; i--) {
                    NewMessage message = newMessages[i];
                    if (message.getDialogId().equals(mDialogId)) {
                        ChatMessage chatMessage = new ChatMessage(message.getUserDataFrom(), message.getText(), uid);
                        mData.addLast(chatMessage);
                    }
                }
                notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<NewMessagesResponse> call, Throwable t) {

            }
        });
    }

    public void setViewed() {
        final String uid = PreferenceManager.getDefaultSharedPreferences(mContext).getString(LoginActivity.ID, null);
        if (mDialogId == null || uid == null) {
            return;
        }
        ExchangeClient client = RetrofitService.createService(ExchangeClient.class);
        Call<SetViewedResponse> setViewedResponseCall = client.setViewed(mDialogId, uid, ExchangeClient.apiKey);
        setViewedResponseCall.enqueue(new Callback<SetViewedResponse>() {
            @Override
            public void onResponse(Call<SetViewedResponse> call, Response<SetViewedResponse> response) {
                Log.e(LOG_TAG, "VIEWED HAS BEEN SET");
            }

            @Override
            public void onFailure(Call<SetViewedResponse> call, Throwable t) {

            }
        });
    }
}