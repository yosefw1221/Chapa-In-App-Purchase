package com.yosef.chapainapppurchase;

import androidx.annotation.NonNull;

/**
 * This is super class for all payment types
 */
public abstract class PaymentType extends ChapaRequestData implements Cloneable {

    public PaymentType(float amount) {
        super(Chapa.getConfiguration());
        setAmount(amount);
    }

    public PaymentType() {
        super(Chapa.getConfiguration());
    }

    /**
     * this method is executed automatically when payment is successful
     */
    public abstract void onPaymentSuccess();

    /**
     * this method is executed automatically when payment is failed
     */
    public abstract void onPaymentFail(ChapaError error);

    /**
     * this method is executed automatically when payment is canceled
     */
    public abstract void onPaymentCancel();

    @NonNull
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @NonNull
    @Override
    public String toString() {
        return "{" + "amount=" + getAmount() +
                ", currency=" + getCurrencyString() +
                ", customer=" + getCustomer() +
                ", callback_url=" + getCallbackUrl() +
                ", customization=" + getCustomization() +
                ", tx_ref='" + getTx_ref() + '\'' +
                '}';
    }

}
