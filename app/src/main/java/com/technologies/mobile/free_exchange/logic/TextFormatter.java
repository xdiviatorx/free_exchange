package com.technologies.mobile.free_exchange.logic;

import android.content.Context;

/**
 * Created by diviator on 30.08.2016.
 */
public class TextFormatter {

    Context mContext;

    public TextFormatter(Context context){
        mContext = context;
    }

    public String highlight(String string, String textToHighlight){
        return string.replaceAll(textToHighlight,"<font color='red'>"+textToHighlight+"</font>");
        //"<font color='red'>"+textToHighlight+"</font>"
        //"<span style='background-color: #FFFF00'>" + textToHighlight + "</span>"
    }
}
