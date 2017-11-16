package com.example.asadullahsansi.lifeadmin.Util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.asadullahsansi.lifeadmin.R;
import com.tapadoo.alerter.Alerter;

import dmax.dialog.SpotsDialog;

/**
 * Created by asadullahsansi on 11/9/17.
 */

public class Util_Func {

    public static String category = "Category";
    public static String subCategory = "SubCategory";
    public static String item = "Item";

    public static boolean isNetworkAvaliable(Context _context) {
        ConnectivityManager _connectivityManager = (ConnectivityManager) _context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if ((_connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE) != null && _connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED)
                || (_connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI) != null && _connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                .getState() == NetworkInfo.State.CONNECTED)) {
            return true;
        } else {
            return false;
        }
    }

    public static void Alert(Context _context, String title, String content) {
        Alerter.create((Activity) _context)
                .setTitle(title)
                .setText(content)
                .setBackgroundColorRes(R.color.colorAccent)
                .setIcon(R.mipmap.ic_launcher)
                .setDuration(1700)
                .show();
    }

}
