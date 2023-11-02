package com.example.reciclaapp;

import android.graphics.drawable.Drawable;

import com.example.reciclaapp.models.McqRecolector;

public class HistorialItem {
    Drawable backgroundDrawable; // New variable for background
    String fecha, horario, materiales, estado;
    int estadoColor;
    boolean isRated;
    String id;
    McqRecolector recolector;
    Long timeStamp;

    public HistorialItem(Drawable backgroundDrawable, String fecha, String horario, String materiales, String estado, int estadoColor, boolean isRated, String id, McqRecolector recolector, Long timeStamp) {
        this.backgroundDrawable = backgroundDrawable;
        this.fecha = fecha;
        this.horario = horario;
        this.materiales = materiales;
        this.estado = estado;
        this.estadoColor = estadoColor;
        this.isRated = isRated;
        this.id = id;
        this.recolector = recolector;
        this.timeStamp = timeStamp;
    }

    public Long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public McqRecolector getRecolector() {
        return recolector;
    }

    public void setRecolector(McqRecolector recolector) {
        this.recolector = recolector;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean getIsRated() {
        return isRated;
    }

    public void setIsRated(boolean rated) {
        isRated = rated;
    }

    public Drawable getBackgroundDrawable() {
        return backgroundDrawable;
    }

    public void setBackgroundDrawable(Drawable backgroundDrawable) { this.backgroundDrawable = backgroundDrawable;}

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public String getMateriales() {
        return materiales;
    }

    public void setMateriales(String materiales) {
        this.materiales = materiales;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getEstadoColor() { return estadoColor; }

    public void setEstadoColor(int estadoColor) { this.estadoColor = estadoColor; }
}
