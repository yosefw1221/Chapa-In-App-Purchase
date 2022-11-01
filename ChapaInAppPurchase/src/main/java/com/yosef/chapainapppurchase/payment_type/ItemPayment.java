package com.yosef.chapainapppurchase.payment_type;

import android.content.Context;

import androidx.annotation.NonNull;

import com.yosef.chapainapppurchase.ChapaError;
import com.yosef.chapainapppurchase.PaymentType;
import com.yosef.chapainapppurchase.enums.ItemProperties;
import com.yosef.chapainapppurchase.utils.EncryptedKeyValue;

public class ItemPayment extends PaymentType {

    final Context context;
    private final String itemKey;
    private final EncryptedKeyValue encryptedSharedPref;
    private final ItemProperties itemProperties;
    private Object updatedValue;

    public <T> ItemPayment(@NonNull Context context, float amount, @NonNull String itemKey, @NonNull T value) throws ChapaError {
        this(context, amount, itemKey, value, ItemProperties.REPLACE);
    }

    public <T> ItemPayment(@NonNull Context context, float amount, @NonNull String itemKey, @NonNull T value, ItemProperties itemProperties) throws ChapaError {
        super(amount);
        this.context = context;
        this.itemKey = itemKey;
        this.itemProperties = itemProperties;
        encryptedSharedPref = new EncryptedKeyValue(context, EncryptedKeyValue.PREF_CHAPA_PAYED_ITEM);
        if (itemProperties != ItemProperties.REPLACE) {
            if (value instanceof Integer) {
                updatedValue = (int) updateValue(encryptedSharedPref.getValue(itemKey, 0), (Integer) value);
            } else if (value instanceof Float) {
                updatedValue = (float) updateValue(encryptedSharedPref.getValue(itemKey, 0F), (Float) value);
            } else if (value instanceof Double) {
                updatedValue = updateValue(encryptedSharedPref.getValue(itemKey, 0.0), (Double) value);
            } else if (value instanceof Long) {
                updatedValue = (long) updateValue(encryptedSharedPref.getValue(itemKey, 0L), (Long) value);
            } else
                throw new ChapaError(ChapaError.UNSUPPORTED_DATA_TYPE, "Unsupported Data Type " + value.getClass().getGenericSuperclass());
        }
    }

    public String getItemKey() {
        return itemKey;
    }

    public Object getUpdatedValue() {
        return updatedValue;
    }

    public ItemProperties getItemProperties() {
        return itemProperties;
    }

    private double updateValue(double prevValue, double newValue) {
        switch (itemProperties) {
            case ADD:
                return prevValue + newValue;
            case SUBTRACT:
                return prevValue - newValue;
            case MULTIPLY:
                if (prevValue == 0) return newValue;
                return prevValue * newValue;
            default:
                return newValue;
        }
    }

    @Override
    public void onPaymentSuccess() {
        encryptedSharedPref.putValue(itemKey, String.valueOf(updatedValue));
    }

    @Override
    public void onPaymentFail(ChapaError chapaError) {

    }

    @Override
    public void onPaymentCancel() {

    }
}