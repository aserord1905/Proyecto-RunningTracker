package com.example.runningtracker.dao;

import com.example.runningtracker.model.Usuario;

import java.sql.Connection;

public class DAOImpl implements DAO{

    @Override
    public boolean insertarUsuario(Usuario usuario) {
        Connection c = null;
        boolean valueReturn = false;
        String consulta = "SELECT * FROM usuario";
        String sql = "INSERT INTO usuario VALUES(?,?,?,?,?,?)";

        return false;
    }
}
