package com.yosef.chapainapppurchase.payment_type;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import com.yosef.chapainapppurchase.ChapaError;
import com.yosef.chapainapppurchase.ChapaUtil;
import com.yosef.chapainapppurchase.PaymentType;
import com.yosef.chapainapppurchase.utils.EncryptedKeyValue;
import com.yosef.chapainapppurchase.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Uses to unlock premium features of app.
 * it generate a unique tx-ref for each user with app name and {@link #planName} and device id.
 * so the payment will be unique for each device and only work one appPayment for one device.
 * a device can have single app plan, if user upgrade to other plan the previous plan will replaced with new one
 */
public class AppPayment extends PaymentType {
    private final Context context;
    private final Class<? extends Activity> appMainActivity;
    private final String planName;
    private String androidID;

    private AppPayment(Context context, double amount, Class<? extends Activity> appMainActivity, @NonNull String planName) {
        super(amount);
        this.context = context;
        this.appMainActivity = appMainActivity;
        this.planName = planName;
        androidID = Utils.getAndroidId(context);
        String paymentId;
        String appName = Utils._getAppName(context);
        if (androidID != null) {
            paymentId = androidID + formattedContent(planName + "-" + appName, -1);
            setTx_ref("Tx-App-" + paymentId);
        } else {
            androidID = Utils.getPseudoDeviceId();
            //  generate random transaction reference
            setTx_ref(ChapaUtil.generateTransactionRef(30, "Tx-AppR-" + formattedContent(appName, 10)));
        }
    }

    /**
     * Preferable for apps that has both freemium and premium feature
     *
     * @param amount   : amount user to be paid
     * @param planName : app payment plan,  e.g. Basic, Premium...
     */
    public AppPayment(Context context, double amount, String planName) {
        this(context, amount, null, planName);
    }

    /**
     * Preferable for apps that has only premium feature
     *
     * @param amount          : amount user to be paid
     * @param appMainActivity : App's Main activity class that launches after a successful payment
     * @param planName        : app payment plan,  e.g. Basic, Premium...
     */
    public AppPayment(Context context, double amount, String planName, @NonNull Class<? extends Activity> appMainActivity) {
        this(context, amount, appMainActivity, planName);
    }

    private String formattedContent(String content, int size) {
        String formattedContent = content.replaceAll("[^a-zA-Z0-9_-]", "");
        if (formattedContent.length() > size && size > 0)
            return formattedContent.substring(0, size);
        return formattedContent;
    }


    @Override
    public void onPaymentSuccess() {
        try {
            JSONObject paymentData = new JSONObject();
            String appName = Utils._getAppName(context);
            if (appMainActivity != null) paymentData.put("launcher", appMainActivity.getName());
            paymentData.put("plan", planName);
            paymentData.put("phoneId", androidID);
            paymentData.put("appName", appName);
            paymentData.put("phoneIdFallback", Utils.getPseudoDeviceId());
            EncryptedKeyValue encryptedKeyValue = new EncryptedKeyValue(context, EncryptedKeyValue.PREF_CHAPA_PAYED_ITEM);
            encryptedKeyValue.putValue("appPayment", paymentData.toString());
            if (appMainActivity != null) {
                Intent intent = new Intent(context, appMainActivity);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("plan", planName);
                new Handler(Looper.getMainLooper()).postDelayed(() -> context.startActivity(intent), 2500);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPaymentFail(ChapaError error) {

    }

    @Override
    public void onPaymentCancel() {

    }

    public Class<? extends Activity> getAppMainActivity() {
        return appMainActivity;
    }

    public String getPlanName() {
        return planName;
    }
}
