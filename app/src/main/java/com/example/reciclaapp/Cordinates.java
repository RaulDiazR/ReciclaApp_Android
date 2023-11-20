package com.example.reciclaapp;

import com.google.gson.annotations.SerializedName;
/**
 * La clase Coordinates representa las coordenadas geográficas de una ubicación,
 * incluyendo la latitud y la longitud.
 */
public class Cordinates {
    // Atributos de la clase
    @SerializedName("lat")
    private String latitude;    // Latitud de la ubicación

    @SerializedName("lon")
    private String longitude;   // Longitud de la ubicación

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }
}
