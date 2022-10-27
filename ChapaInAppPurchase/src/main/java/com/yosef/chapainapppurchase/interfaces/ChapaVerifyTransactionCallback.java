package com.yosef.chapainapppurchase.interfaces;

import androidx.annotation.Nullable;

import com.yosef.chapainapppurchase.ChapaError;
import com.yosef.chapainapppurchase.model.Transaction;


public interface ChapaVerifyTransactionCallback {
    void onResult(boolean verified, @Nullable Transaction transaction);

    void onError(ChapaError error);
}
