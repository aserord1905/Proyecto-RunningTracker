package com.example.runningtracker.model;

public class Desafios {
    private String id_desafios;

    private String id_usuarios;

    private float kilometros_desafio;

    public Desafios() {

    }

    public Desafios(String id_desafios, String id_usuarios, float kilometros_desafio) {
        this.id_desafios = id_desafios;
        this.id_usuarios = id_usuarios;
        this.kilometros_desafio = kilometros_desafio;
    }

    public String getId_desafios() {
        return id_desafios;
    }

    public void setId_desafios(String id_desafios) {
        this.id_desafios = id_desafios;
    }

    public String getId_usuarios() {
        return id_usuarios;
    }

    public void setId_usuarios(String id_usuarios) {
        this.id_usuarios = id_usuarios;
    }

    public float getKilometros_desafio() {
        return kilometros_desafio;
    }

    public void setKilometros_desafio(float kilometros_desafio) {
        this.kilometros_desafio = kilometros_desafio;
    }
}
