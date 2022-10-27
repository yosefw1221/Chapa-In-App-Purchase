package com.yosef.chapainapppurchase.enums;

import androidx.annotation.NonNull;

public enum Currency {
    USD("USD"),
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
