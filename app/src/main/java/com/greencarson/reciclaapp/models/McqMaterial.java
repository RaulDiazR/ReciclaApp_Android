package com.greencarson.reciclaapp.models;
/**
 * La clase McqMaterial representa un material reciclable y sus atributos asociados.
 * Los objetos de esta clase contienen información sobre la unidad de medida, cantidad,
 * URL de la foto y nombre del material.
 */
public class McqMaterial {

    private String unidad; // Unidad de medida del material (por ejemplo, kilogramos)
    private int cantidad; // Cantidad del material disponible
    private String fotoUrl; // URL de la foto que representa el material
    private String nombre; // Nombre del material reciclable

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
