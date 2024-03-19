package com.yosef.chapainapppurchase.utils;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class e {

    public static String a(final String s, String key) {
        try {
            final Cipher instance;
            (instance = Cipher.getInstance("AES/GCM/NoPadding")).init(1, new SecretKeySpec(SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1").generateSecret(new PBEKeySpec(key.toCharArray(), key.getBytes(), 128, 256)).getEncoded(), "AES/GCM/NoPadding"));
            return a(instance.doFinal(s.getBytes()));
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static String a(final String s) {
        try {
            final Cipher instance;
            (instance = Cipher.getInstance("AES/GCM/NoPadding")).init(1, new SecretKeySpec(SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1").generateSecret(new PBEKeySpec(s.toCharArray(), s.getBytes(), 128, 256)).getEncoded(), "AES/GCM/NoPadding"));
            return a(instance.doFinal(s.getBytes()));
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static String b(final String s, String key) {
        try {
            final Cipher instance;
            (instance = Cipher.getInstance("AES/GCM/NoPadding")).init(2, new SecretKeySpec(SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1").generateSecret(new PBEKeySpec(key.toCharArray(), key.getBytes(), 128, 256)).getEncoded(), "AES/GCM/NoPadding"));
            return new String(instance.doFinal(c(s)));
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private static byte[] c(final String s) {
        final int n;
        final byte[] array = new byte[n = s.length() / 2];
        for (int i = 0; i < n; ++i) {
            array[i] = Integer.valueOf(s.substring(2 * i, 2 * i + 2), 16).byteValue();
        }
        return array;
    }

    private static String a(byte[] array) {
        final StringBuilder sb = new StringBuilder(2 * array.length);
        for (int length = (array).length, i = 0; i < length; ++i) {
            final byte b = array[i];
            sb.append("0123456789ABCDEF".charAt(b >> 4 & 0xF)).append("0123456789ABCDEF".charAt(b & 0xF));
        }
        return sb.toString();
    }
}
