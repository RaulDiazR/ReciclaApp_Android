package com.example.reciclaapp;

import android.graphics.Bitmap;

public class MaterialesItem {
    private final int imageResource; // Resource ID for the item's image
    private final String name; // Name or text associated with the item
    int materialQuantity;
    String materialUnit;
    Bitmap fotoMaterial;

    public MaterialesItem(int imageResource, String name) {
        this.imageResource = imageResource;
        this.name = name;
        this.fotoMaterial = null;
    }

    public Bitmap getFotoMaterial() {
        return fotoMaterial;
    }

    public void setFotoMaterial(Bitmap fotoMaterial) {
        this.fotoMaterial = fotoMaterial;
    }

    public int getMaterialQuantity() {
        return materialQuantity;
    }

    public void setMaterialQuantity(int materialQuantity) {
        this.materialQuantity = materialQuantity;
    }

    public String getMaterialUnit() {
        return materialUnit;
    }

    public void setMaterialUnit(String materialUnit) {
        this.materialUnit = materialUnit;
    }

    public int getImageResource() {
        return imageResource;
    }

    public String getName() {
        return name;
    }
}
