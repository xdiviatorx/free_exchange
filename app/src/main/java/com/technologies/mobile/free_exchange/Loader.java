package com.technologies.mobile.free_exchange;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

/**
 * Created by diviator on 03.09.2016.
 */
public class Loader {

    private static ProgressDialog mDialog = null;
    private static boolean mVisible = false;

    public static void showSender(Context context) {
        if (!mVisible) {
            mDialog = createProgressDialog(context);
            if (!isOnline(context)) {
                createAlertDialog(context).show();
                //Toast.makeText(context, R.string.no_internet_connection, Toast.LENGTH_LONG).show();
                return;
            }
            mDialog.show();
            mVisible = true;
        }
    }

    private static ProgressDialog createProgressDialog(Context context) {
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setCancelable(false);
        dialog.setMessage(context.getString(R.string.sending));
        return dialog;
    }

    private static boolean isAlertDialogShown = false;

    private static AlertDialog createAlertDialog(Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.conn_error)
                .setMessage(R.string.conn_check)
                .setCancelable(true)
                .setPositiveButton(R.string.close,null)
                .setIcon(R.drawable.error);
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                isAlertDialogShown = false;
            }
        });
        return builder.create();
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

    private static boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public static void showProgressBar(Context context){
        ProgressBar mProgressBar = (ProgressBar) ((Activity) context).findViewById(R.id.pb);
        ProgressBar dialogProgressBar = (ProgressBar) ((Activity) context).findViewById(R.id.pb_dialog);
        ProgressBar subsExsProgressBar = (ProgressBar) ((Activity) context).findViewById(R.id.pb_subs_exs);
        alertIfNoInternet(context);
        if( mProgressBar != null ) {
            mProgressBar.setVisibility(View.VISIBLE);
            Log.e("progress","NOT NULL mProgressBar");
        }
        if( dialogProgressBar != null ) {
            dialogProgressBar.setVisibility(View.VISIBLE);
            Log.e("progress","NOT NULL dialogProgressBar");
        }
        if( subsExsProgressBar != null ) {
            subsExsProgressBar.setVisibility(View.VISIBLE);
            Log.e("progress","NOT NULL dialogProgressBar");
        }
        //Log.e("progress","show");
    }

    public static void alertIfNoInternet(Context context){
        if( !isOnline(context) && !isAlertDialogShown){
            createAlertDialog(context).show();
            isAlertDialogShown = true;
        }
    }

    public static void hideProgressBar(Context context){
        ProgressBar mProgressBar = (ProgressBar) ((Activity) context).findViewById(R.id.pb);
        ProgressBar dialogProgressBar = (ProgressBar) ((Activity) context).findViewById(R.id.pb_dialog);
        ProgressBar subsExsProgressBar = (ProgressBar) ((Activity) context).findViewById(R.id.pb_subs_exs);
        if( mProgressBar != null ) {
            mProgressBar.setVisibility(View.GONE);
        }
        if( dialogProgressBar != null ) {
            dialogProgressBar.setVisibility(View.GONE);
        }
        if( subsExsProgressBar != null ) {
            subsExsProgressBar.setVisibility(View.GONE);
        }
    }

}
