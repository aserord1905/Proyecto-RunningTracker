package com.example.runningtracker.model;

import java.sql.Time;
import java.util.Date;

public class Entrenamiento {
    private String id_entrenamientos;

    private String id_usuario;

    private String distancia;

    private String tiempo;


    public Entrenamiento() {
    }

    public Entrenamiento(String id_entrenamientos, String id_usuario, String distancia, String tiempo) {
        this.id_entrenamientos = id_entrenamientos;
        this.id_usuario = id_usuario;
        this.distancia = distancia;
        this.tiempo = tiempo;
    }

    public String getTiempo() {
        return tiempo;
    }

    public void setTiempo(String tiempo) {
        this.tiempo = tiempo;
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


    @Override
    public String toString() {
        return "Entrenamiento{" +
                "id_entrenamientos='" + id_entrenamientos + '\'' +
                ", id_usuario='" + id_usuario + '\'' +
                ", distancia='" + distancia + '\'' +
                ", tiempo='" + tiempo + '\'' +
                '}';
    }
}
