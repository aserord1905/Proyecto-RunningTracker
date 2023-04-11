package com.example.runningtracker.model;

import java.sql.Time;
import java.util.Date;

public class Entrenamiento {
    private String id_entrenamientos;

    private String id_usuario;

    private Date fecha;

    private float distancia;

    private Time tiempo;

    private float velocidad_media;

    private int calorias_quemadas;

    //Coonstructor por defecto
    public Entrenamiento() {

    }

    public Entrenamiento(String id_entrenamientos, String id_usuario, Date fecha, float distancia, Time tiempo, float velocidad_media, int calorias_quemadas) {
        this.id_entrenamientos = id_entrenamientos;
        this.id_usuario = id_usuario;
        this.fecha = fecha;
        this.distancia = distancia;
        this.tiempo = tiempo;
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

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public float getDistancia() {
        return distancia;
    }

    public void setDistancia(float distancia) {
        this.distancia = distancia;
    }

    public Time getTiempo() {
        return tiempo;
    }

    public void setTiempo(Time tiempo) {
        this.tiempo = tiempo;
    }

    public float getVelocidad_media() {
        return velocidad_media;
    }

    public void setVelocidad_media(float velocidad_media) {
        this.velocidad_media = velocidad_media;
    }

    public int getCalorias_quemadas() {
        return calorias_quemadas;
    }

    public void setCalorias_quemadas(int calorias_quemadas) {
        this.calorias_quemadas = calorias_quemadas;
    }
}
