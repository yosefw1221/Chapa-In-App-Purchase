package com.yosef.chapainapppurchase.utils;

import android.content.Context;

public class Cipher {
    /**
     * Encrypts the given string using the given key.
     *
     * @param data The string to encrypt.
     * @param key  The key to use for encryption.
     * @return The encrypted string.
     */
    public static String encrypt(String data, String key) {
        return e.a(data, key);
    }

    /**
     * Decrypts the given string using the given key.
     *
     * @param encrypted The string to decrypt.
     * @param key       The key to use for decryption.
     * @return The decrypted string.
     */
    public static String decrypt(String encrypted, String key) {
        return e.b(encrypted, key);
    }

    /**
     * Encrypts the given string using generated key.
     *
     * @param context application context
     * @param data    The string to encrypt.
     * @return The encrypted string.
     */
    public static String encrypt(Context context, String data) {
        return e.a(data, c(context));
    }

    /**
     * Decrypts the given string using generated key.
     *
     * @param context   application context
     * @param encrypted The string to decrypt.
     * @return The decrypted string.
     */
    public static String decrypt(Context context, String encrypted) {
        return e.b(encrypted, c(context));
    }

    private static String c(Context context) {
        String p = context.getPackageName();
        final StringBuilder sb = new StringBuilder();
        byte[] b = p.getBytes();
        byte len = (byte) (p.length());
        for (int i = 0; i < p.length(); i++)
            sb.append(p.charAt(Math.abs((int) (b[i] - ~b[p.length() - i - 1]) % len)));
        return sb.toString();
    }
}
