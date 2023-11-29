package com.greencarson.reciclaapp;

import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;
import java.util.Map;

public class FavoriteModel {
    private ArrayList<DocumentReference> centros;
    private ArrayList<DocumentReference> recolectores;
    private ArrayList<String> CenterNames;

    public FavoriteModel() {
        this.centros = new ArrayList<>();
        this.recolectores = new ArrayList<>();
        this.CenterNames = new ArrayList<>();
    }

    public FavoriteModel(Map<String, Object> data) {

        this.CenterNames = new ArrayList<>();

        if (data.containsKey("centros")) {
            centros = (ArrayList<DocumentReference>) data.get("centros");
            System.out.println(centros);
        }

        if (data.containsKey("recolectores")) {
            recolectores = (ArrayList<DocumentReference>) data.get("recolectores");
        }
    }

    public ArrayList<String> getCenterNames() {
        return CenterNames;
    }

    public void setCenterNames(ArrayList<String> centerNames) {
        CenterNames = centerNames;
    }

    public ArrayList<DocumentReference> getCentros() {
        return centros;
    }

    public void setCentros(ArrayList<DocumentReference> centros) {
        this.centros = centros;
    }

    public ArrayList<DocumentReference> getRecolectores() {
        return recolectores;
    }

    public void setRecolectores(ArrayList<DocumentReference> recolectores) {
        this.recolectores = recolectores;
    }
}
