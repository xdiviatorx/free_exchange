package com.technologies.mobile.free_exchange.activities;

import android.animation.Animator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.technologies.mobile.free_exchange.R;
import com.technologies.mobile.free_exchange.adapters.CategorySpinnerAdapter;
import com.technologies.mobile.free_exchange.adapters.SearchPullAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher, AbsListView.OnScrollListener, AdapterView.OnItemSelectedListener{

    private String LOG_TAG = "mySearch";

    private float translation;// = -100;

    ImageButton down;
    ImageButton up;

    LinearLayout spinner;
    LinearLayout tvSearch;

    ImageButton back;
    ImageButton clear;

    LinearLayout toolbar;

    EditText etGives;
    EditText etGets;

    private ListView lv;
    private SearchPullAdapter lvAdapter;

    private int lastFirstVisibleItem = 0;

    private AppCompatSpinner appCompatSpinner;
    private CategorySpinnerAdapter spinnerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        translation = getResources().getDimension(R.dimen.search_bar_animation);
        translation*=-1;

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        initToolbar();

        initViews();

        setDefaultToolbarState();
    }

    private void initToolbar(){
        initCategorySpinner();

        down = (ImageButton) findViewById(R.id.maximize);
        up = (ImageButton) findViewById(R.id.minimize);

        spinner = (LinearLayout) findViewById(R.id.spinner);
        tvSearch = (LinearLayout) findViewById(R.id.tvSearch);
        tvSearch.setOnClickListener(this);

        back = (ImageButton) findViewById(R.id.back);
        clear = (ImageButton) findViewById(R.id.clear);

        back.setOnClickListener(this);
        clear.setOnClickListener(this);

        toolbar = (LinearLayout) findViewById(R.id.searchBar);

        etGives = (EditText) findViewById(R.id.etGives);
        etGets = (EditText) findViewById(R.id.etGets);
        etGives.addTextChangedListener(this);
        etGets.addTextChangedListener(this);

        down.setOnClickListener(this);

        up.setOnClickListener(this);
    }

    private void initCategorySpinner(){
        appCompatSpinner = (AppCompatSpinner) findViewById(R.id.category);

        String[] from = {CategorySpinnerAdapter.NAME};
        int[] to = {R.id.tv};
        ArrayList<HashMap<String,Object>> data = new ArrayList<>();

        spinnerAdapter = new CategorySpinnerAdapter(this,data,R.layout.spinner_item,from,to);
        spinnerAdapter.initSpinner();

        appCompatSpinner.setAdapter(spinnerAdapter);

        appCompatSpinner.setOnItemSelectedListener(this);
    }

    private void initViews(){
        lv = (ListView) findViewById(R.id.lv);

        String[] from = {SearchPullAdapter.GIVE,SearchPullAdapter.GET,SearchPullAdapter.PLACE,SearchPullAdapter.CONTACTS,SearchPullAdapter.DATE};
        int[] to = {R.id.gives,R.id.gets,R.id.place,R.id.contacts,R.id.date};
        ArrayList<HashMap<String,Object>> data = new ArrayList<>();

        lvAdapter = new SearchPullAdapter(this,data,R.layout.exchange_item,from,to);

        lv.setAdapter(lvAdapter);

        lv.setOnScrollListener(this);

        lvAdapter.initialUploading();
    }

    private void setDefaultToolbarState(){
        tvSearch.setAlpha(0);
        down.animate().setDuration(0).alpha(0).start();
        down.setVisibility(View.GONE);

        //START WITH MINIMUM
        toolbar.animate().setDuration(0).translationY(translation).start();
        up.animate().alpha(0).start();
        down.animate().alpha(1).start();
        down.setVisibility(View.VISIBLE);
        //down.animate().setDuration(500).translationY(translation).start();

        etGets.animate().setDuration(0).alpha(0).start();
        etGives.animate().setDuration(0).alpha(0).start();

        spinner.animate().setDuration(0).alpha(0).start();
        tvSearch.animate().setDuration(0).alpha(1).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                tvSearch.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                //hideKeyboard();
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        }).start();

        back.animate().setDuration(0).translationY(-1*translation).start();
        clear.animate().setDuration(0).translationY(-1*translation).start();
        clear.animate().setDuration(0).alpha(0).start();

        //LET'S MAXIMIZE
        maximize();
    }

    public void onClearPressed(){
        etGets.setText("");
        etGives.setText("");
        if( spinnerAdapter.getData().size() != 0 ) {
            appCompatSpinner.setSelection(0);
        }
    }

    public void setPreviews(){
        TextView tvGives = (TextView) findViewById(R.id.preview_gives);
        TextView tvGets = (TextView) findViewById(R.id.preview_gets);
        TextView tvCategory = (TextView) findViewById(R.id.preview_category);

        tvGives.setText(etGives.getText().toString());
        tvGets.setText(etGets.getText().toString());
        String categoryName;
        if( spinnerAdapter.getData().size() != 0  ) {
            categoryName = spinnerAdapter.getData().get(appCompatSpinner.getSelectedItemPosition()).get(CategorySpinnerAdapter.NAME).toString();
        }else{
            categoryName = getResources().getString(R.string.no_category);
        }
        tvCategory.setText(categoryName);
    }

    public void minimize(){
        setPreviews();

        toolbar.animate().setDuration(500).translationY(translation).start();

        //up.animate().setDuration(500).translationY(translation).start();
        up.animate().alpha(0).start();
        down.animate().alpha(1).start();
        down.setVisibility(View.VISIBLE);
        //down.animate().setDuration(500).translationY(translation).start();

        etGets.animate().setDuration(200).alpha(0).start();
        etGives.animate().setDuration(200).alpha(0).start();

        spinner.animate().setDuration(500).alpha(0).start();
        tvSearch.animate().setDuration(500).alpha(1).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                tvSearch.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                hideKeyboard();
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        }).start();

        back.animate().setDuration(500).translationY(-1*translation).start();
        clear.animate().setDuration(500).translationY(-1*translation).start();
        clear.animate().setDuration(200).alpha(0).start();
    }

    public void maximize(){
        toolbar.animate().setDuration(500).translationY(0).start();

        //up.animate().setDuration(500).translationY(0).start();
        //down.animate().setDuration(500).translationY(0).start();
        up.animate().alpha(1);
        down.animate().alpha(0);
        down.setVisibility(View.GONE);

        etGets.animate().setDuration(500).alpha(1).start();
        etGives.animate().setDuration(500).alpha(1).start();

        spinner.animate().setDuration(500).alpha(1).start();
        tvSearch.animate().setDuration(200).alpha(0).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                tvSearch.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        }).start();


        back.animate().setDuration(500).translationY(0).start();
        clear.animate().setDuration(500).translationY(0).start();
        clear.animate().setDuration(500).alpha(1).start();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:{
                finish();
                break;
            }
            case R.id.minimize:{
                minimize();
                break;
            }
            case R.id.maximize:{
                maximize();
                break;
            }
            case R.id.tvSearch:{
                maximize();
                break;
            }
            case R.id.clear:{
                onClearPressed();
                break;
            }
        }
    }

    private void hideKeyboard(){
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(this);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        //Log.e(LOG_TAG,"***");
        //Log.e(LOG_TAG,"get: " + etGets.getText().toString());
        //Log.e(LOG_TAG,"give: " +etGives.getText().toString());
        //TODO NEW UPLOADING
        newSearch();
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {
        if( lastFirstVisibleItem < absListView.getFirstVisiblePosition() ) {
            minimize();
        }
        lastFirstVisibleItem = absListView.getFirstVisiblePosition();
    }

    @Override
    public void onScroll(AbsListView absListView, int i, int i1, int i2) {
        if( i >= Math.max(i2-10,10) ) {
            lvAdapter.additionalUploading(i2);
        }
    }

    private void newSearch(){
        String[] from = {SearchPullAdapter.GIVE,SearchPullAdapter.GET,SearchPullAdapter.PLACE,SearchPullAdapter.CONTACTS,SearchPullAdapter.DATE};
        int[] to = {R.id.gives,R.id.gets,R.id.place,R.id.contacts,R.id.date};
        ArrayList<HashMap<String,Object>> data = new ArrayList<>();

        lvAdapter = new SearchPullAdapter(this,data,R.layout.exchange_item,from,to);
        lv.setAdapter(lvAdapter);

        int category = 0;
        if( spinnerAdapter.getData().size() != 0 ){
            category = (int) spinnerAdapter.getData().get(appCompatSpinner.getSelectedItemPosition()).get(CategorySpinnerAdapter.ID);
        }

        lvAdapter.setUploadingParams(etGives.getText().toString(),etGets.getText().toString(),category);
        lvAdapter.initialUploading();

        lastFirstVisibleItem = 0;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        newSearch();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
