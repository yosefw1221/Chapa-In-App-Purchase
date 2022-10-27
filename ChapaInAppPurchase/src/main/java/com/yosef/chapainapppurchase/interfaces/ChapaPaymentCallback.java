package com.yosef.chapainapppurchase.interfaces;

import androidx.annotation.NonNull;

import com.yosef.chapainapppurchase.PaymentType;


public interface ChapaPaymentCallback<T extends PaymentType> extends _PaymentCallback<T> {
    void onSuccess(@NonNull String tx_ref, @NonNull T paymentType);

    @Override
    default void onSuccess(T paymentType) {
        onSuccess(paymentType.getTx_ref(), paymentType);
    }

}
