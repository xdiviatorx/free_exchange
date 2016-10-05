package com.technologies.mobile.free_exchange.activities;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.WindowManager;

import com.technologies.mobile.free_exchange.R;
import com.technologies.mobile.free_exchange.fragments.DialogFragment;

public class DialogActivity extends AppCompatActivity {

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        initToolbar();

        initDialog(getIntent());
    }

    private void initToolbar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_arrow);
    }

    private void initDialog(Intent data){
        String dialogId = data.getStringExtra(DialogFragment.DIALOG_ID);
        String interlocutorId = data.getStringExtra(DialogFragment.INTERLOCUTOR_ID);
        String interlocutorName = data.getStringExtra(DialogFragment.INTERLOCUTOR_NAME);
        String interlocutorVkId = data.getStringExtra(DialogFragment.INTERLOCUTOR_VK_ID);

        DialogFragment dialogFragment = new DialogFragment();
        Bundle args = new Bundle();
        args.putString(DialogFragment.DIALOG_ID,dialogId);
        args.putString(DialogFragment.INTERLOCUTOR_ID,interlocutorId);
        args.putString(DialogFragment.INTERLOCUTOR_NAME,interlocutorName);
        args.putString(DialogFragment.INTERLOCUTOR_VK_ID,interlocutorVkId);
        dialogFragment.setArguments(args);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content,dialogFragment,DialogFragment.TAG)
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }
}
