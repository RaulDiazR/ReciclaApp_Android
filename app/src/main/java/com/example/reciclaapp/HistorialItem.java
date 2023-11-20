package com.example.reciclaapp;

import android.graphics.drawable.Drawable;

import com.example.reciclaapp.models.McqMaterial;
import com.example.reciclaapp.models.McqRecolector;

import java.util.List;
/*
   Clase HistorialItem: Representa un elemento de historial que se muestra en el RecyclerView.
   Contiene información sobre la fecha, horario, materiales, estado, etc. de una actividad histórica.
   También incluye métodos getter y setter para acceder y modificar sus atributos.
*/
public class HistorialItem {
    Drawable backgroundDrawable; // New variable for background
    String fecha, horario, totalMateriales, estado;
    int estadoColor;
    boolean isRated;
    String id;
    McqRecolector recolector;
    Long timeStamp;
    boolean enPersona;

    List<McqMaterial> materialesList;

    public HistorialItem(Drawable backgroundDrawable, String fecha, String horario, String totalMateriales, String estado, int estadoColor, boolean isRated, String id, McqRecolector recolector, Long timeStamp, List<McqMaterial> materialesList, boolean enPersona) {
        this.backgroundDrawable = backgroundDrawable;
        this.fecha = fecha;
        this.horario = horario;
        this.totalMateriales = totalMateriales;
        this.estado = estado;
        this.estadoColor = estadoColor;
        this.isRated = isRated;
        this.id = id;
        this.recolector = recolector;
        this.timeStamp = timeStamp;
        this.materialesList = materialesList;
        this.enPersona = enPersona;
    }

    public HistorialItem() {
    }

    public boolean getEnPersona() {
        return enPersona;
    }

    public void setEnPersona(boolean enPersona) {
        this.enPersona = enPersona;
    }

    public boolean isRated() {
        return isRated;
    }

    public void setRated(boolean rated) {
        isRated = rated;
    }

    public List<McqMaterial> getMaterialesList() {
        return materialesList;
    }

    public void setMaterialesList(List<McqMaterial> materialesList) {
        this.materialesList = materialesList;
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

    public String getTotalMateriales() {
        return totalMateriales;
    }

    public void setTotalMateriales(String totalMateriales) {
        this.totalMateriales = totalMateriales;
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
