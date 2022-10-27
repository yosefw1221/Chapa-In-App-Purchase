package com.yosef.chapainapppurchase.model;

import androidx.annotation.NonNull;

public class Customization {
    private String title;
    private String description;
    private String logo;

    public Customization(String title, String description) {
        this.title = title;
        this.description = description;
    }
    public Customization(String title, String description, String logo) {
        this.title = title;
        this.description = description;
        this.logo = logo;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getLogo() {
        return logo;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    @NonNull
    @Override
    public String toString() {
        return "Customization{" + "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", logo='" + logo + '\'' +
                '}';
    }
}
