package com.example.runningtracker.model;

import java.sql.Time;
import java.util.Date;

public class Entrenamiento {
    private String id_entrenamientos;

    private String id_usuario;

    private String distancia;

    private String velocidad_media;

    private String calorias_quemadas;

    public Entrenamiento() {
    }

    public Entrenamiento(String id_entrenamientos, String id_usuario, String distancia, String velocidad_media, String calorias_quemadas) {
        this.id_entrenamientos = id_entrenamientos;
        this.id_usuario = id_usuario;
        this.distancia = distancia;
        this.velocidad_media = velocidad_media;
        this.calorias_quemadas = calorias_quemadas;
    }

    public String getId_entrenamientos() {
        return id_entrenamientos;
    }

    public void setId_entrenamientos(String id_entrenamientos) {
        this.id_entrenamientos = id_entrenamientos;
    }

    public String getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(String id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getDistancia() {
        return distancia;
    }

    public void setDistancia(String distancia) {
        this.distancia = distancia;
    }

    public String getVelocidad_media() {
        return velocidad_media;
    }

    public void setVelocidad_media(String velocidad_media) {
        this.velocidad_media = velocidad_media;
    }

    public String getCalorias_quemadas() {
        return calorias_quemadas;
    }

    public void setCalorias_quemadas(String calorias_quemadas) {
        this.calorias_quemadas = calorias_quemadas;
    }

    @Override
    public String toString() {
        return "Entrenamiento{" +
                "id_entrenamientos='" + id_entrenamientos + '\'' +
                ", id_usuario='" + id_usuario + '\'' +
                ", distancia='" + distancia + '\'' +
                ", velocidad_media='" + velocidad_media + '\'' +
                ", calorias_quemadas='" + calorias_quemadas + '\'' +
                '}';
    }
}
