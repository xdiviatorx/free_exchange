package com.technologies.mobile.free_exchange;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

/**
 * Created by diviator on 03.09.2016.
 */
public class Loader {

    private static ProgressDialog mDialog = null;
    private static boolean mVisible = false;
    private static ProgressBar mProgressBar = null;

    public static void showSender(Context context) {
        if (!mVisible) {
            mDialog = createDialog(context);
            if (!isOnline(context)) {
                Toast.makeText(context, R.string.no_internet_connection, Toast.LENGTH_LONG).show();
                return;
            }
            mDialog.show();
            mVisible = true;
        }
    }

    public static ProgressDialog createDialog(Context context) {
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setCancelable(false);
        dialog.setMessage(context.getString(R.string.sending));
        return dialog;
    }

    public static void hideSender() {
        if (mDialog == null) {
            return;
        }
        if (mVisible) {
            mDialog.dismiss();
            mVisible = false;
        }
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public static void showProgressBar(Context context){
        mProgressBar = (ProgressBar) ((Activity) context).findViewById(R.id.pb);
        if( mProgressBar != null ) {
            mProgressBar.setVisibility(View.VISIBLE);
        }
    }

    public static void hideProgressBar(Context context){
        mProgressBar = (ProgressBar) ((Activity) context).findViewById(R.id.pb);
        if( mProgressBar != null ) {
            mProgressBar.setVisibility(View.GONE);
        }
    }

}
