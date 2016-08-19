package com.technologies.mobile.free_exchange.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;
import com.technologies.mobile.free_exchange.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by diviator on 18.08.2016.
 */
public class SearchPullAdapter extends SimpleAdapter {

    Context context;

    ArrayList<HashMap<String,Object>> data;

    private String IMAGE = "IMAGE";

    public SearchPullAdapter(Context context, ArrayList<HashMap<String,Object>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
        this.data = data;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view =  super.getView(position, convertView, parent);

        RoundedImageView roundedImageView = (RoundedImageView) view.findViewById(R.id.image);

        String imageUrl = (String) data.get(position).get(IMAGE);

        if( !imageUrl.isEmpty() ) {
            Picasso.with(context)
                    .load(imageUrl)
                    .fit() // will explain later
                    .into(roundedImageView);
        }else{
            roundedImageView.setImageResource(R.drawable.no_photo);
        }

        return view;
    }
}
