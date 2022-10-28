package com.yosef.chapainapppurchase;

import android.util.Log;

class Logger {
    static boolean debugMode;

    static void initialize(boolean debug) {
        debugMode = debug;
    }

    static void d(String tag, String message) {
        if (debugMode) Log.d(tag, message);
    }

    static void i(String tag, String message) {
        if (debugMode) Log.i(tag, message);
    }

    static void v(String tag, String message) {
        if (debugMode) Log.v(tag, message);
    }

    static void e(String tag, String message) {
        Log.e(tag, message);
    }

    static void e(String tag, String message, Throwable throwable) {
        Log.e(tag, message, throwable);
    }

    static void w(String tag, String message) {
        Log.w(tag, message);
    }

    static void w(String tag, String message, Throwable throwable) {
        Log.w(tag, message, throwable);
    }

}