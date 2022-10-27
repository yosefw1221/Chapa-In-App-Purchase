package com.yosef.chapainapppurchase;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.yosef.chapainapppurchase.interfaces.ChapaPaymentCallback;
import com.yosef.chapainapppurchase.model.Customer;
import com.yosef.chapainapppurchase.utils.EncryptedKeyValue;
import com.yosef.chapainapppurchase.utils.Validator;


public class Chapa {
    private static ChapaConfiguration _chapaConfiguration;
    private static Chapa _chapaInstance;
    private static String currentUserAppPlan;
    private Customer _customer;

    private Chapa(ChapaConfiguration chapaConfiguration) throws ChapaError {
        if (chapaConfiguration == null)
            throw new ChapaError(ChapaError.CHAPA_NOT_INITIALIZED, "ChapaConfiguration is not initialized");
        _chapaConfiguration = chapaConfiguration;
    }

    public static Chapa getInstance() throws ChapaError {
        if (_chapaInstance == null) _chapaInstance = new Chapa(_chapaConfiguration);
        return _chapaInstance;
    }

    public synchronized static boolean isInitialized() {
        return _chapaInstance != null;
    }

    public static ChapaConfiguration getConfiguration() {
        return _chapaConfiguration;
    }

    public synchronized static Chapa init(@NonNull Context context, @NonNull ChapaConfiguration chapaConfiguration) throws ChapaError {
        ChapaError error = Validator.validateChapaConfiguration(context, chapaConfiguration);
        if (error != null) throw error;
        _chapaInstance = new Chapa(chapaConfiguration);
        return _chapaInstance;
    }


    public void setCustomer(@NonNull Customer customer) {
        ChapaConfiguration._customer = customer;
        _chapaConfiguration.setCustomer(customer);
    }

    public void setCustomer(@NonNull String firstName, @NonNull String lastName, @NonNull String email) {
        Customer customer = new Customer(firstName, lastName, email);
        _chapaConfiguration.setCustomer(customer);
        ChapaConfiguration._customer = customer;
    }

    synchronized public <T extends PaymentType> void pay(@NonNull Context context, @NonNull T paymentType, @NonNull ChapaPaymentCallback<T> chapaPaymentCallback) {
        processPayment(context, paymentType, chapaPaymentCallback);
    }

    synchronized public <T extends PaymentType> void pay(@NonNull Context context, @NonNull T paymentType) {
        processPayment(context, paymentType, null);
    }

    private <T extends PaymentType> void processPayment(Context context, T paymentType, ChapaPaymentCallback<T> callback) {
        try {
            PaymentType _paymentType = (PaymentType) paymentType.clone();
            Log.d("Chapa", "processPayment: " + _paymentType);
            if (paymentType.getCustomer() == null) loadSavedCustomer(context, paymentType);
            if (paymentType.getTx_ref() == null)
                _paymentType.setTx_ref(ChapaUtil.generateTransactionRef(15, "TX-"));
            ChapaCheckoutPage chapaCheckoutPage = new ChapaCheckoutPage(context);
            chapaCheckoutPage.processPayment(_paymentType, callback);
        } catch (Exception e) {
            callback.onError(new ChapaError(ChapaError.INTERNAL_ERROR, e.getMessage()));
        }
    }

    private void loadSavedCustomer(Context context, PaymentType paymentType) {
        EncryptedKeyValue pref = new EncryptedKeyValue(context, "CHAPA_IN_APP_PAYMENT");
        String customerStr = pref.getValue("customer", null);
        if (customerStr != null) {
            String[] data = customerStr.split(",");
            Customer customer = new Customer(data[0], data[1], data[2]);
            paymentType.setCustomer(customer);
            setCustomer(customer);
        }
    }


}
