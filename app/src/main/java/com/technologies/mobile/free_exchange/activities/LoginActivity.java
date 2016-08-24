package com.technologies.mobile.free_exchange.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.technologies.mobile.free_exchange.R;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKError;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

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
                VKSdk.login(this,this.getPackageName());
                break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {
                Toast.makeText(getApplicationContext(),R.string.login_successfully,Toast.LENGTH_LONG).show();
                setResult(RESULT_OK);
                finish();
            }
            @Override
            public void onError(VKError error) {
                Toast.makeText(getApplicationContext(),R.string.login_error,Toast.LENGTH_LONG).show();
            }
        }) ) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
