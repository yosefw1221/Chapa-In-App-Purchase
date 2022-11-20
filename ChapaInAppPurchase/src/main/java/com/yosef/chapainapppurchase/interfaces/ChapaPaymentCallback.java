package com.yosef.chapainapppurchase.interfaces;

import androidx.annotation.NonNull;

import com.yosef.chapainapppurchase.ChapaError;
import com.yosef.chapainapppurchase.PaymentType;

/**
 * Interface uses to handle payment callbacks
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

public interface ChapaPaymentCallback<T extends PaymentType> extends _PaymentCallback<T> {
    /**
     * Called when payment is successful
     *
     * @param paymentType PaymentType object
     * @param tx_ref      Transaction reference of the payment
     */
    void onSuccess(@NonNull String tx_ref, @NonNull T paymentType);

    @Override
    default void onSuccess(T paymentType) {
        onSuccess(paymentType.getTx_ref(), paymentType);
    }

}
