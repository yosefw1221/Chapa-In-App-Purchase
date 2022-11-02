package com.yosef.chapainapppurchase.model;

import androidx.annotation.NonNull;

/**
 * The customizations field (optional) allows you
 * Customize the look and feel of the payment modal. You can set a logo,
 * the store name to be displayed (title),and a description for the payment.
 */
public class Customization {
    private String title;
    private String description;
    private String logo;

    /**
     * Customization of chapa payment modal
     *
     * @param title       customized title of payment modal
     * @param description customized description of payment modal
     */
    public Customization(String title, String description) {
        this.title = title;
        this.description = description;
    }

    /**
     * Customization of chapa payment modal
     *
     * @param title       customized title of payment modal
     * @param description customized description of payment modal
     * @param logo        customized logo of payment modal
     */
    public Customization(String title, String description, String logo) {
        this.title = title;
        this.description = description;
        this.logo = logo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    @NonNull
    @Override
    public String toString() {
        return "Customization{" + "title='" + title + '\'' + ", description='" + description + '\'' + ", logo='" + logo + '\'' + '}';
    }
}
