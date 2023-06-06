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
    public boolean insertarUsuario(String sexo, String peso, String username, String password, String permiso) throws SQLException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = Conexion.getConnection();

            String query = "SELECT MAX(id_usuario) FROM usuarios";
            PreparedStatement stmt = connection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery(query);
            String lastIdStr = rs.next() ? rs.getString(1) : "0"; // Si la tabla está vacía, se asigna el valor 0 por defecto

            // Convertir el último valor de id_usuario a int y sumarle uno
            int newId = Integer.parseInt(lastIdStr) + 1;
            String consulta = "INSERT INTO usuarios (id_usuario, sexo, peso, username, password, tipo) VALUES ('"+newId+"','" + sexo + "', '" + peso + "', '" + username + "', '" + password + "', '" + permiso + "')";
            PreparedStatement statement = connection.prepareStatement(consulta);
            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                // Obtener el id del rol según el valor de permiso
                int idRol;
                if (permiso.equalsIgnoreCase("usuario")) {
                    idRol = 1; // Id del rol usuario = 1
                } else {
                    idRol = 2;  //Id del rol administrador = 2
                }

                // Insertar en la tabla usuario_rol
                String insertUsuarioRol = "INSERT INTO usuariorol (id_usuario, id_rol) VALUES (?, ?)";
                PreparedStatement statementUsuarioRol = connection.prepareStatement(insertUsuarioRol);
                statementUsuarioRol.setInt(1, newId);
                statementUsuarioRol.setInt(2, idRol);
                statementUsuarioRol.executeUpdate();

                return true;
            } else {
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void actualizarDesafio(Connection connection, String idDesafio, String nuevosKilometros, String nuevaDescripcion) {
        String consulta = "UPDATE desafios SET kilometros_desafio = ?, descripcion = ? WHERE id_desafio = ?";
        try (PreparedStatement statement = connection.prepareStatement(consulta)) {
            statement.setString(1, nuevosKilometros);
            statement.setString(2, nuevaDescripcion);
            statement.setString(3, idDesafio);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean insertarDesafio(String idUsuario, String id_desafio, double distanciaTotal) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = Conexion.getConnection();

            String query = "SELECT MAX(id_historial) FROM historial_desafios";
            PreparedStatement stmt = connection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            String lastIdStr = rs.next() ? rs.getString(1) : "0"; // Si la tabla está vacía, se asigna el valor 0 por defecto

            // Convertir el último valor de id_usuario a int y sumarle uno
            int newIdDesafio = Integer.parseInt(lastIdStr) + 1;
            String consulta = "INSERT INTO historial_desafios (id_historial, id_usuario, id_desafio, kilometros_realizados) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(consulta);
            statement.setInt(1, newIdDesafio);
            statement.setString(2, idUsuario);
            statement.setString(3, id_desafio);
            statement.setDouble(4, distanciaTotal);

            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
