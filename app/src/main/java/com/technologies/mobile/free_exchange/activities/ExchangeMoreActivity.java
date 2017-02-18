package com.technologies.mobile.free_exchange.activities;

import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.technologies.mobile.free_exchange.CommentsCountManager;
import com.technologies.mobile.free_exchange.R;
import com.technologies.mobile.free_exchange.adapters.CommentAdapter;
import com.technologies.mobile.free_exchange.listeners.OnImageClickListener;
import com.technologies.mobile.free_exchange.rest.ExchangeClient;
import com.technologies.mobile.free_exchange.rest.RetrofitService;
import com.technologies.mobile.free_exchange.rest.model.AddCommentResponse;
import com.technologies.mobile.free_exchange.rest.model.Comment;
import com.technologies.mobile.free_exchange.rest.model.SearchExtraditionItem;
import com.technologies.mobile.free_exchange.rest.model.User;
import com.technologies.mobile.free_exchange.views.AutomaticPhotoLayout;
import com.technologies.mobile.free_exchange.views.PhotosListLayout;

import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExchangeMoreActivity extends AppCompatActivity implements AbsListView.OnScrollListener, View.OnClickListener, OnImageClickListener {

    public static final String LOG_TAG = "commentsActivity";

    public static final String EXCHANGE = "EXCHANGE";

    ListView lvComments;
    CommentAdapter cAdapter;

    EditText etComment;
    ImageButton ibSendComment;

    View header;

    Toolbar toolbar;

    SearchExtraditionItem curItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange_more);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        curItem = getIntent().getParcelableExtra(EXCHANGE);

        initToolbar();
        initView();
    }

    public void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_arrow);
    }

    public void initView() {
        lvComments = (ListView) findViewById(R.id.lvComments);
        cAdapter = new CommentAdapter(this, R.layout.item_comment);
        lvComments.setAdapter(cAdapter);
        cAdapter.setOfferId(curItem.getId());
        cAdapter.init();
        lvComments.setOnScrollListener(this);
        //lvComments.setTranscriptMode(ListView.TRANSCRIPT_MODE_NORMAL);
        //lvComments.setStackFromBottom(true);
        initHeader();

        etComment = (EditText) findViewById(R.id.etComment);

        ibSendComment = (ImageButton) findViewById(R.id.ibSendComment);
        ibSendComment.setOnClickListener(this);
    }

    private void initHeader() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        header = inflater.inflate(R.layout.header_more, null, false);

        PhotosListLayout aplPhotos = (PhotosListLayout) header.findViewById(R.id.pllPhotos);

        TextView tvName = (TextView) header.findViewById(R.id.tvName);
        TextView tvDate = (TextView) header.findViewById(R.id.date);

        TextView tvGives = (TextView) header.findViewById(R.id.gives);
        TextView tvGets = (TextView) header.findViewById(R.id.gets);
        TextView tvPlace = (TextView) header.findViewById(R.id.place);
        TextView tvContacts = (TextView) header.findViewById(R.id.contacts);

        if (curItem.getPhotosArray() != null && curItem.getPhotosArray().length != 0 && !curItem.getPhotosArray()[0].isEmpty()) {
            aplPhotos.addPhotos(Arrays.asList(curItem.getPhotosArray()));
            aplPhotos.setOnImageClickListener(this);
        }

        tvName.setText(curItem.getUserData().getName());
        tvDate.setText(curItem.getDate());

        tvGives.setText(curItem.getGive());
        tvGets.setText(curItem.getGet());
        tvPlace.setText(curItem.getPlace());
        tvContacts.setText(curItem.getContacts());

        lvComments.addHeaderView(header);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ibSendComment: {
                if (!etComment.getText().toString().isEmpty()) {
                    showComment();
                    sendComment();
                    etComment.setText("");
                    CommentsCountManager.setCommentCount(curItem.getCommentsCount()+1,curItem.getId());
                }
                break;
            }
        }
    }

    public void showComment() {
        cAdapter.addComment(prepareComment());
    }

    public void sendComment(){
        Comment comment = prepareComment();
        ExchangeClient client = RetrofitService.createService(ExchangeClient.class);
        client.addComment(comment.getOfferId(),comment.getUid(),comment.getText(),ExchangeClient.apiKey).enqueue(new Callback<AddCommentResponse>() {
            @Override
            public void onResponse(Call<AddCommentResponse> call, Response<AddCommentResponse> response) {

            }

            @Override
            public void onFailure(Call<AddCommentResponse> call, Throwable t) {
                Log.e(LOG_TAG, "SEND COMMENT ERROR " + t.toString());
            }
        });
    }

    private Comment prepareComment(){
        Comment comment = new Comment();
        User userData = new User();
        String uid = PreferenceManager.getDefaultSharedPreferences(this).getString(LoginActivity.ID,"");
        String text = etComment.getText().toString();
        String name = PreferenceManager.getDefaultSharedPreferences(this).getString(LoginActivity.NAME,"");
        String photoUri = PreferenceManager.getDefaultSharedPreferences(this).getString(LoginActivity.PHOTO,"");

        comment.setText(text);
        comment.setOfferId(String.valueOf(curItem.getId()));
        comment.setUid(uid);

        userData.setId(uid);
        userData.setName(name);
        userData.setPhoto(photoUri);

        comment.setUserData(userData);

        return comment;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {

    }

    @Override
    public void onScroll(AbsListView absListView, int i, int i1, int i2) {
        if (i > Math.max(i2 - 10, 10)) {
            cAdapter.additionalDownloading(i2);
        }
    }


    @Override
    public void onImageClicked(int imageIndex) {
        Intent intent = new Intent(this, ImagePreviewActivity.class);
        String[] images = curItem.getPhotosArray();
        intent.putExtra(ImagePreviewActivity.IMAGES, images);
        intent.putExtra(ImagePreviewActivity.ITEM,imageIndex);
        startActivity(intent);
    }
}
