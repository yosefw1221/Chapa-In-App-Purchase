package com.yosef.chapainapppurchase;

public class ChapaError extends Exception {
    public static int INTERNAL_ERROR = 500;
    public static int CHAPA_ERROR = 400;
    public static int UNSUPPORTED_DATA_TYPE = 422;
    public static int CHAPA_NOT_INITIALIZED = 433;
    public static int INVALID_CHAPA_CHECKOUT_URL = 422;
    public static int CONNECTIVITY_PROBLEM = 599;
    public static int INVALID_DATA = 600;
    public static int INVALID_CUSTOMER_DATA = 601;
    public static int INVALID_CALLBACK_URL = 602;
    public static int INVALID_CHAPA_KEY = 603;
    public static int INVALID_CURRENCY = 604;
    public static int INVALID_TX_REF = 605;
    public static int INVALID_AMOUNT = 606;
    public static int TEST_KEY_IN_PRODUCTION = 607;
    public static int INITIALIZE_ON_APP_MAIN_ACTIVITY = 700;
    public static int PHONE_ID_MISMATCH = 701;

    private final int code;

    public ChapaError(int code, String message) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
