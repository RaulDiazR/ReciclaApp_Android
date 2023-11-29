package com.greencarson.reciclaapp;

import java.util.Map;

public class MaterialModel {
    private String filename = "";
    private String imageURL = "";
    private String name = "";

    public MaterialModel(String filename, String imageURL) {
        this.filename = filename;
        this.imageURL = imageURL;
    }

    public MaterialModel(Map<String, Object> data) {
        if (data.containsKey("filename")) {
            this.filename = (String) data.get("filename");
        }

        if (data.containsKey("imageUrl")) {
            this.imageURL = (String) data.get("imageUrl");
        }
    }

    public void imprimirTodo() {
        System.out.println(this.filename);
        System.out.println(this.imageURL);
    }

    public boolean isValid() {
        return !this.imageURL.equals("") && !this.imageURL.isEmpty();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
