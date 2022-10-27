package com.yosef.chapainapppurchase.model;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;

public class Transaction {
    private Customer customer;
    private String currency;
    private float amount;
    private float charge;
    private String mode;
    private String method;
    private String type;
    private String status;
    private String reference;
    private String tx_ref;
    private Customization customization;
    private String meta;
    private Date created_at;
    private Date updated_at;

    public Transaction(String transactionResponse) {
        try {
            JSONObject jsonObject = new JSONObject(transactionResponse);
            this.customer = new Customer(jsonObject.getString("first_name"), jsonObject.getString("last_name"), jsonObject.getString("email"));
            this.currency = jsonObject.getString("currency");
            this.amount = (float) jsonObject.getDouble("amount");
            this.charge = (float) jsonObject.getDouble("charge");
            this.mode = jsonObject.getString("mode");
            this.method = jsonObject.getString("method");
            this.type = jsonObject.getString("type");
            this.status = jsonObject.getString("status");
            this.reference = jsonObject.getString("reference");
            this.tx_ref = jsonObject.getString("tx_ref");
            JSONObject customizationObject = jsonObject.getJSONObject("customization");
            this.customization = new Customization(customizationObject.getString("title"), customizationObject.getString("description"), customizationObject.getString("logo"));
            this.meta = jsonObject.getString("meta");
            this.created_at = parseDate(jsonObject.getString("created_at"));
            this.updated_at = parseDate(jsonObject.getString("updated_at"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Customer getCustomer() {
        return customer;
    }

    public String getCurrencyString() {
        return currency;
    }

    public Currency getCurrency() {
        return Currency.getInstance("ETB");
    }

    public float getAmount() {
        return amount;
    }

    public float getCharge() {
        return charge;
    }

    public String getMode() {
        return mode;
    }

    public String getMethod() {
        return method;
    }

    public String getType() {
        return type;
    }

    public String getStatus() {
        return status;
    }

    public String getReference() {
        return reference;
    }

    public String getTx_ref() {
        return tx_ref;
    }

    public Customization getCustomization() {
        return customization;
    }

    public String getMeta() {
        return meta;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public Date getUpdated_at() {
        return updated_at;
    }

    private Date parseDate(String date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
        format.setTimeZone(java.util.TimeZone.getTimeZone("GMT"));
        try {
            return format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    @NonNull
    @Override
    public String toString() {
        return "Transaction{" + "customer=" + customer +
                ", currency='" + currency + '\'' +
                ", amount=" + amount +
                ", charge=" + charge +
                ", mode='" + mode + '\'' +
                ", method='" + method + '\'' +
                ", type='" + type + '\'' +
                ", status='" + status + '\'' +
                ", reference='" + reference + '\'' +
                ", tx_ref='" + tx_ref + '\'' +
                ", customization=" + customization +
                ", meta='" + meta + '\'' +
                ", created_at=" + created_at +
                ", updated_at=" + updated_at +
                '}';
    }
}
