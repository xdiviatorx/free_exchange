package com.technologies.mobile.free_exchange.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.bumptech.glide.Glide;
import com.technologies.mobile.free_exchange.R;
import com.technologies.mobile.free_exchange.listeners.OnImageClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by diviator on 07.11.2016.
 */

public class AutomaticPhotoLayout extends TableLayout {

    private static final String LOG_TAG = "AutomaticPhotoLayout";

    public static final int FIRST_COLUMN_INDEX = 0;
    public static final int SECOND_COLUMN_INDEX = 1;
    public static final int THIRD_COLUMN_INDEX = 2;

    OnImageClickListener onImageClickListener;

    ArrayList<ImageView> imageViewsList = new ArrayList<>();

    int photos = 0;

    public AutomaticPhotoLayout(Context context) {
        super(context);
    }

    public AutomaticPhotoLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void addPhotos(List<String> urlList){
        for( String url : urlList) {
            addPhoto(url);
        }
    }

    public void addPhoto(String url) {
        TableRow row;
        int height;
        if (photos < 3) {
            initRow(FIRST_COLUMN_INDEX);
            row = (TableRow) getChildAt(FIRST_COLUMN_INDEX);
            height = mGetTableHeight(FIRST_COLUMN_INDEX);
        } else if (photos < 5) {
            initRow(SECOND_COLUMN_INDEX);
            row = (TableRow) getChildAt(SECOND_COLUMN_INDEX);
            height = mGetTableHeight(SECOND_COLUMN_INDEX);
        } else {
            initRow(THIRD_COLUMN_INDEX);
            row = (TableRow) getChildAt(THIRD_COLUMN_INDEX);
            height = mGetTableHeight(THIRD_COLUMN_INDEX);
        }
        row.addView(getImage(url,height));

        photos++;
    }

    public void removeAll(){
        removeAllViews();
        imageViewsList.clear();
        photos=0;
    }

    protected void initRow(int index) {
        int count = getChildCount();
        if (count <= index) {
            TableRow tableRow = getTableRow(mGetTableHeight(index));
            addView(tableRow);
            Log.e(LOG_TAG,"row added");
        }
    }

    protected TableRow getTableRow(int height) {
        TableRow tableRow = new TableRow(getContext());
        TableLayout.LayoutParams lp = new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, height);
        tableRow.setLayoutParams(lp);

        return tableRow;
    }

    private int mGetTableHeight(int index) {
        switch (index) {
            case FIRST_COLUMN_INDEX: {
                return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,200,getResources().getDisplayMetrics());
            }
            case SECOND_COLUMN_INDEX: {
                return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,150,getResources().getDisplayMetrics());
            }
            case THIRD_COLUMN_INDEX: {
                return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,80,getResources().getDisplayMetrics());
            }
            default:{
                return 0;
            }
        }
    }

    protected ImageView getImage(String url, int height){
        ImageView imageView = new ImageView(getContext());
        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,height,1);
        lp.setMargins(3,3,3,3);
        imageView.setLayoutParams(lp);
        imageView.setMinimumWidth(1000);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        if( !url.isEmpty() ) {
            Glide.with(getContext()).load(url).into(imageView);
        }else{
            imageView.setImageResource(R.drawable.no_photo_big);
        }

        Log.e(LOG_TAG,"image added");
        setListener(imageView,photos);

        return imageView;
    }

    public void setOnImageClickListener(OnImageClickListener onImageClickListener) {
        this.onImageClickListener = onImageClickListener;
    }

    protected void setListener(ImageView imageView, final int imageIndex){
        imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if( onImageClickListener != null ){
                    onImageClickListener.onImageClicked(imageIndex);
                }
            }
        });
    }
}
