package com.roadstar.customerr.common.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import androidx.annotation.NonNull;

/**
 * Created by $Bilal on 11/6/2015.
 */
public class NetworkUtil {

    // if available and reachable -> true ; false, otherwise
    public static boolean isNetworkReachable() {
        return false;
    }


    public static boolean isNetworkConnected(@NonNull Context context) {
        if (context != null) {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            return netInfo != null && netInfo.isConnected();
        }
        return false;
    }


    public static boolean hasNetworkConnection(@NonNull Context context) {
        if (context != null) {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo wifiNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (wifiNetwork != null && wifiNetwork.isConnected()) {
                return true;
            }
            NetworkInfo mobileNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            return mobileNetwork != null && mobileNetwork.isConnected();
        }
        return false;
    }

    public static boolean hasWLANConnection(Context context){
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo network = cm.getActiveNetworkInfo();
        if (network == null)
            return false;
        return network.getType() == ConnectivityManager.TYPE_WIFI && network.isConnected();
    }

    public static boolean hasMobileConnection(Context context){
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo == null)
            return false;
        return networkInfo.getType() == ConnectivityManager.TYPE_MOBILE && networkInfo.isConnected();
    }
}
