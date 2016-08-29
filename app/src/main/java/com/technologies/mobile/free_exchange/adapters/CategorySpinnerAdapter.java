package com.technologies.mobile.free_exchange.adapters;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.technologies.mobile.free_exchange.R;
import com.technologies.mobile.free_exchange.rest.ExchangeClient;
import com.technologies.mobile.free_exchange.rest.RetrofitService;
import com.technologies.mobile.free_exchange.rest.model.Categories;
import com.technologies.mobile.free_exchange.rest.model.CategoriesResponse;
import com.technologies.mobile.free_exchange.rest.model.Category;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by diviator on 26.08.2016.
 */
public class CategorySpinnerAdapter extends SimpleAdapter {

    private String LOG_TAG = "mySpinnerAdapter";

    public static String NAME = "NAME";
    public static String ID = "ID";

    ArrayList<HashMap<String,Object>> data;

    private Context context;

    public CategorySpinnerAdapter(Context context, ArrayList<HashMap<String,Object>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
        this.data = data;
        this.context = context;
    }

    public ArrayList<HashMap<String, Object>> getData() {
        return data;
    }

    public void initSpinner(){
        ExchangeClient client = RetrofitService.createService(ExchangeClient.class);

        Call<Categories> categoriesCall = client.getCategoriesList(ExchangeClient.apiKey);

        categoriesCall.enqueue(new Callback<Categories>() {
            @Override
            public void onResponse(Call<Categories> call, Response<Categories> response) {
                Categories categories = response.body();
                CategoriesResponse categoriesResponse = categories.getCategoriesResponse();
                Category[] categoriesArray = categoriesResponse.getCategories();

                for( Category category : categoriesArray ){

                    //if( category.getDisplay() == 1 ) {
                        HashMap<String, Object> item = new HashMap<>();
                        item.put(NAME,category.getName());
                        item.put(ID,category.getId());
                        data.add(item);
                    //}

                }
                notifyDataSetChanged();
                Log.e(LOG_TAG,"SUCCESS");
            }

            @Override
            public void onFailure(Call<Categories> call, Throwable t) {
                Log.e(LOG_TAG,"FAILURE");
                Log.e(LOG_TAG,t.toString());
            }
        });
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View view = View.inflate(context, R.layout.spinner_dropdown_item, null);

        TextView name = (TextView) view.findViewById(R.id.tv);
        name.setText(data.get(position).get(NAME).toString());

        LinearLayout divider = new LinearLayout(context);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,1);
        divider.setBackgroundResource(R.color.colorMediumGray);
        divider.setLayoutParams(lp);

        ((LinearLayout) view).addView(divider);

        return view;
    }
}
