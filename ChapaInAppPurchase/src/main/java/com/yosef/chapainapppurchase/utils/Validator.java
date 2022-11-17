package com.yosef.chapainapppurchase.utils;

import android.content.Context;
import android.util.Log;
import android.util.Patterns;

import com.yosef.chapainapppurchase.ChapaConfiguration;
import com.yosef.chapainapppurchase.ChapaError;
import com.yosef.chapainapppurchase.ChapaRequestData;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * Validate chapa configuration and request data
 */
public class Validator {

    public static ChapaError validateChapaConfiguration(Context context, ChapaConfiguration config) {
        Log.d("Validating ", "configuration  " + config);
        if (config == null) return new ChapaError(ChapaError.INVALID_DATA, "Invalid Data");
        else if (config.getKey() == null || config.getKey().trim().isEmpty())
            return new ChapaError(ChapaError.INVALID_CHAPA_KEY, "Invalid Key, Chapa secret key should not be empty ");
        if (context != null && !Utils.debugMode(context) && config.getKey().contains("TEST"))
            return new ChapaError(ChapaError.TEST_KEY_IN_PRODUCTION, "Could not use Test key in Production");
        else if (config.getCustomer() != null) {
            if (config.getCustomer().getFirstName() == null || config.getCustomer().getLastName().isEmpty())
                return new ChapaError(ChapaError.INVALID_CUSTOMER_DATA, "Invalid FirstName, Customer's first should not be empty");
            if (config.getCustomer().getLastName() == null || config.getCustomer().getFirstName().isEmpty())
                return new ChapaError(ChapaError.INVALID_CUSTOMER_DATA, "Invalid LastName, Customer's lastName should not be empty");
            if (config.getCustomer().getEmail() == null || !Patterns.EMAIL_ADDRESS.matcher(config.getCustomer().getEmail()).matches())
                return new ChapaError(ChapaError.INVALID_CUSTOMER_DATA, "Invalid Email, Customer's email should a valid email address");
        } else if (config.getCallbackUrl() != null && !Patterns.WEB_URL.matcher(config.getCallbackUrl()).matches())
            return new ChapaError(ChapaError.INVALID_CALLBACK_URL, "Invalid Callback-Url, CallbackUrl should be a valid url");
        return null;
    }

    public static ChapaError validateRequestData(Context context, ChapaRequestData requestData, boolean skipCustomer) {
        if (requestData == null) return new ChapaError(ChapaError.INVALID_DATA, "Invalid Data");
        else if (requestData.getAmount() < 1)
            return new ChapaError(ChapaError.INVALID_AMOUNT, "Invalid Amount, Minimum Amount should be 1");
        else if (requestData.getCurrency() == null)
            return new ChapaError(ChapaError.INVALID_CURRENCY, "Invalid Currency, Currency should not be null");
        else if (requestData.getCustomer() == null && !skipCustomer)
            return new ChapaError(ChapaError.INVALID_CUSTOMER_DATA, "Invalid Customer, Customer should not be null");
        else if (!isValidTxRef(requestData.getTx_ref()))
            return new ChapaError(ChapaError.INVALID_TX_REF, "Invalid TX_REF, TX_REF should be alphanumeric characters between 6 and 100 characters");
        return validateChapaConfiguration(context, requestData);
    }

    public static boolean isValidTxRef(String tx_ref) {
        if (tx_ref == null) return false;
        Pattern tx_ref_pattern = Pattern.compile("^[a-zA-Z0-9_-]{6,200}$");
        Matcher matcher = tx_ref_pattern.matcher(tx_ref);
        return matcher.matches();
    }
}
