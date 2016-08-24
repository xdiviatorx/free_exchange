package com.technologies.mobile.free_exchange.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.technologies.mobile.free_exchange.R;
import com.vk.sdk.VKSdk;

/**
 * Created by diviator on 24.08.2016.
 */
public class NavigationRVAdapter extends RecyclerView.Adapter<NavigationRVAdapter.MyViewHolder> {

    private static int TYPE_HEADER = 0;
    private static int TYPE_ITEM = 1;

    private Context context;
    private String[] categories;
    private int[] icons;
    private String photoUrl;
    private String name;

    public NavigationRVAdapter(Context context, String[] categories, int[] icons, String photoUrl, String name){
        this.context = context;
        this.icons = icons;
        this.categories = categories;
        //TODO get photo and name
        this.photoUrl = photoUrl;
        this.name = name;
    }

    public void setPersonalData(String name, String photoUrl){
        this.name = name;
        this.photoUrl = photoUrl;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if( viewType == TYPE_HEADER ){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.header,parent,false);
            return new MyViewHolder(view,viewType);
        }
        if( viewType == TYPE_ITEM ){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.navigation_item,parent,false);
            return new MyViewHolder(view,viewType);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if( holder.holderId == TYPE_ITEM ){
            holder.icon.setImageResource(icons[position-1]);
            holder.category.setText(categories[position-1]);
        }
        if( holder.holderId == TYPE_HEADER ){
            holder.name.setText(name);
            if( !photoUrl.isEmpty() ) {
                Picasso.with(context)
                        .load(photoUrl)
                        .into(holder.photo);
            }
        }
    }

    @Override
    public int getItemCount() {
        return categories.length+1;
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder{

        int holderId;
        ImageView photo;
        TextView name;
        ImageView icon;
        TextView category;
        View itemView;

        public MyViewHolder(View itemView, int viewType) {
            super(itemView);
            this.itemView = itemView;
            if( viewType == TYPE_HEADER ){
                holderId = TYPE_HEADER;
                name = (TextView) itemView.findViewById(R.id.headerName);
                photo = (ImageView) itemView.findViewById(R.id.headerPhoto);
            }else{
                holderId = TYPE_ITEM;
                icon = (ImageView) itemView.findViewById(R.id.navigationIcon);
                category = (TextView) itemView.findViewById(R.id.navigationText);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if( isPositionHeader(position) ){
            return TYPE_HEADER;
        }else{
            return TYPE_ITEM;
        }
    }

    private boolean isPositionHeader(int position){
        return position == 0;
    }
}
