package com.technologies.mobile.free_exchange.fragments;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatCheckBox;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.technologies.mobile.free_exchange.Loader;
import com.technologies.mobile.free_exchange.R;
import com.technologies.mobile.free_exchange.activities.LoginActivity;
import com.technologies.mobile.free_exchange.rest.ExchangeClient;
import com.technologies.mobile.free_exchange.rest.RetrofitService;
import com.technologies.mobile.free_exchange.rest.model.AddSubscribeListResponse;
import com.technologies.mobile.free_exchange.rest.model.EditSubscribeListResponse;
import com.technologies.mobile.free_exchange.views.WrappingRelativeLayout;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by diviator on 05.10.2016.
 */

public class CreateSubscribeFragment extends Fragment implements View.OnClickListener {

    public static final String LOG_TAG = "createSubscribeFragment";

    public static final String TAG = "createSubscribeTag";

    public static final String ITEMS_GIVE = "ITEMS_GIVE";
    public static final String ITEMS_GET = "ITEMS_GET";
    public static final String NOTIFICATION = "NOTIFICATION";
    public static final String LIST_ID = "LIST_ID";

    EditText mEtGives;
    EditText mEtGets;

    ImageButton mAddGiveTag;
    ImageButton mAddGetTag;

    WrappingRelativeLayout mGiveTagsLayout;
    WrappingRelativeLayout mGetTagsLayout;

    AppCompatCheckBox cbNotification;

    AppCompatButton bApply;

    String lastTitle;

    @Nullable
    private String listId;
    private int notification;
    @Nullable
    private ArrayList<String> itemsGive;
    @Nullable
    private ArrayList<String> itemsGet;

    public CreateSubscribeFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            listId = getArguments().getString(LIST_ID);
            notification = getArguments().getInt(NOTIFICATION);
            itemsGet = getArguments().getStringArrayList(ITEMS_GET);
            itemsGive = getArguments().getStringArrayList(ITEMS_GIVE);
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_create_subscribe, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setTitle();
        initViews(view);
    }

    private void initViews(View view) {
        bApply = (AppCompatButton) view.findViewById(R.id.bApply);
        if (getArguments() != null) {
            bApply.setText(R.string.subs_update);
        }
        bApply.setOnClickListener(this);

        mAddGiveTag = (ImageButton) view.findViewById(R.id.bAddGiveTag);
        mAddGiveTag.setOnClickListener(this);

        mAddGetTag = (ImageButton) view.findViewById(R.id.bAddGetTag);
        mAddGetTag.setOnClickListener(this);

        mGiveTagsLayout = (WrappingRelativeLayout) view.findViewById(R.id.wrlGivesTags);
        mGetTagsLayout = (WrappingRelativeLayout) view.findViewById(R.id.wrlGetsTags);

        cbNotification = (AppCompatCheckBox) view.findViewById(R.id.cbNotification);
        cbNotification.setChecked(true);

        initEdit();

        mEtGets = (EditText) view.findViewById(R.id.etGets);
        mEtGets.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = editable.toString();
                if (text.length() != 0 && text.charAt(text.length() - 1) == ',') {
                    String tag = mEtGets.getText().toString().substring(0, text.length() - 1);
                    mEtGets.setText("");
                    mGetTagsLayout.addTag(tag);
                }
            }
        });
        mEtGets.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    if (mEtGets.getText().length() != 0) {
                        mGetTagsLayout.addTag(mEtGets.getText().toString());
                        mEtGets.setText("");
                    }
                }
            }
        });

        mEtGives = (EditText) view.findViewById(R.id.etGives);
        mEtGives.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = editable.toString();
                if (text.length() != 0 && text.charAt(text.length() - 1) == ',') {
                    String tag = mEtGives.getText().toString().substring(0, text.length() - 1);
                    mEtGives.setText("");
                    mGiveTagsLayout.addTag(tag);
                }
            }
        });
        mEtGives.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    if (mEtGives.getText().length() != 0) {
                        mGiveTagsLayout.addTag(mEtGives.getText().toString());
                        mEtGives.setText("");
                    }
                }
            }
        });
    }

    private void initEdit() {
        if (getArguments() != null) {
            Log.e(LOG_TAG,"initEdit + args != null");
            for (String tag : itemsGive) {
                Log.e(LOG_TAG,"+");
                mGiveTagsLayout.addTag(tag);
            }
            for (String tag : itemsGet) {
                Log.e(LOG_TAG,"-");
                mGetTagsLayout.addTag(tag);
            }
            Log.e(LOG_TAG, "NOTIFICATION = " + notification);
            switch (notification) {
                case 0: {
                    cbNotification.setChecked(false);
                    break;
                }
                case 1: {
                    cbNotification.setChecked(true);
                    break;
                }
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bAddGiveTag: {
                String tag = mEtGives.getText().toString();
                mEtGives.setText("");
                mGiveTagsLayout.addTag(tag);
                break;
            }
            case R.id.bAddGetTag: {
                String tag = mEtGets.getText().toString();
                mEtGets.setText("");
                mGetTagsLayout.addTag(tag);
                break;
            }
            case R.id.bApply: {
                applySubscribe();
                break;
            }
        }
    }

    private void setTitle() {
        lastTitle = getActivity().getTitle().toString();
        String newTitle;
        if (getArguments() == null) {
            newTitle = getString(R.string.subs_new_sub);
        } else {
            newTitle = getString(R.string.subs_edit_sub);
        }
        getActivity().setTitle(newTitle);
    }

    public void setLastTitle() {
        if (lastTitle != null) {
            getActivity().setTitle(lastTitle);
        }
    }

    private void applySubscribe() {
        String uid = PreferenceManager.getDefaultSharedPreferences(getContext()).getString(LoginActivity.ID, null);
        if (uid == null) {
            throwError();
            return;
        }

        JSONArray categories;
        try {
            categories = new JSONArray("[\"0\"]");
        } catch (JSONException e) {
            e.printStackTrace();
            throwError();
            return;
        }

        JSONArray itemsGive;
        itemsGive = new JSONArray(getGives());

        JSONArray itemsGet;
        itemsGet = new JSONArray(getGets());

        int notification = 1;
        if (!cbNotification.isChecked()) {
            notification = 0;
        }
        if( getArguments() == null ){
            createNewSubscribe(uid,categories,itemsGive,itemsGet,notification);
        }else{
            updateSubscribe(categories,itemsGive,itemsGet,notification);
        }
    }

    private void createNewSubscribe(String uid, JSONArray categories, JSONArray itemsGive, JSONArray itemsGet, int notification){
        Loader.showSender(getContext());
        ExchangeClient client = RetrofitService.createService(ExchangeClient.class);
        client.addSubscribeList(uid, itemsGet, itemsGive,
                categories, notification, ExchangeClient.apiKey).enqueue(new Callback<AddSubscribeListResponse>() {
            @Override
            public void onResponse(Call<AddSubscribeListResponse> call, Response<AddSubscribeListResponse> response) {
                FragmentAdapter fragmentAdapter = new FragmentAdapter(getActivity());
                fragmentAdapter.initFragment(FragmentAdapter.SUBSCRIBES);

                Toast.makeText(getContext(), R.string.subs_create_success, Toast.LENGTH_LONG).show();

                Loader.hideSender();
            }

            @Override
            public void onFailure(Call<AddSubscribeListResponse> call, Throwable t) {
                Log.e(LOG_TAG, "ADD ERROR " + t.toString());

                throwError();

                Loader.hideSender();
            }
        });
    }

    private void updateSubscribe(JSONArray categories, JSONArray itemsGive, JSONArray itemsGet, int notification){
        Loader.showSender(getContext());
        ExchangeClient client = RetrofitService.createService(ExchangeClient.class);
        client.editSubscribeList(listId,itemsGet,itemsGive,categories,notification,ExchangeClient.apiKey).enqueue(new Callback<EditSubscribeListResponse>() {
            @Override
            public void onResponse(Call<EditSubscribeListResponse> call, Response<EditSubscribeListResponse> response) {
                FragmentAdapter fragmentAdapter = new FragmentAdapter(getActivity());
                fragmentAdapter.initFragment(FragmentAdapter.SUBSCRIBES);

                Toast.makeText(getContext(), R.string.subs_edit_success, Toast.LENGTH_LONG).show();

                Loader.hideSender();
            }

            @Override
            public void onFailure(Call<EditSubscribeListResponse> call, Throwable t) {
                Log.e(LOG_TAG, "ADD ERROR " + t.toString());

                throwError();

                Loader.hideSender();
            }
        });
    }

    private void throwError() {
        if( getArguments() == null ) {
            Toast.makeText(getContext(), R.string.subs_create_error, Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(getContext(), R.string.subs_edit_error, Toast.LENGTH_LONG).show();
        }
    }

    public ArrayList<String> getGives() {
        ArrayList<String> gives = new ArrayList<>();
        for (Map.Entry e : mGiveTagsLayout.getTags().entrySet()) {
            gives.add(e.getValue().toString());
            Log.e(LOG_TAG, e.getValue().toString());
        }
        if (mEtGives.getText().length() != 0) {
            gives.add(mEtGives.getText().toString());
        }
        return gives;
    }

    public ArrayList<String> getGets() {
        ArrayList<String> gets = new ArrayList<>();
        for (Map.Entry e : mGetTagsLayout.getTags().entrySet()) {
            gets.add(e.getValue().toString());
            Log.e(LOG_TAG, e.getValue().toString());
        }
        if (mEtGets.getText().length() != 0) {
            gets.add(mEtGets.getText().toString());
        }
        return gets;
    }

}
