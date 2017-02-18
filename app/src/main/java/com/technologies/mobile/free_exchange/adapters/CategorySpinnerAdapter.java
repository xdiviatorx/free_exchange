package com.technologies.mobile.free_exchange.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.technologies.mobile.free_exchange.R;
import com.technologies.mobile.free_exchange.model.CategoriesManager;
import com.technologies.mobile.free_exchange.model.Category;

import java.util.ArrayList;

/**
 * Created by diviator on 26.08.2016.
 */
public class CategorySpinnerAdapter extends BaseAdapter {

    private String LOG_TAG = "mySpinnerAdapter";

    public static String NAME = "NAME";
    public static String ID = "ID";

    ArrayList<Category> mData;

    private Context mContext;
    private int mResource;

    public CategorySpinnerAdapter(Context context, int resource) {
        this.mContext = context;
        this.mResource = resource;
        mData = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Category getItem(int i) {
        return mData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return getItem(i).getId();
    }

    public ArrayList<Category> getData() {
        return mData;
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(mContext).inflate(mResource,parent,false);

        TextView name = (TextView) view.findViewById(R.id.tv);
        name.setText(getItem(pos).getName());

        return view;
    }

    public void initSpinner(){
        CategoriesManager categoriesManager = new CategoriesManager();
        mData.addAll(categoriesManager.getCategories(mContext));
        notifyDataSetChanged();
    }

    @Override
    public View getDropDownView(int pos, View convertView, ViewGroup parent) {
        View view = View.inflate(mContext, R.layout.spinner_dropdown_item, null);

        TextView name = (TextView) view.findViewById(R.id.tv);
        name.setText(getItem(pos).getName());

        LinearLayout divider = new LinearLayout(mContext);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,1);
        divider.setBackgroundResource(R.color.colorMediumGray);
        divider.setLayoutParams(lp);

        ((LinearLayout) view).addView(divider);

        return view;
    }
}
