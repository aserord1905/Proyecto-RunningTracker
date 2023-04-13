package com.example.runningtracker.dao;


import com.example.runningtracker.model.Usuario;

import java.sql.SQLException;

public interface DAO {

    boolean insertarUsuario (Usuario usuario) throws SQLException;

    boolean modificarUsuario(Usuario usuario) throws SQLException;
}
