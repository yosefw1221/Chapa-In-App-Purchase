package com.yosef.chapainapppurchase.interfaces;

import com.yosef.chapainapppurchase.ChapaError;

public interface ChapaGetCheckOutUrlCallBack {
    void onSuccess(String checkoutUrl);

    void onFail(ChapaError error);
}
