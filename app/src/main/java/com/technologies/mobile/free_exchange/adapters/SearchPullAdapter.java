package com.technologies.mobile.free_exchange.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;
import com.technologies.mobile.free_exchange.R;
import com.technologies.mobile.free_exchange.activities.ImagePreviewActivity;
import com.technologies.mobile.free_exchange.activities.MainActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by diviator on 18.08.2016.
 */
public class SearchPullAdapter extends SimpleAdapter{

    Context context;

    ArrayList<HashMap<String,Object>> data;


    public SearchPullAdapter(Context context, ArrayList<HashMap<String,Object>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
        this.data = data;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view =  super.getView(position, convertView, parent);

        RoundedImageView roundedImageView = (RoundedImageView) view.findViewById(R.id.image);

        String imageUrl = (String) data.get(position).get(MainActivity.IMAGE);

        if( !imageUrl.isEmpty() ) {
            Picasso.with(context)
                    .load(imageUrl)
                    .fit() // will explain later
                    .into(roundedImageView);
        }else{
            roundedImageView.setImageResource(R.drawable.no_photo);
        }

        roundedImageView.setOnClickListener(new OnImageClickListener(position));

        return view;
    }

    private class OnImageClickListener implements View.OnClickListener{

        int position;

        public OnImageClickListener(int position){
            this.position = position;
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(context, ImagePreviewActivity.class);
            ArrayList<String> strings = (ArrayList<String>) data.get(position).get(MainActivity.IMAGES);
            String[] images = (String[]) strings.toArray();
            intent.putExtra(MainActivity.IMAGES,images);
            context.startActivity(intent);
        }
    }


}
