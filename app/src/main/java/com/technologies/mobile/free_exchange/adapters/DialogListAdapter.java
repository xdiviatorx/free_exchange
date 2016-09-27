package com.technologies.mobile.free_exchange.adapters;

import android.content.Context;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;
import com.technologies.mobile.free_exchange.Loader;
import com.technologies.mobile.free_exchange.R;
import com.technologies.mobile.free_exchange.activities.LoginActivity;
import com.technologies.mobile.free_exchange.rest.ExchangeClient;
import com.technologies.mobile.free_exchange.rest.RetrofitService;
import com.technologies.mobile.free_exchange.rest.model.Dialog;
import com.technologies.mobile.free_exchange.rest.model.ListDialogsResponse;
import com.technologies.mobile.free_exchange.rest.model.User;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKRequest;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by diviator on 20.09.2016.
 */
public class DialogListAdapter extends SimpleAdapter {

    private static final String LOG_TAG = "myLogsDialogListAdapter";

    public static final String INTERLOCUTOR_NAME = "INTERLOCUTOR_NAME";
    public static final String INTERLOCUTOR_ID = "INTERLOCUTOR_ID";
    public static final String INTERLOCUTOR_AVATAR_LINK = "INTERLOCUTOR_AVATAR_LINK";
    public static final String LAST_MESSAGE = "LAST_MESSAGE";
    public static final String DIALOG_ID = "DIALOG_ID";

    private static final int UPLOAD_LENGTH = 20;

    ArrayList<HashMap<String,Object>> mData;
    Context mContext;

    private boolean uploading = false;

    public DialogListAdapter(Context context, ArrayList<HashMap<String,Object>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
        mData = data;
        mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view =  super.getView(position, convertView, parent);

        RoundedImageView rivAvatar = (RoundedImageView) view.findViewById(R.id.rivAvatar);

        if( !mData.get(position).get(INTERLOCUTOR_AVATAR_LINK).toString().isEmpty() ) {
            Picasso.with(mContext)
                    .load(mData.get(position).get(INTERLOCUTOR_AVATAR_LINK).toString())
                    .into(rivAvatar);
        }

        return view;
    }

    public ArrayList<HashMap<String, Object>> getData() {
        return mData;
    }

    public void initialUploading(){
        Loader.showProgressBar(mContext);
        uploading=true;
        execGetDialogs(0,UPLOAD_LENGTH);
    }

    public void additionalUploading(int start){
        if( !uploading  ) {
            uploading = true;
            execGetDialogs(start, UPLOAD_LENGTH);
        }
    }

    public void execGetDialogs(int offset, int count){
        ExchangeClient client = RetrofitService.createService(ExchangeClient.class);

        String uid = PreferenceManager.getDefaultSharedPreferences(mContext).getString(LoginActivity.ID,null);
        if( uid == null ){
            return;
        }

        Call<ListDialogsResponse> listDialogsResponseCall = client.getDialogs(uid, offset, count, ExchangeClient.apiKey);
        listDialogsResponseCall.enqueue(new Callback<ListDialogsResponse>() {
            @Override
            public void onResponse(Call<ListDialogsResponse> call, Response<ListDialogsResponse> response) {
                Dialog[] dialogs = response.body().getListDialogs().getDialogs();
                for( Dialog dialog : dialogs ){
                    HashMap<String,Object> item = new HashMap<String, Object>();
                    item.put(INTERLOCUTOR_AVATAR_LINK,"");
                    item.put(INTERLOCUTOR_NAME,dialog.getDialog_id());
                    item.put(LAST_MESSAGE,"");

                    item.put(DIALOG_ID,dialog.getDialog_id());
                    item.put(INTERLOCUTOR_ID,findInterlocutorId(dialog.getUsers()));
                    mData.add(item);
                }
                notifyDataSetChanged();
                uploading=false;
                Loader.hideProgressBar(mContext);
            }

            @Override
            public void onFailure(Call<ListDialogsResponse> call, Throwable t) {
                Log.e(LOG_TAG,t.toString());
                uploading=false;
                Loader.hideProgressBar(mContext);
            }
        });
    }

    @Nullable
    public String findInterlocutorId(User[] users){
        String uid = PreferenceManager.getDefaultSharedPreferences(mContext).getString(LoginActivity.ID,null);
        if( uid == null ){
            return null;
        }
        for( int i = 0; i < users.length; i++ ){
            if( !users[i].getId().equals(uid) ){
                return users[i].getId();
            }
        }
        return null;
    }

}
