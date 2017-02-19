package com.technologies.mobile.free_exchange.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ListView;

import com.technologies.mobile.free_exchange.R;
import com.technologies.mobile.free_exchange.adapters.CityAdapter;
import com.technologies.mobile.free_exchange.listeners.OnDialogListener;
import com.technologies.mobile.free_exchange.model.CityManager;
import com.technologies.mobile.free_exchange.rest.ExchangeClient;
import com.technologies.mobile.free_exchange.rest.RetrofitService;
import com.technologies.mobile.free_exchange.rest.model.City;
import com.technologies.mobile.free_exchange.rest.model.GetCitiesResponse;

import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by diviator on 19.02.2017.
 */

public class CityChooseDialog extends DialogFragment implements View.OnClickListener {

    City mCurrentCity;

    private OnDialogListener onDialogListener;

    ListView lvCities;
    AppCompatButton acbCancel;
    AppCompatButton acbOk;

    CityAdapter cityAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return inflater.inflate(R.layout.fragment_choose_city, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        initList();
    }

    private void initViews(View view) {
        lvCities = (ListView) view.findViewById(R.id.lvCities);
        acbCancel = (AppCompatButton) view.findViewById(R.id.acbCancel);
        acbCancel.setOnClickListener(this);
        acbOk = (AppCompatButton) view.findViewById(R.id.acbOk);
        acbOk.setVisibility(View.GONE);
        acbOk.setOnClickListener(this);
    }

    private void initList() {
        mCurrentCity = CityManager.getGlobalCity();
        RetrofitService.createService(ExchangeClient.class).getCities(ExchangeClient.apiKey).enqueue(new Callback<GetCitiesResponse>() {
            @Override
            public void onResponse(Call<GetCitiesResponse> call, Response<GetCitiesResponse> response) {
                if (response != null && response.body() != null && response.body().getCities() != null && response.body().getCities().length != 0) {
                    cityAdapter = new CityAdapter(getContext(), Arrays.asList(response.body().getCities()), mCurrentCity);
                    lvCities.setAdapter(cityAdapter);
                    acbOk.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<GetCitiesResponse> call, Throwable t) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.acbOk:{
                CityManager.setGlobalCity(getContext(), cityAdapter.getCurrentCity());
                if( onDialogListener != null ){
                    onDialogListener.onDismiss(true);
                }
            }
            case R.id.acbCancel:{
                getDialog().dismiss();
                break;
            }
        }
    }

    public void setOnDialogListener(OnDialogListener onDialogListener) {
        this.onDialogListener = onDialogListener;
    }
}
