package com.yosef.chapainapppurchase.interfaces;

import androidx.annotation.Nullable;

import com.yosef.chapainapppurchase.ChapaError;
import com.yosef.chapainapppurchase.model.Transaction;

/**
 * Interface uses to verify chapa transaction
 * <p>
 * {@link #onResult(boolean verified, Transaction)}  "verified" returns true if transaction is verified
 * </p>
 * <p>
 * {@link #onError(ChapaError)} fails due to other issues
 *
 * @see ChapaError
 * </p>
 */
public interface ChapaVerifyTransactionCallback {
    /**
     * verify transaction
     *
     * @param verified    true if transaction is verified otherwise false
     * @param transaction if verified {@link Transaction} otherwise null
     */
    void onResult(boolean verified, @Nullable Transaction transaction);

    /**
     * Called when error occurred in verifying transaction
     *
     * @param error {@link ChapaError}
     */
    void onError(ChapaError error);
}
