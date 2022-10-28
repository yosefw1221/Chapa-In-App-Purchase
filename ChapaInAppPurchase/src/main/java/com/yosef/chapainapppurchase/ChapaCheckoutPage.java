package com.yosef.chapainapppurchase;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.PorterDuff;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.Patterns;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.yosef.chapainapppurchase.interfaces.ChapaCheckoutUrlCallback;
import com.yosef.chapainapppurchase.interfaces.ChapaGetCheckOutUrlCallBack;
import com.yosef.chapainapppurchase.interfaces.ChapaVerifyTransactionCallback;
import com.yosef.chapainapppurchase.interfaces._PaymentCallback;
import com.yosef.chapainapppurchase.model.Customer;
import com.yosef.chapainapppurchase.model.Transaction;
import com.yosef.chapainapppurchase.payment_type.AppPayment;
import com.yosef.chapainapppurchase.utils.EncryptedKeyValue;
import com.yosef.chapainapppurchase.utils.Utils;
import com.yosef.chapainapppurchase.utils.Validator;

import java.util.Objects;

public class ChapaCheckoutPage {
    private static boolean isInstanceRunning = false;
    final ProgressBar webProgressView;
    private final WebView webView;
    private final ProgressBar loadingView;
    private final BottomSheetDialog _this;
    private final Context context;
    private final String TAG = "ChapaPaymentDialog --> ";
    private PaymentType paymentType;
    private _PaymentCallback callback;
    private PaymentPageStatus paymentPageStatus;
    private ChapaError _hasChapaError;
    private EncryptedKeyValue pref;
    @SuppressLint("SetJavaScriptEnabled")

    public ChapaCheckoutPage(@NonNull Context context) {
        this.context = context;
        _this = new BottomSheetDialog(context, R.style.BottomSheetDialogTheme);
        pref = new EncryptedKeyValue(context, EncryptedKeyValue.PREF_CHAPA);
        _this.setCanceledOnTouchOutside(false);
        loadingView = new ProgressBar(context);
        loadingView.setPadding(50, 60, 50, 60);
        webView = new WebView(context);
        webProgressView = new ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);
        loadingView.getIndeterminateDrawable().setColorFilter(new LightingColorFilter(0xFF000000, Color.parseColor("#80C348")));
        webProgressView.getIndeterminateDrawable().setColorFilter(new LightingColorFilter(0xFF000000, Color.parseColor("#80C348")));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        webProgressView.setLayoutParams(params);
        webProgressView.setIndeterminate(true);
        webView.addView(webProgressView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new ChapaWebClient());
        webView.addJavascriptInterface(new ChapaWebInterface(), "Chapa");
        _this.setOnDismissListener(dialogInterface -> {
            if (paymentPageStatus != PaymentPageStatus.PAYMENT_SUCCESSFUL && _hasChapaError == null) {
                paymentType.onPaymentCancel();
                if (callback != null) callback.onCancel();
            } else if (_hasChapaError != null) {
                paymentType.onPaymentFail(_hasChapaError);
                if (callback != null) callback.onError(_hasChapaError);
            }
            isInstanceRunning = false;
        });
    }

    public synchronized void processPayment(@NonNull String chapaCheckoutUrl, @NonNull ChapaCheckoutUrlCallback callback) {
        if (!_this.isShowing()) {
            showPaymentDialog();
            this.callback = callback;
            if (Patterns.WEB_URL.matcher(chapaCheckoutUrl).matches() && chapaCheckoutUrl.contains("checkout.chapa.co")) {
                handleView(PaymentPageStatus.LOADING_CHECKOUT_PAGE);
                webView.loadUrl(chapaCheckoutUrl);
            } else
                hasError(new ChapaError(ChapaError.INVALID_CHAPA_CHECKOUT_URL, "Invalid Chapa Checkout Url"));
        }
    }

    synchronized <T extends PaymentType> void processPayment(@NonNull PaymentType paymentType, _PaymentCallback<T> callback) {
        if (!_this.isShowing()) {
            showPaymentDialog();
            this.callback = callback;
            this.paymentType = paymentType;
            if (!Utils.isConnectionAvailable(context)) {
                hasError(new ChapaError(ChapaError.CONNECTIVITY_PROBLEM, "No internet connection"));
            } else {
                ChapaError paymentDataError = Validator.validateRequestData(context, paymentType, true);
                if (paymentDataError != null) {
                    hasError(paymentDataError);
                } else if (paymentType.getCustomer() == null)
                    _this.setContentView(new CustomerDialog(context, new CustomerCallback() {
                        @Override
                        public void onSuccess(Customer customer) {
                            paymentType.setCustomer(customer);
                            loadCheckoutPage();
                        }

                        @Override
                        public void onCanceled() {
                            _this.dismiss();
                        }
                    }));
                else loadCheckoutPage();
            }
        }

    }

    private void loadCheckoutPage() {
        handleView(PaymentPageStatus.LOADING_CHECKOUT_PAGE);
        String checkoutUrl = pref.getValue(paymentType.getTx_ref(), null);
        if (checkoutUrl != null) {
            webView.loadUrl(checkoutUrl);
        } else ChapaUtil.getCheckoutUrl(paymentType, new ChapaGetCheckOutUrlCallBack() {
            @Override
            public void onSuccess(String checkoutUrl) {
                pref.putValue(paymentType.getTx_ref(), checkoutUrl);
                new Handler(Looper.getMainLooper()).post(() -> webView.loadUrl(checkoutUrl));
            }

            @Override
            public void onFail(ChapaError error) {
                if ((paymentType instanceof AppPayment) && Objects.requireNonNull(error.getMessage()).contains("Transaction reference has been used before") && paymentType.getTx_ref().contains(Objects.requireNonNull(Utils.getAndroidId(context)))) {
                    verifyAppPayment(paymentType);
                } else {
                    hasError(error);
                }
            }
        });
    }

    private void hasError(ChapaError error) {
        _hasChapaError = error;
        if (paymentType.isShowPaymentError())
            new Handler(Looper.getMainLooper()).post(() -> handleView(PaymentPageStatus.PAYMENT_FAILED));
        else _this.dismiss();
    }

    private void handleView(PaymentPageStatus status) {
        paymentPageStatus = status;
        switch (status) {
            case LOADING_CHECKOUT_PAGE:
                _this.setContentView(loadingView);
                break;
            case PAYMENT_SUCCESSFUL:
                new Handler().postDelayed(_this::dismiss, 2000);
                break;
            case CHECKOUT_PAGE_LOADED:
                _this.setContentView(webView);
                break;
            case PAYMENT_FAILED:
                _this.setContentView(ErrorPage("Failed to Process Payment", _hasChapaError.getMessage()));
                new Handler().postDelayed(_this::dismiss, 3000);
                break;

        }
    }

    private View ErrorPage(String title, String detail) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(10, 10, 10, 10);
        LinearLayout parent = new LinearLayout(context);
        LinearLayout messagesLayout = new LinearLayout(context);
        messagesLayout.setOrientation(LinearLayout.VERTICAL);
        ImageView errorImage = new ImageView(context);
        errorImage.setImageResource(android.R.drawable.stat_sys_warning);
        errorImage.setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
        errorImage.setLayoutParams(params);
        TextView errorTitle = new TextView(context);
        errorTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        errorTitle.setLayoutParams(params);
        errorTitle.setTextColor(Color.RED);
        errorTitle.setText(title);
        TextView description = new TextView(context);
        description.setTextColor(Color.BLACK);
        description.setLayoutParams(params);
        description.setText(detail);
        parent.setPadding(50, 60, 50, 60);
        messagesLayout.setPadding(20, 0, 20, 0);
        parent.setOrientation(LinearLayout.HORIZONTAL);
        messagesLayout.addView(errorTitle);
        messagesLayout.addView(description);
        parent.addView(errorImage);
        parent.addView(messagesLayout);
        return parent;
    }

    private void showPaymentDialog() {
        if (!isInstanceRunning) _this.show();
        isInstanceRunning = true;
    }

    private void verifyAppPayment(PaymentType appPayment) {
        ChapaUtil.verifyTransaction(appPayment.getTx_ref(), new ChapaVerifyTransactionCallback() {
            @Override
            public void onResult(boolean verified, @Nullable Transaction transaction) {
                if (verified) {
                    pref.removeValue(paymentType.getTx_ref());
                    paymentPageStatus = PaymentPageStatus.PAYMENT_SUCCESSFUL;
                    _this.dismiss();
                    appPayment.onPaymentSuccess();
                    if (callback != null) callback.onSuccess(appPayment);
                } else {
                    paymentType.setTx_ref(ChapaUtil.generateTransactionRef(20, "TX-AppR-"));
                    loadCheckoutPage();
                }
            }

            @Override
            public void onError(ChapaError error) {
                appPayment.onPaymentFail(error);
                hasError(error);
            }
        });
    }

    enum PaymentPageStatus {
        LOADING_CHECKOUT_PAGE, CHECKOUT_PAGE_LOADED, PROCESSING_PAYMENT, PAYMENT_SUCCESSFUL, PAYMENT_FAILED
    }

    class ChapaWebInterface {
        @JavascriptInterface
        public void cancelPayment() {
            Log.d(TAG, "cancelPayment: ");
            _this.dismiss();
        }

        @JavascriptInterface
        public void processingPayment() {
            Log.d(TAG, "processingPayment: ");
            paymentPageStatus = PaymentPageStatus.PROCESSING_PAYMENT;
            _this.setCancelable(false);
            webView.setClickable(false);
        }
    }

    class ChapaWebClient extends WebViewClient {
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            webProgressView.setVisibility(View.GONE);
            if (url.contains("receipt")) {
                handleView(PaymentPageStatus.PAYMENT_SUCCESSFUL);
                paymentType.onPaymentSuccess();
                pref.removeValue(paymentType.getTx_ref());
                if (callback != null) callback.onSuccess(paymentType);
            } else if (paymentPageStatus != PaymentPageStatus.CHECKOUT_PAGE_LOADED)
                handleView(PaymentPageStatus.CHECKOUT_PAGE_LOADED);
            _this.setCancelable(true);
            webView.setClickable(true);
            String script = "var f = document.querySelector('form'); console.log(document); var a = document.getElementsByTagName('a')||[]; for(const el of a){ if(el.href==='javascript:close_window();'){ el.setAttribute('href','javascript:void(0);');el.setAttribute('onclick','Chapa.cancelPayment();'); }};  if(f){ f.addEventListener('submit', function(e){Chapa.processingPayment();})};";
            view.evaluateJavascript(script, null);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            webProgressView.setVisibility(View.VISIBLE);
            return false;
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            hasError(new ChapaError(500, description));
        }
    }
}
