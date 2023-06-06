package com.example.runningtracker.dao;


import com.example.runningtracker.model.Usuario;

import java.sql.Connection;
import java.sql.SQLException;

public interface DAO {

    boolean insertarUsuario (String sexo, String peso, String username, String password, String permiso) throws SQLException;

    void actualizarDesafio (Connection connection, String idDesafio, String nuevosKilometros, String nuevaDescripcion);

    boolean insertarDesafio(String idUsuario, String id_desafio ,double distanciaTotal);

}
