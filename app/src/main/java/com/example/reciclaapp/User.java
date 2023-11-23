package com.example.reciclaapp;

public class User {
    private String nombre;
    private String apellidos;
    private String correo;
    private String telefono;
    private String fechaNacimiento;
    private int rank_points;
    private int highest1;

    public User() {
        // Default constructor required for Firestore
    }

    public User(String nombre, String apellidos, String correo, String telefono, String fechaNacimiento, int rank_points, int highest1) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.correo = correo;
        this.telefono = telefono;
        this.fechaNacimiento = fechaNacimiento;
        this.rank_points = rank_points;
        this.highest1 = highest1;
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

    public String getCorreo() { return correo; }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {this.fechaNacimiento = fechaNacimiento;}

    public int getRank_points() { return rank_points; }

    public void setRank_points(int rank_points) {this.rank_points = rank_points;}

    public int getHighest1() { return highest1; }

    public void setHighest1(int highest1) {this.highest1 = highest1;}
}
