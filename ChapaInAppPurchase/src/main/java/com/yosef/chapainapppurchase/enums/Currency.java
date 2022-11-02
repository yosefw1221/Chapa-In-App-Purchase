package com.yosef.chapainapppurchase.enums;

import androidx.annotation.NonNull;

/**
 * Currency supported by chapa
 * {@link #ETB}
 * {@link #USD}
 */

public enum Currency {
    /**
     * US Dollar
     */
    USD("USD"),

    /**
     * Ethiopian Birr
     */
    ETB("ETB");
    private final String currency;

    Currency(String currency) {
        this.currency = currency;
    }

    @NonNull
    public String toString() {
        return currency;
    }
}
