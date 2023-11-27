package com.example.reciclaapp;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CenterModel {
    private String categoria = "";
    private List<String> dias = new ArrayList<>();
    private String direccion = "";
    private boolean estado = false;
    private String hora_apertura = "";
    private String hora_cierre = "";
    private String imagen = "";
    private Double latitud = 0.0;
    private Double longitud = 0.0;
    private String nombre = "";
    private String num_telefonico = "";
    private List<String> materiales = new ArrayList<>();

    public CenterModel(Map<String, Object> data) {
        if (data.containsKey("categoria")) {
            this.categoria = (String) data.get("categoria");
        }
        if (data.containsKey("dias")) {
            this.dias = (List<String>) data.get("dias");
        }
        if (data.containsKey("direccion")) {
            this.direccion = (String) data.get("direccion");
        }
        if (data.containsKey("estado")) {
            this.estado = (boolean) data.get("estado");
        }
        if (data.containsKey("hora_apertura")) {
            this.hora_apertura = (String) data.get("hora_apertura");
        }
        if (data.containsKey("hora_cierre")) {
            this.hora_cierre = (String) data.get("hora_cierre");
        }
        if (data.containsKey("imagen")) {
            this.imagen = (String) data.get("imagen");
        }
        if (data.containsKey("latitud")) {
            this.latitud = (Double) data.get("latitud");
        }
        if (data.containsKey("longitud")) {
            this.longitud = (Double) data.get("longitud");
        }
        if (data.containsKey("nombre")) {
            this.nombre = (String) data.get("nombre");
        }
        if (data.containsKey("num_telefonico")) {
            this.num_telefonico = (String) data.get("num_telefonico");
        }
        if (data.containsKey("materiales")) {
            this.materiales = (List<String>) data.get("materiales");
        }
    }

    public void imprimirTodo() {
        System.out.println("Categoria: " + this.categoria);
        System.out.println("Dias: " + this.dias);
        System.out.println("Direccion: " + this.direccion);
        System.out.println("Estado: " + this.estado);
        System.out.println("Hora de Apertura: " + this.hora_apertura);
        System.out.println("Hora de Cierre: " + this.hora_cierre);
        System.out.println("Imagen: " + this.imagen);
        System.out.println("Latitud: " + this.latitud);
        System.out.println("Longitud: " + this.longitud);
        System.out.println("Nombre: " + this.nombre);
        System.out.println("Numero Telefónico: " + this.num_telefonico);
        System.out.println("Materiales: " + this.materiales);
    }

    public boolean isValid() {
        return  !latitud.equals(0.0) &&
                !longitud.equals(0.0);
    }

    public String getNum_telefonico() {
        return num_telefonico;
    }

    public void setNum_telefonico(String num_telefonico) {
        this.num_telefonico = num_telefonico;
    }

    public List<String> getMateriales() {
        return materiales;
    }

    public void setMateriales(List<String> materiales) {
        this.materiales = materiales;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public List<String> getDias() {
        return dias;
    }

    public void setDias(List<String> dias) {
        this.dias = dias;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public String getHora_apertura() {
        return hora_apertura;
    }

    public void setHora_apertura(String hora_apertura) {
        this.hora_apertura = hora_apertura;
    }

    public String getHora_cierra() {
        return hora_cierre;
    }

    public void setHora_cierra(String hora_cierra) {
        this.hora_cierre = hora_cierra;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
