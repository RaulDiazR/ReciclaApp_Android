package com.example.reciclaapp;
/**
 * Clase de modelo para los datos del encabezado en la actividad MaterialesActivity.
 * Contiene información sobre la fecha y el tiempo de la orden.
 */
public class MaterialesHeaderItem {
    private final String fechaOrden;
    private final String tiempoOrden;

    public MaterialesHeaderItem(String fechaOrden, String tiempoOrden) {
        this.fechaOrden = fechaOrden;
        this.tiempoOrden = tiempoOrden;
    }

    public String getFechaOrden() {
        return fechaOrden;
    }

    public String getTiempoOrden() {
        return tiempoOrden;
    }
}
