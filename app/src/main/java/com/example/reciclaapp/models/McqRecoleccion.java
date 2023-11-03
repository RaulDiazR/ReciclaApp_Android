package com.example.reciclaapp.models;

import java.util.Map;

public class McqRecoleccion {
    private String Rid;
    private String idUsuarioCliente;
    private String idRecolector;
    private String fechaRecoleccion;
    private String horaRecoleccionInicio;
    private String horaRecoleccionFinal;
    private String comentarios;
    private boolean enPersona;
    private boolean calificado;
    private String estado;
    private Long timeStamp;

    private Map<String, McqMaterial> materiales;

    public McqRecoleccion() {
    }

    public Long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public boolean getCalificado() {
        return calificado;
    }

    public void setCalificado(boolean rated) {
        calificado = rated;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public boolean getEnPersona() {
        return enPersona;
    }

    public void setEnPersona(boolean enPersona) {
        this.enPersona = enPersona;
    }

    public String getRid() {
        return Rid;
    }

    public void setRid(String rid) {
        Rid = rid;
    }

    public String getIdUsuarioCliente() {
        return idUsuarioCliente;
    }

    public void setIdUsuarioCliente(String idUsuarioCliente) {
        this.idUsuarioCliente = idUsuarioCliente;
    }

    public String getIdRecolector() {
        return idRecolector;
    }

    public void setIdRecolector(String idRecolector) {
        this.idRecolector = idRecolector;
    }

    public String getFechaRecoleccion() {
        return fechaRecoleccion;
    }

    public void setFechaRecoleccion(String fechaRecoleccion) {
        this.fechaRecoleccion = fechaRecoleccion;
    }

    public String getHoraRecoleccionInicio() {
        return horaRecoleccionInicio;
    }

    public void setHoraRecoleccionInicio(String horaRecoleccionInicio) {
        this.horaRecoleccionInicio = horaRecoleccionInicio;
    }

    public String getHoraRecoleccionFinal() {
        return horaRecoleccionFinal;
    }

    public void setHoraRecoleccionFinal(String horaRecoleccionFinal) {
        this.horaRecoleccionFinal = horaRecoleccionFinal;
    }

    public String getComentarios() {
        return comentarios;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }

    public Map<String, McqMaterial> getMateriales() {
        return materiales;
    }

    public void setMateriales(Map<String, McqMaterial> materiales) {
        this.materiales = materiales;
    }
}