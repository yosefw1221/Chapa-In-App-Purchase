package com.yosef.chapainapppurchase.interfaces;

import androidx.annotation.NonNull;

import com.yosef.chapainapppurchase.ChapaError;
import com.yosef.chapainapppurchase.PaymentType;

/**
 * Internal Interface uses to handle payment callbacks
 * <p>
 * {@link #onSuccess(PaymentType)} called when payment is successful and return a PaymentType object
 * </p>
 * <p>
 * {@link #onCancel()} called when user cancel the payment
 * </p>
 * <p>
 * {@link #onError(ChapaError)} called when payment fails
 *
 * @see ChapaError
 * </p>
 */
public interface _PaymentCallback<T extends PaymentType> {
    /**
     * Called when payment is successful
     *
     * @param paymentType PaymentType
     */
    void onSuccess(T paymentType);

    /**
     * Called when error occurred in payment
     */
    void onError(@NonNull ChapaError chapaError);

    /**
     * Called when payment is canceled
     */
    void onCancel();
}
