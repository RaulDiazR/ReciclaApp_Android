package com.greencarson.reciclaapp;

public class CentroFavModel {

    private String centro_name;
    private String centro_address;
    private int centro_image;

    // Constructor
    public CentroFavModel(String centro_name, String centro_address, int centro_image) {
        this.centro_name = centro_name;
        this.centro_address = centro_address;
        this.centro_image = centro_image;
    }

    // Getter and Setter
    public String getCentro_name() {
        return centro_name;
    }

    public void setCentro_name(String centro_name) {
        this.centro_name = centro_name;
    }

    public String getCentro_address() {
        return centro_address;
    }

    public void setCentro_address(String centro_address) {
        this.centro_address = centro_address;
    }

    public int getCentro_image() {
        return centro_image;
    }

    public void setCentro_image(int centro_image) {
        this.centro_image = centro_image;
    }
}
