package com.technologies.mobile.free_exchange.model;

import android.content.Context;

import com.technologies.mobile.free_exchange.R;

import java.util.ArrayList;

/**
 * Created by diviator on 13.01.2017.
 */

public class CategoriesManager {

    public static int CATEGORY_ID_WITHOUT_CATEGORY = 0;
    public static int CATEGORY_ID_CLOTHES = 1;
    public static int CATEGORY_ID_SHOES = 2;
    public static int CATEGORY_ID_FOR_CHILDREN = 3;
    public static int CATEGORY_ID_ALL_FOR_BEAUTY = 4;
    public static int CATEGORY_ID_TECHNIQUE = 5;
    public static int CATEGORY_ID_ALL_FOR_HOME = 6;
    public static int CATEGORY_ID_BOOKS = 7;
    public static int CATEGORY_ID_AUTO_AND_MOTO = 8;
    public static int CATEGORY_ID_OTHERS = 9;
    public static int CATEGORY_ID_SERVICES = 10;

    public CategoriesManager(){

    }

    public ArrayList<Category> getCategories(Context context){
        String[] names = context.getResources().getStringArray(R.array.categoriesNames);
        int[] ids = context.getResources().getIntArray(R.array.categoriesIds);

        ArrayList<Category> categories = new ArrayList<>();

        for( int i = 0; i < names.length; i++){
            categories.add(new Category(names[i],ids[i]));
        }

        return categories;
    }

    public Category getById(Context context, int catsId){
        ArrayList<Category> categories = getCategories(context);
        for( Category category : categories){
            if( category.getId() == catsId ){
                return category;
            }
        }
        return new Category("Undefined",catsId);
    }

    public int getIndexInSpinnerById(Context context, int catsId){
        ArrayList<Category> categories = getCategories(context);
        for( int i = 0; i < categories.size(); i++){
            if( categories.get(i).getId() == catsId ){
                return i;
            }
        }
        return 0;
    }

}
