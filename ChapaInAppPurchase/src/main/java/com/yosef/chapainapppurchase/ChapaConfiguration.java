package com.yosef.chapainapppurchase;

import androidx.annotation.NonNull;

import com.yosef.chapainapppurchase.enums.Currency;
import com.yosef.chapainapppurchase.model.Customer;
import com.yosef.chapainapppurchase.model.Customization;


public class ChapaConfiguration {
    protected static Customer _customer;
    private String key;
    private Currency currency = Currency.ETB;
    private String callback_url;
    private Customization customization;
    private boolean showPaymentError;
    private String appUniqueName;

    public String getKey() {
        return key;
    }

    /**
     * This will be your private key from Chapa. When on test mode use the test key, and when on live mode use the live key.
     *
     * @param key chapa secretKey - Required
     */
    public void setKey(@NonNull String key) {
        this.key = key;
    }

    public Currency getCurrency() {
        return currency;
    }

    /**
     * The currency in which all the charges are made. Currency allowed is ETB and USD.
     *
     * @param currency "ETB" | "USD" default "ETB"
     */
    public void setCurrency(@NonNull Currency currency) {
        this.currency = currency;
    }

    public String getCurrencyString() {
        return currency.name();
    }

    public String getCallbackUrl() {
        return callback_url;
    }

    /**
     * Api that trigger when payment is successful. This should ideally be a script that uses the verify endpoint
     * on the Chapa API to check the status of the transaction.
     *
     * @param callback_url your backend endpoint url that triggered after payment is successfully
     */

    public void setCallbackUrl(@NonNull String callback_url) {
        this.callback_url = callback_url;
    }

    /**
     * Customize the look and feel of the payment modal. You can set a logo,
     * the store name to be displayed (title),and a description for the payment.
     */

    public void setCustomization(String title, String description, String logo) {
        this.customization = new Customization(title, description, logo);
    }

    public Customization getCustomization() {
        return customization;
    }

    public void setCustomization(Customization customization) {
        this.customization = customization;
    }

    public Customer getCustomer() {
        return _customer;
    }

    public void setCustomer(@NonNull Customer customer) {
        _customer = customer;
    }

    public void setCustomer(@NonNull String firstName, @NonNull String lastName, @NonNull String email) {
        _customer = new Customer(firstName, lastName, email);
    }

    public void showPaymentError(boolean showPaymentError) {
        this.showPaymentError = showPaymentError;
    }

    public boolean isShowPaymentError() {
        return showPaymentError;
    }

    public String getAppUniqueName() {
        return appUniqueName;
    }

    public void setAppUniqueName(String appUniqueName) {
        this.appUniqueName = appUniqueName;
    }

    @NonNull
    @Override
    public String toString() {
        return "ChapaConfiguration{" + "key='" + key + '\'' + ", currency='" + currency + '\'' + ", callback_url='" + callback_url + '\'' + ", customer='" + _customer + '\'' + ", customization=" + customization + '}';
    }




}
