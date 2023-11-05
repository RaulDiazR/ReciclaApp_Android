package com.example.reciclaapp.models;

public class McqRecolector {
    String nombre;
    String apellidos;
    String telefono;
    String fotoUrl;
    int cantidadResenas;
    int sumaResenas;

    public McqRecolector() {
    }

    public McqRecolector(String nombre, String apellidos, String telefono, String fotoUrl, int cantidadResenas, int sumaResenas) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.telefono = telefono;
        this.fotoUrl = fotoUrl;
        this.cantidadResenas = cantidadResenas;
        this.sumaResenas = sumaResenas;
    }

    public float calcularCalificacion() {
        if (cantidadResenas > 0) {
            float rating = (float) sumaResenas / (float) cantidadResenas;
            return Math.round(rating * 10.0f) / 10.0f; // Round to one decimal place
        }
        return 0.0f;
    }

    public int getCantidadResenas() {
        return cantidadResenas;
    }

    public void setCantidadResenas(int cantidadResenas) {
        this.cantidadResenas = cantidadResenas;
    }

    public int getSumaResenas() {
        return sumaResenas;
    }

    public void setSumaResenas(int sumaResenas) {
        this.sumaResenas = sumaResenas;
    }

    public String getFotoUrl() {
        return fotoUrl;
    }

    public void setFotoUrl(String fotoUrl) {
        this.fotoUrl = fotoUrl;
    }

    public String generarNombreCompleto(){
        return nombre + " " + apellidos;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}
