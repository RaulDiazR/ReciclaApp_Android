package com.example.reciclaapp;

public class MaterialesItem {
    private final int imageResource; // Resource ID for the item's image
    private final String name; // Name or text associated with the item

    public MaterialesItem(int imageResource, String name) {
        this.imageResource = imageResource;
        this.name = name;
    }

    public int getImageResource() {
        return imageResource;
    }

    public String getText() {
        return name;
    }
}
