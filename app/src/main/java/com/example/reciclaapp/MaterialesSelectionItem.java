package com.example.reciclaapp;

public class MaterialesSelectionItem {
    private final int imageResource; // Resource ID for the item's image
    private final String name; // Name or text associated with the item

    public MaterialesSelectionItem(int imageResource, String name) {
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
