package com.greencarson.reciclaapp;
import com.google.gson.annotations.SerializedName;

/*
   Clase LocationData: Representa los datos de ubicación que incluyen información de dirección.
   - Utiliza la anotación @SerializedName para vincular el campo "address" con la respuesta JSON.
   - Contiene un método getter para acceder a la instancia de la clase Address.

*/
public class LocationData {
    @SerializedName("address")
    private Address address;

    // Método getter para acceder a la instancia de la clase Address
    public Address getAddress() {
        return address;
    }
}
