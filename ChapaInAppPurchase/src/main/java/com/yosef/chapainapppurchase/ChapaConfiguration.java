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
    private boolean showPaymentError = true;
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
     * @see Currency
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
     * The customizations field (optional) allows you
     * Customize the look and feel of the payment modal. You can set a logo,
     * the store name to be displayed (title),and a description for the payment.
     *
     * @param title       title of the payment modal
     * @param description description of the payment modal
     * @param logo        url of the logo to be displayed on the payment modal
     */
    public void setCustomization(String title, String description, String logo) {
        this.customization = new Customization(title, description, logo);
    }


    public Customization getCustomization() {
        return customization;
    }

    /**
     * Customize the look and feel of the payment model
     *
     * @param customization Customization object {@link Customization}
     */
    public void setCustomization(Customization customization) {
        this.customization = customization;
    }

    public Customer getCustomer() {
        return _customer;
    }

    /**
     * Sets the customer information
     *
     * @param customer Customer object {@link Customer}
     */
    public void setCustomer(@NonNull Customer customer) {
        _customer = customer;
    }

    /**
     * Sets the customer information
     *
     * @param firstName first name of the customer
     * @param lastName  last name of the customer
     * @param email     email of the customer
     */
    public void setCustomer(@NonNull String firstName, @NonNull String lastName, @NonNull String email) {
        _customer = new Customer(firstName, lastName, email);
    }

    /**
     * Controls the default behaviors of Payment Modal when an error occurs.
     * If set to true, the payment modal will be closed when an error occurs.
     * If set to false, the payment modal will show error message when an error occurs.
     *
     * @param showPaymentError default true
     */
    public void showPaymentError(boolean showPaymentError) {
        this.showPaymentError = showPaymentError;
    }

    public boolean isShowPaymentError() {
        return showPaymentError;
    }

    @NonNull
    @Override
    public String toString() {
        return "ChapaConfiguration{" + "key='" + key + '\'' + ", currency='" + currency + '\'' + ", callback_url='" + callback_url + '\'' + ", customer='" + _customer + '\'' + ", customization=" + customization + '}';
    }


}
