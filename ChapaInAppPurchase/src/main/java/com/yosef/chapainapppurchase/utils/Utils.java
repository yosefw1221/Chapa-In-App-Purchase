package com.yosef.chapainapppurchase.utils;

import android.content.Context;
import android.net.ConnectivityManager;

public class Utils {

    public static boolean isConnectionAvailable(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return manager.getActiveNetworkInfo() != null && manager.getActiveNetworkInfo().isConnected();
    }
}
