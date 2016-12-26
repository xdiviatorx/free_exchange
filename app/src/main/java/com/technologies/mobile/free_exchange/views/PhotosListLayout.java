package com.technologies.mobile.free_exchange.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.widget.ImageView;
import android.widget.TableRow;

import com.bumptech.glide.Glide;
import com.technologies.mobile.free_exchange.R;

/**
 * Created by diviator on 21.12.2016.
 */

public class PhotosListLayout extends AutomaticPhotoLayout {
    public PhotosListLayout(Context context) {
        super(context);
    }

    public PhotosListLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void addPhoto(String url) {
        //super.addPhoto(url);
        TableRow row;
        int height;

        initRow(THIRD_COLUMN_INDEX);
        row = (TableRow) getChildAt(photos);
        height = getDefaultHeight();

        row.addView(getImage(url,height));

        photos++;
    }

    @Override
    protected void initRow(int index) {
        //super.initRow(index);
        TableRow tableRow = getTableRow(getDefaultHeight());
        addView(tableRow);
    }

    protected int getDefaultHeight(){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,200,getResources().getDisplayMetrics());
        //return LayoutParams.WRAP_CONTENT;
    }


}
