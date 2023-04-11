package com.example.runningtracker.model;

public class Usuario {
    private String id;
    private String username;
    private String password;
    private float peso;
    private String sexo;
    private String tipo;

    public Usuario() {
    }

    public Usuario(String id, String username, String password, float peso, String sexo, String tipo) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.peso = peso;
        this.sexo = sexo;
        this.tipo = tipo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public float getPeso() {
        return peso;
    }

    public void setPeso(float peso) {
        this.peso = peso;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
