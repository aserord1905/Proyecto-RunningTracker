package com.example.runningtracker.model;

public class Usuario {
    private int id_usuario;
    private String nombre_usuario;
    private String apellidos;
    private String contrasenia;
    private double peso; //Expresada en kg (Formato XX,X)
    private double altura; // Expresada en metros (Y,YY)
    private String sexo; //Hombre o mujer.

    public Usuario(int id_usuario, String nombre_usuario, String apellidos, String contrasenia, double peso, double altura, String sexo) {
        this.id_usuario = id_usuario;
        this.nombre_usuario = nombre_usuario;
        this.apellidos = apellidos;
        this.contrasenia = contrasenia;
        this.peso = peso;
        this.altura = altura;
        this.sexo = sexo;
    }

    public Usuario() {
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getNombre_usuario() {
        return nombre_usuario;
    }

    public void setNombre_usuario(String nombre_usuario) {
        this.nombre_usuario = nombre_usuario;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public double getAltura() {
        return altura;
    }

    public void setAltura(double altura) {
        this.altura = altura;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }
}
