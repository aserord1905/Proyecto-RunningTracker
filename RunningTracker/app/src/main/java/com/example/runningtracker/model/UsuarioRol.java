package com.example.runningtracker.model;

public class UsuarioRol {
    private String id_usuario;
    private String id_rol;

    public UsuarioRol(String id_usuario, String id_rol) {
        this.id_usuario = id_usuario;
        this.id_rol = id_rol;
    }

    public UsuarioRol() {
    }

    public String getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(String id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getId_rol() {
        return id_rol;
    }

    public void setId_rol(String id_rol) {
        this.id_rol = id_rol;
    }
}
