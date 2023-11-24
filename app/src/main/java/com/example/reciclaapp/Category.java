package com.example.reciclaapp;

public class Category {
    private String nombre;
    private String imageResourceLink;

    public Category(String title, String imageResourceId) {
        this.nombre = title;
        this.imageResourceLink = imageResourceId;
    }

    public String getTitle() {
        return nombre;
    }

    public String getImageResourceId() {
        return imageResourceLink;
    }
}
