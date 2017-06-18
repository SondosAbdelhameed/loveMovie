package com.example.heroshe.lovemovies;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by herohe on 30/04/16.
 */
public class Utils {

    public static boolean isNetworkAvailable(Activity activity) {
        ConnectivityManager mConnectivity =
                (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mNetworkInfo = mConnectivity.getActiveNetworkInfo();
        return mNetworkInfo != null && mNetworkInfo.isConnectedOrConnecting();
    }
}
