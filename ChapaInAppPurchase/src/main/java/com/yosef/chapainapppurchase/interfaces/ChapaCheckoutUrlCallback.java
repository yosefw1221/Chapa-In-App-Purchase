package com.yosef.chapainapppurchase.interfaces;


import com.yosef.chapainapppurchase.PaymentType;

public interface ChapaCheckoutUrlCallback extends _PaymentCallback {
     void onSuccess();

     @Override
     default void onSuccess(PaymentType paymentType){
         onSuccess();
     }
}
