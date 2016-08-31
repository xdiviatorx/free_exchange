package com.technologies.mobile.free_exchange.views;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.technologies.mobile.free_exchange.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by diviator on 31.08.2016.
 */
public class WrappingRelativeLayout extends RelativeLayout implements View.OnClickListener {

    public static final String LOG_TAG = "myWrappedLayout";

    Context mContext;

    LayoutInflater mInflater;

    private int newId = 0;

    private Map<Integer, String> mTags;

    public WrappingRelativeLayout(Context context) {
        super(context);
        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mTags = new HashMap<>();
    }

    public WrappingRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mTags = new HashMap<>();
    }

    public void addTag(String tag) {
        View view = mInflater.inflate(R.layout.child_tag, this, false);

        int id = getNewId();
        view.setId(id);
        addView(view);

        mTags.put(id, tag);

        TextView tvTag = (TextView) view.findViewById(R.id.tvTag);
        tvTag.setText(tag);

        TextView bDeleteTag = (TextView) view.findViewById(R.id.bDeleteTag);
        bDeleteTag.setOnClickListener(this);

        childrenReposition();
        Log.e(LOG_TAG,"Tag added");
    }

    private int getNewId() {
        newId++;
        return newId;
    }

    private void childrenReposition() {
        int lineWidth = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View curItem = getChildAt(i);
            RelativeLayout.LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            curItem.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
            lineWidth += curItem.getMeasuredWidth();

            if (i > 0) {
                int prevId = getChildAt(i - 1).getId();

                if (lineWidth < getWidth()) {
                    layoutParams.addRule(RelativeLayout.RIGHT_OF, prevId);
                    layoutParams.addRule(RelativeLayout.ALIGN_TOP, prevId);
                } else {
                    lineWidth = curItem.getMeasuredWidth();
                    layoutParams.addRule(RelativeLayout.BELOW, prevId);
                    layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                }

            } else {
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            }
            curItem.setLayoutParams(layoutParams);
        }
    }

    public void removeTag(int id) {
        View view = findViewById(id);
        removeView(view);

        mTags.remove(id);

        childrenReposition();
    }

    public void clearAll() {
        removeAllViews();
        mTags.clear();
    }

    public Map<Integer, String> getTags() {
        return mTags;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bDeleteTag: {
                int removeId = ((View) view.getParent()).getId();
                removeTag(removeId);
                break;
            }
        }
    }
}
