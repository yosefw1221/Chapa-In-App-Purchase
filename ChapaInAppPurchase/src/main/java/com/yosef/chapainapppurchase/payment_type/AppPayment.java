package com.yosef.chapainapppurchase.payment_type;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import com.yosef.chapainapppurchase.ChapaConfiguration;
import com.yosef.chapainapppurchase.ChapaError;
import com.yosef.chapainapppurchase.ChapaUtil;
import com.yosef.chapainapppurchase.PaymentType;
import com.yosef.chapainapppurchase.utils.EncryptedKeyValue;
import com.yosef.chapainapppurchase.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * PaymentType to unlock premium features of app.
 * it generate a unique tx-ref for each user with {@link ChapaConfiguration#getAppUniqueName()} and {@link #planName} and device id.
 * so the payment will be unique for each device and only work one appPayment for one device.
 * it support multiple plan. so you can have multiple plan for your app.
 */
public class AppPayment extends PaymentType {
    private final Context context;
    private final Class<? extends Activity> appMainActivity;
    private final String planName;
    private final String androidID;

    /**
     * Creates PaymentType for app payment
     *
     * @param amount          : amount user to be paid
     * @param appMainActivity : App's Main activity class that launches after a successful payment
     * @param planName        : app payment plan,  e.g. Basic, Premium...
     */
    public AppPayment(Context context, float amount, @NonNull Class<? extends Activity> appMainActivity, @NonNull String planName) {
        super(amount);
        this.context = context;
        this.appMainActivity = appMainActivity;
        this.planName = planName;
        androidID = Utils.getAndroidId(context);
        String paymentId;
        String appUniqueName = getAppUniqueName() == null ? Utils.getAppName(context) : getAppUniqueName();
        if (androidID != null) {
            paymentId = androidID + formattedContent(planName + "-" + appUniqueName, 0);
            setTx_ref("Tx-App-" + paymentId);
        } else {
            setTx_ref(ChapaUtil.generateTransactionRef(30, "Tx-AppR-" + formattedContent(appUniqueName, 10)));
        }
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
            paymentData.put("launcher", appMainActivity.getName());
            paymentData.put("plan", planName);
            paymentData.put("phoneId", androidID);
            paymentData.put("phoneIdFallback", Utils.getPseudoDeviceId());
            EncryptedKeyValue encryptedKeyValue = new EncryptedKeyValue(context, EncryptedKeyValue.PREF_CHAPA_PAYED_ITEM);
            encryptedKeyValue.putValue("appPayment", paymentData.toString());
            Intent intent = new Intent(context, appMainActivity);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            new Handler(Looper.getMainLooper()).postDelayed(() -> context.startActivity(intent), 2500);
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
