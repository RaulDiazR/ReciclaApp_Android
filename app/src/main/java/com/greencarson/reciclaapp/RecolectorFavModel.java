package com.greencarson.reciclaapp;

public class RecolectorFavModel {

    private String recolector_name;
    private int recolector_rating;
    private int recolector_image;

    // Constructor
    public RecolectorFavModel(String recolector_name, int recolector_rating, int recolector_image) {
        this.recolector_name = recolector_name;
        this.recolector_rating = recolector_rating;
        this.recolector_image = recolector_image;
    }

    // Getter and Setter
    public String getRecolector_name() {
        return recolector_name;
    }

    public void setRecolector_name(String recolector_name) {
        this.recolector_name = recolector_name;
    }

    public int getRecolector_rating() {
        return recolector_rating;
    }

    public void setRecolector_rating(int recolector_rating) {
        this.recolector_rating = recolector_rating;
    }

    public int getRecolector_image() {
        return recolector_image;
    }

    public void setRecolector_image(int recolector_image) {
        this.recolector_image = recolector_image;
    }
}
