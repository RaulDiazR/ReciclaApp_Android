package com.example.reciclaapp.models;

import java.util.Map;
/**
 * La clase McqRecoleccion encapsula información sobre la recolección de materiales reciclables.
 * Contiene detalles como el identificador de la recolección, el usuario cliente asociado, fecha y
 * horarios de recolección, comentarios, estado de la recolección, entre otros atributos.
 */
public class McqRecoleccion {

    private String Rid;                      // Identificador único de la recolección
    private String idUsuarioCliente;         // Identificador del usuario cliente asociado
    private String fechaRecoleccion;         // Fecha de la recolección
    private String horaRecoleccionInicio;    // Hora de inicio de la recolección
    private String horaRecoleccionFinal;     // Hora de finalización de la recolección
    private String comentarios;              // Comentarios relacionados con la recolección
    private boolean enPersona;               // Indica si la recolección se realiza en persona
    private boolean calificado;              // Indica si la recolección ha sido calificada
    private boolean recolectada;             // Indica si la recolección ha sido completada
    private String estado;                   // Estado actual de la recolección
    private Long timeStamp;                  // Marca de tiempo asociada a la recolección
    private Map<String, Object> recolector;  // Información del recolector asociado

    private Map<String, Map<String, Object>> materiales;  // Información detallada de los materiales recolectados


    public McqRecoleccion() {
    }

    public boolean getRecolectada() {
        return recolectada;
    }

    public void setRecolectada(boolean recolectada) {
        this.recolectada = recolectada;
    }

    public Map<String, Object> getRecolector() {
        return recolector;
    }

    public void setRecolector(Map<String, Object> recolector) {
        this.recolector = recolector;
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

    public Map<String, Map<String, Object>> getMateriales() {
        return materiales;
    }

    public void setMateriales(Map<String, Map<String, Object>> materiales) {
        this.materiales = materiales;
    }
}