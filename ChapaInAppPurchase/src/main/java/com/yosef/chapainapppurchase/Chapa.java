package com.yosef.chapainapppurchase;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.yosef.chapainapppurchase.interfaces.ChapaPaymentCallback;
import com.yosef.chapainapppurchase.model.Customer;
import com.yosef.chapainapppurchase.payment_type.AppPayment;
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

    /**
     * Gets the Chapa instance
     *
     * @return Chapa instance
     * @throws ChapaError {@link ChapaError#CHAPA_NOT_INITIALIZED} if Chapa is not initialized with {@link Chapa#init(Context, ChapaConfiguration)}
     */
    public static Chapa getInstance() throws ChapaError {
        if (_chapaInstance == null) _chapaInstance = new Chapa(_chapaConfiguration);
        return _chapaInstance;
    }

    /**
     * Checks if Chapa is initialized
     *
     * @return true if Chapa is initialized, false otherwise
     */
    public synchronized static boolean isInitialized() {
        return _chapaInstance != null;
    }

    /**
     * Gets initialized ChapaConfiguration
     *
     * @return ChapaConfiguration
     */
    public static ChapaConfiguration getConfiguration() {
        return _chapaConfiguration;
    }

    /**
     * Initializes Chapa
     *
     * @param context            Application context
     * @param chapaConfiguration ChapaConfiguration to initialize Chapa with
     * @return Chapa instance
     * @throws ChapaError if chapaConfiguration is not valid
     * @see Validator#validateChapaConfiguration(Context, ChapaConfiguration)
     */
    public synchronized static Chapa init(@NonNull Context context, @NonNull ChapaConfiguration chapaConfiguration) throws ChapaError {
        ChapaError error = Validator.validateChapaConfiguration(context, chapaConfiguration);
        Logger.initialize(Utils.debugMode(context));
        Logger.d("Initialize Chapa, ", chapaConfiguration.toString() + "  " + Utils._getAppName(context));
        if (error != null) throw error;
        loadAppPaymentData(context);
        _chapaInstance = new Chapa(chapaConfiguration);
        return _chapaInstance;
    }

    /**
     * Gets the current user app plan
     *
     * @return current user app plan
     * @see AppPayment
     */
    public static String getCurrentUserAppPlan() {
        return currentUserAppPlan;
    }

    static void setCurrentUserAppPlan(String currentUserAppPlan) {
        Chapa.currentUserAppPlan = currentUserAppPlan;
    }

    private static void loadAppPaymentData(Context context) {
        EncryptedKeyValue pref = new EncryptedKeyValue(context, EncryptedKeyValue.PREF_CHAPA_PAYED_ITEM);
        String appPaymentData = pref.getValue("appPayment", null);
        Logger.d("Chapa", "loadAppPaymentData: " + appPaymentData);
        if (appPaymentData != null) {
            try {
                JSONObject appPayment = new JSONObject(appPaymentData);
                String className = appPayment.has("launcher") ? appPayment.getString("launcher") : null;
                String plan = appPayment.getString("plan");
                String phoneId = appPayment.getString("phoneId");
                String phoneIdFallback = appPayment.getString("phoneIdFallback");
                String appName = appPayment.getString("appName");
                if (appName.equals(Utils._getAppName(context)) && (phoneId.equals(Utils.getAndroidId(context)) || phoneIdFallback.equals(Utils.getPseudoDeviceId()))) {
                    setCurrentUserAppPlan(plan);
                    // launch app's main activity
                    if (className != null) {
                        Class<?> launcher = Class.forName(className);
                        Intent intent = new Intent(context, launcher);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("plan", plan);
                        if (launcher.getName().equals(getTopActivityName(context)))
                            throw new ChapaError(ChapaError.INITIALIZE_ON_APP_MAIN_ACTIVITY, "Could not initialize on App Main Activity");
                        context.startActivity(intent);
                    }
                } else throw new ChapaError(ChapaError.PHONE_ID_MISMATCH, "PhoneId not match");
            } catch (Exception e) {
                Logger.e("CHAPA ", "LOAD APP PAYMENT ", e);
            }
        }
    }

    private static String getTopActivityName(Context context) {
        try {
            ComponentName cn;
            ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M)
                cn = am.getAppTasks().get(0).getTaskInfo().topActivity;
            else cn = am.getRunningTasks(1).get(0).topActivity;
            Logger.e("TopActivity", cn.getClassName());
            return cn.getClassName();
        } catch (Exception e) {
            Logger.e("getTopActivityName", e.getMessage());
            return null;
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

    /**
     * Processes chapa payment from PaymentType Object
     *
     * @param context              Application context
     * @param paymentType          Object extends {@link PaymentType}
     * @param chapaPaymentCallback ChapaPaymentCallback to get the payment result
     * @see ChapaPaymentCallback
     */
    synchronized public <T extends PaymentType> void pay(@NonNull Context context, @NonNull T paymentType, @NonNull ChapaPaymentCallback<T> chapaPaymentCallback) {
        processPayment(context, paymentType, chapaPaymentCallback);
    }

    /**
     * Processes chapa payment from PaymentType Object
     *
     * @param context     Application context
     * @param paymentType Object extends {@link PaymentType}
     * @see ChapaPaymentCallback
     */
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
