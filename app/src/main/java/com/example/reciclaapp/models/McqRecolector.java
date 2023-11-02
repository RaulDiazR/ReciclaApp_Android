package com.example.reciclaapp.models;

public class McqRecolector {
    String id;
    String nombre;
    String apellidos;
    String telefono;

    public McqRecolector() {
    }

    public String generarNombreCompleto(){
        return nombre + " " + apellidos;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
