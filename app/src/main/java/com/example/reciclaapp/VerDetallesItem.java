package com.example.reciclaapp;
/**
 * Clase que representa un elemento de la lista de detalles de materiales seleccionados en una orden de reciclaje.
 */
public class VerDetallesItem {
    private int iconoMaterial;
    private String nombreMaterial;
    private String unidadMaterial;
    private String cantidadMaterial;
    private String fotoUrl;

    public VerDetallesItem(int iconoMaterial, String text, String unidadMaterial, String cantidadMaterial, String fotoUrl) {
        this.iconoMaterial = iconoMaterial;
        this.nombreMaterial = text;
        this.unidadMaterial = unidadMaterial;
        this.cantidadMaterial = cantidadMaterial;
        this.fotoUrl = fotoUrl;
    }

    public String getFotoUrl() {
        return fotoUrl;
    }

    public void setFotoUrl(String fotoUrl) {
        this.fotoUrl = fotoUrl;
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

