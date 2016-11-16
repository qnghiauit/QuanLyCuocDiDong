package com.uit.nst95.quanlycuocdidong.tools;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by TNS on 11/12/2016.
 */

public class NetworkChecker {

    // private constructor
    private NetworkChecker() {

    }

    /**
     * Method to check if device is currently connecting to the internet ?
     *
     * @return true if device connected to internet, otherwise returns false
     */
    public static boolean isInternetConnectionAvailable(Context context) {
        ConnectivityManager connectivityManager;
        connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
