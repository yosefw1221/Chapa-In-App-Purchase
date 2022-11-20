package com.yosef.chapainapppurchase.interfaces;

import com.yosef.chapainapppurchase.ChapaError;

/**
 * Interface uses to handle payment callbacks
 * {@link #onSuccess(String)}
 * {@link #onFail(ChapaError)}
 */

public interface ChapaGetCheckOutUrlCallBack {
    /**
     * Returns chapa checkout url
     *
     * @param checkoutUrl chapa checkoutUrl
     */
    void onSuccess(String checkoutUrl);

    /**
     * Called when error occurred in getting checkout url
     *
     * @param error {@link ChapaError}
     */
    void onFail(ChapaError error);
}
