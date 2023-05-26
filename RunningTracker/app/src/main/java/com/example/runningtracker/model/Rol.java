package com.example.runningtracker.model;

public class Rol {
    private String nombre_rol;
    private String rol_id;

    public Rol(String nombre_rol, String rol_id) {
        this.nombre_rol = nombre_rol;
        this.rol_id = rol_id;
    }

    public Rol() {
    }

    public String getNombre_rol() {
        return nombre_rol;
    }

    public void setNombre_rol(String nombre_rol) {
        this.nombre_rol = nombre_rol;
    }

    public String getRol_id() {
        return rol_id;
    }

    public void setRol_id(String rol_id) {
        this.rol_id = rol_id;
    }
}
