package com.yosef.chapainapppurchase;

import android.content.Context;

import androidx.annotation.NonNull;

import com.yosef.chapainapppurchase.enums.ItemProperties;
import com.yosef.chapainapppurchase.utils.EncryptedKeyValue;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Gets and Manipulate Payed Items
 */
public class PurchasedItems {
    public final EncryptedKeyValue encryptedKeyValue;

    public PurchasedItems(@NonNull Context context) {
        encryptedKeyValue = new EncryptedKeyValue(context, EncryptedKeyValue.PREF_CHAPA_PAYED_ITEM);
    }

    /**
     * Gets payed item value of type {@link String}
     *
     * @param key          : item key
     * @param defaultValue : default value if the item key is not found
     * @return : item value if the item key is found, otherwise return the default value
     */
    public String getValue(@NonNull String key, String defaultValue) {
        return encryptedKeyValue.getValue(key, defaultValue);
    }

    /**
     * Gets payed item value of type {@link Integer}
     *
     * @param key          : item key
     * @param defaultValue : default value if the item key is not found
     * @return : item value if the item key is found, otherwise return the default value
     */
    public int getValue(@NonNull String key, int defaultValue) {
        return encryptedKeyValue.getValue(key, defaultValue);
    }

    /**
     * Gets payed item value of type {@link Float}
     *
     * @param key          : item key
     * @param defaultValue : default value if the item key is not found
     * @return : item value if the item key is found, otherwise return the default value
     */
    public float getValue(@NonNull String key, float defaultValue) {
        return encryptedKeyValue.getValue(key, defaultValue);
    }

    /**
     * Gets payed item value of type {@link Boolean}
     *
     * @param key          : item key
     * @param defaultValue : default value if the item key is not found
     * @return : item value if the item key is found, otherwise return the default value
     */
    public boolean getValue(@NonNull String key, boolean defaultValue) {
        return encryptedKeyValue.getValue(key, defaultValue);
    }

    /**
     * Gets payed item value of type {@link Long}
     *
     * @param key          : item key
     * @param defaultValue : default value if the item key is not found
     * @return : item value if the item key is found, otherwise return the default value
     */
    public long getValue(@NonNull String key, long defaultValue) {
        return encryptedKeyValue.getValue(key, defaultValue);
    }

    /**
     * Gets payed item value of type {@link Double}
     *
     * @param key          : item key
     * @param defaultValue : default value if the item key is not found
     * @return : item value if the item key is found, otherwise return the default value
     */
    public double getValue(@NonNull String key, double defaultValue) {
        return encryptedKeyValue.getValue(key, defaultValue);
    }

    /**
     * Gets payed item value of type {@link Integer}
     *
     * @param key : item key
     * @return : item value if the item key is found, otherwise return 0
     */
    public int getInt(@NonNull String key) {
        return encryptedKeyValue.getValue(key, 0);
    }

    /**
     * Gets payed item value of type {@link Float}
     *
     * @param key : item key
     * @return : item value if the item key is found, otherwise return 0
     */
    public float getFloat(@NonNull String key) {
        return encryptedKeyValue.getValue(key, 0.0f);
    }

    /**
     * Gets payed item value of type {@link Long}
     *
     * @param key : item key
     * @return : item value if the item key is found, otherwise return 0
     */
    public long getLong(@NonNull String key) {
        return encryptedKeyValue.getValue(key, 0L);
    }

    /**
     * Gets payed item value of type {@link Double}
     *
     * @param key : item key
     * @return : item value if the item key is found, otherwise return 0
     */
    public double getDouble(@NonNull String key) {
        return encryptedKeyValue.getValue(key, 0.0);
    }

    /**
     * Gets payed item value of type {@link Boolean}
     *
     * @param key : item key
     * @return : item value if the item key is found, otherwise return false
     */
    public boolean getBoolean(@NonNull String key) {
        return encryptedKeyValue.getValue(key, false);
    }

    /**
     * Gets payed item value of type {@link String}
     *
     * @param key : item key
     * @return : item value if the item key is found, otherwise return 0
     */
    public String getString(@NonNull String key) {
        return encryptedKeyValue.getValue(key, null);
    }

    /**
     * updates payedItem of type {@link String}
     *
     * @param key      : item key
     * @param newValue : value to be inserted/replaced
     * @return : new updated value
     */

    public String updateValue(@NonNull String key, @NonNull String newValue) {
        encryptedKeyValue.putValue(key, newValue);
        return encryptedKeyValue.getValue(key, null);
    }

    /**
     * updates payedItem of type {@link String}
     *
     * @param key      : item key
     * @param newValue : value to be inserted/replaced
     * @return : new updated value
     */
    public boolean updateValue(@NonNull String key, boolean newValue) {
        encryptedKeyValue.putValue(key, newValue);
        return encryptedKeyValue.getValue(key, newValue);
    }

    /**
     * updates payedItem of type {@link Double}
     *
     * @param key            : item key to be updated
     * @param value          : value
     * @param itemProperties : oneOf {@link ItemProperties#REPLACE} {@link ItemProperties#ADD} {@link ItemProperties#SUBTRACT} {@link ItemProperties#MULTIPLY}
     * @return : new updated value
     */
    public double updateValue(@NonNull String key, double value, @NonNull ItemProperties itemProperties) {
        return updateNumericValue(key, value, itemProperties);
    }

    /**
     * updates payedItem of type {@link Float}
     *
     * @param key            : item key to be updated
     * @param value          : value
     * @param itemProperties : oneOf {@link ItemProperties#REPLACE} {@link ItemProperties#ADD} {@link ItemProperties#SUBTRACT} {@link ItemProperties#MULTIPLY}
     * @return : new updated value
     */
    public float updateValue(@NonNull String key, float value, @NonNull ItemProperties itemProperties) {
        return Double.valueOf(updateNumericValue(key, value, itemProperties)).floatValue();
    }

    /**
     * updates payedItem of type {@link Long}
     *
     * @param key            : item key to be updated
     * @param value          : value
     * @param itemProperties : oneOf {@link ItemProperties#REPLACE} {@link ItemProperties#ADD} {@link ItemProperties#SUBTRACT} {@link ItemProperties#MULTIPLY}
     * @return : new updated value
     */
    public long updateValue(@NonNull String key, long value, @NonNull ItemProperties itemProperties) {
        return Double.valueOf(updateNumericValue(key, value, itemProperties)).longValue();
    }

    /**
     * updates payedItem of type {@link Integer}
     *
     * @param key            : item key to be updated
     * @param value          : value
     * @param itemProperties : oneOf {@link ItemProperties#REPLACE} {@link ItemProperties#ADD} {@link ItemProperties#SUBTRACT} {@link ItemProperties#MULTIPLY}
     * @return : new updated value
     */
    public int updateValue(@NonNull String key, int value, @NonNull ItemProperties itemProperties) {
        return Double.valueOf(updateNumericValue(key, value, itemProperties)).intValue();
    }

    /**
     * removes PayedItem
     *
     * @param key : item key to be removed
     */
    public void removeValue(@NonNull String key) {
        encryptedKeyValue.removeValue(key);
    }

    /**
     * restore PurchasedItems to the device
     *
     * @param encryptedPurchasedItemJson : encrypted json data of PurchasedItems
     * @throws JSONException : throws if encryptedPurchasedItemJson is invalid
     */
    public void restorePurchase(JSONObject encryptedPurchasedItemJson) throws JSONException {
        encryptedKeyValue.restoreEncryptedPreference(encryptedPurchasedItemJson);
    }

    /**
     * restore PurchasedItems to the device
     *
     * @param encryptedPurchasedItemRaw : encrypted json string of PurchasedItems
     * @throws JSONException : throws if encryptedPurchasedItemRaw is invalid
     */
    public void restorePurchase(String encryptedPurchasedItemRaw) throws JSONException {
        encryptedKeyValue.restoreEncryptedPreference(encryptedPurchasedItemRaw);
    }

    /**
     * gets all PurchasedItems data
     *
     * @return encrypted Json string of all purchasedItem
     */
    public String getAllEncryptedPayedItemAsJSONString() {
        return encryptedKeyValue.getAllEncryptedDataAsJSONString();
    }

    /**
     * gets all PurchasedItems data
     *
     * @return encrypted Json Object of all purchasedItem
     */
    public JSONObject getAllEncryptedPayedItemAsJSON() {
        return encryptedKeyValue.getAllEncryptedData();
    }

    private double updateNumericValue(@NonNull String key, double value, @NonNull ItemProperties itemProperties) {
        double prevValue = encryptedKeyValue.getValue(key, 0.0);
        double newValue = value;
        switch (itemProperties) {
            case ADD:
                newValue = prevValue + value;
                break;
            case SUBTRACT:
                newValue = prevValue - value;
                break;
            case MULTIPLY:
                if (prevValue != 0) newValue = prevValue * newValue;
        }
        encryptedKeyValue.putValue(key, newValue);
        return newValue;
    }


}
