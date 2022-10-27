package com.yosef.chapainapppurchase;

import com.yosef.chapainapppurchase.model.Customer;

interface CustomerCallback {
    void onSuccess(Customer customer);

    void onCanceled();
}
