package com.example.runningtracker.model;

public class Historial {
    private String id_historial;

    private String id_desafios;

    private String id_usuarios;

    private float kilometros_realizados;

    public Historial() {
    }

    public Historial(String id_historial, String id_desafios, String id_usuarios, float kilometros_realizados) {
        this.id_historial = id_historial;
        this.id_desafios = id_desafios;
        this.id_usuarios = id_usuarios;
        this.kilometros_realizados = kilometros_realizados;
    }

    public String getId_historial() {
        return id_historial;
    }

    public void setId_historial(String id_historial) {
        this.id_historial = id_historial;
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

    public float getKilometros_realizados() {
        return kilometros_realizados;
    }

    public void setKilometros_realizados(float kilometros_realizados) {
        this.kilometros_realizados = kilometros_realizados;
    }
}
