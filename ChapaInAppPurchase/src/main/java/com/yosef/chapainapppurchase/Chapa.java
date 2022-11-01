package com.yosef.chapainapppurchase;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.yosef.chapainapppurchase.interfaces.ChapaPaymentCallback;
import com.yosef.chapainapppurchase.model.Customer;
import com.yosef.chapainapppurchase.utils.EncryptedKeyValue;
import com.yosef.chapainapppurchase.utils.Utils;
import com.yosef.chapainapppurchase.utils.Validator;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;


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
        ChapaError error = Validator.validateChapaConfiguration(context,chapaConfiguration);
        Logger.initialize(Utils.debugMode(context));
        Logger.d("Initialize Chapa",chapaConfiguration.toString());
        if (error != null) throw error;
        loadAppPaymentData(context);
        _chapaInstance = new Chapa(chapaConfiguration);
        return _chapaInstance;
    }

    public static String getCurrentUserAppPlan() {
        return currentUserAppPlan;
    }

    private static void setCurrentUserAppPlan(String currentUserAppPlan) {
        Chapa.currentUserAppPlan = currentUserAppPlan;
    }

    private static void loadAppPaymentData(Context context) {
        EncryptedKeyValue pref = new EncryptedKeyValue(context, EncryptedKeyValue.PREF_CHAPA_PAYED_ITEM);
        String appPaymentData = pref.getValue("appPayment", null);
        Log.d("Chapa", "loadAppPaymentData: " + appPaymentData);
        if (appPaymentData != null) {
            try {
                JSONObject appPayment = new JSONObject(appPaymentData);
                String className = appPayment.getString("launcher");
                String plan = appPayment.getString("plan");
                String phoneId = appPayment.getString("phoneId");
                String phoneIdFallback = appPayment.getString("phoneIdFallback");
                if(phoneId.equals(Utils.getAndroidId(context)) || phoneIdFallback.equals(Utils.getPseudoDeviceId())){
                    setCurrentUserAppPlan(plan);
                    Class<?> launcher = Class.forName(className);
                    Intent intent = new Intent(context, launcher);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("plan", plan);
                    setCurrentUserAppPlan(plan);
                    Activity callerActivity = (Activity) context;
                    String callerClassName = callerActivity.getClass().getName();
                    if (launcher.getName().equals(callerClassName))
                        throw new ChapaError(ChapaError.INITIALIZE_ON_APP_MAIN_ACTIVITY, "Could not initialize on App Main Activity");
                    context.startActivity(intent);
                }else
                    throw new ChapaError(ChapaError.PHONE_ID_MISMATCH,"PhoneId not match");
            } catch (Exception e) {
                Log.e("CHAPA ","LOAD APP PAYMENT ",e);
            }
        }
    }

    public void setCustomer(@NotNull Customer customer) {
        ChapaConfiguration._customer = customer;
        _chapaConfiguration.setCustomer(customer);
    }

    public void setCustomer(@NotNull String firstName, @NotNull String lastName, @NotNull String email) {
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
