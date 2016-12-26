package com.technologies.mobile.free_exchange.fragments;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.technologies.mobile.free_exchange.MyApplication;
import com.technologies.mobile.free_exchange.R;
import com.technologies.mobile.free_exchange.adapters.RecyclerAddedImagesAdapter;
import com.technologies.mobile.free_exchange.rest.SendAsyncTask;
import com.technologies.mobile.free_exchange.views.WrappingRelativeLayout;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by diviator on 24.08.2016.
 */
public class AddFragment extends Fragment implements View.OnClickListener {

    private static final String LOG_TAG = "instance";

    public static final String ADD_FRAGMENT_TAG = "ADD_FRAGMENT_TAG";

    private static final String GIVES = "GIVES";
    private static final String GETS = "GETS";

    private static final int GALLERY_REQUEST = 347;
    private static final int CAMERA_REQUEST = 719;

    public static final String PM = "PM";
    public static final String PHONE = "PHONE";
    public static final String OTHER = "OTHER";

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

    EditText mEtPlace;

    AppCompatCheckBox cbPm;
    AppCompatCheckBox cbPhone;
    AppCompatCheckBox cbOther;

    EditText mEtPhone;
    EditText mEtOther;

    AppCompatButton mBSend;

    private Uri mJustPhoto;

    private Tracker mTracker;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //restore(savedInstanceState);
        mTracker = ((MyApplication) getActivity().getApplication()).getDefaultTracker();
        mTracker.setScreenName(MyApplication.ADDING_CATEGORY);
        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory(MyApplication.ADDING_CATEGORY)
                .setAction(MyApplication.LAUNCHED_ACTION).build());
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

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        Log.e(LOG_TAG,"ON RESTORE FRAGMENT");
    }

    private void restore(Bundle savedInstanceState){
        if( savedInstanceState != null ){
            Log.e(LOG_TAG,savedInstanceState.getString("key","no"));
            ArrayList<String> gives = savedInstanceState.getStringArrayList(GIVES);
            if( gives != null ) {
                Log.e(LOG_TAG, "GIVES LENGTH = " + gives.size());
                mGiveTagsLayout.addTags(savedInstanceState.getStringArrayList(GIVES));
                for( String s : gives ){
                    Log.e(LOG_TAG,s);
                }
            }

            //mGetTagsLayout.addTags(savedInstanceState.getStringArrayList(GETS));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.e(LOG_TAG,"SAVING ADD FRAGMENT INSTANCE");
        outState.putString("key","value");
        outState.putStringArrayList(GIVES,getGives());
        outState.putStringArrayList(GETS,getGets());
        super.onSaveInstanceState(outState);
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
                if (text.length() != 0 &&
                        ( text.charAt(text.length() - 1) == ','
                                || text.charAt(text.length() - 1) == '.'
                                || text.charAt(text.length() - 1) == ' ' ) ) {
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
                if (text.length() != 0 && ( text.charAt(text.length() - 1) == ','
                        || text.charAt(text.length() - 1) == '.'
                        || text.charAt(text.length() - 1) == ' ' ) ) {
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

        mEtPlace = (EditText) view.findViewById(R.id.etPlace);

        mEtPhone = (EditText) view.findViewById(R.id.etPhone);
        mEtOther = (EditText) view.findViewById(R.id.etOther);

        // checkboxes
        cbPm = (AppCompatCheckBox) view.findViewById(R.id.cbPm);

        cbPhone = (AppCompatCheckBox) view.findViewById(R.id.cbPhone);
        cbPhone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
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
                if (b) {
                    mEtOther.setVisibility(View.VISIBLE);
                } else {
                    mEtOther.setVisibility(View.INVISIBLE);
                }
            }
        });

        mBSend = (AppCompatButton) view.findViewById(R.id.bSend);
        mBSend.setOnClickListener(this);
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "photo_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        return image;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bMakePhoto: {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // Ensure that there's a camera activity to handle the intent
                mJustPhoto = null;
                if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    // Create the File where the photo should go
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                    } catch (IOException ex) {
                        // Error occurred while creating the File
                    }
                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        Uri photoURI = Uri.fromFile(photoFile);
                        mJustPhoto = Uri.fromFile(photoFile);
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(takePictureIntent, CAMERA_REQUEST);
                    }
                }
                /*
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
                */
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
            case R.id.bSend: {
                if( validate() ) {
                    SendAsyncTask send = new SendAsyncTask(this);
                    send.execute();
                    mTracker.send(new HitBuilders.EventBuilder()
                            .setCategory(MyApplication.ADDING_CATEGORY)
                            .setAction(MyApplication.ADDING_ACTION).build());
                }else{
                    Toast.makeText(getContext(),R.string.fields_missed,Toast.LENGTH_LONG).show();
                }
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
                    if (mJustPhoto != null) {
                        Uri imageUri = mJustPhoto;
                        mRecyclerAdapter.addPhoto(imageUri);
                    }
                }
                break;
            }
        }
    }

    public ArrayList<Uri> getPhotos() {
        ArrayList<Uri> uris = new ArrayList<>();

        List<Map<String, Object>> data = mRecyclerAdapter.getData();
        for (Map<String, Object> item : data) {
            uris.add((Uri) item.get(RecyclerAddedImagesAdapter.URI));
        }

        return uris;
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

    public String getPlace() {
        return mEtPlace.getText().toString();
    }

    public Map<String, Object> getContacts() {
        Map<String, Object> contacts = new HashMap<>();
        contacts.put(PM, false);
        if (cbPm.isChecked()) {
            contacts.put(PM, true);
        }
        contacts.put(PHONE, "");
        if (cbPhone.isChecked()) {
            contacts.put(PHONE, mEtPhone.getText().toString());
        }
        contacts.put(OTHER, "");
        if (cbOther.isChecked()) {
            contacts.put(OTHER, mEtOther.getText().toString());
        }
        return contacts;
    }

    private boolean validate() {
        return getGets().size() != 0 && getGives().size() != 0;
    }
}
