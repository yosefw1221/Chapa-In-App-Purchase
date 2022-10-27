package com.yosef.chapainapppurchase.interfaces;

import androidx.annotation.NonNull;

import com.yosef.chapainapppurchase.ChapaError;
import com.yosef.chapainapppurchase.PaymentType;

public interface _PaymentCallback<T extends PaymentType> {
    void onSuccess(T paymentType);

    void onError(@NonNull ChapaError chapaError);

    void onCancel();
}
