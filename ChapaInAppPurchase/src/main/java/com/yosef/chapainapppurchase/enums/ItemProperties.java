package com.yosef.chapainapppurchase.enums;

/**
 * Item behaviours used in ItemPayment
 * {@link #REPLACE}
 * {@link #ADD}
 * {@link #SUBTRACT}
 * {@link #MULTIPLY}
 */
public enum ItemProperties {

    /**
     * Replace previous value with the new one.
     * valid to any item type
     */
    REPLACE,

    /**
     * Sum previous value with current one.
     * valid to numeric item type
     */
    ADD,

    /**
     * Subtract current value from previous.
     * valid to numeric item type
     */
    SUBTRACT,

    /**
     * Multiply previous value with the new one.
     * valid to numeric item type
     */
    MULTIPLY
}
