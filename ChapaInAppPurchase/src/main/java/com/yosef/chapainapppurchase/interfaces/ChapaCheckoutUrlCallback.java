package com.yosef.chapainapppurchase.interfaces;


import com.yosef.chapainapppurchase.ChapaError;
import com.yosef.chapainapppurchase.PaymentType;

/**
 * Interface uses to handle payment callbacks using checkout url
 * <p>
 * {@link #onSuccess()}
 * </p>
 * <p>
 * {@link #onError(ChapaError)}
 * </p>
 * <p>
 * {@link #onCancel()}
 * </p>
 */
public interface ChapaCheckoutUrlCallback extends _PaymentCallback {
    /**
     * Called when payment is successful
     */
    void onSuccess();

    @Override
    default void onSuccess(PaymentType paymentType) {
        onSuccess();
    }
}
