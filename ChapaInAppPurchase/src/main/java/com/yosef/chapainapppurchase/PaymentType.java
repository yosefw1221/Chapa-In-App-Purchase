package com.yosef.chapainapppurchase;

import androidx.annotation.NonNull;

public abstract class PaymentType extends ChapaRequestData implements Cloneable {

    public PaymentType(float amount) {
        super(Chapa.getConfiguration());
        setAmount(amount);
    }

    public PaymentType() {
        super(Chapa.getConfiguration());
    }

    /**
     * this method is executed automatically when payment is successes
     */
    public abstract void onPaymentSuccess();

    public abstract void onPaymentFail(ChapaError error);

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
