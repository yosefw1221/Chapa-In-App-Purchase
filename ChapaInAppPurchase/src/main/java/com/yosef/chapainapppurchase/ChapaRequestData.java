package com.yosef.chapainapppurchase;

import android.util.Log;

import androidx.annotation.NonNull;

import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Chapa Request Data
 */
public class ChapaRequestData extends ChapaConfiguration {
    private String tx_ref;
    private double amount;

    public ChapaRequestData(ChapaConfiguration c) {
        if (c != null) {
            setKey(c.getKey());
            setCurrency(c.getCurrency());
            setCustomer(c.getCustomer());
            setCallbackUrl(c.getCallbackUrl());
            setCustomization(c.getCustomization());
        }
    }

    public ChapaRequestData() {
    }

    public String getTx_ref() {
        return tx_ref;
    }

    public void setTx_ref(@NonNull String tx_ref) {
        this.tx_ref = tx_ref;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    JSONObject toJsonObject() {
        JSONObject object = new JSONObject();
        try {

            object.put("amount", BigDecimal.valueOf(amount).setScale(2, RoundingMode.CEILING).doubleValue());
            object.put("currency", getCurrency());
            object.put("first_name", getCustomer().getFirstName());
            object.put("last_name", getCustomer().getLastName());
            object.put("email", getCustomer().getEmail());
            object.put("tx_ref", getTx_ref());
            if (getCallbackUrl() != null)
                object.put("callback_url", getCallbackUrl());
            if (getCustomization() != null) {
                object.put("customization[title]", getCustomization().getTitle());
                object.put("customization[description]", getCustomization().getDescription());
                object.put("customization[logo]", getCustomization().getLogo());
            }
            return object;
        } catch (Exception e) {
            Log.e("CHAPA", "CHAPA REQUEST DATA", e);
            return null;
        }
    }
}
