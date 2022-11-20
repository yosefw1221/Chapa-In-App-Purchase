package com.yosef.chapainapppurchase.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.net.ConnectivityManager;
import android.os.Build;

import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

public class Utils {
    private static final String validChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789_-";

    /**
     * Check if the device is connected to the internet
     *
     * @param context Context of the application
     * @return true if the device is connected to the internet
     */
    public static boolean isConnectionAvailable(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return manager.getActiveNetworkInfo() != null && manager.getActiveNetworkInfo().isConnected();
    }

    /**
     * Gets android id of the device
     *
     * @return android id of the device if available otherwise return null
     */
    @SuppressLint("HardwareIds")
    public static String getAndroidId(Context context) {
        String BUGGY_ANDROID_ID = "9774d56d682e549c";
        String androidId = android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        if (androidId.equals(BUGGY_ANDROID_ID)) return null;
        return androidId;
    }

    /**
     * Gets pseudo device id of the device
     *
     * @return 14 character of device pseudo id
     */
    public static String getPseudoDeviceId() {
        return charAt(Build.BOARD, 1, 3, 5) + charAt(Build.BRAND, 3, 7, 5) + charAt(Build.CPU_ABI, 1, 3, 2, 9) + charAt(Build.MODEL, 1, 6, 2, 7);
    }

    public static String charAt(String val, int... index) {
        StringBuilder result = new StringBuilder();
        Pattern validChar = Pattern.compile("[a-zA-Z0-9_-]");
        if (val != null) {
            int size = val.length();
            for (int i : index) {
                if (i < size && validChar.matcher(String.valueOf(val.charAt(i))).matches())
                    result.append(val.charAt(i));
            }
        }
        return result.toString();
    }

    /**
     * Check if the application is in debug mode
     *
     * @param context Context of the application
     * @return true if the application is in debug mode
     */
    public static boolean debugMode(@NotNull Context context) {
        return 0 != (context.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE);
    }

    /**
     * Gets the name of the application
     *
     * @param context Context of the application
     * @return Application name
     */
    public static String _getAppName(Context context) {
//        String appName = context.getApplicationInfo().loadLabel(context.getPackageManager()).toString();
        String packageName = context.getPackageName();
        return packageName.substring(packageName.lastIndexOf(".") + 1);
    }
}
