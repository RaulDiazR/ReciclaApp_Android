package com.example.reciclaapp;

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
