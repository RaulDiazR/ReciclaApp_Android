package com.example.reciclaapp.models;
/**
 * La clase McqRecolector modela a un recolector de materiales reciclables.
 * Contiene información sobre el nombre, apellidos, teléfono, foto de perfil,
 * cantidad de reseñas recibidas, suma total de calificaciones y un identificador único.
 * Además, proporciona métodos para calcular la calificación promedio y generar el
 * nombre completo del recolector.
 */
public class McqRecolector {

    String nombre;               // Nombre del recolector
    String apellidos;            // Apellidos del recolector
    String telefono;             // Número de teléfono del recolector
    String fotoUrl;              // URL de la foto de perfil del recolector
    int cantidadResenas;         // Cantidad total de reseñas recibidas por el recolector
    int sumaResenas;             // Suma total de calificaciones recibidas por el recolector
    String id;                   // Identificador único del recolector


    public McqRecolector() {
    }

    public McqRecolector(String nombre, String apellidos, String telefono, String fotoUrl, int cantidadResenas, int sumaResenas, String id) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.telefono = telefono;
        this.fotoUrl = fotoUrl;
        this.cantidadResenas = cantidadResenas;
        this.sumaResenas = sumaResenas;
        this.id = id;
    }

    public float calcularCalificacion() {
        if (cantidadResenas > 0) {
            float rating = (float) sumaResenas / (float) cantidadResenas;
            return Math.round(rating * 10.0f) / 10.0f; // Round to one decimal place
        }
        return 0.0f;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
