package com.example.runningtracker.conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
        private static final String url = "jdbc:mysql://192.168.1.76:3306/runningtracker?useSSL=false&allowPublicKeyRetrieval=true";
        private static final String username = "alejandro";
        private static final String password = "root";

        public static Connection getConnection() throws SQLException {
            return DriverManager.getConnection(url, username, password);
        }
    }
