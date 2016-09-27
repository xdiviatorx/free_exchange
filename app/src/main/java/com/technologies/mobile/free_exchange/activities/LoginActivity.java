package com.technologies.mobile.free_exchange.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.technologies.mobile.free_exchange.R;
import com.technologies.mobile.free_exchange.rest.ExchangeClient;
import com.technologies.mobile.free_exchange.rest.RetrofitService;
import com.technologies.mobile.free_exchange.rest.model.AddUserResponse;
import com.technologies.mobile.free_exchange.rest.model.GetUserResponse;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    public static final String ID = "ID";

    Button vkLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViews();
    }

    private void initViews(){
        vkLogin = (Button) findViewById(R.id.vkLogin);
        vkLogin.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    public void onClick(View view) {
        switch( view.getId() ){
            case R.id.vkLogin:{
                VKSdk.login(this, VKScope.WALL, VKScope.PHOTOS);
                vkLogin.setVisibility(View.INVISIBLE);
                break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {
                setResult(RESULT_OK);
                siteLogin(res.userId);
            }
            @Override
            public void onError(VKError error) {
                Toast.makeText(getApplicationContext(),R.string.login_error,Toast.LENGTH_LONG).show();
                vkLogin.setVisibility(View.VISIBLE);
            }
        }) ) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void siteLogin(final String vkId){
        ExchangeClient client = RetrofitService.createService(ExchangeClient.class);
        JSONObject by;
        JSONArray fields;
        try {
            by = new JSONObject("{ \"vk_id\" : " + vkId + "}");
            fields = new JSONArray("[\"id\"]");
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),R.string.login_error,Toast.LENGTH_LONG).show();
            vkLogin.setVisibility(View.VISIBLE);
            return;
        }
        Call<GetUserResponse> getUserResponseCall = client.getUsersBy(by,fields,ExchangeClient.apiKey);
        getUserResponseCall.enqueue(new Callback<GetUserResponse>() {
            @Override
            public void onResponse(Call<GetUserResponse> call, Response<GetUserResponse> response) {
                if( response.body().getResponse().getUsers().length == 0 ){
                    siteSignUp(vkId);
                }else{
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(ID,response.body().getResponse().getUsers()[0].getId());
                    editor.apply();
                    Toast.makeText(getApplicationContext(),R.string.login_successfully,Toast.LENGTH_LONG).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<GetUserResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(),R.string.login_error,Toast.LENGTH_LONG).show();
                vkLogin.setVisibility(View.VISIBLE);
            }
        });
    }

    private void siteSignUp(String vkId){
        ExchangeClient client = RetrofitService.createService(ExchangeClient.class);
        Call<AddUserResponse> addUserResponseCall = client.addUser(vkId,ExchangeClient.apiKey);
        addUserResponseCall.enqueue(new Callback<AddUserResponse>() {
            @Override
            public void onResponse(Call<AddUserResponse> call, Response<AddUserResponse> response) {
                if( response.body().getResponse().getStatus().equals("OK") ){
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(ID,response.body().getResponse().getUid());
                    editor.apply();
                    Toast.makeText(getApplicationContext(),R.string.sign_up_successfully,Toast.LENGTH_LONG).show();
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(),R.string.sign_up_error,Toast.LENGTH_LONG).show();
                    vkLogin.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<AddUserResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(),R.string.sign_up_error,Toast.LENGTH_LONG).show();
                vkLogin.setVisibility(View.VISIBLE);
            }
        });
    }
}
