package com.example.reciclaapp;

public class VerDetallesItem {
    private int iconoMaterial;
    private String nombreMaterial;
    private String unidadMaterial;
    private String cantidadMaterial;

    public VerDetallesItem(int imageResource, String text, String unidadMaterial, String cantidadMaterial) {
        this.iconoMaterial = imageResource;
        this.nombreMaterial = text;
        this.unidadMaterial = unidadMaterial;
        this.cantidadMaterial = cantidadMaterial;
    }

    public int getIconoMaterial() {
        return iconoMaterial;
    }

    public void setIconoMaterial(int iconoMaterial) {
        this.iconoMaterial = iconoMaterial;
    }

    public String getNombreMaterial() {
        return nombreMaterial;
    }

    public void setNombreMaterial(String text) {
        this.nombreMaterial = text;
    }

    public String getUnidadMaterial() {
        return unidadMaterial;
    }

    public void setUnidadMaterial(String unidadMaterial) {
        this.unidadMaterial = unidadMaterial;
    }

    public String getCantidadMaterial() {
        return cantidadMaterial;
    }

    public void setCantidadMaterial(String cantidadMaterial) {
        this.cantidadMaterial = cantidadMaterial;
    }
}

