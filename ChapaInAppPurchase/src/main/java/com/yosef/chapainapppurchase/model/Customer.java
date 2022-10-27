package com.yosef.chapainapppurchase.model;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

public class Customer {
    private final String first_name;
    private final String last_name;
    private final String email;

    public Customer(@NonNull String first_name, @NotNull String last_name, @NonNull String email) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
    }

    public String getFirstName() {
        return first_name;
    }

    public String getLastName() {
        return last_name;
    }

    public String getEmail() {
        return email;
    }

    @NonNull
    @Override
    public String toString() {
        return "Customer{" +
                "first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
