package com.example.runningtracker.dao;

import android.util.Log;
import android.widget.Toast;

import com.example.runningtracker.RegisterActivity;
import com.example.runningtracker.conexion.Conexion;
import com.example.runningtracker.model.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DAOImpl implements DAO{


    @Override
    public boolean insertarUsuario(Usuario usuario) throws SQLException {
        Connection  c= null;
        boolean valueReturn = false;
        String sql = "INSERT INTO usuarios VALUES(?,?,?,?,?) WHERE id_usuario = 1";

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");
            c = Conexion.getConnection();
            c.setAutoCommit(false);
            // Desactivar el autocommit

            if (c!=null){
                Log.d("MSG","Conexion realizada a la base de datos");

                PreparedStatement sqlStatement = c.prepareStatement(sql);

                sqlStatement.setString(1, usuario.getUsername());
                sqlStatement.setString(2, usuario.getPassword());
                sqlStatement.setString(3, usuario.getPeso());
                sqlStatement.setString(4, usuario.getSexo());
                sqlStatement.setString(5, usuario.getTipo());

                if (sqlStatement.executeUpdate() > 0) {
                   valueReturn=true;
                }

                c.commit();
                c.close();
            }else
                Log.d("MSG","No existe conexion con la FUKIN BD");

        }catch (SQLException e) {
            c.rollback();
             throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
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
            sqlStatement.setString(3, usuario.getPeso());
            sqlStatement.setString(4, usuario.getSexo());
            sqlStatement.setString(5, usuario.getTipo());
            sqlStatement.setString(6, usuario.getId_usuario());

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
