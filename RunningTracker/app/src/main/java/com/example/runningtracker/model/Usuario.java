package com.example.runningtracker.model;

public class Usuario {
    private String id_usuario;
    private String username;
    private String password;
    private String peso;
    private String sexo;
    private String tipo;

    public Usuario() {
    }



    public Usuario(String id_usuario, String username) {
        this.id_usuario = id_usuario;
        this.username = username;
    }

    public Usuario(String id_usuario, String username, String password, String peso, String sexo, String tipo) {
        this.id_usuario = id_usuario;
        this.username = username;
        this.password = password;
        this.peso = peso;
        this.sexo = sexo;
        this.tipo = tipo;
    }

    public Usuario(String username, String password, String peso, String sexo, String tipo) {
        this.username = username;
        this.password = password;
        this.peso = peso;
        this.sexo = sexo;
        this.tipo = tipo;
    }


    public String getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(String id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getPeso() {
        return peso;
    }

    public void setPeso(String peso) {
        this.peso = peso;
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
