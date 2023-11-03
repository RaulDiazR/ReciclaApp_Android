package com.example.reciclaapp.models;

public class McqMaterial {
    private String unidad;
    private int cantidad;
    private String fotoUrl;
    private String nombre;

    public McqMaterial() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUnidad() {
        return unidad;
    }

    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getFotoUrl() {
        return fotoUrl;
    }

    public void setFotoUrl(String fotoEvidencia) {
        this.fotoUrl = fotoEvidencia;
    }
}
