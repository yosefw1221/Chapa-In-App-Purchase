package com.yosef.chapainapppurchase;

import android.util.Log;

import androidx.annotation.NonNull;

import com.yosef.chapainapppurchase.interfaces.ChapaGetCheckOutUrlCallBack;
import com.yosef.chapainapppurchase.interfaces.ChapaVerifyTransactionCallback;
import com.yosef.chapainapppurchase.model.Transaction;
import com.yosef.chapainapppurchase.utils.Validator;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ChapaUtil {
    public static synchronized void getCheckoutUrl(@NonNull PaymentType paymentType, @NonNull ChapaGetCheckOutUrlCallBack checkOutCallBack) {
        try {
            ChapaError paymentDataError = Validator.validateRequestData(null, paymentType, false);
            if (paymentDataError != null) checkOutCallBack.onFail(paymentDataError);
            else {
                String requestBody = paymentType.toJsonObject().toString();
                MediaType JSON = MediaType.get("application/json; charset=utf-8");
                Request request = new Request.Builder().addHeader("Authorization", "Bearer " + paymentType.getKey().trim()).url("https://api.chapa.co/v1/transaction/initialize").post(RequestBody.create(requestBody, JSON)).build();
                new OkHttpClient().newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        checkOutCallBack.onFail(new ChapaError(ChapaError.CONNECTIVITY_PROBLEM, e.getMessage()));
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        String _response = Objects.requireNonNull(response.body()).string();
                        try {
                            Log.d("checkout response", _response);
                            JSONObject res = new JSONObject(_response);
                            String status = res.getString("status");
                            if (status.equalsIgnoreCase("success")) {
                                JSONObject data = res.getJSONObject("data");
                                String checkoutUrl = data.getString("checkout_url");
                                checkOutCallBack.onSuccess(checkoutUrl);
                            } else
                                checkOutCallBack.onFail(new ChapaError(ChapaError.CHAPA_ERROR, res.getString("message")));
                        } catch (JSONException e) {
                            checkOutCallBack.onFail(new ChapaError(ChapaError.INTERNAL_ERROR, e.getMessage()));
                        }
                    }
                });
            }
        } catch (Exception e) {
            checkOutCallBack.onFail(new ChapaError(ChapaError.INTERNAL_ERROR, e.getMessage()));
        }
    }

    public static synchronized void verifyTransaction(@NonNull String tx_ref, @NonNull ChapaVerifyTransactionCallback verifyTransactionCallback) {
        try {
            ChapaConfiguration configuration = Chapa.getConfiguration();
            if (configuration != null)
                _verifyTransaction(tx_ref, configuration.getKey(), verifyTransactionCallback);
            else throw new ChapaError(ChapaError.CHAPA_NOT_INITIALIZED, "Chapa not initialized");
        } catch (ChapaError e) {
            verifyTransactionCallback.onError(e);
        }
    }

    public static synchronized void verifyTransaction(@NonNull String tx_ref, @NonNull String chapaKey, @NonNull ChapaVerifyTransactionCallback verifyTransactionCallback) {
        _verifyTransaction(tx_ref, chapaKey, verifyTransactionCallback);
    }

    private static void _verifyTransaction(@NonNull String tx_ref, @NonNull String key, ChapaVerifyTransactionCallback verifyTransactionCallback) {
        try {
            Request request = new Request.Builder().addHeader("Authorization", "Bearer " + key.trim()).url("https://api.chapa.co/v1/transaction/verify/" + tx_ref.replace(" ", "")).get().build();
            new OkHttpClient().newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    verifyTransactionCallback.onError(new ChapaError(ChapaError.CONNECTIVITY_PROBLEM, e.getMessage()));
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    String _response = Objects.requireNonNull(response.body()).string();
                    try {
                        JSONObject res = new JSONObject(_response);
                        String status = res.getString("status");
                        String data = res.getString("data");
                        boolean success = status.equalsIgnoreCase("success");
                        verifyTransactionCallback.onResult(success, new Transaction(data));
                        Log.d("Verify Transaction", _response);

                    } catch (JSONException e) {
                        verifyTransactionCallback.onError(new ChapaError(ChapaError.INTERNAL_ERROR, e.getMessage()));
                    }
                }
            });
        } catch (Exception e) {
            verifyTransactionCallback.onError(new ChapaError(ChapaError.INTERNAL_ERROR, e.getMessage()));
        }
    }


    public static String generateTransactionRef(int length, String prefix) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder result = new StringBuilder();
        int random = length;
        if (prefix != null) random -= prefix.length();
        while (random > 0) {
            double index = Math.random() * characters.length();
            result.append(characters.charAt((int) index));
            random--;
        }
        Log.d("Transaction Ref", prefix + result);
        return prefix + result;
    }
}
