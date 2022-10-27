package com.yosef.chapainapppurchase.payment_type;


import com.yosef.chapainapppurchase.ChapaError;
import com.yosef.chapainapppurchase.PaymentType;

public class BasicPayment extends PaymentType {

    public BasicPayment(float amount) {
        super(amount);
    }

    @Override
    public void onPaymentSuccess() {

    }

    @Override
    public void onPaymentFail(ChapaError chapaError) {

    }

    @Override
    public void onPaymentCancel() {

    }

}
