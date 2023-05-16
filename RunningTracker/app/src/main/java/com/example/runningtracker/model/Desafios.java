package com.example.runningtracker.model;

public class Desafios {
    private String id_desafio;
    private String nombre;
    private String kilometros_desafio;
    private String descripcion;


    public Desafios() {
    }

    public Desafios(String id_desafio, String nombre, String kilometros_desafio, String descripcion) {
        this.id_desafio = id_desafio;
        this.nombre = nombre;
        this.kilometros_desafio = kilometros_desafio;
        this.descripcion = descripcion;
    }

    public String getId_desafio() {
        return id_desafio;
    }

    public void setId_desafio(String id_desafio) {
        this.id_desafio = id_desafio;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getKilometros_desafio() {
        return kilometros_desafio;
    }

    public void setKilometros_desafio(String kilometros_desafio) {
        this.kilometros_desafio = kilometros_desafio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
