package com.technologies.mobile.free_exchange.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.technologies.mobile.free_exchange.R;
import com.technologies.mobile.free_exchange.adapters.RecyclerAddedImagesAdapter;
import com.technologies.mobile.free_exchange.views.WrappingRelativeLayout;

/**
 * Created by diviator on 24.08.2016.
 */
public class AddFragment extends Fragment implements View.OnClickListener {

    private static final String LOG_TAG = "myLogsAddFragment";

    private static final int GALLERY_REQUEST = 347;
    private static final int CAMERA_REQUEST = 719;

    ImageButton mMakePhoto;
    ImageButton mGetFromGallery;

    RecyclerView mImagesRecycler;
    RecyclerAddedImagesAdapter mRecyclerAdapter;
    RecyclerView.LayoutManager mLayoutManager;

    ImageButton mAddGiveTag;
    ImageButton mAddGetTag;

    WrappingRelativeLayout mGiveTagsLayout;
    WrappingRelativeLayout mGetTagsLayout;

    EditText mEtGives;
    EditText mEtGets;

    AppCompatCheckBox cbPm;
    AppCompatCheckBox cbPhone;
    AppCompatCheckBox cbOther;

    EditText mEtPhone;
    EditText mEtOther;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
    }

    public void initViews(View view) {
        // photo buttons
        mMakePhoto = (ImageButton) view.findViewById(R.id.bMakePhoto);
        mMakePhoto.setOnClickListener(this);

        mGetFromGallery = (ImageButton) view.findViewById(R.id.bGetFromGallery);
        mGetFromGallery.setOnClickListener(this);

        // image recycler
        mImagesRecycler = (RecyclerView) view.findViewById(R.id.rvPhotos);

        mRecyclerAdapter = new RecyclerAddedImagesAdapter(getContext());
        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        mImagesRecycler.setLayoutManager(mLayoutManager);
        mImagesRecycler.setAdapter(mRecyclerAdapter);

        mImagesRecycler.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                Log.e(LOG_TAG, "Focus changed");
                if (!b) {
                    Log.e(LOG_TAG, "Focus lost");
                    mRecyclerAdapter.hideAllDeleteButtons();
                }
            }
        });

        // image buttons
        mAddGiveTag = (ImageButton) view.findViewById(R.id.bAddGiveTag);
        mAddGiveTag.setOnClickListener(this);

        mAddGetTag = (ImageButton) view.findViewById(R.id.bAddGetTag);
        mAddGetTag.setOnClickListener(this);

        mGiveTagsLayout = (WrappingRelativeLayout) view.findViewById(R.id.wrlGivesTags);
        mGetTagsLayout = (WrappingRelativeLayout) view.findViewById(R.id.wrlGetsTags);

        // edit texts
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

        mEtPhone = (EditText) view.findViewById(R.id.etPhone);
        mEtOther = (EditText) view.findViewById(R.id.etOther);

        // checkboxes
        cbPm = (AppCompatCheckBox) view.findViewById(R.id.cbPm);

        cbPhone = (AppCompatCheckBox) view.findViewById(R.id.cbPhone);
        cbPhone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if( b ){
                    mEtPhone.setVisibility(View.VISIBLE);
                } else {
                    mEtPhone.setVisibility(View.INVISIBLE);
                }
            }
        });

        cbOther = (AppCompatCheckBox) view.findViewById(R.id.cbOther);
        cbOther.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if( b ){
                    mEtOther.setVisibility(View.VISIBLE);
                } else {
                    mEtOther.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bMakePhoto: {
                PackageManager packageManager = getActivity().getPackageManager();
                if (packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                        startActivityForResult(intent, CAMERA_REQUEST);
                    } else {
                        Toast.makeText(getContext(), R.string.no_camera_on_device, Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getContext(), R.string.no_camera_on_device, Toast.LENGTH_LONG).show();
                }
                break;
            }
            case R.id.bGetFromGallery: {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, GALLERY_REQUEST);
                break;
            }
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
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case GALLERY_REQUEST: {
                if (resultCode == Activity.RESULT_OK) {
                    Uri imageUri = data.getData();
                    mRecyclerAdapter.addPhoto(imageUri);
                }
                break;
            }
            case CAMERA_REQUEST: {
                if (resultCode == Activity.RESULT_OK) {
                    Uri imageUri = data.getData();
                    mRecyclerAdapter.addPhoto(imageUri);
                }
                break;
            }
        }
    }

    private void send(){
        String itemsPut = ""; // ...\n...\n...
        String itemsGet = ""; // ...\n...\n...
        String contactPm = ""; // {0,1}
        String contactPhone = ""; // phone
        String geo = ""; // place

        String[] photos; // image urls
    }
}
