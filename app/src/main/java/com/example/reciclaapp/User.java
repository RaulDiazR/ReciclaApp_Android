package com.example.reciclaapp;

import java.net.URL;

public class User {
    private String nombres;
    private String apellidos;
    private String correo;
    private String telefono;
    private String fechaNacimiento;
    private String fotoPerfil;
    private int rank_points;
    private int highest1;

    public User() {
        // Default constructor required for Firestore
    }

    public User(String nombres, String apellidos, String correo, String telefono, String fechaNacimiento, String fotoPerfil, int rank_points, int highest1) {
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.correo = correo;
        this.telefono = telefono;
        this.fechaNacimiento = fechaNacimiento;
        this.fotoPerfil = fotoPerfil;
        this.rank_points = rank_points;
        this.highest1 = highest1;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
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

    public String getFotoPerfil() { return fotoPerfil; }

    public void setFotoPerfil(String fotoPerfil) {this.fotoPerfil = fotoPerfil;}

    public int getRank_points() { return rank_points; }

    public void setRank_points(int rank_points) {this.rank_points = rank_points;}

    public int getHighest1() { return highest1; }

    public void setHighest1(int highest1) {this.highest1 = highest1;}
}
