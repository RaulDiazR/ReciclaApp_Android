package com.example.reciclaapp;

public class User {
    private String nombre_s;
    private String apellido_s;
    private String correo;
    private String telefono;
    private String fecha_nacimiento;
    private int rank_points;
    private int highest1;

    public User() {
        // Default constructor required for Firestore
    }

    public User(String nombre_s, String apellido_s, String correo, String telefono, String fecha_nacimiento, int rank_points, int highest1) {
        this.nombre_s = nombre_s;
        this.apellido_s = apellido_s;
        this.correo = correo;
        this.telefono = telefono;
        this.fecha_nacimiento = fecha_nacimiento;
        this.rank_points = rank_points;
        this.highest1 = highest1;
    }

    public String getNombre_s() {
        return nombre_s;
    }

    public void setNombre_s(String nombre_s) {
        this.nombre_s = nombre_s;
    }

    public String getApellido_s() {
        return apellido_s;
    }

    public void setApellido_s(String apellido_s) {
        this.apellido_s = apellido_s;
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

    public String getFecha_nacimiento() {
        return fecha_nacimiento;
    }

    public void setFecha_nacimiento(String fecha_nacimiento) {this.fecha_nacimiento = fecha_nacimiento;}

    public int getRank_points() { return rank_points; }

    public void setRank_points(int rank_points) {this.rank_points = rank_points;}

    public int getHighest1() { return highest1; }

    public void setHighest1(int highest1) {this.highest1 = highest1;}
}
