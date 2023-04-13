package com.example.runningtracker.dao;

import com.example.runningtracker.conexion.Conexion;
import com.example.runningtracker.model.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DAOImpl implements DAO{
    private static Connection connection = null;

    @Override
    public boolean insertarUsuario(Usuario usuario) throws SQLException {
        Connection c = null;
        boolean valueReturn = false;
        String sql = "INSERT INTO usuario (username, password, peso, sexo, tipo) VALUES(?,?,?,?,?)";

        try {
            c = Conexion.getConnection();
            // Desactivar el autocommit
            c.setAutoCommit(false);
            PreparedStatement sqlStatement = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            sqlStatement.setString(1, usuario.getUsername());
            sqlStatement.setString(2, usuario.getPassword());
            sqlStatement.setFloat(3, usuario.getPeso());
            sqlStatement.setString(4, usuario.getSexo());
            sqlStatement.setString(5, usuario.getTipo());

            if (sqlStatement.executeUpdate() > 0) {
                ResultSet generatedKeys = sqlStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    // Obtener el ID autoincremental generado por la base de datos
                    String idGenerado = generatedKeys.getString(1);
                    usuario.setId(idGenerado);
                    valueReturn = true;
                }
            }

            c.commit();

            //Nose si esto dara error
            c.close();
        }catch (SQLException e) {
            c.rollback();
            throw new RuntimeException(e);
        }
        return valueReturn;
    }

    @Override
    public boolean modificarUsuario(Usuario usuario) throws SQLException {
        Connection c = null;
        boolean valueReturn = false;
        String sql = "UPDATE usuario SET Username = ?, Password = ?, Peso = ?, Sexo = ?, Tipo = ? WHERE Id = ?";

        try{
            c = Conexion.getConnection();
            //AUTOCOMMIT
            c.setAutoCommit(false);
            PreparedStatement sqlStatement = c.prepareStatement(sql);
            sqlStatement.setString(1, usuario.getUsername());
            sqlStatement.setString(2, usuario.getPassword());
            sqlStatement.setFloat(3, usuario.getPeso());
            sqlStatement.setString(4, usuario.getSexo());
            sqlStatement.setString(5, usuario.getTipo());
            sqlStatement.setString(6, usuario.getId());

            if (sqlStatement.executeUpdate()>0){
                valueReturn = true;
            }

            c.commit();

            //Nose si esto dara error
            c.close();
        }catch (SQLException e) {
            c.rollback();
            throw new RuntimeException(e);
        }
        return valueReturn;
    }
}
