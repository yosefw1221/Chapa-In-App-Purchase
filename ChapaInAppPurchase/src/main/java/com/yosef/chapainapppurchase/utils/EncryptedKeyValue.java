package com.yosef.chapainapppurchase.utils;

import android.app.backup.BackupManager;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.Objects;

/**
 * Responsible for a key-value store that is encrypted using AES-256.
 */
public class EncryptedKeyValue {
    public static final String PREF_CHAPA_PAYED_ITEM = "chapa_payed_item";
    public static final String PREF_CHAPA = "chapa_pref";
    public static final String CHAPA_TRANSACTION = "chapa_transaction";
    public static String n;
    private static String k;
    private final SharedPreferences preferences;
    private final SharedPreferences.Editor editor;
    private final BackupManager backupManager;


    public EncryptedKeyValue(Context context, @NonNull String preferenceName) {
        preferences = context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE);
        editor = preferences.edit();
        backupManager = new BackupManager(context);
        k = context.getPackageName();
        n = Utils.getPseudoDeviceId();
    }

    public static String c(String d) {
        final StringBuilder sb = new StringBuilder();
        byte[] b = d.getBytes();
        byte len = (byte) (k.length());
        int size = b.length;
        int lenP = (byte) n.length();
        for (int i = 0; i < Math.min(size, 8); i++)
            sb.append(k.charAt(Math.abs((int) (b[i] - ~b[size - i - 1]) % len))).append(n.charAt((int) (b[i] >> 2) % lenP));
        return sb.toString();
    }

    public synchronized void putValue(String key, String value) {
        editor.putString(e.a(key), e.a(value, c(key)));
        editor.commit();
        backupManager.dataChanged();
    }

    public synchronized void putValue(String key, int value) {
        editor.putString(e.a(key), e.a(Integer.toString(value), c(key)));
        editor.commit();
        backupManager.dataChanged();
    }

    public synchronized void putValue(String key, double value) {
        editor.putString(e.a(key), e.a(Double.toString(value), c(key)));
        editor.commit();
        backupManager.dataChanged();
    }

    public synchronized void putValue(String key, float value) {
        editor.putString(e.a(key), e.a(Float.toString(value), c(key)));
        editor.commit();
        backupManager.dataChanged();
    }

    public synchronized void putValue(String key, Boolean value) {
        editor.putString(e.a(key), e.a(Boolean.toString(value), c(key)));
        editor.commit();
        backupManager.dataChanged();
    }

    public synchronized void putValue(String key, long defaultValue) {
        editor.putString(e.a(key), e.a(Long.toString(defaultValue), c(key)));
        editor.commit();
        backupManager.dataChanged();
    }

    public synchronized String getValue(String key, String defaultValue) {
        try {
            String temp = preferences.getString(e.a(key), null);
            if (temp == null) return defaultValue;
            return e.b(temp, c(key));
        } catch (Exception e) {
            return defaultValue;
        }
    }

    private synchronized double getNumber(String key, double defaultValue) {
        try {
            String temp = preferences.getString(e.a(key), null);
            if (temp == null) return defaultValue;
            return Double.parseDouble(Objects.requireNonNull(e.b(temp, c(key))));
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public synchronized int getValue(String key, int defaultValue) {
        return (int) getNumber(key, defaultValue);
    }

    public synchronized double getValue(String key, double defaultValue) {
        return getNumber(key, defaultValue);
    }

    public synchronized float getValue(String key, float defaultValue) {
        return (float) getNumber(key, defaultValue);
    }

    public synchronized boolean getValue(String key, boolean defaultValue) {
        try {
            String temp = preferences.getString(e.a(key), null);
            if (temp == null) return defaultValue;
            String bool = e.b(temp, c(key));
            if (bool != null && bool.equals("true")) return true;
            if (bool != null && bool.equals("false")) return false;
            return defaultValue;
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public synchronized long getValue(String key, long defaultValue) {
        try {
            String temp = preferences.getString(e.a(key), null);
            if (temp == null) return defaultValue;
            return Long.parseLong(Objects.requireNonNull(e.b(temp, c(key))));
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public synchronized void removeValue(String key) {
        editor.remove(e.a(key));
        editor.commit();
        backupManager.dataChanged();
    }

    /**
     * Gets Encrypted KeyValue stored
     *
     * @return JSONObject of encrypted key value pairs
     */
    public JSONObject getAllEncryptedData() {
        try {
            JSONObject pref = new JSONObject();
            for (String key : preferences.getAll().keySet()) {
                pref.put(key, preferences.getString(key, ""));
            }
            return pref;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Gets Encrypted KeyValue stored
     *
     * @return JSON String of encrypted key value pairs
     */
    public String getAllEncryptedDataAsJSONString() {
        JSONObject encryptedData = getAllEncryptedData();
        if (encryptedData == null) return null;
        return encryptedData.toString();
    }

    /**
     * Restores EncryptedKeyValue from Encrypted JSON String
     *
     * @param encryptedJSON Encrypted KeyValue JSONObject
     * @throws JSONException if the JSON String is invalid
     */
    public synchronized void restoreEncryptedPreference(JSONObject encryptedJSON) throws JSONException {
        Iterator<String> keys = encryptedJSON.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            editor.putString(key, encryptedJSON.getString(key));
        }
        editor.commit();
    }

    /**
     * Restores EncryptedKeyValue from Encrypted JSON String
     *
     * @param encryptedJSONString Encrypted KeyValue JSON String
     * @throws JSONException if the JSON String is invalid
     */
    public synchronized void restoreEncryptedPreference(String encryptedJSONString) throws JSONException {
        JSONObject encryptedJSON = new JSONObject(encryptedJSONString);
        restoreEncryptedPreference(encryptedJSON);
    }
}
